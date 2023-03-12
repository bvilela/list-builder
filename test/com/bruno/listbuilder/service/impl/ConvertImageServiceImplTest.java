package com.bruno.listbuilder.service.impl;

import com.bruno.listbuilder.builder.VidaCristaExtractWeekDtoBuilder;
import com.bruno.listbuilder.config.AppProperties;
import com.bruno.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import com.bruno.listbuilder.dto.limpeza.FinalListLimpezaItemLayout2DTO;
import com.bruno.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.service.limpeza.impl.LimpezaWriterServiceImpl;
import com.bruno.listbuilder.service.vidacrista.impl.VidaCristaWriterServiceImpl;
import com.bruno.listbuilder.utils.TestUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

class ConvertImageServiceImplTest {

	@InjectMocks
	private ConvertImageServiceImpl service;
	
	@InjectMocks
	private AppProperties properties;
	
	@InjectMocks
	private LimpezaWriterServiceImpl serviceLimpeza;
	
	@InjectMocks
	private VidaCristaWriterServiceImpl serviceVidaCrista;

	@BeforeEach
	public void setup() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		service = new ConvertImageServiceImpl();
		String pathOutput = Paths.get("src", "test", "resources").toFile().getAbsolutePath();
		FieldUtils.writeField(properties, "outputDir", pathOutput, true);
		serviceLimpeza = new LimpezaWriterServiceImpl(properties);
		serviceVidaCrista = new VidaCristaWriterServiceImpl(properties);
	}
	
	@AfterAll
	static void setupAfterAll() {
		TestUtils.cleanResourceDir();
	}

	@Test
	void shouldConvertParamNotDefinid() throws IllegalAccessException {
		Assertions.assertDoesNotThrow(() -> service.convertToImage(null));
	}

	@Test
	void shouldConvertParamFalse() throws IllegalAccessException {
		setConvertParamActive(false);
		Assertions.assertDoesNotThrow(() -> service.convertToImage(null));
	}

	@Test
	void shouldConvertParamTrueSuccessOnePages() throws IllegalAccessException, ListBuilderException {
		setConvertParamActive(true);
		var path = generateLimpezaPDF();
		Assertions.assertDoesNotThrow(() -> service.convertToImage(path));
		File file = new File(path.toString().replace(".pdf", ".png"));
		Assertions.assertTrue(file.exists());
	}
	
	@Test
	void shouldConvertParamTrueSuccessTwoPages() throws IllegalAccessException, ListBuilderException {
		setConvertParamActive(true);
		var path = generateVidaCristaPDF();
		Assertions.assertDoesNotThrow(() -> service.convertToImage(path));
		File file1 = new File(path.toString().replace(".pdf", "-pag01.png"));
		File file2 = new File(path.toString().replace(".pdf", "-pag02.png"));
		Assertions.assertTrue(file1.exists());
		Assertions.assertTrue(file2.exists());
	}

	@Test
	void shouldConvertException() throws IllegalAccessException {
		setConvertParamActive(true);
		var ex = Assertions.assertThrows(ListBuilderException.class, () -> service.convertToImage(null));
		Assertions.assertTrue(ex.getMessage().contains("Erro ao Convert PDF para Imagem - Erro: "));
	}

	private void setConvertParamActive(boolean param) throws IllegalAccessException {
		FieldUtils.writeField(service, "convertPdfToImage", param, true);
	}
	
	private Path generateLimpezaPDF() throws ListBuilderException {
		FinalListLimpezaDTO dto = FinalListLimpezaDTO.builder().itemsLayout2(List.of(
				FinalListLimpezaItemLayout2DTO.builder().withGroup("P1, P2")
				.withDate1(LocalDate.now()).withLabel1("label1")
				.withDate2(LocalDate.now()).withLabel2("label2").build(),
				FinalListLimpezaItemLayout2DTO.builder().withGroup("P1, P2")
				.withDate1(LocalDate.now()).withLabel1("label1").build()
		)).build();
		return serviceLimpeza.writerPDF(dto, "footerMessage", "headerMessage", 2);
	}
	
	private Path generateVidaCristaPDF() throws ListBuilderException {
		List<VidaCristaExtractWeekDTO> list = List.of(VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build(),
				VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build(),
				VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build(),
				VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build());
		var path = serviceVidaCrista.writerPDF(list);
		return path;
	}

}
