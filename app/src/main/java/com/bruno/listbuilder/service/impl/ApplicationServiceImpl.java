package com.bruno.listbuilder.service.impl;

import com.bruno.listbuilder.service.ApplicationService;
import com.bruno.listbuilder.service.OrchestratorService;
import lombok.RequiredArgsConstructor;
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
	public void runApplication() {
		try {
			executeApplication();

		} catch (Exception e) {
			log.error("Aplicação Finalizada com Erro!", e);
			SpringApplication.exit(context, () -> -1);
		}
	}

	private void executeApplication() throws Exception {
		log.info("Aplicação Iniciada...");

		var service = orchestratorService.validateAndGetServiceByListType();
		service.generateList();

		log.info("Aplicação Finalizada com Sucesso!");

		SpringApplication.exit(context, () -> 0);
	}

}
