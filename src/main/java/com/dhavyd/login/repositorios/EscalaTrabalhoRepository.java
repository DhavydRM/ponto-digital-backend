package com.dhavyd.login.repositorios;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dhavyd.login.entidades.EscalaTrabalho;

@Repository
public interface EscalaTrabalhoRepository extends JpaRepository<EscalaTrabalho, Long> {
    List<EscalaTrabalho> findByUsuarioId(Long usuarioId);
    List<EscalaTrabalho> findByUsuarioIdAndAtivoTrue(Long usuarioId);
    boolean existsByUsuarioIdAndDiaSemanaAndAtivoTrue(Long usuarioId, DayOfWeek diaSemana);
}
