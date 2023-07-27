package br.com.bvilela.listbuilder.validator;

import br.com.bvilela.listbuilder.dto.audience.FileInputDataAudienceDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class AudienceValidator {

    private AudienceValidator() {}

    public static void validateData(FileInputDataAudienceDTO dto) {
        log.info("Validando dados de entrada");

        GenericValidator.validateDto(dto);

        dto.setMeetingDayMidweek(
                GenericValidator.validMeetingDay(dto.getMeetingDayMidweek(), "Meio de Semana"));
        dto.setMeetingDayWeekend(
                GenericValidator.validMeetingDay(dto.getMeetingDayWeekend(), "Fim de Semana"));

        log.info("Dados de entrada validados com sucesso!");
    }
}
