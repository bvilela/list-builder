package com.bruno.listbuilder.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bruno.listbuilder.builder.VidaCristaExtractWeekDtoBuilder;
import com.bruno.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import com.bruno.listbuilder.dto.limpeza.FinalListLimpezaItemDTO;
import com.bruno.listbuilder.dto.limpeza.FinalListLimpezaItemLayout2DTO;
import com.bruno.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bvilela.lib.service.GoogleCalendarService;

@SpringBootApplication
class NotificationServiceImplTest {

	@InjectMocks
	private NotificationServiceImpl service;

	@Mock
	private GoogleCalendarService calendarService;
	
	private static final int LAYOUT1 = 1;
	private static final int LAYOUT2 = 2;	

	private final FinalListLimpezaDTO dtoLimpeza = FinalListLimpezaDTO.builder().itemsLayout2(List.of(
			FinalListLimpezaItemLayout2DTO.builder().withGroup("P1 P2")
			.withDate1(LocalDate.now()).withLabel1("label1")
			.withDate2(LocalDate.now()).withLabel2("label2").build(),
			FinalListLimpezaItemLayout2DTO.builder().withGroup("P1, P2")
			.withDate1(LocalDate.now()).withLabel1("label1")
			.withDate2(LocalDate.now()).withLabel2("label2").build()
	)).build();

	private final List<LocalDate> dtoAssistencia = List.of(LocalDate.now());

	private final List<VidaCristaExtractWeekDTO> dtoVidaCrista = List
			.of(VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build());

	@BeforeEach
	void setupBeforeEach() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		service = new NotificationServiceImpl(calendarService);
	}
	
	
	// *********************** ASSISTENCIA *********************** \\
	@Test
	void shouldAssitenciaNotifActiveFalse() {
		assertDoesNotThrow(() -> service.assistencia(dtoAssistencia));
	}
	
	@Test
	void shouldAssitenciaNotifActiveTrue() throws IllegalAccessException {
		setNotifActive();
		assertDoesNotThrow(() -> service.assistencia(dtoAssistencia));
	}

	
	// *********************** LIMPEZA *********************** \\
	@Test
	void shouldLimpezaNotifActiveFalse() {
		assertDoesNotThrow(() -> service.limpeza(dtoLimpeza, LAYOUT2));
	}

	@Test
	void shouldLimpezaNotifActiveTrueNotifNameNull() throws IllegalAccessException {
		setNotifActive();
		assertThrows(ListBuilderException.class, () -> service.limpeza(dtoLimpeza, LAYOUT2));
	}
	
	@Test
	void shouldAssistenciaNotifActiveTrueWithoutNotifPersonSuccess() throws IllegalAccessException {
		setNotifActive();
		setNotifName("xpto");
		assertDoesNotThrow(() -> service.limpeza(dtoLimpeza, LAYOUT2));
	}
	
	@Test
	void shouldAssistenciaNotifActiveTrueNotifPersonSuccess() throws IllegalAccessException {
		setNotifActive();
		setNotifName("P1");
		assertDoesNotThrow(() -> service.limpeza(dtoLimpeza, LAYOUT2));
	}
	
	@Test
	void shouldAssistenciaNotifActiveTrueNotifPersonLayout1Success() throws IllegalAccessException {
		var item = new FinalListLimpezaItemDTO(LocalDate.of(2022, 06, 10), "Label", "P1, P2, P3");
		final FinalListLimpezaDTO dtoLimpezaLayout1 = FinalListLimpezaDTO.builder().items(List.of(item)).build();
		setNotifActive();
		setNotifName("P1");
		assertDoesNotThrow(() -> service.limpeza(dtoLimpezaLayout1, LAYOUT1));
	}
	
	@Test
	void shouldVidaCristaNotifActiveGroupNull() throws IllegalAccessException {
		setNotifActive();
		setnotifChristianlifeMidweekMeetingDay("terca");
		var item = dtoLimpeza.getItemsLayout2().get(0);
		var newItem = FinalListLimpezaItemLayout2DTO.builder()
				.withDate1(item.getDate1())
				.withDate2(item.getDate2())
				.withGroup(null)
				.withLabel1(item.getLabel1())
				.withLabel2(item.getLabel2()).build();
		List<FinalListLimpezaItemLayout2DTO> newList = new ArrayList<>(dtoLimpeza.getItemsLayout2());
		newList.add(newItem);
		var newDtoLimpeza = FinalListLimpezaDTO.builder().itemsLayout2(newList).build();
		setNotifName("XPTO");
		assertDoesNotThrow(() -> service.limpeza(newDtoLimpeza, LAYOUT2));
	}
	
	@Test
	void shouldAssistenciaNotifActiveTrueNotifPersonPreMeetingSuccess() throws IllegalAccessException {
		setNotifActive();
		setNotifName("P1");
		FieldUtils.writeField(service, "notifCleaningPreMeeting", true, true);
		assertDoesNotThrow(() -> service.limpeza(dtoLimpeza, LAYOUT2));
	}
	
	
	// *********************** VIDA CRISTA *********************** \\
	@Test
	void shouldVidaCristaNotifActiveFalse() {
		var list = List.of(VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build());
		assertDoesNotThrow(() -> service.vidaCrista(list));
	}
	
	@Test
	void shouldVidaCristaNotifActiveTrueNotifNameNull() throws IllegalAccessException {
		setNotifActive();
		assertThrows(ListBuilderException.class, () -> service.vidaCrista(dtoVidaCrista));
	}
	
	@Test
	void shouldVidaCristaNotifActiveTrueWithoutNotifSuccess() throws IllegalAccessException {
		setNotifActive();
		setnotifChristianlifeMidweekMeetingDay("terca");
		setNotifName("P1");
		assertDoesNotThrow(() -> service.vidaCrista(dtoVidaCrista));
	}
	
	@Test
	void shouldVidaCristaNotifActiveTrueNotifSuccess() throws IllegalAccessException {
		setNotifActive();
		setnotifChristianlifeMidweekMeetingDay("terca");
		var name = dtoVidaCrista.get(0).getItems().get(0).getParticipants().get(0);
		setNotifName(name);
		assertDoesNotThrow(() -> service.vidaCrista(dtoVidaCrista));
	}
	
	@Test
	void shouldVidaCristaNotifActiveTrueButNameItemNull() throws IllegalAccessException {
		setNotifActive();
		setnotifChristianlifeMidweekMeetingDay("terca");
		dtoVidaCrista.get(0).getItems().get(0).setParticipants(null);
		setNotifName("XPTO");
		assertDoesNotThrow(() -> service.vidaCrista(dtoVidaCrista));
	}
	
	@Test
	void shouldVidaCristaMidweekMeetingDayNullException() throws IllegalAccessException {
		setNotifActive();
		dtoVidaCrista.get(0).getItems().get(0).getParticipants().get(0);
		var name = dtoVidaCrista.get(0).getItems().get(0).getParticipants().get(0);
		setNotifName(name);
		setnotifChristianlifeMidweekMeetingDay("teste");
		var ex = assertThrows(ListBuilderException.class, () -> service.vidaCrista(dtoVidaCrista));
		assertEquals("Propriedade 'notif.christianlife.midweek.meeting.day' n??o ?? um Dia da Semana V??lido!", ex.getMessage());
	}
	
	@Test
	void shouldVidaCristaMidweekMeetingDayInvalidException() throws IllegalAccessException {
		setNotifActive();
		dtoVidaCrista.get(0).getItems().get(0).getParticipants().get(0);
		var name = dtoVidaCrista.get(0).getItems().get(0).getParticipants().get(0);
		setNotifName(name);
		var ex = assertThrows(ListBuilderException.class, () -> service.vidaCrista(dtoVidaCrista));
		assertEquals("Defina a propriedade 'notif.christianlife.midweek.meeting.day'!", ex.getMessage());
	}

	// *********************** UTILS *********************** \\
	private void setNotifActive() throws IllegalAccessException {
		FieldUtils.writeField(service, "notifActive", true, true);
	}
	
	private void setnotifChristianlifeMidweekMeetingDay(String value) throws IllegalAccessException {
		FieldUtils.writeField(service, "notifChristianlifeMidweekMeetingDay", value, true);
	}
	
	private void setNotifName(String name) throws IllegalAccessException {
		FieldUtils.writeField(service, "notifName", name, true);
	}
	
}
