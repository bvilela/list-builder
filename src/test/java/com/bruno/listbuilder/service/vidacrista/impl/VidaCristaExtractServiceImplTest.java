package com.bruno.listbuilder.service.vidacrista.impl;

import static com.bruno.listbuilder.enuns.VidaCristaExtractItemType.LABEL;
import static com.bruno.listbuilder.enuns.VidaCristaExtractItemType.NO_PARTICIPANTS;
import static com.bruno.listbuilder.enuns.VidaCristaExtractItemType.PRESIDENT;
import static com.bruno.listbuilder.enuns.VidaCristaExtractItemType.READ_OF_WEEK;
import static com.bruno.listbuilder.enuns.VidaCristaExtractItemType.WITH_PARTICIPANTS;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.bruno.listbuilder.dto.vidacrista.VidaCristaExtractWeekItemDTO;
import com.bruno.listbuilder.enuns.VidaCristaExtractItemType;
import com.bruno.listbuilder.exception.ListBuilderException;

class VidaCristaExtractServiceImplTest {

	@InjectMocks
	private VidaCristaExtractServiceImpl service;

	@BeforeEach
	public void setup() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		service = new VidaCristaExtractServiceImpl();
	}

	@Test
	void shouldGetUrlMeetingWorkbook() {
		var url = service.getUrlMeetingWorkbook(LocalDate.of(2021, 12, 27));
		assertEquals("https://www.jw.org/pt/biblioteca/jw-apostila-do-mes/janeiro-fevereiro-2022-mwb/", url);
	}

	@Test
	void shouldGetUrlMeetingWorkbookCase2() {
		var url = service.getUrlMeetingWorkbook(LocalDate.of(2022, 1, 31));
		assertEquals("https://www.jw.org/pt/biblioteca/jw-apostila-do-mes/janeiro-fevereiro-2022-mwb/", url);
	}

	@Test
	void shouldGetUrlMeetingWorkbookCase3() {
		var url = service.getUrlMeetingWorkbook(LocalDate.of(2022, 4, 25));
		assertEquals("https://www.jw.org/pt/biblioteca/jw-apostila-do-mes/maio-junho-2022-mwb/", url);
	}

	@Test
	void shouldGetUrlMeetingWorkbookCase4() {
		var url = service.getUrlMeetingWorkbook(LocalDate.of(2022, 5, 30));
		assertEquals("https://www.jw.org/pt/biblioteca/jw-apostila-do-mes/maio-junho-2022-mwb/", url);
	}
	
	@Test
	void shouldGetUrlMeetingWorkbookCase5() {
		var url = service.getUrlMeetingWorkbook(LocalDate.of(2022, 12, 31));
		assertEquals("https://www.jw.org/pt/biblioteca/jw-apostila-do-mes/janeiro-fevereiro-2023-mwb/", url);
	}

	@Test
	void shouldSanitizerTextCase1() {
		baseSanitizerText("2-8 de maio", "2-8&nbsp;de maio");
	}

	@Test
	void shouldSanitizerTextCase2() {
		baseSanitizerText("'text1'", "‘text1’");
	}

	@Test
	void shouldSanitizerTextCase3() {
		baseSanitizerText("chunk1 chunk2!", "chunk1 <em>chunk2!</em>");
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
	void shouldExtractWeeksBySite() throws ListBuilderException, IOException {
		var url = "https://www.jw.org/pt/biblioteca/jw-apostila-do-mes/maio-junho-2022-mwb/";
		var list = service.extractWeeksBySite(url);
		assertDoesNotThrow(() -> service.extractWeekItemsBySite(list));
		var week = list.get(0);
		assertEquals("2-8 de maio", week.getLabelDate());
		assertEquals(LocalDate.of(2022, 5, 2), week.getDate1());
		assertEquals(LocalDate.of(2022, 5, 8), week.getDate2());
		assertEquals(
				"/pt/biblioteca/jw-apostila-do-mes/maio-junho-2022-mwb/Programa%C3%A7%C3%A3o-da-semana-de-2-8-de-maio-de-2022-na-Apostila-da-Reuni%C3%A3o-Vida-e-Minist%C3%A9rio/",
				week.getLink());
		assertEquals(18, week.getItems().size());
		int i = 0;
		checkWeekItem(week.getItems().get(i++), PRESIDENT, "Presidente da Reunião");
		checkWeekItem(week.getItems().get(i++), READ_OF_WEEK, "1 SAMUEL 27-29");
		checkWeekItem(week.getItems().get(i++), WITH_PARTICIPANTS, "Cântico 71 e oração");
		checkWeekItem(week.getItems().get(i++), NO_PARTICIPANTS, "Comentários iniciais");
		checkWeekItem(week.getItems().get(i++), LABEL, "TESOUROS DA PALAVRA DE DEUS");
		checkWeekItem(week.getItems().get(i++), WITH_PARTICIPANTS, "\"A estratégia de guerra de Davi\"");
		checkWeekItem(week.getItems().get(i++), WITH_PARTICIPANTS, "Joias espirituais");
		checkWeekItem(week.getItems().get(i++), WITH_PARTICIPANTS, "Leitura da Bíblia");
		checkWeekItem(week.getItems().get(i++), LABEL, "FAÇA SEU MELHOR NO MINISTÉRIO");
		checkWeekItem(week.getItems().get(i++), WITH_PARTICIPANTS, "Vídeo da primeira conversa");
		checkWeekItem(week.getItems().get(i++), WITH_PARTICIPANTS, "Primeira conversa — designação 1");
		checkWeekItem(week.getItems().get(i++), WITH_PARTICIPANTS, "Primeira conversa — designação 2");
		checkWeekItem(week.getItems().get(i++), LABEL, "NOSSA VIDA CRISTÃ");
		checkWeekItem(week.getItems().get(i++), NO_PARTICIPANTS, "Cântico 129");
		checkWeekItem(week.getItems().get(i++), WITH_PARTICIPANTS, "Firmes apesar de oposição");
		checkWeekItem(week.getItems().get(i++), WITH_PARTICIPANTS, "Estudo bíblico de congregação");
		checkWeekItem(week.getItems().get(i++), NO_PARTICIPANTS, "Comentários finais");
		checkWeekItem(week.getItems().get(i++), WITH_PARTICIPANTS, "Cântico 94 e oração");
	}

	@Test
	void shouldExtractWeeksBySiteAnotherSite() throws ListBuilderException, IOException {
		var url = "https://www.google.com.br";
		var ex = assertThrows(ListBuilderException.class, () -> service.extractWeeksBySite(url));
		assertEquals("Erro ao ler Cabeçalho do site", ex.getMessage());
	}

	@Test
	void shouldExtractMonthsAndConvertToDatesException() {
		var ex = assertThrows(ListBuilderException.class, () -> service.extractMonthsAndConvertToDates("abc", 2022));
		assertEquals("Erro ao extrair os meses da label abc", ex.getMessage());
	}

	@Test
	void shouldExtractMonthsAndConvertToDatesOneMonthSuccess() throws ListBuilderException {
		var list = service.extractMonthsAndConvertToDates("5-11 de setembro", 2022);
		assertEquals(List.of(LocalDate.of(2022, 9, 5), LocalDate.of(2022, 9, 11)), list);
	}

	@Test
	void shouldExtractMonthsAndConvertToDatesTwoMonthsSuccess() throws ListBuilderException {
		var list = service.extractMonthsAndConvertToDates("26 de setembro-2 de outubro", 2022);
		assertEquals(List.of(LocalDate.of(2022, 9, 26), LocalDate.of(2022, 10, 2)), list);
	}
	
	@Test
	void shouldExtractMonthsAndConvertToDatesTwoMonthsTwoYearCase1Success() throws ListBuilderException {
		var list = service.extractMonthsAndConvertToDates("26 de dezembro de 2022–1.º de janeiro de 2023", 2022);
		assertEquals(List.of(LocalDate.of(2022, 12, 26), LocalDate.of(2023, 1, 1)), list);
	}
	
	@Test
	void shouldExtractMonthsAndConvertToDatesTwoMonthsTwoYearCase2Success() throws ListBuilderException {
		var list = service.extractMonthsAndConvertToDates("26 de dezembro de 2022-1.º de janeiro de 2023", 2022);
		assertEquals(List.of(LocalDate.of(2022, 12, 26), LocalDate.of(2023, 1, 1)), list);
	}
	
	@Test
	void shouldAdjustQuotesCase1() {
		var text = service.adjustQuotes("\"Teste\" ");
		assertEquals("\"Teste\"", text);
	}
	
	@Test
	void shouldAdjustQuotesCase2() {
		var text = service.adjustQuotes("\" Teste");
		assertEquals("\"Teste\"", text);
	}
	
	@Test
	void shouldAdjustQuotesCase3() {
		var text = service.adjustQuotes("Teste");
		assertEquals("Teste", text);
	}

	private void baseExtractYearFromLabelDate(int year, String label) {
		assertEquals(year, service.extractYearFromLabelDate(label));
	}

	private void baseSanitizerText(String target, String source) {
		assertEquals(target, service.sanitizerText(source));
	}

	private void checkWeekItem(VidaCristaExtractWeekItemDTO item, VidaCristaExtractItemType type, String title) {
		assertEquals(type, item.getType());
		assertEquals(title, item.getTitle());
		assertNull(item.getParticipants());
		assertFalse(item.toString().isBlank());
	}

}
