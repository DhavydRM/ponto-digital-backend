package com.dhavyd.login.job;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dhavyd.login.entidades.JobExecucao;
import com.dhavyd.login.repositorios.JobExecucaoRepository;
import com.dhavyd.login.servico.DeteccaoAusenciaService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JobMeioDia {

    private DeteccaoAusenciaService deteccaoService;
    private JobExecucaoRepository jobExecucaoRepository;

    @Scheduled(cron = "0 55 11 * * *")
    public void executar() {
        LocalDate hoje = LocalDate.now();
        deteccaoService.detectarParaData(hoje);

        JobExecucao job = jobExecucaoRepository.findByNomeJob("JOB_MEIO_DIA")
                .orElse(new JobExecucao(null, "JOB_MEIO_DIA", null));
        job.setUltimaExecucao(LocalDateTime.now());
        jobExecucaoRepository.save(job);
    }
}
