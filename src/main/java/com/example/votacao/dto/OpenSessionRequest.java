package com.example.votacao.dto;

import jakarta.validation.constraints.Positive;

public record OpenSessionRequest(@Positive Integer durationInMinutes) {
}
