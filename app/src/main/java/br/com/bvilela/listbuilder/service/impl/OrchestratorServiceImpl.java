package br.com.bvilela.listbuilder.service.impl;

import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.listtype.InvalidListTypeException;
import br.com.bvilela.listbuilder.exception.listtype.RequiredListTypeException;
import br.com.bvilela.listbuilder.exception.listtype.ServiceListTypeNotFoundException;
import br.com.bvilela.listbuilder.service.BaseGenerateService;
import br.com.bvilela.listbuilder.service.OrchestratorService;
import br.com.bvilela.listbuilder.utils.AppUtils;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrchestratorServiceImpl implements OrchestratorService {

    @Value("${tipo.lista:#{null}}")
    private String listType;

    private final Map<String, BaseGenerateService> generateServiceStrategyMap;

    @Override
    public BaseGenerateService validateAndGetServiceByListType()
            throws RequiredListTypeException, InvalidListTypeException,
                    ServiceListTypeNotFoundException {
        var validListType = validateListType();
        return getServiceByListType(validListType);
    }

    private ListTypeEnum validateListType()
            throws InvalidListTypeException, RequiredListTypeException {
        if (AppUtils.valueIsNullOrBlank(listType)) {
            throw new RequiredListTypeException();
        }
        return ListTypeEnum.getByName(listType);
    }

    private BaseGenerateService getServiceByListType(ListTypeEnum listType)
            throws ServiceListTypeNotFoundException {
        log.info("Verificando qual Service utilizar...");

        var strategy = generateServiceStrategyMap.get(listType.toString());

        if (strategy == null) {
            throw new ServiceListTypeNotFoundException(listType);
        }

        log.info("Utilizando Service: {}", strategy.getClass().getSimpleName());
        return strategy;
    }
}
