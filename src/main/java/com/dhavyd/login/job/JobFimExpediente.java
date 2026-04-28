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
public class JobFimExpediente {

    private static final int MAX_DIAS_CATCHUP = 5;

    private DeteccaoAusenciaService deteccaoService;
    private JobExecucaoRepository jobExecucaoRepository;

    @Scheduled(cron = "0 55 17 * * *")
    public void executar() {
        LocalDate hoje = LocalDate.now();

        deteccaoService.detectarParaData(hoje);

        JobExecucao job = jobExecucaoRepository.findByNomeJob("JOB_FIM_EXPEDIENTE")
                .orElse(new JobExecucao(null, "JOB_FIM_EXPEDIENTE", null));

        LocalDateTime ultimaExec = job.getUltimaExecucao();
        if (ultimaExec != null) {
            LocalDate ultimaData = ultimaExec.toLocalDate();
            int diasCatchup = 0;

            for (LocalDate data = ultimaData.plusDays(1);
                    data.isBefore(hoje) && diasCatchup < MAX_DIAS_CATCHUP;
                    data = data.plusDays(1)) {
                deteccaoService.detectarParaData(data);
                diasCatchup++;
            }
        } else {
            deteccaoService.detectarParaData(hoje.minusDays(1));
        }

        job.setUltimaExecucao(LocalDateTime.now());
        jobExecucaoRepository.save(job);
    }
}
