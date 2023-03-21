package com.bruno.listbuilder.service.impl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.exception.listtype.InvalidListTypeException;
import com.bruno.listbuilder.exception.listtype.RequiredListTypeException;
import com.bruno.listbuilder.exception.listtype.ServiceListTypeNotFoundException;
import com.bruno.listbuilder.service.BaseGenerateService;
import com.bruno.listbuilder.service.OrchestratorService;
import com.bruno.listbuilder.service.assistencia.impl.AssistenciaGenerateServiceImpl;
import com.bruno.listbuilder.service.designacao.impl.DesignacaoGenerateServiceImpl;
import com.bruno.listbuilder.service.discurso.impl.DiscursoGenerateServiceImpl;
import com.bruno.listbuilder.service.limpeza.impl.LimpezaGenerateServiceImpl;
import com.bruno.listbuilder.service.vidacrista.impl.VidaCristaGenerateServiceImpl;
import com.bruno.listbuilder.utils.AppUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrchestratorServiceImpl implements OrchestratorService {

	@Value("${tipo.lista:#{null}}")
	private String listType;

	private final LimpezaGenerateServiceImpl limpezaService;
	private final AssistenciaGenerateServiceImpl assistenciaService;
	private final DiscursoGenerateServiceImpl discursoService;
	private final VidaCristaGenerateServiceImpl vidaCristaService;
	private final DesignacaoGenerateServiceImpl designacaoService;

	@Override
	public BaseGenerateService validateAndGetServiceByListType()
			throws RequiredListTypeException, InvalidListTypeException, ServiceListTypeNotFoundException {
		var validListType = validateListType();
		return getServiceByListType(validListType);
	}

	private ListTypeEnum validateListType() throws InvalidListTypeException, RequiredListTypeException {
		if (AppUtils.valueIsNullOrBlank(listType)) {
			throw new RequiredListTypeException();
		}
		return ListTypeEnum.getByName(listType);
	}

	private BaseGenerateService getServiceByListType(ListTypeEnum listType) throws ServiceListTypeNotFoundException {
		log.info("Verificando qual Service utilizar...");
		
		var listServices = List.of(limpezaService, assistenciaService, discursoService, vidaCristaService,
				designacaoService);
		
		var optionalService = listServices.stream().filter(s -> s.getListType() == listType).findFirst();
		
		if (optionalService.isPresent()) {
			log.info("Utilizando Service: {}", optionalService.get().getClass().getSimpleName());
			return optionalService.get();
		}

		throw new ServiceListTypeNotFoundException(listType);
	}
}
