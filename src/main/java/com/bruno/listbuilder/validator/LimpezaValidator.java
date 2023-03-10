package com.bruno.listbuilder.validator;

import com.bruno.listbuilder.dto.DateServiceInputDTO;
import com.bruno.listbuilder.dto.limpeza.FileInputDataLimpezaDTO;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.utils.AppUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class LimpezaValidator {

	private LimpezaValidator() {}
	
	public static DateServiceInputDTO validAndConvertData(FileInputDataLimpezaDTO dto) throws ListBuilderException {
		log.info("Validando dados de entrada");

		GenericValidator.validateDto(dto);

		dto.setMeetingDayMidweek(GenericValidator.validMeetingDay(dto.getMeetingDayMidweek(), "Meio de Semana"));
		dto.setMeetingDayWeekend(GenericValidator.validMeetingDay(dto.getMeetingDayWeekend(), "Fim de Semana"));

		var listRemoveConverted = AppUtils.validAndConvertListDates(dto.getRemoveFromList(), "Remover da Lista", log);
		var mapDatesConverted = AppUtils.validAndConvertMapDates(dto.getAddToList(), "Adicionar na Lista", log);

		var dateServiceDto = new DateServiceInputDTO(dto, listRemoveConverted, mapDatesConverted);

		log.info("Dados de entrada validados com sucesso!");

		return dateServiceDto;
	}

}
