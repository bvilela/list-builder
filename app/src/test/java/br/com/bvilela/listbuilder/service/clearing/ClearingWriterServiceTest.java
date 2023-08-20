package br.com.bvilela.listbuilder.service.clearing;

import br.com.bvilela.listbuilder.builder.clearing.ClearingWriterDtoBuilder;
import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.utils.PropertiesTestUtils;
import br.com.bvilela.listbuilder.utils.TestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class ClearingWriterServiceTest {

    @InjectMocks private ClearingWriterService service;

    @InjectMocks private AppProperties appProperties;

    @BeforeEach
    void setupBeforeEach() {
        String pathOutput = Paths.get("src", "test", "resources").toFile().getAbsolutePath();
        new PropertiesTestUtils(appProperties).setOutputDir(pathOutput);
        service = new ClearingWriterService(appProperties);
    }

    @AfterAll
    static void setupAfterAll() {
        TestUtils.cleanResourceDir();
    }

    @Test
    void shouldWriterPDFLayout1Success() {
        var dto = ClearingWriterDtoBuilder.createMockLayout1();
        assertDoesNotThrow(() -> service.writerPDF(dto, "footerMessage", "headerMessage", 1));
        assertFalse(dto.toString().isBlank());
    }

    @Test
    void shouldWriterPDFLayout2Success() {
        var dto = ClearingWriterDtoBuilder.createMockLayout2();
        assertDoesNotThrow(() -> service.writerPDF(dto, "footerMessage", "headerMessage", 2));
        assertFalse(dto.toString().isBlank());
    }
}
