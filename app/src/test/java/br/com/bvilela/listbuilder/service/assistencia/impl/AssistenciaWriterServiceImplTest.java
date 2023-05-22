package br.com.bvilela.listbuilder.service.assistencia.impl;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.utils.TestUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.bvilela.listbuilder.exception.ListBuilderException;

@SpringBootApplication
class AssistenciaWriterServiceImplTest {

    @InjectMocks private AssistenciaWriterServiceImpl service;

    @InjectMocks private AppProperties properties;

    @BeforeEach
    void setupBeforeEach() throws IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        String pathOutput = Paths.get("src", "test", "resources").toFile().getAbsolutePath();
        FieldUtils.writeField(properties, "outputDir", pathOutput, true);
        service = new AssistenciaWriterServiceImpl(properties);
    }

    @AfterAll
    static void setupAfterAll() {
        TestUtils.cleanResourceDir();
    }

    @Test
    void shouldWriterPDFSuccess() throws ListBuilderException {
        var list =
                List.of(
                        cld(4, 2),
                        cld(4, 4),
                        cld(4, 8),
                        cld(4, 12),
                        cld(4, 16),
                        cld(4, 19),
                        cld(4, 8),
                        cld(4, 23),
                        cld(4, 26),
                        cld(4, 19),
                        cld(4, 30));
        Assertions.assertDoesNotThrow(() -> service.writerPDF(list));
    }

    /** Create Local Date */
    private LocalDate cld(int month, int day) {
        return LocalDate.of(2022, month, day);
    }
}
