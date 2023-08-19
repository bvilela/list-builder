package br.com.bvilela.listbuilder.service.clearing;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

import br.com.bvilela.listbuilder.builder.clearing.FinalListLimpezaDtoBuilder;
import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.utils.PropertiesTestUtils;
import br.com.bvilela.listbuilder.utils.TestUtils;
import java.nio.file.Paths;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
class LimpezaWriterServiceTest {

    @InjectMocks private LimpezaWriterService service;

    @InjectMocks private AppProperties appProperties;

    @BeforeEach
    @SneakyThrows
    void setupBeforeEach() {
        MockitoAnnotations.openMocks(this);
        String pathOutput = Paths.get("src", "test", "resources").toFile().getAbsolutePath();
        new PropertiesTestUtils(appProperties).setOutputDir(pathOutput);
        service = new LimpezaWriterService(appProperties);
    }

    @AfterAll
    static void setupAfterAll() {
        TestUtils.cleanResourceDir();
    }

    @Test
    void shouldWriterPDFLayout1Success() {
        var dto = FinalListLimpezaDtoBuilder.createMockLayout1();
        assertDoesNotThrow(() -> service.writerPDF(dto, "footerMessage", "headerMessage", 1));
        assertFalse(dto.toString().isBlank());
    }

    @Test
    void shouldWriterPDFLayout2Success() {
        var dto = FinalListLimpezaDtoBuilder.createMockLayout2();
        assertDoesNotThrow(() -> service.writerPDF(dto, "footerMessage", "headerMessage", 2));
        assertFalse(dto.toString().isBlank());
    }
}
