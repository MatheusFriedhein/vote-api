package com.example.votacao.service;

import com.example.votacao.client.AssociateStatusClient;
import com.example.votacao.dto.VoteResultResponse;
import com.example.votacao.model.Agenda;
import com.example.votacao.model.VotingSession;
import com.example.votacao.repository.VoteRepository;
import com.example.votacao.repository.VotingSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoteResultAggregationTest {

    @Mock
    private VoteRepository voteRepository;
    @Mock
    private VotingSessionRepository votingSessionRepository;
    @Mock
    private AgendaService agendaService;
    @Mock
    private VotingSessionService votingSessionService;
    @Mock
    private AssociateService associateService;
    @Mock
    private AssociateStatusClient associateStatusClient;

    @InjectMocks
    private VoteService voteService;

    @Test
    void shouldReturnResultUsingSessionCounters() {
        Agenda agenda = Agenda.builder().id(1L).title("Agenda de teste").build();
        VotingSession session = VotingSession.builder()
                .id(99L)
                .agenda(agenda)
                .totalYes(120L)
                .totalNo(80L)
                .totalVotes(200L)
                .build();

        when(agendaService.findEntityById(1L)).thenReturn(agenda);
        when(votingSessionService.findByAgendaId(1L)).thenReturn(session);

        VoteResultResponse response = voteService.getResult(1L);

        assertEquals(120L, response.totalYes());
        assertEquals(80L, response.totalNo());
        assertEquals(200L, response.totalVotes());
        assertEquals("SIM venceu", response.result());
    }
}
