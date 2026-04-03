package com.example.votacao.repository;

import com.example.votacao.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {
}
