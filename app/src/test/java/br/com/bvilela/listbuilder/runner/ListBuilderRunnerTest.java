package br.com.bvilela.listbuilder.runner;

import br.com.bvilela.listbuilder.annotation.NullAndEmptyAndBlankSource;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.listtype.InvalidListTypeException;
import br.com.bvilela.listbuilder.exception.listtype.RequiredListTypeException;
import br.com.bvilela.listbuilder.exception.listtype.ServiceListTypeNotFoundException;
import br.com.bvilela.listbuilder.service.BaseGenerateService;
import br.com.bvilela.listbuilder.service.clearing.ClearingGenerateServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class ListBuilderRunnerTest {

    @InjectMocks
    private ListBuilderRunner runner;

    @Mock
    private Map<String, BaseGenerateService> listTypeStrategyMap;

    @Mock
    private ClearingGenerateServiceImpl clearingService;

    @ParameterizedTest
    @NullAndEmptyAndBlankSource
    void givenRun_whenListTypeNotFilled_thenThrowsException(String listType) {
        setListType(listType);
        assertThrows(RequiredListTypeException.class, () -> runner.run());
        verify(listTypeStrategyMap, never()).get(anyString());
    }

    @Test
    void givenRun_whenListTypeInvalid_thenThrowsException() {
        setListType("xpto");
        assertThrows(InvalidListTypeException.class, () -> runner.run());
        verify(listTypeStrategyMap, never()).get(anyString());
    }

    @Test
    void givenRun_whenListTypeStrategyNotFound_thenThrowsException() {
        setListType(ListTypeEnum.DESIGNACAO.name());
        assertThrows(ServiceListTypeNotFoundException.class, () -> runner.run());
        verify(listTypeStrategyMap, times(1)).get(anyString());
    }

    @Test
    void givenRun_whenListTypeValid_thenSuccessRunApplication() {
        String listType = ListTypeEnum.LIMPEZA.name();
        setListType(listType);
        when(listTypeStrategyMap.get(listType)).thenReturn(clearingService);
        assertDoesNotThrow(() -> runner.run());
        verify(listTypeStrategyMap, times(1)).get(anyString());
    }

    private void setListType(String listType) {
        setField(runner, "listType", listType);
    }
}