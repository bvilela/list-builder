package br.com.bvilela.listbuilder.validator;

import br.com.bvilela.listbuilder.dto.clearing.input.ClearingInputDTO;
import br.com.bvilela.listbuilder.dto.util.DateServiceInputDTO;
import br.com.bvilela.listbuilder.util.AppUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ClearingValidator {

    private ClearingValidator() {}

    @SneakyThrows
    public static DateServiceInputDTO validAndConvertData(ClearingInputDTO dto) {
        log.info("Validando dados de entrada");

        GenericValidator.validateDto(dto);

        dto.setMeetingDayMidweek(
                GenericValidator.validMeetingDay(dto.getMeetingDayMidweek(), "Meio de Semana"));
        dto.setMeetingDayWeekend(
                GenericValidator.validMeetingDay(dto.getMeetingDayWeekend(), "Fim de Semana"));

        var listRemoveConverted =
                AppUtils.validAndConvertListDates(dto.getRemoveFromList(), "Remover da Lista", log);
        var mapDatesConverted =
                AppUtils.validAndConvertMapDates(dto.getAddToList(), "Adicionar na Lista", log);

        var dateServiceDto = new DateServiceInputDTO(dto, listRemoveConverted, mapDatesConverted);

        log.info("Dados de entrada validados com sucesso!");

        return dateServiceDto;
    }
}
