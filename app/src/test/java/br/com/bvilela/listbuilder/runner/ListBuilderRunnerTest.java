package br.com.bvilela.listbuilder.runner;

import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.listtype.InvalidListTypeException;
import br.com.bvilela.listbuilder.exception.listtype.RequiredListTypeException;
import br.com.bvilela.listbuilder.exception.listtype.ServiceListTypeNotFoundException;
import br.com.bvilela.listbuilder.service.BaseGenerateService;
import br.com.bvilela.listbuilder.service.audience.AudienceGenerateServiceImpl;
import br.com.bvilela.listbuilder.service.clearing.ClearingGenerateServiceImpl;
import br.com.bvilela.listbuilder.util.annotation.NullAndBlankSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
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

    private ListBuilderRunner runner;
    @Mock
    private ApplicationContext context;
    @Mock
    private ClearingGenerateServiceImpl clearingService;
    @Mock
    private AudienceGenerateServiceImpl audienceService;

    @BeforeEach
    public void setup() {
        Map<String, BaseGenerateService> serviceMap = new HashMap<>();
        serviceMap.put(ListTypeEnum.LIMPEZA.name(), clearingService);
        serviceMap.put(ListTypeEnum.ASSISTENCIA.name(), audienceService);
        runner = new ListBuilderRunner(context, serviceMap);
    }

    @ParameterizedTest
    @NullAndBlankSource
    void givenRun_whenListTypeNotFilled_ThenThrowsException(String listType) {
        setListType(listType);
        runner.run();
        assertThrows(RequiredListTypeException.class, () -> runner.run());
    }

    @Test
    void givenRun_whenListTypeInvalid_ThenThrowsException() {
        setListType("xpto");
        assertThrows(InvalidListTypeException.class, () -> runner.run());
    }

    @Test
    void givenRun_whenListTypeStrategyNotFound_ThenThrowsException() {
        setListType(ListTypeEnum.DESIGNACAO.toString());
        assertThrows(ServiceListTypeNotFoundException.class, () -> runner.run());
    }

    @Test
    void givenRun_whenListTypeValid_ThenSuccessRunApplication() {
        setListType(ListTypeEnum.LIMPEZA.toString());
        assertDoesNotThrow(() -> runner.run());
    }

    private void setListType(String listType) {
        setField(runner, "listType", listType);
    }
}