package br.com.bvilela.listbuilder.runner;

import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.listtype.InvalidListTypeException;
import br.com.bvilela.listbuilder.exception.listtype.RequiredListTypeException;
import br.com.bvilela.listbuilder.exception.listtype.ServiceListTypeNotFoundException;
import br.com.bvilela.listbuilder.service.BaseGenerateService;
import br.com.bvilela.listbuilder.util.AppUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ListBuilderRunner implements CommandLineRunner {

    @Value("${tipo.lista:#{null}}")
    private String listType;

    private final ApplicationContext context;
    private final Map<String, BaseGenerateService> listTypeStrategyMap;

    @Override
    public void run(String... args)  {
        try {
            log.info("Aplicação Iniciada...");

            var validListType = validateListType();
            var service = getServiceByListType(validListType);
            service.generateList();

            log.info("Aplicação Finalizada com Sucesso!");
        } catch (Exception e) {
            log.error("Aplicação Finalizada com Erro!");
            SpringApplication.exit(context, () -> -1);
        }
    }

    private ListTypeEnum validateListType() {
        if (AppUtils.valueIsNullOrBlank(listType)) {
            log.error("Tipo da Lista não informado!");
            throw new RequiredListTypeException();
        }

        try {
            return ListTypeEnum.getByName(listType);
        } catch (IllegalArgumentException e) {
            log.error("Tipo da Lista inválido! Valores aceitos: " + ListTypeEnum.valuesToString());
            throw new InvalidListTypeException();
        }
    }

    private BaseGenerateService getServiceByListType(ListTypeEnum listType) {
        log.debug("Verificando qual Service utilizar...");

        var strategy = listTypeStrategyMap.get(listType.toString());
        if (strategy == null) {
            log.error("Nenhum Service encontrado para a lista: " + listType);
            throw new ServiceListTypeNotFoundException();
        }

        log.debug("Utilizando Service: {}", strategy.getClass().getSimpleName());
        return strategy;
    }

}