package com.example.votacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateAssociateRequest(@NotBlank @Size(max = 120) String name,
        @NotBlank @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos") String cpf) {
}
