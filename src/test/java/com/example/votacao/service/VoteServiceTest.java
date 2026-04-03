package com.example.votacao.service;

import com.example.votacao.dto.CreateAssociateRequest;
import com.example.votacao.dto.VoteRequest;
import com.example.votacao.exception.BusinessException;
import com.example.votacao.model.Agenda;
import com.example.votacao.model.Associate;
import com.example.votacao.model.VoteValue;
import com.example.votacao.model.VotingSession;
import com.example.votacao.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;
    @Mock
    private AgendaService agendaService;
    @Mock
    private VotingSessionService votingSessionService;
    @Mock
    private AssociateService associateService;

    @InjectMocks
    private VoteService voteService;

    @Test
    void shouldNotAllowDuplicateVote() {
        Agenda agenda = Agenda.builder().id(1L).title("Agenda 1").build();
        VotingSession session = VotingSession.builder()
                .id(1L)
                .agenda(agenda)
                .openedAt(OffsetDateTime.now().minusMinutes(1))
                .closesAt(OffsetDateTime.now().plusMinutes(1))
                .build();
        Associate associate = Associate.builder().id(10L).cpf("12345678901").name("Maria").build();

        when(agendaService.findEntityById(anyLong())).thenReturn(agenda);
        when(votingSessionService.findByAgendaId(anyLong())).thenReturn(session);
        when(associateService.findOrCreate(new CreateAssociateRequest("Maria", "12345678901"))).thenReturn(associate);
        when(voteRepository.existsByAgendaIdAndAssociateId(1L, 10L)).thenReturn(true);

        VoteRequest request = new VoteRequest(new CreateAssociateRequest("Maria", "12345678901"), VoteValue.SIM);

        assertThrows(BusinessException.class, () -> voteService.registerVote(1L, request));
    }
}
