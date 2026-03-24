package com.dhavyd.login.repositorios;

import com.dhavyd.login.entidades.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RegistroRepository extends JpaRepository<Registro, Long> {

    @Query("SELECT r FROM Registro r WHERE r.entrada >= :inicio AND r.entrada <= :fim")
    List<Registro> findByPeriodo(
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim
    );

    @Query(value = "SELECT * FROM registro_de_ponto WHERE entrada >= :inicio AND entrada <= :fim AND usuario_id = :usuarioId", nativeQuery = true)
    List<Registro> findByPeriodoPorUser(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("usuarioId") Long usuarioId
    );
}
