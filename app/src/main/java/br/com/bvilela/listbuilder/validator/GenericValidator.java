package br.com.bvilela.listbuilder.validator;

import java.lang.reflect.InvocationTargetException;

import javax.validation.ConstraintViolation;

import br.com.bvilela.listbuilder.dto.designacao.FileInputDataDesignacaoListDTO;
import br.com.bvilela.listbuilder.dto.designacao.FileInputDataDesignacaoReaderDTO;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.utils.AppUtils;
import br.com.bvilela.listbuilder.utils.DateUtils;
import com.bvilela.utils.ValidationUtils;

public final class GenericValidator {

	private GenericValidator() {
	}

	public static String validMeetingDay(String meetingDay, String msg) throws ListBuilderException {
		meetingDay = AppUtils.removeAccents(meetingDay);
		if (!DateUtils.validDayOfWeek(meetingDay)) {
			throw new ListBuilderException("Dia da Reunião de %s - Valor '%s' não é um Dia da Semana válido!", msg,
					meetingDay);
		}
		return meetingDay;
	}
	
	public static <T> void validateDto(T dto) throws ListBuilderException {
		var violations = ValidationUtils.validateDto(dto);

		if (!violations.isEmpty()) {
			var errors = violations.stream().map(ConstraintViolation::getMessage).toList();
			throw new ListBuilderException("%s", errors.get(0));
		}
	}

	public static <T> void validateParseDto(T dto) throws ListBuilderException {
		try {
			var violations = ValidationUtils.validateParseDto(dto);
			if (!violations.isEmpty()) {
				var errors = violations.stream().map(ConstraintViolation::getMessage).toList();
				throw new ListBuilderException("%s", errors.get(0));
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new ListBuilderException(e.getMessage());
		}
	}
	
	public static <T> void validateParseSubItemsDto(T dto) throws ListBuilderException {		
		try {
			var violations = ValidationUtils.validateParseDto(dto);
			if (!violations.isEmpty()) {
				var errors = violations.stream().map(e -> new RecordError(e.getPropertyPath().toString(), e.getMessage(), e.getLeafBean().getClass())).toList();
				throw new ListBuilderException("%s", errors.get(0).toString());
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new ListBuilderException(e.getMessage());
		}
	}
	
	private record RecordError(String path, String msg, Class<?> myClass) {
	    @Override
	    public String toString() {
	    	if (myClass.equals(FileInputDataDesignacaoListDTO.class) || myClass.equals(FileInputDataDesignacaoReaderDTO.class)) {
		    	var pathPT = path.contains("president") ? "Presidente" : path;
		    	pathPT = pathPT.contains("audioVideo") ? "Aúdio e Vídeo" : pathPT;
		    	pathPT = pathPT.contains("reader.watchtower") ? "Leitor A Sentinela" : pathPT;
		    	pathPT = pathPT.contains("reader.bibleStudy") ? "Leitor Estudo Bíblico" : pathPT;
		        return String.format("%s: %s", pathPT, msg);
	    	}
	    	return String.format("%s", msg);
	    }
	}

}
