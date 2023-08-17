package br.com.bvilela.listbuilder.service.discourse.impl;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.config.MessageConfig;
import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterItemDTO;
import br.com.bvilela.listbuilder.dto.discourse.InputAllThemesDiscourseDTO;
import br.com.bvilela.listbuilder.dto.discourse.InputDiscourseDTO;
import br.com.bvilela.listbuilder.dto.discourse.InputDiscourseItemDTO;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.service.BaseGenerateService;
import br.com.bvilela.listbuilder.service.discourse.DiscourseWriterService;
import br.com.bvilela.listbuilder.utils.AppUtils;
import br.com.bvilela.listbuilder.utils.DateUtils;
import br.com.bvilela.listbuilder.utils.FileUtils;
import br.com.bvilela.listbuilder.validator.DiscursoValidator;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("DISCURSO")
@RequiredArgsConstructor
public class DiscourseGenerateServiceImpl implements BaseGenerateService {

    private final AppProperties properties;
    private final DiscourseWriterService writerService;

    @Override
    public ListTypeEnum getListType() {
        return ListTypeEnum.DISCURSO;
    }

    @Override
    @SneakyThrows
    public void generateList() {
        try {
            logInit(log);

            var dto = getFileInputDataDTO(properties, InputDiscourseDTO.class);

            Path pathAllThemesFile =
                    Paths.get(properties.getInputDir(), "dados-discurso-temas.json");
            var allThemesDto =
                    FileUtils.readInputFile(pathAllThemesFile, InputAllThemesDiscourseDTO.class);

            DiscursoValidator.validAllThemesFile(allThemesDto);
            DiscursoValidator.validFileInputData(dto);
            setThemesByNumberAndConvertDate(dto.getReceive(), "Lista Receber", allThemesDto);
            setThemesByNumberAndConvertDate(dto.getSend(), "Lista Enviar", allThemesDto);

            /*
            if (properties.isDiscourseIncludePresident()){
                includePresident(dto);
            }
            */

            writerService.writerPDF(dto);

            logFinish(log);

        } catch (Exception e) {
            throw defaultListBuilderException(log, e);
        }
    }

    /*
    @SneakyThrows
    private List<String> includePresident(InputDiscourseDTO dto) {
        if (dto.getPresident() == null) {
            return null;
        }

        var president = dto.getPresident();
        var indexLast = president.getList().indexOf(president.getLast());
        if (indexLast < 0) {
            throw new ListBuilderException(MessageConfig.LAST_INVALID, "Presidente");
        }

        var listPresidents = new ArrayList<>();
        for (LocalDate listDate : listDates) {
            indexLast = indexLast == dto.getList().size() - 1 ? 0 : ++indexLast;
            listWriterItems.add(
                    new DesignacaoWriterItemDTO(listDate, dto.getList().get(indexLast)));
        }
    }
    */

    @SneakyThrows
    private static void setThemesByNumberAndConvertDate(
            List<InputDiscourseItemDTO> list,
            String message,
            InputAllThemesDiscourseDTO allThemesDto) {

        log.info("{} - Obtendo Títulos dos Discursos pelo número do tema", message);

        if (AppUtils.listIsNullOrEmpty(list)) {
            log.info("{} - Lista Vazia! Sem temas para obter.", message);
            return;
        }

        for (InputDiscourseItemDTO item : list) {
            setThemeTitleByThemeNumber(allThemesDto, item);
            item.setDateConverted(DateUtils.parse(item.getDate()));
        }

        log.info("Temas obtidos com sucesso!");
    }

    @SneakyThrows
    private static void setThemeTitleByThemeNumber(
            InputAllThemesDiscourseDTO allThemesDto, InputDiscourseItemDTO item) {
        var themeTitleMissing =
                Objects.isNull(item.getThemeTitle()) || item.getThemeTitle().isEmpty();
        if (Objects.nonNull(item.getThemeNumber()) && themeTitleMissing) {
            var title = allThemesDto.getThemes().get(Integer.parseInt(item.getThemeNumber()));
            if (Objects.isNull(title)) {
                throw new ListBuilderException(
                        "Nenhum tema encontrada para o Número: %s", item.getThemeNumber());
            }
            item.setThemeTitle(title);
        }
    }
}
