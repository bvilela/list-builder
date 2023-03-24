package br.com.bvilela.listbuilder.service.impl;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.exception.listtype.InvalidListTypeException;
import br.com.bvilela.listbuilder.exception.listtype.RequiredListTypeException;
import br.com.bvilela.listbuilder.exception.listtype.ServiceListTypeNotFoundException;
import br.com.bvilela.listbuilder.service.assistencia.impl.AssistenciaGenerateServiceImpl;
import br.com.bvilela.listbuilder.service.assistencia.impl.AssistenciaWriterServiceImpl;
import br.com.bvilela.listbuilder.service.designacao.impl.DesignacaoGenerateServiceImpl;
import br.com.bvilela.listbuilder.service.discurso.impl.DiscursoGenerateServiceImpl;
import br.com.bvilela.listbuilder.service.limpeza.impl.LimpezaGenerateServiceImpl;
import br.com.bvilela.listbuilder.service.limpeza.impl.LimpezaWriterServiceImpl;
import br.com.bvilela.listbuilder.service.vidacrista.impl.VidaCristaGenerateServiceImpl;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.service.NotificationService;
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

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		limpezaService = new LimpezaGenerateServiceImpl(properties, limpezaWriterService, dateService, groupService, notificationService);
		assistenciaService = new AssistenciaGenerateServiceImpl(properties, dateService, assistenciaWriterService, notificationService);
		service = new OrchestratorServiceImpl(limpezaService, assistenciaService, discursoService, vidaCristaService, designacaoService);
	}
	
	@Test
	@DisplayName("If ListTypeNull, then throws RequiredListTypeException")
	@SneakyThrows
	void executeListTypeNullShouldBeInvalid() {
		setListType(null);
    	Assertions.assertThrows(RequiredListTypeException.class, () -> service.validateAndGetServiceByListType());
	}
	
	@Test
	void executeListTypeEmptyShouldBeInvalid() throws IllegalAccessException {
		setListType("");
    	Assertions.assertThrows(RequiredListTypeException.class, () -> service.validateAndGetServiceByListType());
	}
	
	@Test
	void executeListTypeShouldBeInvalid() throws IllegalAccessException {
		setListType("xpto");
    	Assertions.assertThrows(InvalidListTypeException.class, () -> service.validateAndGetServiceByListType());
	}
	
	@Test
	void shouldExecuteServiceNotFound() throws IllegalAccessException {
		setListType(ListTypeEnum.DESIGNACAO.toString());
		Assertions.assertThrows(ServiceListTypeNotFoundException.class, () -> service.validateAndGetServiceByListType());
	}
	
	@Test
	void shouldExecuteServiceSuccess() throws IllegalAccessException, InvalidListTypeException, RequiredListTypeException, ServiceListTypeNotFoundException {
		setListType(ListTypeEnum.LIMPEZA.toString());
		var serviceRet = service.validateAndGetServiceByListType();
		Assertions.assertNotNull(serviceRet);
	}

	@SneakyThrows
	private void setListType(String listType) {
		FieldUtils.writeField(service, "listType", listType, true);
	}
}
