package com.example.votacao.dto;

public record VoteResultResponse(Long agendaId, String title, long totalYes, long totalNo,
                                 long totalVotes, String result) {}
