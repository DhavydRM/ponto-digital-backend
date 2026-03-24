package com.dhavyd.login.dto;

import com.dhavyd.login.entidades.Registro;
import com.dhavyd.login.entidades.Usuario;
import com.dhavyd.login.entidades.enums.StatusRegistro;
import com.dhavyd.login.entidades.enums.Turnos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record RegistroDeUsuarioDTO(Long id, String username, String email,
                                   LocalDateTime entrada, LocalDateTime saida,
                                   Turnos turno, StatusRegistro status) {

    public static RegistroDeUsuarioDTO registroUsuarioToDTO(Registro registro) {
        return new RegistroDeUsuarioDTO(registro.getId(),
                registro.getUsuario().getNome(), registro.getUsuario().getEmail(),
                registro.getEntrada(), registro.getSaida(),
                Turnos.retornarTurno(registro.getEntrada()),
                StatusRegistro.retornaStatus(registro.getSaida()));
    }

    public static List<RegistroDeUsuarioDTO> listaRegistroUsuarioToDTO (List<Registro> registros) {
        return registros
                .stream()
                .map(RegistroDeUsuarioDTO::registroUsuarioToDTO)
                .toList();
    }

    public static List<RegistroDeUsuarioDTO> listaRegistroAllUsers (List<Registro> registros, List<Usuario> usuarios) {
        List<RegistroDeUsuarioDTO> listaCompleta = new ArrayList<>(registros
                .stream()
                .map(RegistroDeUsuarioDTO::registroUsuarioToDTO)
                .toList());

        List<Usuario> test = usuarios.stream()
                .peek(user -> {
                    System.out.println("1");
                    boolean presente = false;
                    for (RegistroDeUsuarioDTO x: listaCompleta) {
                        if (x.email.equals(user.getEmail())) {
                            presente = true;
                        }
                    }
                    if (!presente) {
                        listaCompleta.add(new RegistroDeUsuarioDTO((long)(Math.random() * (99999 - 1 + 1)) + 1, user.getNome(),
                                user.getEmail(), null, null, null, StatusRegistro.UNDEFINED));
                    }
                }).toList();


        return listaCompleta;
    }

}
