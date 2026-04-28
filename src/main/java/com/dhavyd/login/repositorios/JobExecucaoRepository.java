package com.dhavyd.login.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dhavyd.login.entidades.JobExecucao;

@Repository
public interface JobExecucaoRepository extends JpaRepository<JobExecucao, Long> {
    Optional<JobExecucao> findByNomeJob(String nomeJob);
}
