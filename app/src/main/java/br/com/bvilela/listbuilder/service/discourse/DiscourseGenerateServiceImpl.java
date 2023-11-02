package br.com.bvilela.listbuilder.service.discourse;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.config.MessageConfig;
import br.com.bvilela.listbuilder.dto.discourse.input.DiscourseInputAllThemesDTO;
import br.com.bvilela.listbuilder.dto.discourse.input.DiscourseInputDTO;
import br.com.bvilela.listbuilder.dto.discourse.input.DiscourseInputItemDTO;
import br.com.bvilela.listbuilder.dto.discourse.writer.DiscourseWriterDTO;
import br.com.bvilela.listbuilder.dto.util.CircularList;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.service.BaseGenerateService;
import br.com.bvilela.listbuilder.util.AppUtils;
import br.com.bvilela.listbuilder.util.DateUtils;
import br.com.bvilela.listbuilder.util.FileUtils;
import br.com.bvilela.listbuilder.validator.DiscourseValidator;

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
public class DiscourseGenerateServiceImpl implements BaseGenerateService {

    private final AppProperties properties;
    private final DiscourseWriterService writerService;

    @Override
    public ListTypeEnum getListType() {
        return ListTypeEnum.DISCURSO;
    }

    @Override
    public void generateList() {
        try {
            log.info(logInitMessage());

            var dto = getFileInputDataDTO(properties, DiscourseInputDTO.class);

            Path pathAllThemesFile =
                    Paths.get(properties.getInputDir(), "dados-discurso-temas.json");
            var allThemesDto =
                    FileUtils.readInputFile(pathAllThemesFile, DiscourseInputAllThemesDTO.class);

            DiscourseValidator.validAllThemesFile(allThemesDto);
            DiscourseValidator.validFileInputData(dto);
            setThemesByNumberAndConvertDate(dto.getReceive(), "Lista Receber", allThemesDto);
            setThemesByNumberAndConvertDate(dto.getSend(), "Lista Enviar", allThemesDto);

            var writerDto = dto.convertToWriterDto();

            if (properties.isDiscourseIncludePresident()) {
                includePresident(dto, writerDto);
            }

            writerService.writerPDF(writerDto);

            log.info(logFinishMessage());

        } catch (Exception ex) {
            log.error(logErrorMessage(ex));
            throw ex;
        }
    }

    @SneakyThrows
    private void includePresident(DiscourseInputDTO dto, DiscourseWriterDTO writerDto) {
        if (dto.getPresident() == null) {
            return;
        }

        var president = dto.getPresident();
        final int initialIndex = president.getList().indexOf(president.getLast());
        if (initialIndex < 0) {
            throw new ListBuilderException(MessageConfig.LAST_INVALID, "Presidente");
        }

        var circularList = new CircularList<>(dto.getPresident().getList(), initialIndex);
        writerDto.getReceive().forEach(e -> e.setPresident(circularList.next()));
    }

    @SneakyThrows
    private static void setThemesByNumberAndConvertDate(
            List<DiscourseInputItemDTO> list,
            String message,
            DiscourseInputAllThemesDTO allThemesDto) {

        log.info("{} - Obtendo Títulos dos Discursos pelo número do tema", message);

        if (AppUtils.listIsNullOrEmpty(list)) {
            log.info("{} - Lista Vazia! Sem temas para obter.", message);
            return;
        }

        for (DiscourseInputItemDTO item : list) {
            setThemeTitleByThemeNumber(allThemesDto, item);
            item.setDateConverted(DateUtils.parse(item.getDate()));
        }

        log.info("Temas obtidos com sucesso!");
    }

    @SneakyThrows
    private static void setThemeTitleByThemeNumber(
            DiscourseInputAllThemesDTO allThemesDto, DiscourseInputItemDTO item) {
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
