package com.dhavyd.login.servico;

import com.dhavyd.login.dto.RegistroDeUsuarioDTO;
import com.dhavyd.login.dto.RegistroUpdateDTO;
import com.dhavyd.login.entidades.Registro;
import com.dhavyd.login.entidades.Usuario;
import com.dhavyd.login.repositorios.RegistroRepository;
import com.dhavyd.login.repositorios.UsuarioRepository;
import com.dhavyd.login.servico.execoes.RecursoNaoEncontrado;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.Objects;

@Service
public class RegistroService {

    private final RegistroRepository repository;
    private final UsuarioRepository usuarioRepository;

    public RegistroService(RegistroRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<RegistroDeUsuarioDTO> buscarTodos(LocalDate dataInicial, LocalDate dataFinal, boolean allUsers) {
        LocalDateTime inicioDia = dataInicial.atStartOfDay();
        LocalDateTime fimDia = dataFinal.atTime(LocalTime.MAX);

        if (allUsers) { // Por hora, não passar um periodo maior que 1 dia
            List<Usuario> usuarios = usuarioRepository.findByAtivo(true);
            List<Registro> registros = repository.findByPeriodo(inicioDia, fimDia);
            return RegistroDeUsuarioDTO.listaRegistroAllUsers(registrosPorPeriodo(registros, dataInicial, dataFinal), usuarios);
        }

        List<Registro> registros = repository.findByPeriodo(inicioDia, fimDia);
        if(Objects.nonNull(registros)) {
            return RegistroDeUsuarioDTO.listaRegistroUsuarioToDTO(registros);
        }
        return null;

    }

    public RegistroDeUsuarioDTO buscarPorId(Long id) {
         return RegistroDeUsuarioDTO.registroUsuarioToDTO(repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Recurso não encontrado!")));
    }

    public List<RegistroDeUsuarioDTO> buscarPorIdUsuario(Long idUsuario, boolean carregarRegistrosToday, LocalDate dataInicial, LocalDate dataFinal) {

        Usuario usuario = usuarioRepository.findById(idUsuario) // Busca o usuário no banco de dados através do ID
                .orElseThrow(() -> new RecursoNaoEncontrado("Usuário não encontrado!"));

        if (carregarRegistrosToday) { // Se verdadeiro, retorna todas os Registros onde a entrada seja do dia requisitado
            LocalDateTime inicio = LocalDate.now().atStartOfDay();
            LocalDateTime fim = LocalDate.now().atTime(LocalTime.MAX);
            return RegistroDeUsuarioDTO.listaRegistroUsuarioToDTO(repository.findByPeriodoPorUser(inicio, fim, idUsuario));

        }

        if (Objects.nonNull(dataFinal)) { // Retorna todos os regitros que estejam dentro do intervalo de DATAINICIAL e DATAFINAL
            LocalDateTime inicio = dataInicial.atStartOfDay();
            LocalDateTime fim = dataFinal.atTime(LocalTime.MAX);
            return RegistroDeUsuarioDTO.listaRegistroUsuarioToDTO(repository.findByPeriodoPorUser(inicio, fim, idUsuario));

        } else if (Objects.nonNull(dataInicial)){ // Retorna os registros apenas com entrada de DATAINICIAL
            LocalDateTime inicio = dataInicial.atStartOfDay();
            return RegistroDeUsuarioDTO.listaRegistroUsuarioToDTO(repository.findByPeriodoPorUser(inicio, inicio, idUsuario));

        }

        return RegistroDeUsuarioDTO.listaRegistroUsuarioToDTO(usuario.getRegistroDePontos()); // Em ultimo caso, retorna todos os registros que o usuário tem!
    }

    public Registro marcarEntrada(Long usuarioId) {
        LocalDateTime registroExato = LocalDateTime.now(); // Pega o registro no exato momento que a função é chamada
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow();
        Registro registro = new Registro(registroExato, usuario, null);

        List<Registro> registrosExistentes = usuario.getRegistroDePontos() // Verifica se já tem registros naquele dia
                .stream()
                .filter(registroDePonto -> registroDePonto.getEntrada()
                        .toLocalDate()
                        .equals(LocalDate.now()))
                .toList();

        if (registrosExistentes.size() >= 2) {
            return null; // Não permite que o usuário marque mais de 2 entradas no mesmo dia
        }else {
            return repository.save(registro);
        }
    }

    public Registro marcarSaida(Long usuarioId) {
        LocalDateTime registroExato = LocalDateTime.now();
        Usuario user = usuarioRepository.findById(usuarioId).orElse(null);
        assert user != null;
        Registro ultimoRegistro = user.getRegistroDePontos().getLast();

        if (!Objects.nonNull(ultimoRegistro.getSaida())) { // Retorna verdadeiro se a saída do usuário estiver vazia
            ultimoRegistro.setSaida(registroExato);
            return repository.save(ultimoRegistro);
        }

        return null;
    }

    public void deletarRegistro(Long id) {
        repository.deleteById(id);
    }

    public Registro atualizarResgistro(Long id, RegistroUpdateDTO novosDados) {
        Registro registro = repository.getReferenceById(id);
        atualizarRegistroDePonto(registro, novosDados);
        return repository.save(registro);
    }

    private void atualizarRegistroDePonto(Registro registro, RegistroUpdateDTO novosDados) {
        registro.setEntrada(novosDados.entrada());
        registro.setSaida(novosDados.saida());
        registro.setObservacao(novosDados.observacao());
    }

    private List<Registro> registrosPorPeriodo(List<Registro> registroDePontos, LocalDate dataInicial, LocalDate dataFinal) {

        return registroDePontos.stream()
                .map(registroDePonto -> {
                    final var entrada = registroDePonto.getEntrada().toLocalDate();
                    if (entrada.isAfter(dataInicial) && entrada.isBefore(dataFinal) ||
                            entrada.isEqual(dataInicial) ||
                            entrada.isEqual(dataFinal)) {

                        return registroDePonto;
                    }
                    return null;
                }).filter(Objects::nonNull)
                .toList();
    }

}
