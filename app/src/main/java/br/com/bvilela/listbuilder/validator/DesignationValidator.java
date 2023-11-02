package br.com.bvilela.listbuilder.validator;

import br.com.bvilela.listbuilder.dto.designation.input.DesignationInputDTO;
import br.com.bvilela.listbuilder.dto.designation.writer.DesignationWriterDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DesignationValidator {

    public static void validateData(DesignationInputDTO dto) {
        log.info("Validando Dados de Entrada!");

        GenericValidator.validateParseSubItemsDto(dto);

        dto.setMidweekMeetingDay(
                GenericValidator.validMeetingDay(dto.getMidweekMeetingDay(), "Meio de Semana"));
        dto.setWeekendMeetingDay(
                GenericValidator.validMeetingDay(dto.getWeekendMeetingDay(), "Fim de Semana"));

        log.info("Dados de Entrada Validados com Sucesso!");
    }

    public static void checkDtoWriter(DesignationWriterDTO dtoWriter) {
        log.info("Verificando dados para gerar PDF");
        GenericValidator.validateDto(dtoWriter);
        log.info("Dados verificados com sucesso!");
    }
}
