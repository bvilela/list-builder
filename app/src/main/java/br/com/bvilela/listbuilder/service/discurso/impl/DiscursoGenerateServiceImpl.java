package br.com.bvilela.listbuilder.service.discurso.impl;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.dto.discurso.DiscursoAllThemesDTO;
import br.com.bvilela.listbuilder.dto.discurso.FileInputDataDiscursoDTO;
import br.com.bvilela.listbuilder.dto.discurso.FileInputDataDiscursoItemDTO;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.service.BaseGenerateService;
import br.com.bvilela.listbuilder.service.discurso.DiscursoWriterService;
import br.com.bvilela.listbuilder.utils.AppUtils;
import br.com.bvilela.listbuilder.utils.DateUtils;
import br.com.bvilela.listbuilder.utils.FileUtils;
import br.com.bvilela.listbuilder.validator.DiscursoValidator;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("DISCURSO")
@RequiredArgsConstructor
public class DiscursoGenerateServiceImpl implements BaseGenerateService {

    private final AppProperties properties;
    private final DiscursoWriterService writerService;

    @Override
    public ListTypeEnum getListType() {
        return ListTypeEnum.DISCURSO;
    }

    @Override
    @SneakyThrows
    public void generateList() {
        try {
            logInit(log);

            var dto = getFileInputDataDTO(properties, FileInputDataDiscursoDTO.class);

            Path pathAllThemesFile =
                    Paths.get(properties.getInputDir(), "dados-discurso-temas.json");
            var allThemesDto =
                    FileUtils.readInputFile(pathAllThemesFile, DiscursoAllThemesDTO.class);

            DiscursoValidator.validAllThemesFile(allThemesDto);
            DiscursoValidator.validFileInputData(dto);
            setThemesByNumberAndConvertDate(dto.getReceive(), "Lista Receber", allThemesDto);
            setThemesByNumberAndConvertDate(dto.getSend(), "Lista Enviar", allThemesDto);

            writerService.writerPDF(dto);

            logFinish(log);

        } catch (Exception e) {
            throw defaultListBuilderException(e);
        }
    }

    @SneakyThrows
    private static void setThemesByNumberAndConvertDate(
            List<FileInputDataDiscursoItemDTO> list,
            String message,
            DiscursoAllThemesDTO allThemesDto) {

        log.info("{} - Obtendo Títulos dos Discursos pelo número do tema", message);

        if (AppUtils.listIsNullOrEmpty(list)) {
            log.info("{} - Lista Vazia! Sem temas para obter.", message);
            return;
        }

        for (FileInputDataDiscursoItemDTO item : list) {
            setThemeTitleByThemeNumber(allThemesDto, item);
            item.setDateConverted(DateUtils.parse(item.getDate()));
        }

        log.info("Temas obtidos com sucesso!");
    }

    @SneakyThrows
    private static void setThemeTitleByThemeNumber(
            DiscursoAllThemesDTO allThemesDto, FileInputDataDiscursoItemDTO item) {
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
