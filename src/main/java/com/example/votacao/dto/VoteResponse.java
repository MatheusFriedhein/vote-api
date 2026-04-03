package com.example.votacao.dto;

import com.example.votacao.model.VoteValue;

import java.time.OffsetDateTime;

public record VoteResponse(Long voteId, Long agendaId, Long associateId,
        VoteValue vote, OffsetDateTime createdAt) {
}
