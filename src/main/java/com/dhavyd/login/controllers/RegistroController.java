package com.dhavyd.login.controllers;

import com.dhavyd.login.dto.RegistroDeUsuarioDTO;
import com.dhavyd.login.entidades.Registro;
import com.dhavyd.login.servico.RegistroService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.*;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/registros")
public class RegistroController {

    private RegistroService service;

    @GetMapping// GET: Retorna todos os registros de ponto podendo fazer fitro por dia
    public ResponseEntity<List<RegistroDeUsuarioDTO>> buscarTodos(@RequestParam(name = "dataInicial", defaultValue = "") LocalDate dataInicial,
                                                                  @RequestParam(name = "dataFinal", defaultValue = "") LocalDate dataFinal,
                                                                  @RequestParam(name = "allUsers", defaultValue = "false") boolean allUsers) { // ResponseEntity é a classe responsavel por todas as requisições HTTP
        List<RegistroDeUsuarioDTO> registros = service.buscarTodos(dataInicial, dataFinal, allUsers);

        return ResponseEntity.ok().body(registros); // Retorna um JSON listando os RegistroDePontos no Body
    }

    @GetMapping(value = "/{id}") // GET: Retorna um registro pelo id informado
    public ResponseEntity<RegistroDeUsuarioDTO> buscarPorId(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok().body(service.buscarPorId(id));
    }

    @GetMapping(value = "/usuario/{usuarioId}") // GET: Busca os registros de um único usuário podendo filtrar por data
    public ResponseEntity<List<RegistroDeUsuarioDTO>> buscarPorIdUsario(@PathVariable("usuarioId") Long id,
                                                                        @RequestParam(name = "carregarRegistrosToday", defaultValue = "false") boolean carregarRegitrosToday,
                                                                        @RequestParam(name = "dataInicial", defaultValue = "" ) LocalDate dataInicial,
                                                                        @RequestParam(name = "dataFinal", defaultValue = "") LocalDate dataFinal) {
        List<RegistroDeUsuarioDTO> registros = service.buscarPorIdUsuario(id, carregarRegitrosToday, dataInicial, dataFinal);
        return ResponseEntity.ok().body(registros);
    }

    @PostMapping(value = "/entrada/{usuarioId}") // POST: Marca a entrada de um usuário
    public ResponseEntity<Registro> marcarEntrada(@PathVariable Long usuarioId) {
        Registro registroDePonto = service.marcarEntrada(usuarioId);

        if (Objects.nonNull(registroDePonto)) {
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(registroDePonto.getId()).toUri();
            return ResponseEntity.created(uri).body(registroDePonto);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping(value = "/saida/{usuarioId}") // POST: Marca a saída de um usuário
    public ResponseEntity<Registro> marcarSaida(@PathVariable Long usuarioId) {
        Registro registroDePonto = service.marcarSaida(usuarioId);

        if (Objects.nonNull(registroDePonto)) { // Retorna 200 se o registro tiver sido marcado
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(registroDePonto.getId()).toUri();
            return ResponseEntity.created(uri).body(registroDePonto);
        } else { // Erro 400 ao marcar a saida
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping(value = "/{id}") // DELETE: Deleta um registro por id
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletarRegistro(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}") // Atualiza um registro por id
    public ResponseEntity<Registro> atualizar(@PathVariable Long id,
                                              @RequestBody Registro RegistroDePonto) {
        RegistroDePonto = service.atualizarResgistro(id, RegistroDePonto);
        return ResponseEntity.ok().body(RegistroDePonto);
    }
}
