package com.example.votacao.repository;

import com.example.votacao.model.Vote;
import com.example.votacao.model.VoteValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByAgendaIdAndAssociateId(Long agendaId, Long associateId);
    long countByAgendaIdAndValue(Long agendaId, VoteValue value);
}
