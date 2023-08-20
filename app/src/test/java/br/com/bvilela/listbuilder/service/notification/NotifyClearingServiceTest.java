package br.com.bvilela.listbuilder.service.notification;

import br.com.bvilela.listbuilder.builder.clearing.ClearingWriterDtoBuilder;
import br.com.bvilela.listbuilder.config.NotifyProperties;
import br.com.bvilela.listbuilder.dto.clearing.writer.ClearingWriterDTO;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.utils.PropertiesTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class NotifyClearingServiceTest {

    @InjectMocks private NotifyClearingService service;
    @InjectMocks private NotifyProperties notifyProperties;

    private static final int CLEARING_LAYOUT_1 = 1;
    private static final int CLEARING_LAYOUT_2 = 2;

    private PropertiesTestUtils propertiesUtils;

    @BeforeEach
    void setupBeforeEach() {
        propertiesUtils = new PropertiesTestUtils(notifyProperties);
        service = new NotifyClearingService(notifyProperties);
    }

    @Test
    void createEventsNotifyInactive() {
        propertiesUtils.setNotifyActive(false);
        var dto = ClearingWriterDTO.builder().build();
        var events = service.createEvents(dto, CLEARING_LAYOUT_1);
        assertTrue(events.isEmpty());
    }

    @Test
    void createEventsNotifyActiveAndNotifyNameNotFilled() {
        propertiesUtils.setNotifyActive(true);
        var dto = ClearingWriterDTO.builder().build();
        assertThrows(
                ListBuilderException.class, () -> service.createEvents(dto, CLEARING_LAYOUT_1));
    }

    @Test
    void createEventsNotifyActiveLayout1NotifyNanmeNotFoundSuccess() {
        propertiesUtils.setNotifyActive(true);
        propertiesUtils.setNotifyName("XPTO");
        var dto = ClearingWriterDtoBuilder.createMockLayout1();
        var events = service.createEvents(dto, CLEARING_LAYOUT_1);
        assertEquals(1, events.size());
        assertTrue(events.get(0).getSummary().contains("Fazer Lista"));
    }

    @Test
    void createEventsNotifyActiveLayout1Success() {
        propertiesUtils.setNotifyActive(true);
        propertiesUtils.setNotifyName("Person1");
        var dto = ClearingWriterDtoBuilder.createMockLayout1();
        var events = service.createEvents(dto, CLEARING_LAYOUT_1);
        assertEquals(2, events.size());
        assertEquals("Limpeza Salão", events.get(0).getSummary());
        assertTrue(events.get(1).getSummary().contains("Fazer Lista"));
    }

    @Test
    void createEventsNotifyActiveLayout2Success() {
        propertiesUtils.setNotifyActive(true);
        propertiesUtils.setNotifyName("Person1");
        var dto = ClearingWriterDtoBuilder.createMockLayout2();
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
        var dto = ClearingWriterDtoBuilder.createMockLayout2();
        var events = service.createEvents(dto, CLEARING_LAYOUT_2);
        assertEquals(3, events.size());
        assertEquals("Limpeza Pré-Reunião", events.get(0).getSummary());
        assertEquals("Limpeza Pós-Reunião", events.get(1).getSummary());
        assertTrue(events.get(2).getSummary().contains("Fazer Lista"));
    }
}
