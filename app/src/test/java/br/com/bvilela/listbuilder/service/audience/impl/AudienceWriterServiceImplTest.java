package br.com.bvilela.listbuilder.service.audience.impl;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.enuns.AudienceWriterLayoutEnum;
import br.com.bvilela.listbuilder.service.audience.AudienceWriterService;
import br.com.bvilela.listbuilder.utils.TestUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootApplication
class AudienceWriterServiceImplTest {

    @InjectMocks private AppProperties properties;
    @Mock private AudienceWriterService service;

    @BeforeEach
    @SneakyThrows
    void setupBeforeEach() {
        MockitoAnnotations.openMocks(this);
        String pathOutput = Paths.get("src", "test", "resources").toFile().getAbsolutePath();
        FieldUtils.writeField(properties, "outputDir", pathOutput, true);
    }

    @AfterAll
    static void setupAfterAll() {
        TestUtils.cleanResourceDir();
    }

    @Test
    void writerPDFLayoutFullSuccess() {
        var dates = List.of(
            "02/01", "05/01", "09/01", "12/01", "16/01", "19/01", "23/01", "26/01", "30/01",
            "02/02", "06/02", "09/02", "13/02", "16/02", "20/02", "23/02", "27/02"
        );
        var list = TestUtils.createListLocalDates(dates, 2022);
        assertDoesNotThrow(() -> service.writerPDF(list, AudienceWriterLayoutEnum.FULL));
    }

    @Test
    void writerPDFLayoutCompactSuccess() {
        var dates = List.of(
                "02/01", "05/01", "09/01", "12/01", "16/01", "19/01", "23/01", "26/01", "30/01",
                "02/02", "06/02", "09/02", "13/02", "16/02", "20/02", "23/02", "27/02",
                "02/03", "06/03", "09/03", "13/03", "16/03", "20/03", "23/03", "27/03", "30/03"
        );
        var list = TestUtils.createListLocalDates(dates, 2022);
        assertDoesNotThrow(() -> service.writerPDF(list, AudienceWriterLayoutEnum.COMPACT));
    }

}
