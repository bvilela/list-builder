package br.com.bvilela.listbuilder.validator;

import br.com.bvilela.listbuilder.config.MessageConfig;
import br.com.bvilela.listbuilder.dto.discourse.InputAllThemesDiscourseDTO;
import br.com.bvilela.listbuilder.dto.discourse.InputDiscourseDTO;
import br.com.bvilela.listbuilder.dto.discourse.InputDiscourseItemDTO;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.utils.AppUtils;
import java.util.List;
import java.util.Objects;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DiscursoValidator {

    private DiscursoValidator() {}

    public static void validAllThemesFile(InputAllThemesDiscourseDTO dto) {
        log.info("Arquivos de Temas - Validando Dados de Entrada!");
        GenericValidator.validateDto(dto);
        log.info("Arquivos de Temas - Validado com Sucesso!");
    }

    @SneakyThrows
    public static void validFileInputData(InputDiscourseDTO dto) {
        log.info("Validando Dados de Entrada!");

        GenericValidator.validateDto(dto);

        if (AppUtils.listIsNullOrEmpty(dto.getReceive())
                && AppUtils.listIsNullOrEmpty(dto.getSend())) {
            throw new ListBuilderException(MessageConfig.LIST_SEND_REVEICE_NULL);
        }

        validItem(dto.getReceive(), "Lista Receber");
        validItem(dto.getSend(), "Lista Enviar");

        log.info("Dados de Entrada Validados com Sucesso!");
    }

    private static void validItem(List<InputDiscourseItemDTO> list, String message) {
        log.info("{} - Validando", message);

        if (AppUtils.listIsNullOrEmpty(list)) {
            log.info("{} - Vazia. Validacao Abortada", message);
            return;
        }

        for (InputDiscourseItemDTO item : list) {
            GenericValidator.validateDto(item);
            validTheme(item);
        }

        log.info("{} - Validada com Sucesso!", message);
    }

    @SneakyThrows
    private static void validTheme(InputDiscourseItemDTO item) {
        var themeNumberMissing =
                Objects.isNull(item.getThemeNumber()) || item.getThemeNumber().isBlank();
        var themeTitleMissing =
                Objects.isNull(item.getThemeTitle()) || item.getThemeTitle().isBlank();

        if (themeNumberMissing && themeTitleMissing) {
            throw new ListBuilderException(
                    "Data: %s - Informe o Número do Tema ou Título!", item.getDate());
        }

        if (!themeNumberMissing) {
            try {
                Integer.parseInt(item.getThemeNumber());
            } catch (NumberFormatException ex) {
                throw new ListBuilderException(
                        "%s - Numero do Tema invalido. Não é um número válido!", item.getDate());
            }
        }
    }
}
