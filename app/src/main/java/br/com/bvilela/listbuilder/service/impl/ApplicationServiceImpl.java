package br.com.bvilela.listbuilder.service.impl;

import br.com.bvilela.listbuilder.service.ApplicationService;
import br.com.bvilela.listbuilder.service.OrchestratorService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationContext context;
    private final OrchestratorService orchestratorService;

    @Override
    @SneakyThrows
    public void runApplication() {
        try {
            executeApplication();
        } catch (Exception e) {
            log.error("Aplicação Finalizada com Erro!");
            SpringApplication.exit(context, () -> -1);
        }
    }

    @SneakyThrows
    private void executeApplication() {
        log.info("Aplicação Iniciada...");

        var service = orchestratorService.validateAndGetServiceByListType();
        service.generateList();

        log.info("Aplicação Finalizada com Sucesso!");

        SpringApplication.exit(context, () -> 0);
    }
}
