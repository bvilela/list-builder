package com.bruno.listbuilder;

import com.bruno.listbuilder.service.impl.ApplicationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


class ApplicationTests {

    @InjectMocks
    private Application application;

    @Mock
    private ApplicationServiceImpl service;

    @BeforeEach
    public void setup() throws IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        application = new Application(service);
    }

    @Test
    void shouldBeMain() {
        Assertions.assertDoesNotThrow(() -> application.run(null));
    }

    @Test
    void shouldPostConstruct() {
        Assertions.assertDoesNotThrow(() -> application.init());
    }

    @Test
    void shouldBeExitCodeEventListenerBean() {
        Assertions.assertDoesNotThrow(() -> application.exitCodeEventListenerBean());
    }

}
