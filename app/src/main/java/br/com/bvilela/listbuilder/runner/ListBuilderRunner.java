package br.com.bvilela.listbuilder.runner;

import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.listtype.RequiredListTypeException;
import br.com.bvilela.listbuilder.exception.listtype.ServiceListTypeNotFoundException;
import br.com.bvilela.listbuilder.service.BaseGenerateService;
import br.com.bvilela.listbuilder.util.AppUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListBuilderRunner implements CommandLineRunner {

    @Value("${tipo.lista:#{null}}")
    private String listType;

    private final ApplicationContext context;
    private final Map<String, BaseGenerateService> generateServiceStrategyMap;

    @SneakyThrows
    @Override
    public void run(String... args)  {
        try {
            runApplication();
            SpringApplication.exit(context, () -> 0);
        } catch (Exception e) {
            log.error("Erro: {}", e.getMessage());
            log.error("Aplicação Finalizada com Erro!");
            SpringApplication.exit(context, () -> -1);
        }
    }

    public void runApplication() {
        log.info("Aplicação Iniciada...");

        var validListType = validateListType();
        var service = getServiceByListType(validListType);
        service.generateList();

        log.info("Aplicação Finalizada com Sucesso!");
    }

    @SneakyThrows
    private ListTypeEnum validateListType() {
        if (AppUtils.valueIsNullOrBlank(listType)) {
            throw new RequiredListTypeException();
        }
        return ListTypeEnum.getByName(listType);
    }

    @SneakyThrows
    private BaseGenerateService getServiceByListType(ListTypeEnum listType) {
        log.debug("Verificando qual Service utilizar...");

        var strategy = generateServiceStrategyMap.get(listType.toString());

        if (strategy == null) {
            throw new ServiceListTypeNotFoundException(listType);
        }

        log.debug("Utilizando Service: {}", strategy.getClass().getSimpleName());
        return strategy;
    }

}