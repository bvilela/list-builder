package br.com.bvilela.listbuilder.validator;

import br.com.bvilela.listbuilder.dto.audience.AudienceInputDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class AudienceValidator {

    private AudienceValidator() {}

    public static void validateData(AudienceInputDTO dto) {
        log.info("Validando dados de entrada");

        GenericValidator.validateDto(dto);

        dto.setMidweekMeetingDay(
                GenericValidator.validMeetingDay(dto.getMidweekMeetingDay(), "Meio de Semana"));
        dto.setWeekendMeetingDay(
                GenericValidator.validMeetingDay(dto.getWeekendMeetingDay(), "Fim de Semana"));

        log.info("Dados de entrada validados com sucesso!");
    }
}
