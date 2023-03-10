package com.bruno.listbuilder.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bruno.listbuilder.config.AppProperties;
import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.exception.listtype.InvalidListTypeException;
import com.bruno.listbuilder.exception.listtype.RequiredListTypeException;
import com.bruno.listbuilder.exception.listtype.ServiceListTypeNotFoundException;
import com.bruno.listbuilder.service.ConvertImageService;
import com.bruno.listbuilder.service.NotificationService;
import com.bruno.listbuilder.service.assistencia.impl.AssistenciaGenerateServiceImpl;
import com.bruno.listbuilder.service.assistencia.impl.AssistenciaWriterServiceImpl;
import com.bruno.listbuilder.service.designacao.impl.DesignacaoGenerateServiceImpl;
import com.bruno.listbuilder.service.discurso.impl.DiscursoGenerateServiceImpl;
import com.bruno.listbuilder.service.limpeza.impl.LimpezaGenerateServiceImpl;
import com.bruno.listbuilder.service.limpeza.impl.LimpezaWriterServiceImpl;
import com.bruno.listbuilder.service.vidacrista.impl.VidaCristaGenerateServiceImpl;

@SpringBootApplication
class OrchestratorServiceImplTest {

	@InjectMocks
	private OrchestratorServiceImpl service;
	
	@InjectMocks
	private LimpezaGenerateServiceImpl limpezaService;
	
	@InjectMocks
	private AssistenciaGenerateServiceImpl assistenciaService;
	
	@Mock
	private DiscursoGenerateServiceImpl discursoService;
	
	@Mock
	private VidaCristaGenerateServiceImpl vidaCristaService;
	
	@Mock
	private AppProperties properties;
	
	@Mock
	private DateServiceImpl dateService;
	
	@Mock
	private GroupServiceImpl groupService;
	
	@Mock
	private LimpezaWriterServiceImpl limpezaWriterService;
	
	@Mock
	private AssistenciaWriterServiceImpl assistenciaWriterService;
	
	@Mock
	private DesignacaoGenerateServiceImpl designacaoService;
	
	@Mock
	private NotificationService notificationService;
	
	@Mock
	private ConvertImageService convertImageService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		limpezaService = new LimpezaGenerateServiceImpl(properties, dateService, groupService, limpezaWriterService, notificationService, convertImageService);
		assistenciaService = new AssistenciaGenerateServiceImpl(properties, dateService, assistenciaWriterService, notificationService);
		service = new OrchestratorServiceImpl(limpezaService, assistenciaService, discursoService, vidaCristaService, designacaoService);
	}
	
	@Test
	void executeListTypeNullShouldBeInvalid() throws IllegalAccessException {
		setListType(null);
    	assertThrows(RequiredListTypeException.class, () -> service.validateAndGetServiceByListType());
	}
	
	@Test
	void executeListTypeEmptyShouldBeInvalid() throws IllegalAccessException {
		setListType("");
    	assertThrows(RequiredListTypeException.class, () -> service.validateAndGetServiceByListType());
	}
	
	@Test
	void executeListTypeShouldBeInvalid() throws IllegalAccessException {
		setListType("xpto");
    	assertThrows(InvalidListTypeException.class, () -> service.validateAndGetServiceByListType());
	}
	
	@Test
	void shouldExecuteServiceNotFound() throws IllegalAccessException {
		setListType(ListTypeEnum.DESIGNACAO.toString());
		assertThrows(ServiceListTypeNotFoundException.class, () -> service.validateAndGetServiceByListType());
	}
	
	@Test
	void shouldExecuteServiceSuccess() throws IllegalAccessException, InvalidListTypeException, RequiredListTypeException, ServiceListTypeNotFoundException {
		setListType(ListTypeEnum.LIMPEZA.toString());
		var serviceRet = service.validateAndGetServiceByListType();
		assertNotNull(serviceRet);
	}
	
	private void setListType(String listType) throws IllegalAccessException {
		FieldUtils.writeField(service, "listType", listType, true);
	}
}
