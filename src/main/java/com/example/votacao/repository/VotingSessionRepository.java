package com.example.votacao.repository;

import com.example.votacao.model.VotingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VotingSessionRepository extends JpaRepository<VotingSession, Long> {

    Optional<VotingSession> findByAgendaId(Long agendaId);

    boolean existsByAgendaId(Long agendaId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
        UPDATE voting_sessions
           SET total_votes = total_votes + 1,
               total_yes = total_yes + CASE WHEN :voteValue = 'SIM' THEN 1 ELSE 0 END,
               total_no = total_no + CASE WHEN :voteValue = 'NAO' THEN 1 ELSE 0 END,
               version = version + 1
         WHERE agenda_id = :agendaId
    """, nativeQuery = true)
    int incrementCounters(@Param("agendaId") Long agendaId, @Param("voteValue") String voteValue);

}
