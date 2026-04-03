package com.example.votacao.client.dto;

public record UserInfoResponse(
        String status
) {
    public boolean isAbleToVote() {
        return "ABLE_TO_VOTE".equalsIgnoreCase(status);
    }
}
