package br.com.bvilela.listbuilder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class ListBuilderApplicationTests {

    @InjectMocks private ListBuilderApplication listBuilderApplication;

    @Test
    void shouldBeExitCodeEventListenerBean() {
        listBuilderApplication = new ListBuilderApplication();
        Assertions.assertDoesNotThrow(() -> listBuilderApplication.exitCodeEventListenerBean());
    }

}