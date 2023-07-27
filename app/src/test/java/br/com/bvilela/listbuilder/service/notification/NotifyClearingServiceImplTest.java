package br.com.bvilela.listbuilder.service.notification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.bvilela.listbuilder.builder.clearing.FinalListLimpezaDtoBuilder;
import br.com.bvilela.listbuilder.config.NotifyProperties;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.service.notification.impl.NotifyClearingServiceImpl;
import br.com.bvilela.listbuilder.utils.PropertiesTestUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class NotifyClearingServiceImplTest {

    @InjectMocks private NotifyClearingServiceImpl service;
    @InjectMocks private NotifyProperties notifyProperties;

    private static final int CLEARING_LAYOUT_1 = 1;
    private static final int CLEARING_LAYOUT_2 = 2;

    private PropertiesTestUtils propertiesUtils;

    @SneakyThrows
    @BeforeEach
    void setupBeforeEach() {
        MockitoAnnotations.openMocks(this);
        propertiesUtils = new PropertiesTestUtils(notifyProperties);
        service = new NotifyClearingServiceImpl(notifyProperties);
    }

    @Test
    void createEventsNotifyInactive() {
        propertiesUtils.setNotifyActive(false);
        var dto = FinalListLimpezaDTO.builder().build();
        var events = service.createEvents(dto, CLEARING_LAYOUT_1);
        assertTrue(events.isEmpty());
    }

    @Test
    void createEventsNotifyActiveAndNotifyNameNotFilled() {
        propertiesUtils.setNotifyActive(true);
        var dto = FinalListLimpezaDTO.builder().build();
        assertThrows(
                ListBuilderException.class, () -> service.createEvents(dto, CLEARING_LAYOUT_1));
    }

    @Test
    void createEventsNotifyActiveLayout1NotifyNanmeNotFoundSuccess() {
        propertiesUtils.setNotifyActive(true);
        propertiesUtils.setNotifyName("XPTO");
        var dto = FinalListLimpezaDtoBuilder.createMockLayout1();
        var events = service.createEvents(dto, CLEARING_LAYOUT_1);
        assertEquals(1, events.size());
        assertTrue(events.get(0).getSummary().contains("Fazer Lista"));
    }

    @Test
    void createEventsNotifyActiveLayout1Success() {
        propertiesUtils.setNotifyActive(true);
        propertiesUtils.setNotifyName("Person1");
        var dto = FinalListLimpezaDtoBuilder.createMockLayout1();
        var events = service.createEvents(dto, CLEARING_LAYOUT_1);
        assertEquals(2, events.size());
        assertEquals("Limpeza Salão", events.get(0).getSummary());
        assertTrue(events.get(1).getSummary().contains("Fazer Lista"));
    }

    @Test
    void createEventsNotifyActiveLayout2Success() {
        propertiesUtils.setNotifyActive(true);
        propertiesUtils.setNotifyName("Person1");
        var dto = FinalListLimpezaDtoBuilder.createMockLayout2();
        var events = service.createEvents(dto, CLEARING_LAYOUT_2);
        assertEquals(2, events.size());
        assertEquals("Limpeza Salão", events.get(0).getSummary());
        assertTrue(events.get(1).getSummary().contains("Fazer Lista"));
    }

    @Test
    void createEventsNotifyActiveLayout2AndNotifyPreMeetingSuccess() {
        propertiesUtils.setNotifyActive(true);
        propertiesUtils.setNotifyName("Person1");
        propertiesUtils.setNotifyCleaningPreMeeting(true);
        var dto = FinalListLimpezaDtoBuilder.createMockLayout2();
        var events = service.createEvents(dto, CLEARING_LAYOUT_2);
        assertEquals(3, events.size());
        assertEquals("Limpeza Pré-Reunião", events.get(0).getSummary());
        assertEquals("Limpeza Pós-Reunião", events.get(1).getSummary());
        assertTrue(events.get(2).getSummary().contains("Fazer Lista"));
    }
}
