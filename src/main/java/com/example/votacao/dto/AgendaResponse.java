package com.example.votacao.dto;

import java.time.OffsetDateTime;

public record AgendaResponse(Long id, String title, String description, OffsetDateTime createdAt) {
}
