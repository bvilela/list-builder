package br.com.bvilela.listbuilder.service.designation;

import br.com.bvilela.listbuilder.builder.designation.DesignationWriterDtoBuilder;
import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.utils.PropertiesTestUtils;
import br.com.bvilela.listbuilder.utils.TestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class DesignationWriterServiceTest {

    @InjectMocks private DesignationWriterService service;

    @InjectMocks private AppProperties appProperties;

    private PropertiesTestUtils propertiesUtils;

    @BeforeEach
    void setupBeforeEach() {
        propertiesUtils = new PropertiesTestUtils(appProperties);
        String pathOutput = Paths.get("src", "test", "resources").toFile().getAbsolutePath();
        propertiesUtils.setOutputDir(pathOutput);
        service = new DesignationWriterService(appProperties);
    }

    @AfterAll
    static void setupAfterAll() {
        TestUtils.cleanResourceDir();
    }

    @Test
    void shouldWriterPDFSuccess() {
        var dto = DesignationWriterDtoBuilder.create().withRandomData().build();
        assertDoesNotThrow(() -> service.writerPDF(dto));
    }

    @Test
    void shouldWriterPDFCongressoAssembleiaVisitaSuccess() {
        var dto = DesignationWriterDtoBuilder.create().withRandomData().build();
        dto.getAudioVideo().get(0).setName("Teste 1 (congresso)");
        dto.getAudioVideo().get(1).setName("Teste 2 (assembleia)");
        dto.getAudioVideo().get(2).setName("Teste 3 (visita)");
        assertDoesNotThrow(() -> service.writerPDF(dto));
        assertFalse(dto.getAudioVideo().get(0).toString().isEmpty());
    }

    @Test
    void shouldWriterPDFException() {
        propertiesUtils.setOutputDir(null);
        var dto = DesignationWriterDtoBuilder.create().withRandomData().build();
        var exception =
                Assertions.assertThrows(ListBuilderException.class, () -> service.writerPDF(dto));
        assertTrue(exception.getMessage().contains("Erro ao Gerar PDF - Erro"));
    }

    @Test
    void shouldWriterDocxSuccess() {
        var dto = DesignationWriterDtoBuilder.create().withRandomData().build();
        assertDoesNotThrow(() -> service.writerDocx(dto));
    }

    @Test
    void shouldWriterDocxCongressoAssembleiaVisitaSuccess() {
        var dto = DesignationWriterDtoBuilder.create().withRandomData().build();
        dto.getAudioVideo().get(0).setName("Teste 1 (congresso)");
        dto.getAudioVideo().get(1).setName("Teste 2 (assembleia)");
        dto.getAudioVideo().get(2).setName("Teste 3 (visita)");
        assertDoesNotThrow(() -> service.writerDocx(dto));
        assertFalse(dto.getAudioVideo().get(0).toString().isEmpty());
    }

    @Test
    void shouldWriterDocxException() {
        propertiesUtils.setOutputDir(null);
        var dto = DesignationWriterDtoBuilder.create().withRandomData().build();
        var exception =
                Assertions.assertThrows(ListBuilderException.class, () -> service.writerDocx(dto));
        assertTrue(exception.getMessage().contains("Erro ao Gerar Docx - Erro"));
    }
}
