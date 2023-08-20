package br.com.bvilela.listbuilder.service.impl;

import br.com.bvilela.listbuilder.service.OrchestratorService;
import br.com.bvilela.listbuilder.service.clearing.ClearingGenerateServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
class ApplicationServiceImplTest {

    @InjectMocks private ApplicationServiceImpl applicationService;

    @Mock private ApplicationContext context;

    @Mock private OrchestratorService orchestratorService;

    @Mock private ClearingGenerateServiceImpl limpezaService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        applicationService = new ApplicationServiceImpl(context, orchestratorService);
    }

    @Test
    void shouldRunApplicationSuccess() {
        Mockito.when(orchestratorService.validateAndGetServiceByListType())
                .thenReturn(limpezaService);
        Assertions.assertDoesNotThrow(() -> applicationService.runApplication());
    }
}
