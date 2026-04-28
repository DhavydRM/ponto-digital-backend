package com.dhavyd.login.entidades;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.dhavyd.login.entidades.enums.TipoEscala;

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
public class EscalaTrabalho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private TipoEscala tipoEscala;

    @Enumerated(EnumType.STRING)
    private DayOfWeek diaSemana;

    private LocalTime horaEntrada;
    private LocalTime horaSaida;

    private Boolean ativo = true;
}
