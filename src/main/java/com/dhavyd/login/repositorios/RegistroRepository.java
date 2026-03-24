package com.dhavyd.login.repositorios;

import com.dhavyd.login.entidades.Registro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistroRepository extends JpaRepository<Registro, Long> {
}
