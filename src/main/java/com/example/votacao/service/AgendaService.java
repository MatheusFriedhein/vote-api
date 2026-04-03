package com.example.votacao.service;

import com.example.votacao.dto.AgendaResponse;
import com.example.votacao.dto.CreateAgendaRequest;
import com.example.votacao.exception.NotFoundException;
import com.example.votacao.model.Agenda;
import com.example.votacao.repository.AgendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AgendaService {

    private final AgendaRepository agendaRepository;

    @Transactional
    public AgendaResponse create(CreateAgendaRequest request) {
        Agenda agenda = Agenda.builder()
                .title(request.title())
                .description(request.description())
                .createdAt(OffsetDateTime.now())
                .build();

        Agenda saved = agendaRepository.save(agenda);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Agenda findEntityById(Long agendaId) {
        return agendaRepository.findById(agendaId)
                .orElseThrow(() -> new NotFoundException("Pauta não encontrada"));
    }

    private AgendaResponse toResponse(Agenda agenda) {
        return new AgendaResponse(agenda.getId(), agenda.getTitle(), agenda.getDescription(), agenda.getCreatedAt());
    }
}
