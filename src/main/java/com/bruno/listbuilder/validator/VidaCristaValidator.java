package com.bruno.listbuilder.validator;

import com.bruno.listbuilder.dto.vidacrista.FileInputDataVidaCristaDTO;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.utils.AppUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VidaCristaValidator {
	
	private VidaCristaValidator() {
	}

	public static void validInputDto(FileInputDataVidaCristaDTO dto) throws ListBuilderException {
		log.info("Validando Dados de Entrada!");
		GenericValidator.validateParseDto(dto);
		var mapDatesConverted = AppUtils.validAndConvertMapDates(dto.getRemoveWeekFromList(), "Remover da Lista", log);
		dto.setRemoveWeekFromListConverted(mapDatesConverted);
		log.info("Dados de Entrada Validados com Sucesso!");
	}
	
}
