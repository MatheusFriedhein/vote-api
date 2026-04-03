package com.example.votacao.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "voting_sessions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VotingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "agenda_id", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_session_agenda"))
    private Agenda agenda;

    @Column(nullable = false)
    private OffsetDateTime openedAt;

    @Column(nullable = false)
    private OffsetDateTime closesAt;

    @Column(nullable = false)
    @Builder.Default
    private Long totalYes = 0L;

    @Column(nullable = false)
    @Builder.Default
    private Long totalNo = 0L;

    @Column(nullable = false)
    @Builder.Default
    private Long totalVotes = 0L;

    @Version
    private Long version;

    public boolean isOpen(OffsetDateTime now) {
        return !now.isBefore(openedAt) && now.isBefore(closesAt);
    }
}
