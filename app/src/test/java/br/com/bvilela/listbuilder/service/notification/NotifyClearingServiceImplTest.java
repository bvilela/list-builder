package br.com.bvilela.listbuilder.service.notification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.bvilela.listbuilder.config.NotifyProperties;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaItemDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaItemLayout2DTO;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.service.notification.impl.NotifyClearingServiceImpl;
import java.time.LocalDate;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class NotifyClearingServiceImplTest {

    @InjectMocks private NotifyClearingServiceImpl service;
    @InjectMocks private NotifyProperties properties;

    private static final int CLEARING_LAYOUT_1 = 1;
    private static final int CLEARING_LAYOUT_2 = 2;

    @SneakyThrows
    @BeforeEach
    void setupBeforeEach() {
        MockitoAnnotations.openMocks(this);
        service = new NotifyClearingServiceImpl(properties);
    }

    @SneakyThrows
    private void setNotifyActive(boolean value) {
        FieldUtils.writeField(properties, "notifyActive", value, true);
    }

    @SneakyThrows
    private void setNotifyName(String value) {
        FieldUtils.writeField(properties, "notifyName", value, true);
    }

    @SneakyThrows
    private void setNotifyPreMeeting(boolean value) {
        FieldUtils.writeField(properties, "notifyCleaningPreMeeting", value, true);
    }

    @Test
    void createEventsNotifyInactive() {
        setNotifyActive(false);
        var dto = FinalListLimpezaDTO.builder().build();
        var events = service.createEvents(dto, CLEARING_LAYOUT_1);
        assertTrue(events.isEmpty());
    }

    @Test
    void createEventsNotifyActiveAndNotifyNameNotFilled() {
        setNotifyActive(true);
        var dto = FinalListLimpezaDTO.builder().build();
        assertThrows(
                ListBuilderException.class, () -> service.createEvents(dto, CLEARING_LAYOUT_1));
    }

    @Test
    void createEventsNotifyActiveLayout1NotifyNanmeNotFoundSuccess() {
        setNotifyActive(true);
        setNotifyName("XPTO");
        var dto = createFinalListLimpezaDto1();
        var events = service.createEvents(dto, CLEARING_LAYOUT_1);
        assertEquals(1, events.size());
        assertTrue(events.get(0).getSummary().contains("Fazer Lista"));
    }

    @Test
    void createEventsNotifyActiveLayout1Success() {
        setNotifyActive(true);
        setNotifyName("Person1");
        var dto = createFinalListLimpezaDto1();
        var events = service.createEvents(dto, CLEARING_LAYOUT_1);
        assertEquals(2, events.size());
        assertEquals("Limpeza Salão", events.get(0).getSummary());
        assertTrue(events.get(1).getSummary().contains("Fazer Lista"));
    }

    private FinalListLimpezaDTO createFinalListLimpezaDto1() {
        var group = "Person1, Person2, Person3";
        var item = new FinalListLimpezaItemDTO(LocalDate.now(), "label", group);
        return FinalListLimpezaDTO.builder().items(List.of(item)).build();
    }

    @Test
    void createEventsNotifyActiveLayout2Success() {
        setNotifyActive(true);
        setNotifyName("Person1");
        var dto = createFinalListLimpezaDto2();
        var events = service.createEvents(dto, CLEARING_LAYOUT_2);
        assertEquals(2, events.size());
        assertEquals("Limpeza Salão", events.get(0).getSummary());
        assertTrue(events.get(1).getSummary().contains("Fazer Lista"));
    }

    @Test
    void createEventsNotifyActiveLayout2AndNotifyPreMeetingSuccess() {
        setNotifyActive(true);
        setNotifyName("Person1");
        setNotifyPreMeeting(true);
        var dto = createFinalListLimpezaDto2();
        var events = service.createEvents(dto, CLEARING_LAYOUT_2);
        assertEquals(3, events.size());
        assertEquals("Limpeza Pré-Reunião", events.get(0).getSummary());
        assertEquals("Limpeza Pós-Reunião", events.get(1).getSummary());
        assertTrue(events.get(2).getSummary().contains("Fazer Lista"));
    }

    private FinalListLimpezaDTO createFinalListLimpezaDto2() {
        var item =
                FinalListLimpezaItemLayout2DTO.builder()
                        .group("Person1, Person2, Person3")
                        .date1(LocalDate.now().minusDays(1))
                        .label1("label1")
                        .date2(LocalDate.now())
                        .label2("label2")
                        .build();
        return FinalListLimpezaDTO.builder().itemsLayout2(List.of(item)).build();
    }
}
