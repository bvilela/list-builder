package br.com.bvilela.listbuilder.service.impl;

import br.com.bvilela.listbuilder.exception.listtype.InvalidListTypeException;
import br.com.bvilela.listbuilder.exception.listtype.RequiredListTypeException;
import br.com.bvilela.listbuilder.exception.listtype.ServiceListTypeNotFoundException;
import br.com.bvilela.listbuilder.service.limpeza.impl.LimpezaGenerateServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import br.com.bvilela.listbuilder.service.OrchestratorService;

@SpringBootApplication
class ApplicationServiceImplTest {

	@InjectMocks
	private ApplicationServiceImpl applicationService;
	
	@Mock
	private ApplicationContext context;
	
	@Mock
	private OrchestratorService orchestratorService;
	
	@Mock
	private LimpezaGenerateServiceImpl limpezaService;
	
	@BeforeEach
	public void setup() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		applicationService = new ApplicationServiceImpl(context, orchestratorService);
	}
	
	@Test
	void shouldRunApplicationSuccess() throws InvalidListTypeException, RequiredListTypeException, ServiceListTypeNotFoundException {
		Mockito.when(orchestratorService.validateAndGetServiceByListType()).thenReturn(limpezaService);
		Assertions.assertDoesNotThrow(() -> applicationService.runApplication());
	}
	
	@Test
	void shouldRunApplicationException() throws InvalidListTypeException, RequiredListTypeException, ServiceListTypeNotFoundException {
		Mockito.when(orchestratorService.validateAndGetServiceByListType()).thenThrow(new RequiredListTypeException());
		Assertions.assertDoesNotThrow(() -> applicationService.runApplication());
	}
}
