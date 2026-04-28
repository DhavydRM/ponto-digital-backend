package com.dhavyd.login.repositorios;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dhavyd.login.entidades.OcorrenciaAusencia;
import com.dhavyd.login.entidades.enums.StatusOcorrencia;

@Repository
public interface OcorrenciaAusenciaRepository extends JpaRepository<OcorrenciaAusencia, Long> {
    List<OcorrenciaAusencia> findByUsuarioId(Long usuarioId);
    Optional<OcorrenciaAusencia> findByUsuarioIdAndData(Long usuarioId, LocalDate data);
    boolean existsByUsuarioIdAndDataAndTurnoEsperadoInicio(Long usuarioId, LocalDate data, LocalTime turnoEsperadoInicio);
    List<OcorrenciaAusencia> findByStatus(StatusOcorrencia status);
    List<OcorrenciaAusencia> findByUsuarioIdAndDataBetween(Long usuarioId, LocalDate inicio, LocalDate fim);
}
