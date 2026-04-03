package com.example.votacao.dto;

import com.example.votacao.model.VoteValue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record VoteRequest(@Valid @NotNull CreateAssociateRequest associate,
        @NotNull VoteValue vote) {
}
