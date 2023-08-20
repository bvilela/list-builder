package br.com.bvilela.listbuilder.runner;

import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.listtype.InvalidListTypeException;
import br.com.bvilela.listbuilder.exception.listtype.RequiredListTypeException;
import br.com.bvilela.listbuilder.exception.listtype.ServiceListTypeNotFoundException;
import br.com.bvilela.listbuilder.service.BaseGenerateService;
import br.com.bvilela.listbuilder.service.audience.AudienceGenerateServiceImpl;
import br.com.bvilela.listbuilder.service.clearing.ClearingGenerateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class ListBuilderRunnerTest {

    @InjectMocks private ListBuilderRunner runner;
    @Mock private ApplicationContext context;
    @Mock private ClearingGenerateServiceImpl limpezaService;
    @Mock private AudienceGenerateServiceImpl assistenciaService;

    @BeforeEach
    public void setup() {
        Map<String, BaseGenerateService> serviceMap = new HashMap<>();
        serviceMap.put(ListTypeEnum.LIMPEZA.name(), limpezaService);
        serviceMap.put(ListTypeEnum.ASSISTENCIA.name(), assistenciaService);
        runner = new ListBuilderRunner(context, serviceMap);
    }

    @Test
    @DisplayName("If ListTypeNull, then throws RequiredListTypeException")
    void executeListTypeNullShouldBeInvalid() {
        setListType(null);
        assertThrows(RequiredListTypeException.class, () -> runner.runApplication());
    }

    @Test
    void executeListTypeEmptyShouldBeInvalid() {
        setListType("");
        assertThrows(RequiredListTypeException.class, () -> runner.runApplication());
    }

    @Test
    void executeListTypeShouldBeInvalid() {
        setListType("xpto");
        assertThrows(InvalidListTypeException.class, () -> runner.runApplication());
    }

    @Test
    void shouldExecuteServiceNotFound() {
        setListType(ListTypeEnum.DESIGNACAO.toString());
        assertThrows(
                ServiceListTypeNotFoundException.class,
                () -> runner.runApplication());
    }

    @Test
    void shouldExecuteServiceSuccess() {
        setListType(ListTypeEnum.LIMPEZA.toString());
        runner.run();
        assertDoesNotThrow(() -> runner.runApplication());
    }

    private void setListType(String listType) {
        setField(runner, "listType", listType);
    }
}