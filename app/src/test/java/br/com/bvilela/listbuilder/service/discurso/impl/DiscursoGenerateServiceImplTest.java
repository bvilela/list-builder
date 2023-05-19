package br.com.bvilela.listbuilder.service.discurso.impl;

import br.com.bvilela.listbuilder.builder.DiscursoAllThemesDtoBuilder;
import br.com.bvilela.listbuilder.builder.FileInputDataDiscursoDtoBuilder;
import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.config.MessageConfig;
import br.com.bvilela.listbuilder.dto.discurso.FileInputDataDiscursoDTO;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.service.BaseGenerateServiceTest;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootApplication
class DiscursoGenerateServiceImplTest
        extends BaseGenerateServiceTest<FileInputDataDiscursoDTO, FileInputDataDiscursoDtoBuilder> {

    @InjectMocks private DiscursoGenerateServiceImpl service;

    @InjectMocks private AppProperties properties;

    @Mock private DiscursoWriterServiceImpl writerService;

    private static final String MSG_MISSING_THEME_NUMBER_TITLE =
            "Data: 05-06-2022 - Informe o Número do Tema ou Título!";

    public DiscursoGenerateServiceImplTest() throws ListBuilderException {
        super(ListTypeEnum.DISCURSO, FileInputDataDiscursoDtoBuilder.create().withRandomData());
    }

    @BeforeEach
    public void setup() throws IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        FieldUtils.writeField(
                properties,
                "inputDir",
                this.testUtils.getResourceDirectory(),
                true);
        service = new DiscursoGenerateServiceImpl(properties, writerService);
        createFileThemes();
    }

    private void createFileThemes() {
        this.testUtils.writeFileInputDiscursoAllThemes(
                DiscursoAllThemesDtoBuilder.create().withRandomData().build());
    }

    @Test
    void shouldModoExecutionNotNull() {
        assertNotNull(service.getListType());
    }

    @Test
    void shouldGetExecutionMode() {
        assertEquals(this.testUtils.getListType(), service.getListType());
    }

    @Test
    void shouldCorrectFileInputName() {
        assertEquals("dados-discurso.json", service.getListType().getInputFileName());
    }

    @Test
    void shouldGenerateListFileAllThemesInvalidPathFileException() {
        createFileInputDataOK();
        this.testUtils.cleanDirectory();
        validateListBuilderException(MessageConfig.FILE_NOT_FOUND);
    }

    @Test
    void shouldGenerateListFileAllThemesSintaxeException() {
        createFileInputDataOK();
        this.testUtils.writeFileInputDiscursoAllThemesSyntaxError();
        validateListBuilderException(MessageConfig.FILE_SYNTAX_ERROR);
    }

    @Test
    void shouldGenerateListFileAllThemesNullException() {
        createFileInputDataOK();
        this.testUtils.writeFileInputDiscursoAllThemes(
                DiscursoAllThemesDtoBuilder.create().withNullData().build());
        validateListBuilderException(MessageConfig.THEMES_REQUIRED);
    }

    @Test
    void shouldGenerateListFileAllThemesEmptyException() {
        createFileInputDataOK();
        this.testUtils.writeFileInputDiscursoAllThemes(
                DiscursoAllThemesDtoBuilder.create().withEmptyData().build());
        validateListBuilderException(MessageConfig.THEMES_REQUIRED);
    }

    private void createFileInputDataOK() {
        writeFileInputFromDto(builder.build());
    }

    @Test
    void shouldGenerateListFileInputInvalidPathException() {
        validateListBuilderException(MessageConfig.FILE_NOT_FOUND);
    }

    @Test
    void shouldGenerateListFileInputSintaxeException() {
        this.testUtils.writeFileInputSyntaxError();
        validateListBuilderException(MessageConfig.FILE_SYNTAX_ERROR);
    }

    @Test
    void shouldGenerateListFileInputThemeNumberTitleNullException() {
        final String themeNumber = null;
        final String themeTitle = null;
        final String expectedMessageError = MSG_MISSING_THEME_NUMBER_TITLE;
        validateFileInputThemeNumberTitleException(themeNumber, themeTitle, expectedMessageError);
    }

    @Test
    void shouldGenerateListFileInputThemeNumberTitleBlankException() {
        final String themeNumber = " ";
        final String themeTitle = " ";
        final String expectedMessageError = MSG_MISSING_THEME_NUMBER_TITLE;
        validateFileInputThemeNumberTitleException(themeNumber, themeTitle, expectedMessageError);
    }

    @Test
    void shouldGenerateListFileInputThemeNumberInvalidTitleNullException() {
        final String themeNumber = "ABC";
        final String themeTitle = " ";
        final String expectedMessageError =
                "05-06-2022 - Numero do Tema invalido. Não é um número válido!";
        validateFileInputThemeNumberTitleException(themeNumber, themeTitle, expectedMessageError);
    }

    @Test
    void shouldGenerateListFileInputThemeNumberNotFoundException() {
        final String themeNumber = "1234";
        final String themeTitle = null;
        final String expectedMessageError = "Nenhum tema encontrada para o Número: 1234";
        validateFileInputThemeNumberTitleException(themeNumber, themeTitle, expectedMessageError);
    }

    private void validateFileInputThemeNumberTitleException(
            String themeNumber, String themeTitle, String expectedMessageError) {
        var dto = builder.build();
        dto.getReceive().get(0).setThemeNumber(themeNumber);
        dto.getReceive().get(0).setThemeTitle(themeTitle);
        writeFileInputFromDto(dto);
        validateListBuilderException(expectedMessageError);
    }

    @Test
    void shouldGenerateListFileInputSendAndReceiveNull() {
        writeFileInputFromDto(builder.withRandomData().withReceive(null).withSend(null).build());
        validateListBuilderException(MessageConfig.LIST_SEND_REVEICE_NULL);
    }

    @Test
    //TODO: refactory
    void shouldGenerateListSendReceiveSuccessByThemeNumberWithThemeTitleNull() {
        var dto = builder.build();
        dto.getReceive().get(0).setThemeNumber("1");
        dto.getReceive().get(0).setThemeTitle(null);
        validateGenerateListSuccess(dto);
    }

    @Test
    void shouldGenerateListReceiveSuccessByThemeNumberWithThemeTitleNull() {
        var dto = builder.build();
        dto.setSend(null);
        dto.getReceive().get(0).setThemeNumber("1");
        dto.getReceive().get(0).setThemeTitle(null);
        validateGenerateListSuccess(dto);
    }

    @Test
    //TODO: refactory
    void shouldGenerateListSendSuccessByThemeNumberWithThemeTitleNull() {
        var dto = builder.build();
        dto.setReceive(null);
        dto.getSend().get(0).setThemeNumber("1");
        dto.getSend().get(0).setThemeTitle(null);
        validateGenerateListSuccess(dto);
    }

    @Test
    void shouldGenerateListFileInputSuccessByThemeNumberWithThemeTitleEmpty() {
        var dto = builder.build();
        dto.getReceive().get(0).setThemeNumber("1");
        dto.getReceive().get(0).setThemeTitle("");
        validateGenerateListSuccess(dto);
    }

    @Test
    void shouldGenerateListFileInputSuccessByThemeTitle() {
        var dto = builder.build();
        dto.getReceive().get(0).setThemeNumber(null);
        assertNotNull(dto.getReceive().get(0).getThemeTitle());
        validateGenerateListSuccess(dto);
        assertFalse(dto.getReceive().get(0).toString().isBlank());
    }

    void validateGenerateListSuccess(FileInputDataDiscursoDTO dto) {
        writeFileInputFromDto(dto);
        assertDoesNotThrow(() -> service.generateList());
    }

    private void validateListBuilderException(String expectedMessageError) {
        this.testUtils.validateException(
                () -> service.generateList(), expectedMessageError);
    }
}
