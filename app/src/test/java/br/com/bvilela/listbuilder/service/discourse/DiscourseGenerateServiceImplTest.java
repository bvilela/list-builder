package br.com.bvilela.listbuilder.service.discourse;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import br.com.bvilela.listbuilder.builder.DiscursoAllThemesDtoBuilder;
import br.com.bvilela.listbuilder.builder.FileInputDataDiscursoDtoBuilder;
import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.config.MessageConfig;
import br.com.bvilela.listbuilder.dto.discourse.input.DiscourseInputDTO;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.service.BaseGenerateServiceTest;
import br.com.bvilela.listbuilder.utils.PropertiesTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
class DiscourseGenerateServiceImplTest
        extends BaseGenerateServiceTest<DiscourseInputDTO, FileInputDataDiscursoDtoBuilder> {

    @InjectMocks private DiscourseGenerateServiceImpl service;

    @InjectMocks private AppProperties appProperties;

    @Mock private DiscourseWriterService writerService;

    private static final String MSG_MISSING_THEME_NUMBER_TITLE =
            "Data: 05-06-2022 - Informe o Número do Tema ou Título!";

    public DiscourseGenerateServiceImplTest() {
        super(ListTypeEnum.DISCURSO, FileInputDataDiscursoDtoBuilder.create().withRandomData());
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        new PropertiesTestUtils(appProperties).setInputDir(testUtils.getResourceDirectory());
        service = new DiscourseGenerateServiceImpl(appProperties, writerService);
        createFileThemes();
    }

    private void createFileThemes() {
        testUtils.writeFileInputDiscursoAllThemes(
                DiscursoAllThemesDtoBuilder.create().withRandomData().build());
    }

    @Test
    void shouldModoExecutionNotNull() {
        assertNotNull(service.getListType());
    }

    @Test
    void shouldGetExecutionMode() {
        assertEquals(testUtils.getListType(), service.getListType());
    }

    @Test
    void shouldCorrectFileInputName() {
        assertEquals("dados-discurso.json", service.getListType().getInputFileName());
    }

    @Test
    void shouldGenerateListFileAllThemesInvalidPathFileException() {
        createFileInputDataOK();
        testUtils.cleanDirectory();
        validateListBuilderException(MessageConfig.FILE_NOT_FOUND);
    }

    @Test
    void shouldGenerateListFileAllThemesSintaxeException() {
        createFileInputDataOK();
        testUtils.writeFileInputDiscursoAllThemesSyntaxError();
        validateListBuilderException(MessageConfig.FILE_SYNTAX_ERROR);
    }

    @Test
    void shouldGenerateListFileAllThemesNullException() {
        createFileInputDataOK();
        testUtils.writeFileInputDiscursoAllThemes(
                DiscursoAllThemesDtoBuilder.create().withNullData().build());
        validateListBuilderException(MessageConfig.THEMES_REQUIRED);
    }

    @Test
    void shouldGenerateListFileAllThemesEmptyException() {
        createFileInputDataOK();
        testUtils.writeFileInputDiscursoAllThemes(
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
        testUtils.writeFileInputSyntaxError();
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

    @DisplayName("Generate List Send Success - ThemeNumber NotNull and ThemeTitle Not Filled")
    @ParameterizedTest(name = "ThemeTitle is \"{0}\"")
    @NullAndEmptySource
    void shouldGenerateListSendSuccessThemeNumberNotNullThemeTitleNotFilled(String themeTitle) {
        var dto = builder.build();
        dto.setReceive(null);
        dto.getSend().get(0).setThemeNumber("1");
        dto.getSend().get(0).setThemeTitle(themeTitle);
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

    void validateGenerateListSuccess(DiscourseInputDTO dto) {
        writeFileInputFromDto(dto);
        assertDoesNotThrow(() -> service.generateList());
    }

    private void validateListBuilderException(String expectedMessageError) {
        testUtils.validateException(() -> service.generateList(), expectedMessageError);
    }
}
