package com.bruno.listbuilder.validator;

import java.util.List;

import com.bruno.listbuilder.dto.designacao.FileInputDataDesignacaoDTO;
import com.bruno.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;
import com.bruno.listbuilder.exception.ListBuilderException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DesignacaoValidator {
	
	private DesignacaoValidator() {
	}

	public static void validAndConvertData(FileInputDataDesignacaoDTO dto) throws ListBuilderException {
		log.info("Validando Dados de Entrada!");
		
		GenericValidator.validateParseSubItemsDto(dto);
		
		dto.setMeetingDayMidweek(GenericValidator.validMeetingDay(dto.getMeetingDayMidweek(), "Meio de Semana"));
		dto.setMeetingDayWeekend(GenericValidator.validMeetingDay(dto.getMeetingDayWeekend(), "Fim de Semana"));
		
		log.info("Dados de Entrada Validados com Sucesso!");
	}

	public static void checkDtoWriter(DesignacaoWriterDTO dtoWriter) throws ListBuilderException {
		
		log.info("Verificando dados para gerar PDF");

		if (listEmpty(dtoWriter.getPresident())) {
			throw new ListBuilderException("Lista de Presidente vazia!");
		}
		
		if (listEmpty(dtoWriter.getReaderWatchtower())) {
			throw new ListBuilderException("Lista de Leitor de A Sentinela vazia!");
		}
		
		if (listEmpty(dtoWriter.getReaderBibleStudy())) {
			throw new ListBuilderException("Lista de Leitor do Estudo Bíblico vazia!");
		}
		
		if (listEmpty(dtoWriter.getAudioVideo())) {
			throw new ListBuilderException("Lista de AudioVideo vazia!");
		}
		
		if (listEmpty(dtoWriter.getMicrophone())) {
			throw new ListBuilderException("Lista de Microfone Volante vazia!");
		}
		
		if (listEmpty(dtoWriter.getIndicator())) {
			throw new ListBuilderException("Lista de Indicador vazia!");
		}
		
		log.info("Dados verificados com sucesso!");
		
	}
	
	private static <T> boolean listEmpty(List<T> list) {
		return (list == null || list.isEmpty()); 
	}
	
}
