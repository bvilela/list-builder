package br.com.bvilela.listbuilder.service.notification;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import br.com.bvilela.listbuilder.config.NotifyProperties;
import br.com.bvilela.listbuilder.utils.PropertiesTestUtils;
import java.time.LocalDate;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class NotifyAudienceServiceTest {
    @InjectMocks private NotifyAudienceService service;
    @InjectMocks private NotifyProperties notifyProperties;

    private PropertiesTestUtils propertiesUtils;

    @SneakyThrows
    @BeforeEach
    void setupBeforeEach() {
        MockitoAnnotations.openMocks(this);
        propertiesUtils = new PropertiesTestUtils(notifyProperties);
        service = new NotifyAudienceService(notifyProperties);
    }

    @Test
    void createEventNotifyInactive() {
        propertiesUtils.setNotifyActive(false);
        var event = service.createEvent(List.of(LocalDate.now()));
        assertNull(event);
    }

    @Test
    void createEventNotifyActive() {
        propertiesUtils.setNotifyActive(true);
        var event = service.createEvent(List.of(LocalDate.now()));
        assertNotNull(event);
    }
}
