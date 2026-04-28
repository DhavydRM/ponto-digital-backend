package com.dhavyd.login.servico;

import java.util.List;
import java.util.Objects;


import com.dhavyd.login.auth.Token;
import com.dhavyd.login.dto.LoginDTO;
import com.dhavyd.login.dto.UsuarioDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dhavyd.login.entidades.Usuario;
import com.dhavyd.login.repositorios.UsuarioRepository;
import com.dhavyd.login.servico.execoes.RecursoNaoEncontrado;

@Service
@AllArgsConstructor
public class UsuarioService {

    private UsuarioRepository repository;

    public List<UsuarioDTO> buscarTodos() {
        List<Usuario> usuarioList = repository.findByAtivo(true);

        return usuarioList
                .stream()
                .map(
                        UsuarioDTO::usuarioToDTO)
                .toList(); // Retorna uma lista com usuários DTO
    }

    public UsuarioDTO buscarPorId(Long id) {
        return UsuarioDTO.usuarioToDTO(repository.findById(id).orElseThrow(() -> new RecursoNaoEncontrado("id")));
    }

    public Usuario inserir(Usuario usuario) {
        return repository.save(usuario);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public Usuario atualizar(Long id, Usuario usuario) {
        Usuario user = repository.getReferenceById(id);
        atualizarUsuario(user, usuario);
        return repository.save(user);
    }

    public Token autenticarUsuario(LoginDTO login) {
        return Token.gerarToken(login);
    }

    private void atualizarUsuario(Usuario user, Usuario usuario) {
        user.setNome(usuario.getNome());
        user.setEmail(usuario.getEmail());
        user.setSenha(usuario.getSenha());
        user.setFuncao(usuario.getFuncao());
    }
}
