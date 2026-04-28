package com.dhavyd.login.entidades;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.dhavyd.login.entidades.enums.StatusOcorrencia;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OcorrenciaAusencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private LocalDate data;
    private LocalTime turnoEsperadoInicio;
    private LocalTime turnoEsperadoFim;

    @Enumerated(EnumType.STRING)
    private StatusOcorrencia status = StatusOcorrencia.PENDENTE;

    @Column(nullable = true)
    private String observacaoGestor;

    @Column(nullable = true)
    private String documentoPath;

    @ManyToOne
    @JoinColumn(name = "resolvido_por", nullable = true)
    private Usuario resolvidoPor;

    @Column(nullable = true)
    private LocalDateTime resolvidoEm;

    private LocalDateTime criadoEm = LocalDateTime.now();
}
