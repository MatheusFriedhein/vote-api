package com.example.votacao.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "agendas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private OffsetDateTime createdAt;
}
