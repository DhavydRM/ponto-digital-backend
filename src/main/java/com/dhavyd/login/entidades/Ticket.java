package com.dhavyd.login.entidades;

import com.dhavyd.login.entidades.enums.StatusTicket;
import com.dhavyd.login.entidades.enums.Tipo;
import com.dhavyd.login.entidades.enums.TurnoNumber;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime novoHorario;
    private LocalDateTime horarioAtual;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private Tipo tipoRegistro;
    @Enumerated(EnumType.STRING)
    private TurnoNumber turnoNumber;
    private String motivoTroca;
    @Enumerated(EnumType.STRING)
    private StatusTicket status;
    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Usuario funcionario;
    @ManyToOne
    @JoinColumn(name = "registro_id")
    private Registro registroRelacionado;
}
