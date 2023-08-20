package br.com.bvilela.listbuilder.validator;

import br.com.bvilela.lib.utils.ValidationUtils;
import br.com.bvilela.listbuilder.dto.designation.input.DesignationInputReaderDTO;
import br.com.bvilela.listbuilder.dto.util.InputListDTO;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.util.AppUtils;
import br.com.bvilela.listbuilder.util.DateUtils;
import java.lang.reflect.InvocationTargetException;
import javax.validation.ConstraintViolation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GenericValidator {

    @SneakyThrows
    public static String validMeetingDay(String meetingDay, String msg) {
        meetingDay = AppUtils.removeAccents(meetingDay);
        if (!DateUtils.validDayOfWeek(meetingDay)) {
            throw new ListBuilderException(
                    "Dia da Reunião de %s - Valor '%s' não é um Dia da Semana válido!",
                    msg, meetingDay);
        }
        return meetingDay;
    }

    @SneakyThrows
    public static <T> void validateDto(T dto) {
        var violations = ValidationUtils.validateDto(dto);

        if (!violations.isEmpty()) {
            var errors = violations.stream().map(ConstraintViolation::getMessage).toList();
            throw new ListBuilderException("%s", errors.get(0));
        }
    }

    @SneakyThrows
    public static <T> void validateParseDto(T dto) {
        try {
            var violations = ValidationUtils.validateParseDto(dto);
            if (!violations.isEmpty()) {
                var errors = violations.stream().map(ConstraintViolation::getMessage).toList();
                throw new ListBuilderException("%s", errors.get(0));
            }
        } catch (NoSuchMethodException
                | SecurityException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException e) {
            throw new ListBuilderException(e.getMessage());
        }
    }

    @SneakyThrows
    public static <T> void validateParseSubItemsDto(T dto) {
        try {
            var violations = ValidationUtils.validateParseDto(dto);
            if (!violations.isEmpty()) {
                var errors =
                        violations.stream()
                                .map(
                                        e ->
                                                new ErrorDTO(
                                                        e.getPropertyPath().toString(),
                                                        e.getMessage(),
                                                        e.getLeafBean().getClass()))
                                .toList();
                throw new ListBuilderException("%s", errors.get(0).toString());
            }
        } catch (NoSuchMethodException
                | SecurityException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException e) {
            throw new ListBuilderException(e.getMessage());
        }
    }

    @AllArgsConstructor
    private static class ErrorDTO {
        String path;
        String msg;
        Class<?> myClass;

        @Override
        public String toString() {
            if (myClass.equals(InputListDTO.class)
                    || myClass.equals(DesignationInputReaderDTO.class)) {
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
