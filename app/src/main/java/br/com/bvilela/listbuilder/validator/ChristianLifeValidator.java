package br.com.bvilela.listbuilder.validator;

import br.com.bvilela.listbuilder.dto.christianlife.input.ChristianLifeInputDTO;
import br.com.bvilela.listbuilder.util.AppUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChristianLifeValidator {

    @SneakyThrows
    public static void validInputDto(ChristianLifeInputDTO dto) {
        log.info("Validando Dados de Entrada!");
        GenericValidator.validateParseDto(dto);
        var mapDatesConverted =
                AppUtils.validAndConvertMapDates(
                        dto.getRemoveWeekFromList(), "Remover da Lista", log);
        dto.setRemoveWeekFromListConverted(mapDatesConverted);
        log.info("Dados de Entrada Validados com Sucesso!");
    }
}
