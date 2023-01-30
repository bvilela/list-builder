package com.bruno.listbuilder.service.vidacrista.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bruno.listbuilder.builder.VidaCristaExtractWeekDtoBuilder;
import com.bruno.listbuilder.config.AppProperties;
import com.bruno.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import com.bruno.listbuilder.enuns.VidaCristaExtractItemType;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.utils.TestFileUtilsWriteFile;

@SpringBootApplication
class VidaCristaWriterServiceImplTest {

	@InjectMocks
	private VidaCristaWriterServiceImpl service;

	@InjectMocks
	private AppProperties properties;

	@BeforeEach
	void setupBeforeEach() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		String pathOutput = Paths.get("src", "test", "resources").toFile().getAbsolutePath();
		FieldUtils.writeField(properties, "outputDir", pathOutput, true);
		service = new VidaCristaWriterServiceImpl(properties);
	}
	
	@AfterAll
	static void setupAfterAll() {
		TestFileUtilsWriteFile.cleanResourceDir();
	}

	@Test
	void shouldWriterExceptionNotFoundImage() throws ListBuilderException {
		List<VidaCristaExtractWeekDTO> list = List
				.of(VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build());

		list.get(0).getItems().stream().filter(e -> e.getType() == VidaCristaExtractItemType.LABEL).findFirst()
				.ifPresent(e -> e.setTitle("abc"));
		var ex = assertThrows(ListBuilderException.class, () -> service.writerPDF(list));
		var msg = "Erro ao Gerar PDF - Erro: Erro ao obter Imagem da Label - Nenhuma Imagem encontrada para a Label";
		assertEquals(msg, ex.getMessage());
	}

	@Test
	void shouldWriterExceptionNotFoundReadOfWeek() throws ListBuilderException {
		List<VidaCristaExtractWeekDTO> list = List
				.of(VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build());
		var newListItems = new ArrayList<>(list.get(0).getItems());
		newListItems.removeIf(e -> e.getType() == VidaCristaExtractItemType.READ_OF_WEEK);
		list.get(0).setItems(newListItems);

		var ex = assertThrows(ListBuilderException.class, () -> service.writerPDF(list));
		var msg = "Erro ao Gerar PDF - Erro: Erro ao Obter Leitura da Semana";
		assertEquals(msg, ex.getMessage());
	}

	@Test
	void shouldWriterPDFOneMonthSuccess() throws ListBuilderException {
		List<VidaCristaExtractWeekDTO> list = List.of(VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build(),
				VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build(),
				VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build(),
				VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build());
		assertDoesNotThrow(() -> service.writerPDF(list));
	}
	
	@Test
	void shouldWriterPDFOneMonthSuccessWithSkip1Week() throws ListBuilderException {
		List<VidaCristaExtractWeekDTO> list = List.of(VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build(),
				VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build(),
				VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build(),
				VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build());
		list.get(1).setSkip(true);
		list.get(1).setSkipMessage("Message");
		assertFalse(list.get(1).toString().isBlank());
		assertDoesNotThrow(() -> service.writerPDF(list));
	}
	
	@Test
	void shouldWriterPDFTwoMonthsSuccess() throws ListBuilderException {
		List<VidaCristaExtractWeekDTO> list = List.of(VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build(),
				VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build(),
				VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build(),
				VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build(),
				VidaCristaExtractWeekDtoBuilder.create().withRandomDataTwoMonths().build());
		assertDoesNotThrow(() -> service.writerPDF(list));
	}
	
	@Test
	void shouldGetImageByLabelTreasuresWordGodCase1() throws ListBuilderException {
		baseGetImageByLabelTreasuresWordGod("TESOUROS DA PALAVRA");
	}
	
	@Test
	void shouldGetImageByLabelTreasuresWordGodCase2() throws ListBuilderException {
		baseGetImageByLabelTreasuresWordGod("tesouros da palavra");
	}
	
	@Test
	void shouldGetImageByLabelTreasuresWordGodCase3() throws ListBuilderException {
		baseGetImageByLabelTreasuresWordGod("Tesouros DA PalaVRa");
	}
	
	private void baseGetImageByLabelTreasuresWordGod(String label) throws ListBuilderException {
		baseGetImageByLabel(label, "treasures-word-god.jpg");
	}
	
	@Test
	void shouldGetImageByLabelMakeYourBetterCase1() throws ListBuilderException {
		baseGetImageByLabelMakeYourBetter("FAçA SEU MELHOR");
	}
	
	@Test
	void shouldGetImageByLabelMakeYourBetterCase2() throws ListBuilderException {
		baseGetImageByLabelMakeYourBetter("faça seu melhor");
	}
	
	@Test
	void shouldGetImageByLabelMakeYourBetterCase3() throws ListBuilderException {
		baseGetImageByLabelMakeYourBetter("Faça seU MElhOr");
	}
	
	private void baseGetImageByLabelMakeYourBetter(String label) throws ListBuilderException {
		baseGetImageByLabel(label, "make-your-better.jpg");
	}
	
	@Test
	void shouldGetImageByLabelChristianLifeCase1() throws ListBuilderException {
		baseGetImageByLabelChristianLife("NOSSA VIDA CRISTÃ");
	}
	
	@Test
	void shouldGetImageByLabelChristianLifeCase2() throws ListBuilderException {
		baseGetImageByLabelChristianLife("nossa vida cristã");
	}
	
	@Test
	void shouldGetImageByLabelChristianLifeCase3() throws ListBuilderException {
		baseGetImageByLabelChristianLife("nossa VIda CRistã");
	}
	
	private void baseGetImageByLabelChristianLife(String label) throws ListBuilderException {
		baseGetImageByLabel(label, "christian-life.jpg");
	}
	
	private void baseGetImageByLabel(String label, String imageNameExpeted) throws ListBuilderException {
		var imageName = service.getImageNameByLabel(label);
		assertEquals(imageNameExpeted, imageName);
	}
	
	@Test
	void shouldGetImageByLabelException() throws ListBuilderException {
		var ex = assertThrows(ListBuilderException.class, () -> service.getImageNameByLabel("invalid"));
		assertEquals("Erro ao obter Imagem da Label - Nenhuma Imagem encontrada para a Label", ex.getMessage());
	}

}
