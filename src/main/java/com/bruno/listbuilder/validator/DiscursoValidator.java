package com.bruno.listbuilder.validator;

import java.util.List;
import java.util.Objects;

import com.bruno.listbuilder.config.MessageConfig;
import com.bruno.listbuilder.dto.discurso.DiscursoAllThemesDTO;
import com.bruno.listbuilder.dto.discurso.FileInputDataDiscursoDTO;
import com.bruno.listbuilder.dto.discurso.FileInputDataDiscursoItemDTO;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.utils.AppUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DiscursoValidator {

	private DiscursoValidator() {
	}
	
	public static void validAllThemesFile(DiscursoAllThemesDTO dto) throws ListBuilderException {
		log.info("Arquivos de Temas - Validando Dados de Entrada!");
		GenericValidator.validateDto(dto);
		log.info("Arquivos de Temas - Validado com Sucesso!");		
	}

	public static void validFileInputData(FileInputDataDiscursoDTO dto) throws ListBuilderException {
		log.info("Validando Dados de Entrada!");
		
		GenericValidator.validateDto(dto);
		
		if (AppUtils.listIsNullOrEmpty(dto.getReceive()) && AppUtils.listIsNullOrEmpty(dto.getSend())) {
			throw new ListBuilderException(MessageConfig.LIST_SEND_REVEICE_NULL);
		}
		
		validItem(dto.getReceive(), "Lista Receber");
		validItem(dto.getSend(), "Lista Enviar");
		
		log.info("Dados de Entrada Validados com Sucesso!");
	}

	private static void validItem(List<FileInputDataDiscursoItemDTO> list, String message) throws ListBuilderException {
		log.info("{} - Validando", message);
		
		if (AppUtils.listIsNullOrEmpty(list)) {
			log.info("{} - Vazia. Validacao Abortada", message);
			return;
		}

		for (FileInputDataDiscursoItemDTO item : list) {
			GenericValidator.validateDto(item);
			validTheme(item);
		}

		log.info("{} - Validada com Sucesso!", message);
	}

	private static void validTheme(FileInputDataDiscursoItemDTO item) throws ListBuilderException {
		var themeNumberMissing = Objects.isNull(item.getThemeNumber()) || item.getThemeNumber().isBlank();
		var themeTitleMissing = Objects.isNull(item.getThemeTitle()) || item.getThemeTitle().isBlank();

		if (themeNumberMissing && themeTitleMissing) {
			throw new ListBuilderException("Data: %s - Informe o N??mero do Tema ou T??tulo!", item.getDate());
		}

		if (!themeNumberMissing) {
			try {
				Integer.parseInt(item.getThemeNumber());
			} catch (NumberFormatException ex) {
				throw new ListBuilderException("%s - Numero do Tema invalido. N??o ?? um n??mero v??lido!",
						item.getDate());
			}
		}
	}

}
