package br.com.bvilela.listbuilder.validator;

import br.com.bvilela.listbuilder.dto.DateServiceInputDTO;
import br.com.bvilela.listbuilder.dto.audience.FileInputDataAudienceDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class AudienceValidator {

    private AudienceValidator() {}

    public static DateServiceInputDTO validAndConvertData(FileInputDataAudienceDTO dto) {
        log.info("Validando dados de entrada");

        GenericValidator.validateDto(dto);

        dto.setMeetingDayMidweek(
                GenericValidator.validMeetingDay(dto.getMeetingDayMidweek(), "Meio de Semana"));
        dto.setMeetingDayWeekend(
                GenericValidator.validMeetingDay(dto.getMeetingDayWeekend(), "Fim de Semana"));

        var dtoRet = new DateServiceInputDTO(dto);

        log.info("Dados de entrada validados com sucesso!");

        return dtoRet;
    }
}
