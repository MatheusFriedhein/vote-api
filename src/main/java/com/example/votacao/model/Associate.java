package com.example.votacao.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "associates", uniqueConstraints = {
        @UniqueConstraint(name = "uk_associate_cpf", columnNames = "cpf")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Associate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 11)
    private String cpf;
}
