package com.example.votacao.controller;

import com.example.votacao.dto.*;
import com.example.votacao.service.AgendaService;
import com.example.votacao.service.VoteService;
import com.example.votacao.service.VotingSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/agendas")
@RequiredArgsConstructor
@Tag(name = "Agendas", description = "Gerenciamento de agendas e votação")
@SecurityRequirement(name = "basicAuth")
public class AgendaController {

    private final VoteService voteService;
    private final AgendaService agendaService;
    private final VotingSessionService votingSessionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar nova agenda")
    public AgendaResponse create(@Valid @RequestBody CreateAgendaRequest request) {
        return agendaService.create(request);
    }

    @PostMapping("/{agendaId}/sessoes")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Abrir sessão de votação")
    public SessionResponse openSession(@PathVariable Long agendaId,
            @Valid @RequestBody(required = false) OpenSessionRequest request) {
        return votingSessionService.open(agendaId, request);
    }

    @GetMapping("/{agendaId}/sessoes")
    @Operation(summary = "Consultar sessão de votação")
    public SessionResponse getSession(@PathVariable Long agendaId) {
        return votingSessionService.getByAgendaId(agendaId);
    }

    @PostMapping("/{agendaId}/votos")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar voto em uma pauta")
    public VoteResponse vote(@PathVariable Long agendaId, @Valid @RequestBody VoteRequest request) {
        return voteService.registerVote(agendaId, request);
    }

    @GetMapping("/{agendaId}/resultado")
    @Operation(summary = "Contabilizar votos e retornar resultado")
    public VoteResultResponse result(@PathVariable Long agendaId) {
        return voteService.getResult(agendaId);
    }
}
