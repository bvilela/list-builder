package br.com.bvilela.listbuilder.service.vidacrista.impl;

import br.com.bvilela.listbuilder.builder.VidaCristaExtractWeekDtoBuilder;
import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import br.com.bvilela.listbuilder.enuns.VidaCristaExtractItemTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.utils.PropertiesTestUtils;
import br.com.bvilela.listbuilder.utils.TestUtils;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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
class VidaCristaWriterServiceImplTest {

    @InjectMocks private VidaCristaWriterServiceImpl service;

    @InjectMocks private AppProperties appProperties;

    @BeforeEach
    @SneakyThrows
    void setupBeforeEach() {
        MockitoAnnotations.openMocks(this);
        String pathOutput = Paths.get("src", "test", "resources").toFile().getAbsolutePath();
        new PropertiesTestUtils(appProperties).setOutputDir(pathOutput);
        service = new VidaCristaWriterServiceImpl(appProperties);
    }

    @AfterAll
    static void setupAfterAll() {
        TestUtils.cleanResourceDir();
    }

    @Test
    void shouldWriterExceptionNotFoundImage() {
        List<VidaCristaExtractWeekDTO> list =
                List.of(VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build());

        list.get(0).getItems().stream()
                .filter(e -> e.getType() == VidaCristaExtractItemTypeEnum.LABEL)
                .findFirst()
                .ifPresent(e -> e.setTitle("abc"));
        var exception =
                Assertions.assertThrows(ListBuilderException.class, () -> service.writerPDF(list));
        var msg =
                "Erro ao Gerar PDF - Erro: Erro ao obter Imagem da Label - Nenhuma Imagem encontrada para a Label";
        Assertions.assertEquals(msg, exception.getMessage());
    }

    @Test
    void shouldWriterExceptionNotFoundReadOfWeek() {
        List<VidaCristaExtractWeekDTO> list =
                List.of(VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build());
        var newListItems = new ArrayList<>(list.get(0).getItems());
        newListItems.removeIf(e -> e.getType() == VidaCristaExtractItemTypeEnum.READ_OF_WEEK);
        list.get(0).setItems(newListItems);

        var exception =
                Assertions.assertThrows(ListBuilderException.class, () -> service.writerPDF(list));
        var msg = "Erro ao Gerar PDF - Erro: Erro ao Obter Leitura da Semana";
        Assertions.assertEquals(msg, exception.getMessage());
    }

    @Test
    void shouldWriterPDFOneMonthSuccess() {
        List<VidaCristaExtractWeekDTO> list =
                List.of(
                        VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build(),
                        VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build(),
                        VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build(),
                        VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build());
        Assertions.assertDoesNotThrow(() -> service.writerPDF(list));
    }

    @Test
    void shouldWriterPDFOneMonthSuccessWithSkip1Week() {
        List<VidaCristaExtractWeekDTO> list =
                List.of(
                        VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build(),
                        VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build(),
                        VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build(),
                        VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build());
        list.get(1).setSkip(true);
        list.get(1).setSkipMessage("Message");
        Assertions.assertFalse(list.get(1).toString().isBlank());
        Assertions.assertDoesNotThrow(() -> service.writerPDF(list));
    }

    @Test
    void shouldWriterPDFTwoMonthsSuccess() {
        List<VidaCristaExtractWeekDTO> list =
                List.of(
                        VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build(),
                        VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build(),
                        VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build(),
                        VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build(),
                        VidaCristaExtractWeekDtoBuilder.create().withRandomDataTwoMonths().build());
        Assertions.assertDoesNotThrow(() -> service.writerPDF(list));
    }

    @Test
    void shouldGetImageByLabelTreasuresWordGodCase1() {
        baseGetImageByLabelTreasuresWordGod("TESOUROS DA PALAVRA");
    }

    @Test
    void shouldGetImageByLabelTreasuresWordGodCase2() {
        baseGetImageByLabelTreasuresWordGod("tesouros da palavra");
    }

    @Test
    void shouldGetImageByLabelTreasuresWordGodCase3() {
        baseGetImageByLabelTreasuresWordGod("Tesouros DA PalaVRa");
    }

    private void baseGetImageByLabelTreasuresWordGod(String label) {
        baseGetImageByLabel(label, "treasures-word-god.jpg");
    }

    @Test
    void shouldGetImageByLabelMakeYourBetterCase1() {
        baseGetImageByLabelMakeYourBetter("FAçA SEU MELHOR");
    }

    @Test
    void shouldGetImageByLabelMakeYourBetterCase2() {
        baseGetImageByLabelMakeYourBetter("faça seu melhor");
    }

    @Test
    void shouldGetImageByLabelMakeYourBetterCase3() {
        baseGetImageByLabelMakeYourBetter("Faça seU MElhOr");
    }

    private void baseGetImageByLabelMakeYourBetter(String label) {
        baseGetImageByLabel(label, "make-your-better.jpg");
    }

    @Test
    void shouldGetImageByLabelChristianLifeCase1() {
        baseGetImageByLabelChristianLife("NOSSA VIDA CRISTÃ");
    }

    @Test
    void shouldGetImageByLabelChristianLifeCase2() {
        baseGetImageByLabelChristianLife("nossa vida cristã");
    }

    @Test
    void shouldGetImageByLabelChristianLifeCase3() {
        baseGetImageByLabelChristianLife("nossa VIda CRistã");
    }

    private void baseGetImageByLabelChristianLife(String label) {
        baseGetImageByLabel(label, "christian-life.jpg");
    }

    private void baseGetImageByLabel(String label, String imageNameExpeted) {
        var imageName = service.getImageNameByLabel(label);
        Assertions.assertEquals(imageNameExpeted, imageName);
    }

    @Test
    void shouldGetImageByLabelException() {
        var exception =
                Assertions.assertThrows(
                        ListBuilderException.class, () -> service.getImageNameByLabel("invalid"));
        Assertions.assertEquals(
                "Erro ao obter Imagem da Label - Nenhuma Imagem encontrada para a Label",
                exception.getMessage());
    }
}
