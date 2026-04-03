package com.example.votacao.repository;

import com.example.votacao.model.Associate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssociateRepository extends JpaRepository<Associate, Long> {

    Optional<Associate> findByCpf(String cpf);

}
