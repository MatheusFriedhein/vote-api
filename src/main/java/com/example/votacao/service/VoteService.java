package com.example.votacao.service;

import com.example.votacao.client.AssociateStatusClient;
import com.example.votacao.dto.VoteRequest;
import com.example.votacao.dto.VoteResponse;
import com.example.votacao.dto.VoteResultResponse;
import com.example.votacao.exception.BusinessException;
import com.example.votacao.model.Agenda;
import com.example.votacao.model.Associate;
import com.example.votacao.model.Vote;
import com.example.votacao.model.VotingSession;
import com.example.votacao.repository.VoteRepository;
import com.example.votacao.repository.VotingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final VotingSessionRepository votingSessionRepository;

    private final AgendaService agendaService;
    private final AssociateService associateService;
    private final VotingSessionService votingSessionService;

    private final AssociateStatusClient associateStatusClient;

    @Transactional
    public VoteResponse registerVote(Long agendaId, VoteRequest request) {

        Agenda agenda = agendaService.findEntityById(agendaId);
        VotingSession session = votingSessionService.findByAgendaId(agendaId);
        OffsetDateTime now = OffsetDateTime.now();

        if (!session.isOpen(now)) {
            throw new BusinessException("A sessão de votação está encerrada");
        }

        Associate associate = associateService.findOrCreate(request.associate());

        if (voteRepository.existsByAgendaIdAndAssociateId(agendaId, associate.getId())) {
            throw new BusinessException("Associado já votou nesta agenda");
        }

        if (!associateStatusClient.canVote(associate.getCpf())) {
            throw new BusinessException("Associado não está apto a votar");
        }

        try {
            Vote saved = voteRepository.save(Vote.builder()
                    .agenda(agenda)
                    .associate(associate)
                    .value(request.vote())
                    .createdAt(now)
                    .build());

            votingSessionRepository.incrementCounters(agendaId, request.vote().name());
            return new VoteResponse(saved.getId(), agenda.getId(), associate.getId(), saved.getValue(), saved.getCreatedAt());
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessException("Associado já votou nesta agenda");
        }
    }

    @Transactional(readOnly = true)
    public VoteResultResponse getResult(Long agendaId) {
        Agenda agenda = agendaService.findEntityById(agendaId);
        VotingSession session = votingSessionService.findByAgendaId(agendaId);

        long totalYes = session.getTotalYes();
        long totalNo = session.getTotalNo();
        long totalVotes = session.getTotalVotes();
        String result = totalYes > totalNo ? "SIM venceu" : totalNo > totalYes ? "NÃO venceu" : "EMPATE";

        return new VoteResultResponse(agenda.getId(), agenda.getTitle(), totalYes, totalNo, totalVotes, result);
    }
}
