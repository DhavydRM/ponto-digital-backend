package com.dhavyd.login.dto;

import com.dhavyd.login.entidades.Usuario;
import com.dhavyd.login.entidades.enums.Roles;

public record UsuarioDTO(Long id, String name, String email, Roles funcao) {

    public static UsuarioDTO usuarioToDTO(Usuario usuario) {
        return new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getFuncao());
    }
}
