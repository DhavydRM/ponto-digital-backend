package com.dhavyd.login.controllers;

import java.net.URI;
import java.util.List;

import com.dhavyd.login.auth.Token;
import com.dhavyd.login.dto.LoginDTO;
import com.dhavyd.login.dto.UsuarioDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dhavyd.login.entidades.Usuario;
import com.dhavyd.login.servico.UsuarioService;

@AllArgsConstructor
@RestController // Identifica que aqui é o controlador da API(Faz a comunicação direta com o front)
@RequestMapping(value = "usuarios") // Adiciona o endpoint pela qual os metodos serão chamados
public class UsuarioController {

    private UsuarioService service;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> buscarTodos() { // ResponseEntity é a classe responsavel por todas as requisições HTTP
        List<UsuarioDTO> usuarios = service.buscarTodos();
        return ResponseEntity.ok().body(usuarios); // Retorna um JSON listando os usuarios no Body
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        UsuarioDTO usuario = service.buscarPorId(id);
        return ResponseEntity.ok().body(usuario);
    }

    @PostMapping
    public ResponseEntity<Usuario> inserir(@RequestBody Usuario usuario) {
        usuario = service.inserir(usuario);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(usuario);
    }
    
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        usuario = service.atualizar(id, usuario);
        return ResponseEntity.ok().body(usuario);
    }

    @PostMapping(value = "/auth")
    public ResponseEntity<Token> autenticarUsuario(@RequestBody LoginDTO login) {
        Token token = service.autenticarUsuario(login);
        if (token.getToken()) {
            return ResponseEntity.ok().body(token);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
