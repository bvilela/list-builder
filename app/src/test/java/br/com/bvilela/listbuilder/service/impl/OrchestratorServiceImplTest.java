package br.com.bvilela.listbuilder.service.impl;

import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.listtype.InvalidListTypeException;
import br.com.bvilela.listbuilder.exception.listtype.RequiredListTypeException;
import br.com.bvilela.listbuilder.exception.listtype.ServiceListTypeNotFoundException;
import br.com.bvilela.listbuilder.service.BaseGenerateService;
import br.com.bvilela.listbuilder.service.assistencia.impl.AssistenciaGenerateServiceImpl;
import br.com.bvilela.listbuilder.service.limpeza.impl.LimpezaGenerateServiceImpl;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
class OrchestratorServiceImplTest {

    @InjectMocks private OrchestratorServiceImpl service;

    @Mock private LimpezaGenerateServiceImpl limpezaService;

    @Mock private AssistenciaGenerateServiceImpl assistenciaService;

    @Mock private Map<String, BaseGenerateService> generateServiceStrategyMap;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        generateServiceStrategyMap = new HashMap<>();
        generateServiceStrategyMap.put("LIMPEZA", limpezaService);
        generateServiceStrategyMap.put("ASSISTENCIA", assistenciaService);
        service = new OrchestratorServiceImpl(generateServiceStrategyMap);
    }

    @Test
    @DisplayName("If ListTypeNull, then throws RequiredListTypeException")
    @SneakyThrows
    void executeListTypeNullShouldBeInvalid() {
        setListType(null);
        Assertions.assertThrows(
                RequiredListTypeException.class, () -> service.validateAndGetServiceByListType());
    }

    @Test
    void executeListTypeEmptyShouldBeInvalid() throws IllegalAccessException {
        setListType("");
        Assertions.assertThrows(
                RequiredListTypeException.class, () -> service.validateAndGetServiceByListType());
    }

    @Test
    void executeListTypeShouldBeInvalid() throws IllegalAccessException {
        setListType("xpto");
        Assertions.assertThrows(
                InvalidListTypeException.class, () -> service.validateAndGetServiceByListType());
    }

    @Test
    @SneakyThrows
    void shouldExecuteServiceNotFound() {
        setListType("DESIGNACAO");
        Assertions.assertThrows(
                ServiceListTypeNotFoundException.class,
                () -> service.validateAndGetServiceByListType());
    }

    @Test
    @SneakyThrows
    void shouldExecuteServiceSuccess() {
        setListType(ListTypeEnum.LIMPEZA.toString());
        var serviceRet = service.validateAndGetServiceByListType();
        Assertions.assertNotNull(serviceRet);
    }

    @SneakyThrows
    private void setListType(String listType) {
        FieldUtils.writeField(service, "listType", listType, true);
    }
}