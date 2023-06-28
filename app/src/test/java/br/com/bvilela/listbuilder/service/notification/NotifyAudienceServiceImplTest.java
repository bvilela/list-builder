package br.com.bvilela.listbuilder.service.notification;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import br.com.bvilela.listbuilder.config.NotifyProperties;
import br.com.bvilela.listbuilder.service.notification.impl.NotifyAudienceServiceImpl;
import java.time.LocalDate;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class NotifyAudienceServiceImplTest {
    @InjectMocks private NotifyAudienceServiceImpl service;
    @InjectMocks private NotifyProperties properties;

    @SneakyThrows
    @BeforeEach
    void setupBeforeEach() {
        MockitoAnnotations.openMocks(this);
        service = new NotifyAudienceServiceImpl(properties);
    }

    @SneakyThrows
    private void setNotifyActive(boolean value) {
        FieldUtils.writeField(properties, "notifyActive", value, true);
    }

    @Test
    void createEventNotifyInactive() {
        setNotifyActive(false);
        var event = service.createEvent(List.of(LocalDate.now()));
        assertNull(event);
    }

    @Test
    void createEventNotifyActive() {
        setNotifyActive(true);
        var event = service.createEvent(List.of(LocalDate.now()));
        assertNotNull(event);
    }
}
