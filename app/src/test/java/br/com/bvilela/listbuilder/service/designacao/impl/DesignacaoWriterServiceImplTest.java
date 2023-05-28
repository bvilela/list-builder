package br.com.bvilela.listbuilder.service.designacao.impl;

import br.com.bvilela.listbuilder.builder.designacao.DesignacaoWriterDtoBuilder;
import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.utils.TestUtils;
import java.nio.file.Paths;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
class DesignacaoWriterServiceImplTest {

    @InjectMocks private DesignacaoWriterServiceImpl service;

    @InjectMocks private AppProperties properties;

    @BeforeEach
    @SneakyThrows
    void setupBeforeEach() {
        MockitoAnnotations.openMocks(this);
        String pathOutput = Paths.get("src", "test", "resources").toFile().getAbsolutePath();
        FieldUtils.writeField(properties, "outputDir", pathOutput, true);
        service = new DesignacaoWriterServiceImpl(properties);
    }

    @AfterAll
    static void setupAfterAll() {
        TestUtils.cleanResourceDir();
    }

    @Test
    void shouldWriterPDFSuccess() {
        DesignacaoWriterDTO dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        Assertions.assertDoesNotThrow(() -> service.writerPDF(dto));
    }

    @Test
    void shouldWriterPDFCongressoAssembleiaVisitaSuccess() {
        DesignacaoWriterDTO dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        dto.getAudioVideo().get(0).setName("Teste 1 (congresso)");
        dto.getAudioVideo().get(1).setName("Teste 2 (assembleia)");
        dto.getAudioVideo().get(2).setName("Teste 3 (visita)");
        Assertions.assertDoesNotThrow(() -> service.writerPDF(dto));
        Assertions.assertFalse(dto.getAudioVideo().get(0).toString().isEmpty());
    }

    @Test
    @SneakyThrows
    void shouldWriterPDFException() {
        FieldUtils.writeField(properties, "outputDir", null, true);
        DesignacaoWriterDTO dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        var exception =
                Assertions.assertThrows(ListBuilderException.class, () -> service.writerPDF(dto));
        Assertions.assertTrue(exception.getMessage().contains("Erro ao Gerar PDF - Erro"));
    }

    @Test
    void shouldWriterDocxSuccess() {
        DesignacaoWriterDTO dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        Assertions.assertDoesNotThrow(() -> service.writerDocx(dto));
    }

    @Test
    void shouldWriterDocxCongressoAssembleiaVisitaSuccess() {
        DesignacaoWriterDTO dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        dto.getAudioVideo().get(0).setName("Teste 1 (congresso)");
        dto.getAudioVideo().get(1).setName("Teste 2 (assembleia)");
        dto.getAudioVideo().get(2).setName("Teste 3 (visita)");
        Assertions.assertDoesNotThrow(() -> service.writerDocx(dto));
        Assertions.assertFalse(dto.getAudioVideo().get(0).toString().isEmpty());
    }

    @Test
    @SneakyThrows
    void shouldWriterDocxException() {
        FieldUtils.writeField(properties, "outputDir", null, true);
        DesignacaoWriterDTO dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        var exception =
                Assertions.assertThrows(ListBuilderException.class, () -> service.writerDocx(dto));
        Assertions.assertTrue(exception.getMessage().contains("Erro ao Gerar Docx - Erro"));
    }
}
