package com.example.votacao.dto;

import java.time.OffsetDateTime;

public record SessionResponse(Long id, Long agendaId, OffsetDateTime openedAt,
        OffsetDateTime closesAt, boolean open) {
}
