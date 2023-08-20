package br.com.bvilela.listbuilder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class ApplicationTests {

    @InjectMocks private Application application;

    @Test
    void shouldBeExitCodeEventListenerBean() {
        application = new Application();
        Assertions.assertDoesNotThrow(() -> application.exitCodeEventListenerBean());
    }

}