package com.example.votacao.service;

import com.example.votacao.dto.OpenSessionRequest;
import com.example.votacao.dto.SessionResponse;
import com.example.votacao.exception.BusinessException;
import com.example.votacao.model.Agenda;
import com.example.votacao.model.VotingSession;
import com.example.votacao.repository.VotingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class VotingSessionService {

    private static final int DEFAULT_DURATION_MINUTES = 5;

    private final AgendaService agendaService;
    private final VotingSessionRepository votingSessionRepository;

    @Transactional
    public SessionResponse open(Long agendaId, OpenSessionRequest request) {
        if (votingSessionRepository.existsByAgendaId(agendaId)) {
            throw new BusinessException("Já existe uma sessão aberta/criada para esta agenda");
        }

        Agenda agenda = agendaService.findEntityById(agendaId);
        OffsetDateTime openedAt = OffsetDateTime.now();
        int duration = request != null && request.durationInMinutes() != null
                ? request.durationInMinutes() : DEFAULT_DURATION_MINUTES;

        return toResponse(votingSessionRepository.save(VotingSession.builder()
                .agenda(agenda)
                .openedAt(openedAt)
                .closesAt(openedAt.plusMinutes(duration))
                .build()), OffsetDateTime.now());
    }

    @Transactional(readOnly = true)
    public VotingSession findByAgendaId(Long agendaId) {
        return votingSessionRepository.findByAgendaId(agendaId)
                .orElseThrow(() -> new BusinessException("Sessão de votação não encontrada para a agenda"));
    }

    @Transactional(readOnly = true)
    public SessionResponse getByAgendaId(Long agendaId) {
        return toResponse(findByAgendaId(agendaId), OffsetDateTime.now());
    }

    private SessionResponse toResponse(VotingSession session, OffsetDateTime now) {
        return new SessionResponse(
                session.getId(),
                session.getAgenda().getId(),
                session.getOpenedAt(),
                session.getClosesAt(),
                session.isOpen(now)
        );
    }
}
