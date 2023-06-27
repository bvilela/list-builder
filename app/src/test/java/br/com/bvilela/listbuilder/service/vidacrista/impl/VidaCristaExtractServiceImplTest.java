package br.com.bvilela.listbuilder.service.vidacrista.impl;

import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekItemDTO;
import br.com.bvilela.listbuilder.enuns.VidaCristaExtractItemTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class VidaCristaExtractServiceImplTest {

    @InjectMocks private VidaCristaExtractServiceImpl service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        service = new VidaCristaExtractServiceImpl();
    }

    @DisplayName("Get URL Meeting Workbook from URL Success")
    @ParameterizedTest(name = "Date is \"{0}\"")
    @MethodSource("shouldGetUrlMeetingWorkbookParameters")
    void shouldGetUrlMeetingWorkbook(LocalDate localDate, String expectedURL) {
        var url = service.getUrlMeetingWorkbook(localDate);
        Assertions.assertEquals(expectedURL, url);
    }

    private static Stream<Arguments> shouldGetUrlMeetingWorkbookParameters() {
        return Stream.of(
                Arguments.of(
                        LocalDate.of(2021, 12, 27),
                        "https://www.jw.org/pt/biblioteca/jw-apostila-do-mes/janeiro-fevereiro-2022-mwb/"),
                Arguments.of(
                        LocalDate.of(2022, 1, 31),
                        "https://www.jw.org/pt/biblioteca/jw-apostila-do-mes/janeiro-fevereiro-2022-mwb/"),
                Arguments.of(
                        LocalDate.of(2022, 4, 25),
                        "https://www.jw.org/pt/biblioteca/jw-apostila-do-mes/maio-junho-2022-mwb/"),
                Arguments.of(
                        LocalDate.of(2022, 5, 30),
                        "https://www.jw.org/pt/biblioteca/jw-apostila-do-mes/maio-junho-2022-mwb/"),
                Arguments.of(
                        LocalDate.of(2022, 12, 31),
                        "https://www.jw.org/pt/biblioteca/jw-apostila-do-mes/janeiro-fevereiro-2023-mwb/"));
    }

    @DisplayName("Sanitizer Text")
    @ParameterizedTest(name = "Source Text is \"{1}\"")
    @MethodSource("shouldSanitizerTextParameters")
    void shouldSanitizerText(String target, String source) {
        baseSanitizerText(target, source);
    }

    private static Stream<Arguments> shouldSanitizerTextParameters() {
        return Stream.of(
                Arguments.of("2-8 de maio", "2-8&nbsp;de maio"),
                Arguments.of("'text1'", "‘text1’"),
                Arguments.of("chunk1 chunk2!", "chunk1 <em>chunk2!</em>"));
    }

    @Test
    void shouldExtractYearFromLabelDateCase1() {
        baseExtractYearFromLabelDate(2022, "Setembro–outubro de 2022");
    }

    @Test
    void shouldExtractYearFromLabelDateCase2() {
        baseExtractYearFromLabelDate(2022, "Setembro–outubro de 2022");
    }

    @Test
    void shouldExtractYearFromLabelDateCase3() {
        baseExtractYearFromLabelDate(2022, "Julho–agosto de 2022");
    }

    @Test
    void shouldExtractYearFromLabelDateCase4() {
        baseExtractYearFromLabelDate(2022, "Maio–junho de 2022");
    }

    @Test
    void shouldExtractYearFromLabelDateCase5() {
        baseExtractYearFromLabelDate(2022, "Março–abril de 2022");
    }

    @Test
    void shouldExtractYearFromLabelDateCase6() {
        baseExtractYearFromLabelDate(2022, "Janeiro–fevereiro de 2022");
    }

    @Test
    void shouldExtractYearFromLabelDateCase7() {
        baseExtractYearFromLabelDate(2021, "Novembro–dezembro de 2021");
    }

    @Test
    void shouldExtractWeeksBySite() {
        var url = "https://www.jw.org/pt/biblioteca/jw-apostila-do-mes/maio-junho-2022-mwb/";
        var list = service.extractWeeksBySite(url);
        Assertions.assertDoesNotThrow(() -> service.extractWeekItemsBySite(list));
        var week = list.get(0);
        Assertions.assertEquals("2-8 de maio", week.getLabelDate());
        Assertions.assertEquals(LocalDate.of(2022, 5, 2), week.getInitialDate());
        Assertions.assertEquals(LocalDate.of(2022, 5, 8), week.getEndDate());
        Assertions.assertEquals(
                "/pt/biblioteca/jw-apostila-do-mes/maio-junho-2022-mwb/Programa%C3%A7%C3%A3o-da-semana-de-2-8-de-maio-de-2022-na-Apostila-da-Reuni%C3%A3o-Vida-e-Minist%C3%A9rio/",
                week.getLink());
        Assertions.assertEquals(18, week.getItems().size());
        int index = 0;
        checkWeekItem(
                week.getItems().get(index++),
                VidaCristaExtractItemTypeEnum.PRESIDENT,
                "Presidente da Reunião");
        checkWeekItem(
                week.getItems().get(index++),
                VidaCristaExtractItemTypeEnum.READ_OF_WEEK,
                "1 SAMUEL 27-29");
        checkWeekItem(
                week.getItems().get(index++),
                VidaCristaExtractItemTypeEnum.WITH_PARTICIPANTS,
                "Cântico 71 e oração");
        checkWeekItem(
                week.getItems().get(index++),
                VidaCristaExtractItemTypeEnum.NO_PARTICIPANTS,
                "Comentários iniciais");
        checkWeekItem(
                week.getItems().get(index++),
                VidaCristaExtractItemTypeEnum.LABEL,
                "TESOUROS DA PALAVRA DE DEUS");
        checkWeekItem(
                week.getItems().get(index++),
                VidaCristaExtractItemTypeEnum.WITH_PARTICIPANTS,
                "\"A estratégia de guerra de Davi\"");
        checkWeekItem(
                week.getItems().get(index++),
                VidaCristaExtractItemTypeEnum.WITH_PARTICIPANTS,
                "Joias espirituais");
        checkWeekItem(
                week.getItems().get(index++),
                VidaCristaExtractItemTypeEnum.WITH_PARTICIPANTS,
                "Leitura da Bíblia");
        checkWeekItem(
                week.getItems().get(index++),
                VidaCristaExtractItemTypeEnum.LABEL,
                "FAÇA SEU MELHOR NO MINISTÉRIO");
        checkWeekItem(
                week.getItems().get(index++),
                VidaCristaExtractItemTypeEnum.WITH_PARTICIPANTS,
                "Vídeo da primeira conversa");
        checkWeekItem(
                week.getItems().get(index++),
                VidaCristaExtractItemTypeEnum.WITH_PARTICIPANTS,
                "Primeira conversa — designação 1");
        checkWeekItem(
                week.getItems().get(index++),
                VidaCristaExtractItemTypeEnum.WITH_PARTICIPANTS,
                "Primeira conversa — designação 2");
        checkWeekItem(
                week.getItems().get(index++), VidaCristaExtractItemTypeEnum.LABEL, "NOSSA VIDA CRISTÃ");
        checkWeekItem(
                week.getItems().get(index++),
                VidaCristaExtractItemTypeEnum.NO_PARTICIPANTS,
                "Cântico 129");
        checkWeekItem(
                week.getItems().get(index++),
                VidaCristaExtractItemTypeEnum.WITH_PARTICIPANTS,
                "Firmes apesar de oposição");
        checkWeekItem(
                week.getItems().get(index++),
                VidaCristaExtractItemTypeEnum.WITH_PARTICIPANTS,
                "Estudo bíblico de congregação");
        checkWeekItem(
                week.getItems().get(index++),
                VidaCristaExtractItemTypeEnum.NO_PARTICIPANTS,
                "Comentários finais");
        checkWeekItem(
                week.getItems().get(index++),
                VidaCristaExtractItemTypeEnum.WITH_PARTICIPANTS,
                "Cântico 94 e oração");
    }

    @Test
    void shouldExtractWeeksBySiteAnotherSite() {
        var url = "https://www.google.com.br";
        var exception =
                Assertions.assertThrows(
                        ListBuilderException.class, () -> service.extractWeeksBySite(url));
        Assertions.assertEquals("Erro ao ler Cabeçalho do site", exception.getMessage());
    }

    @Test
    void shouldExtractMonthsAndConvertToDatesException() {
        var exception =
                Assertions.assertThrows(
                        ListBuilderException.class,
                        () -> service.extractMonthsAndConvertToDates("abc", 2022));
        Assertions.assertEquals("Erro ao extrair os meses da label abc", exception.getMessage());
    }

    @Test
    void shouldExtractMonthsAndConvertToDatesOneMonthSuccess() {
        var list = service.extractMonthsAndConvertToDates("5-11 de setembro", 2022);
        Assertions.assertEquals(List.of(LocalDate.of(2022, 9, 5), LocalDate.of(2022, 9, 11)), list);
    }

    @Test
    void shouldExtractMonthsAndConvertToDatesTwoMonthsSuccess() {
        var list = service.extractMonthsAndConvertToDates("26 de setembro-2 de outubro", 2022);
        Assertions.assertEquals(
                List.of(LocalDate.of(2022, 9, 26), LocalDate.of(2022, 10, 2)), list);
    }

    @Test
    void shouldExtractMonthsAndConvertToDatesTwoMonthsTwoYearCase1Success() {
        var list =
                service.extractMonthsAndConvertToDates(
                        "26 de dezembro de 2022–1.º de janeiro de 2023", 2022);
        Assertions.assertEquals(
                List.of(LocalDate.of(2022, 12, 26), LocalDate.of(2023, 1, 1)), list);
    }

    @Test
    void shouldExtractMonthsAndConvertToDatesTwoMonthsTwoYearCase2Success() {
        var list =
                service.extractMonthsAndConvertToDates(
                        "26 de dezembro de 2022-1.º de janeiro de 2023", 2022);
        Assertions.assertEquals(
                List.of(LocalDate.of(2022, 12, 26), LocalDate.of(2023, 1, 1)), list);
    }

    @Test
    void shouldAdjustQuotesCase1() {
        var text = service.adjustQuotes("\"Teste\" ");
        Assertions.assertEquals("\"Teste\"", text);
    }

    @Test
    void shouldAdjustQuotesCase2() {
        var text = service.adjustQuotes("\" Teste");
        Assertions.assertEquals("\"Teste\"", text);
    }

    @Test
    void shouldAdjustQuotesCase3() {
        var text = service.adjustQuotes("Teste");
        Assertions.assertEquals("Teste", text);
    }

    private void baseExtractYearFromLabelDate(int year, String label) {
        Assertions.assertEquals(year, service.extractYearFromLabelDate(label));
    }

    private void baseSanitizerText(String target, String source) {
        Assertions.assertEquals(target, service.sanitizerText(source));
    }

    private void checkWeekItem(
            VidaCristaExtractWeekItemDTO item, VidaCristaExtractItemTypeEnum type, String title) {
        Assertions.assertEquals(type, item.getType());
        Assertions.assertEquals(title, item.getTitle());
        Assertions.assertNull(item.getParticipants());
        Assertions.assertFalse(item.toString().isBlank());
    }
}
