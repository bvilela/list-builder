package br.com.bvilela.listbuilder.validator;

import br.com.bvilela.listbuilder.dto.vidacrista.FileInputDataVidaCristaDTO;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.utils.AppUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VidaCristaValidator {

	public static void validInputDto(FileInputDataVidaCristaDTO dto) throws ListBuilderException {
		log.info("Validando Dados de Entrada!");
		GenericValidator.validateParseDto(dto);
		var mapDatesConverted = AppUtils.validAndConvertMapDates(dto.getRemoveWeekFromList(), "Remover da Lista", log);
		dto.setRemoveWeekFromListConverted(mapDatesConverted);
		log.info("Dados de Entrada Validados com Sucesso!");
	}
	
}
