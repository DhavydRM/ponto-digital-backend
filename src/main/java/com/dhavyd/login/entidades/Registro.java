package com.dhavyd.login.entidades;

import com.dhavyd.login.entidades.enums.Turnos;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getSaida() {
        return saida;
    }

    public void setSaida(LocalDateTime saida) {
        this.saida = saida;
    }

    public LocalDateTime getEntrada() {
        return entrada;
    }

    public void setEntrada(LocalDateTime entrada) {
        this.entrada = entrada;
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
