package com.dhavyd.login.entidades;

import com.dhavyd.login.entidades.enums.Turnos;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@ToString
@Table(name = "registro_de_ponto")
public class Registro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    private LocalDateTime entrada;
    private LocalDateTime saida;
    private String observacao;

    @JsonIgnore
    @OneToMany(mappedBy = "registroRelacionado")
    private List<Ticket> ticket;

    public Registro(LocalDateTime entrada, Usuario usuario, Long id) {
        this.entrada = entrada;
        this.usuario = usuario;
        this.id = id;
    }

    public Registro(Long id, Usuario usuario, LocalDateTime entrada, LocalDateTime saida, Turnos turno) {
        this.id = id;
        this.usuario = usuario;
        this.entrada = entrada;
        this.saida = saida;
    }

    public Registro() {

    }

    public void setTicket(Ticket ticket) {
        this.ticket.add(ticket);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Registro that = (Registro) o;
        return Objects.equals(entrada, that.entrada);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(entrada);
    }
}
