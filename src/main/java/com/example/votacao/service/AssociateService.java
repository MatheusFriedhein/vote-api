package com.example.votacao.service;

import com.example.votacao.dto.CreateAssociateRequest;
import com.example.votacao.model.Associate;
import com.example.votacao.repository.AssociateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AssociateService {

    private final AssociateRepository associateRepository;

    @Transactional
    public Associate findOrCreate(CreateAssociateRequest request) {
        return associateRepository.findByCpf(request.cpf())
                .orElseGet(() -> associateRepository.save(Associate.builder()
                        .name(request.name())
                        .cpf(request.cpf())
                        .build()));
    }

}
