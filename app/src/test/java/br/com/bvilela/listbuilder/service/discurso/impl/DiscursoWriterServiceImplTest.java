package br.com.bvilela.listbuilder.service.discurso.impl;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.builder.FileInputDataDiscursoDtoBuilder;
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
class DiscursoWriterServiceImplTest {

    @InjectMocks private DiscursoWriterServiceImpl service;

    @InjectMocks private AppProperties properties;

    @BeforeEach
    void setupBeforeEach() throws IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        String pathOutput = Paths.get("src", "test", "resources").toFile().getAbsolutePath();
        FieldUtils.writeField(properties, "outputDir", pathOutput, true);
        service = new DiscursoWriterServiceImpl(properties);
    }

    @AfterAll
    static void setupAfterAll() {
        TestUtils.cleanResourceDir();
    }

    @Test
    void shouldWriterPDFException() throws ListBuilderException {
        var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
        dto.getReceive().get(0).setDateConverted(null);
        var ex = Assertions.assertThrows(ListBuilderException.class, () -> service.writerPDF(dto));
        Assertions.assertTrue(ex.getMessage().contains("Erro ao Gerar PDF - Erro: "));
    }

    @Test
    void shouldWriterPDFSuccessSendReceive() throws ListBuilderException {
        var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
        Assertions.assertDoesNotThrow(() -> service.writerPDF(dto));
    }

    @Test
    void shouldWriterPDFSuccessSendOnly() throws ListBuilderException {
        var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
        dto.setReceive(null);
        Assertions.assertDoesNotThrow(() -> service.writerPDF(dto));
    }

    @Test
    void shouldWriterPDFSuccessReceiveOnly() throws ListBuilderException {
        var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
        dto.setSend(null);
        Assertions.assertDoesNotThrow(() -> service.writerPDF(dto));
    }

    @Test
    void shouldWriterPDFReceiveWithWildcardSuccess() throws ListBuilderException {
        var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
        dto.getReceive().get(0).setThemeNumber(null);
        dto.getReceive().get(0).setThemeTitle("?");
        Assertions.assertDoesNotThrow(() -> service.writerPDF(dto));
    }

    @Test
    void shouldWriterPDFSendWithWildcardSuccess() throws ListBuilderException {
        var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
        dto.getSend().get(0).setThemeNumber(null);
        dto.getSend().get(0).setThemeTitle("?");
        Assertions.assertDoesNotThrow(() -> service.writerPDF(dto));
    }

    @Test
    void shouldWriterPDFSuccessReceiveListSmaller() throws ListBuilderException {
        var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
        var newList = new ArrayList<>(dto.getReceive());
        newList.remove(0);
        dto.setReceive(newList);
        Assertions.assertDoesNotThrow(() -> service.writerPDF(dto));
    }

    @Test
    void shouldWriterPDFSuccessSendListSmaller() throws ListBuilderException {
        var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
        var newList = new ArrayList<>(dto.getSend());
        newList.remove(0);
        dto.setSend(newList);
        Assertions.assertDoesNotThrow(() -> service.writerPDF(dto));
    }

    @Test
    void shouldGetBaseDateSendNull() {
        var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
        dto.setSend(null);
        dto.getReceive().get(0).setDateConverted(LocalDate.of(2022, 1, 1));
        Assertions.assertEquals(LocalDate.of(2022, 1, 1), service.getBaseDate(dto));
    }

    @Test
    void shouldGetBaseDateReceiveNull() {
        var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
        dto.setReceive(null);
        dto.getSend().get(0).setDateConverted(LocalDate.of(2022, 2, 1));
        Assertions.assertEquals(LocalDate.of(2022, 2, 1), service.getBaseDate(dto));
    }
}
