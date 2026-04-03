package com.example.votacao.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "votes", uniqueConstraints = {
        @UniqueConstraint(name = "uk_vote_agenda_associate", columnNames = {"agenda_id", "associate_id"})
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "agenda_id", nullable = false, foreignKey = @ForeignKey(name = "fk_vote_agenda"))
    private Agenda agenda;

    @ManyToOne(optional = false)
    @JoinColumn(name = "associate_id", nullable = false, foreignKey = @ForeignKey(name = "fk_vote_associate"))
    private Associate associate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private VoteValue value;

    @Column(nullable = false)
    private OffsetDateTime createdAt;
}
