package br.com.bvilela.listbuilder.service.discourse.impl;

import br.com.bvilela.listbuilder.builder.FileInputDataDiscursoDtoBuilder;
import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.service.discourse.DiscourseWriterService;
import br.com.bvilela.listbuilder.utils.PropertiesTestUtils;
import br.com.bvilela.listbuilder.utils.TestUtils;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
class DiscourseWriterServiceTest {

    @InjectMocks private DiscourseWriterService service;
    @InjectMocks private AppProperties appProperties;

    @BeforeEach
    void setupBeforeEach() {
        MockitoAnnotations.openMocks(this);
        String pathOutput = Paths.get("src", "test", "resources").toFile().getAbsolutePath();
        new PropertiesTestUtils(appProperties).setOutputDir(pathOutput);
        service = new DiscourseWriterService(appProperties);
    }

    @AfterAll
    static void setupAfterAll() {
        TestUtils.cleanResourceDir();
    }

    @Test
    void shouldWriterPDFException() {
        var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
        dto.getReceive().get(0).setDateConverted(null);
        var exception =
                Assertions.assertThrows(
                        ListBuilderException.class,
                        () -> service.writerPDF(dto.convertToWriterDto()));
        Assertions.assertTrue(exception.getMessage().contains("Erro ao Gerar PDF - Erro: "));
    }

    @Test
    void shouldWriterPDFSuccessSendReceive() {
        var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
        Assertions.assertDoesNotThrow(() -> service.writerPDF(dto.convertToWriterDto()));
    }

    @Test
    void shouldWriterPDFSuccessSendOnly() {
        var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
        dto.setReceive(null);
        Assertions.assertDoesNotThrow(() -> service.writerPDF(dto.convertToWriterDto()));
    }

    @Test
    void shouldWriterPDFSuccessReceiveOnly() {
        var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
        dto.setSend(null);
        Assertions.assertDoesNotThrow(() -> service.writerPDF(dto.convertToWriterDto()));
    }

    @Test
    void shouldWriterPDFReceiveWithWildcardSuccess() {
        var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
        dto.getReceive().get(0).setThemeNumber(null);
        dto.getReceive().get(0).setThemeTitle("?");
        Assertions.assertDoesNotThrow(() -> service.writerPDF(dto.convertToWriterDto()));
    }

    @Test
    void shouldWriterPDFSendWithWildcardSuccess() {
        var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
        dto.getSend().get(0).setThemeNumber(null);
        dto.getSend().get(0).setThemeTitle("?");
        Assertions.assertDoesNotThrow(() -> service.writerPDF(dto.convertToWriterDto()));
    }

    @Test
    void shouldWriterPDFSuccessReceiveListSmaller() {
        var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
        var newList = new ArrayList<>(dto.getReceive());
        newList.remove(0);
        dto.setReceive(newList);
        Assertions.assertDoesNotThrow(() -> service.writerPDF(dto.convertToWriterDto()));
    }

    @Test
    void shouldWriterPDFSuccessSendListSmaller() {
        var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
        var newList = new ArrayList<>(dto.getSend());
        newList.remove(0);
        dto.setSend(newList);
        Assertions.assertDoesNotThrow(() -> service.writerPDF(dto.convertToWriterDto()));
    }

    @Test
    void shouldGetBaseDateSendNull() {
        var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
        dto.setSend(null);
        dto.getReceive().get(0).setDateConverted(LocalDate.of(2022, 1, 1));
        Assertions.assertEquals(
                LocalDate.of(2022, 1, 1), service.getBaseDate(dto.convertToWriterDto()));
    }

    @Test
    void shouldGetBaseDateReceiveNull() {
        var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
        dto.setReceive(null);
        dto.getSend().get(0).setDateConverted(LocalDate.of(2022, 2, 1));
        Assertions.assertEquals(
                LocalDate.of(2022, 2, 1), service.getBaseDate(dto.convertToWriterDto()));
    }
}