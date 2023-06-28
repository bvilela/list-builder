package br.com.bvilela.listbuilder.service.impl;

import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.listtype.InvalidListTypeException;
import br.com.bvilela.listbuilder.exception.listtype.RequiredListTypeException;
import br.com.bvilela.listbuilder.exception.listtype.ServiceListTypeNotFoundException;
import br.com.bvilela.listbuilder.service.BaseGenerateService;
import br.com.bvilela.listbuilder.service.audience.impl.AudienceGenerateServiceImpl;
import br.com.bvilela.listbuilder.service.limpeza.impl.LimpezaGenerateServiceImpl;
import java.util.HashMap;
import java.util.Map;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootApplication
class OrchestratorServiceImplTest {

    @InjectMocks private OrchestratorServiceImpl service;

    @Mock private LimpezaGenerateServiceImpl limpezaService;

    @Mock private AudienceGenerateServiceImpl assistenciaService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        Map<String, BaseGenerateService> generateServiceStrategyMap = new HashMap<>();
        generateServiceStrategyMap.put(ListTypeEnum.LIMPEZA.name(), limpezaService);
        generateServiceStrategyMap.put(ListTypeEnum.ASSISTENCIA.name(), assistenciaService);
        service = new OrchestratorServiceImpl(generateServiceStrategyMap);
    }

    @Test
    @DisplayName("If ListTypeNull, then throws RequiredListTypeException")
    @SneakyThrows
    void executeListTypeNullShouldBeInvalid() {
        setListType(null);
        assertThrows(
                RequiredListTypeException.class, () -> service.validateAndGetServiceByListType());
    }

    @Test
    void executeListTypeEmptyShouldBeInvalid() {
        setListType("");
        assertThrows(
                RequiredListTypeException.class, () -> service.validateAndGetServiceByListType());
    }

    @Test
    void executeListTypeShouldBeInvalid() {
        setListType("xpto");
        assertThrows(
                InvalidListTypeException.class, () -> service.validateAndGetServiceByListType());
    }

    @Test
    @SneakyThrows
    void shouldExecuteServiceNotFound() {
        setListType(ListTypeEnum.DESIGNACAO.toString());
        assertThrows(
                ServiceListTypeNotFoundException.class,
                () -> service.validateAndGetServiceByListType());
    }

    @Test
    @SneakyThrows
    void shouldExecuteServiceSuccess() {
        setListType(ListTypeEnum.LIMPEZA.toString());
        var serviceRet = service.validateAndGetServiceByListType();
        assertNotNull(serviceRet);
    }

    @SneakyThrows
    private void setListType(String listType) {
        FieldUtils.writeField(service, "listType", listType, true);
    }
}
