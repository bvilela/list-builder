package com.bruno.listbuilder.service.discurso.impl;

import static com.bruno.listbuilder.utils.TestFileUtilsWriteFile.writeFileInputDiscursoFromDto;
import static com.bruno.listbuilder.utils.TestFileUtilsWriteFile.writeFileInputDiscursoSyntaxError;
import static com.bruno.listbuilder.utils.TestFileUtilsWriteFile.writeFileInputDiscursoTemasFromDto;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Paths;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bruno.listbuilder.builder.DiscursoAllThemesDtoBuilder;
import com.bruno.listbuilder.builder.FileInputDataDiscursoDtoBuilder;
import com.bruno.listbuilder.config.AppProperties;
import com.bruno.listbuilder.config.MessageConfig;
import com.bruno.listbuilder.dto.discurso.FileInputDataDiscursoDTO;
import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.service.ConvertImageService;
import com.bruno.listbuilder.utils.FileUtils;
import com.bruno.listbuilder.utils.TestFileUtilsWriteFile;

@SpringBootApplication
class DiscursoGenerateServiceImplTest {

	@InjectMocks
	private DiscursoGenerateServiceImpl service;

	@InjectMocks
	private AppProperties properties;

	@Mock
	private DiscursoWriterServiceImpl writerService;
	
	@Mock
	private ConvertImageService convertImageService;

	private static final String TEMAS_NAO_PODE_SER_VAZIO = "Temas não pode ser vazio";
	private static final String MODE_EXECUTION = ListTypeEnum.DISCURSO.toString();
	private static final String FILE_NAME_DISCURSO_ALL_THEMES = "discursos-temas";
	private static final String FILE_NAME_DATA_DISCURSO_OK = "data-discurso-ok.json";
	private static final String MSG_MISSING_THEME_NUMER_TITLE = "Data: 05-06-2022 - Informe o Número do Tema ou Título!";
	private static String resourceDirectory;

	@BeforeAll
	static void setupBeforeAll() throws ListBuilderException {
		resourceDirectory = Paths.get("src", "test", "resources", MODE_EXECUTION.toLowerCase()).toFile()
				.getAbsolutePath();
		FileUtils.createDirectories(resourceDirectory);
	}

	@BeforeEach
	public void setup() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		FieldUtils.writeField(properties, "inputDir", resourceDirectory, true);
		service = new DiscursoGenerateServiceImpl(properties, writerService, convertImageService);
		writeFileInputDiscursoTemasFromDto(FILE_NAME_DISCURSO_ALL_THEMES,
				DiscursoAllThemesDtoBuilder.create().withRandomData().build());
	}

	@AfterAll
	static void setupAfterAll() {
		TestFileUtilsWriteFile.cleanDirDiscurso();
	}

	@Test
	void shouldModoExecutionNotNull() {
		assertNotNull(service.getExecutionMode());
	}

	@Test
	void shouldGetExecutionMode() {
		assertEquals(ListTypeEnum.DISCURSO, service.getExecutionMode());
	}

	private void createDataDiscursoFileOK() {
		writeFileInputDiscursoFromDto(FILE_NAME_DATA_DISCURSO_OK,
				FileInputDataDiscursoDtoBuilder.create().withRandomData().build());
	}

	@Test
	void shouldGenerateListFileAllThemesInvalidPathFileException() throws IllegalAccessException {
		createDataDiscursoFileOK();
		String fileInvalidNameAllThemes = "xpto.json";
		baseGenerateListException(FILE_NAME_DATA_DISCURSO_OK, fileInvalidNameAllThemes, MessageConfig.FILE_NOT_FOUND);
	}

	@Test
	void shouldGenerateListFileAllThemesSintaxeException() throws IllegalAccessException {
		createDataDiscursoFileOK();
		String fileAllThemesSyntaxError = "dados-discursos-temas-syntax-error.json";
		writeFileInputDiscursoSyntaxError(fileAllThemesSyntaxError);
		baseGenerateListException(FILE_NAME_DATA_DISCURSO_OK, fileAllThemesSyntaxError, MessageConfig.FILE_SYNTAX_ERROR);
	}

	@Test
	void shouldGenerateListFileAllThemesNullException() throws IllegalAccessException {
		createDataDiscursoFileOK();
		String fileAllThemes = "dados-discursos-temas-null.json";
		writeFileInputDiscursoTemasFromDto(fileAllThemes, DiscursoAllThemesDtoBuilder.create().withNullData().build());
		baseGenerateListException(FILE_NAME_DATA_DISCURSO_OK, fileAllThemes, TEMAS_NAO_PODE_SER_VAZIO);
	}

	@Test
	void shouldGenerateListFileAllThemesEmptyException() throws IllegalAccessException {
		createDataDiscursoFileOK();
		String fileAllThemes = "dados-discursos-temas-empty.json";
		writeFileInputDiscursoTemasFromDto(fileAllThemes, DiscursoAllThemesDtoBuilder.create().withEmptyData().build());
		baseGenerateListException(FILE_NAME_DATA_DISCURSO_OK, fileAllThemes, TEMAS_NAO_PODE_SER_VAZIO);
	}

	@Test
	void shouldGenerateListFileInputInvalidPathException() throws IllegalAccessException {
		String fileInvalidName = "xpto.json";
		baseGenerateListException(fileInvalidName, MessageConfig.FILE_NOT_FOUND);
	}

	@Test
	void shouldGenerateListFileInputSintaxeException() throws IllegalAccessException {
		String fileSyntaxError = "data-discurso-syntax-error.json";
		writeFileInputDiscursoSyntaxError(fileSyntaxError);
		baseGenerateListException(fileSyntaxError, MessageConfig.FILE_SYNTAX_ERROR);
	}
	
	@Test
	void shouldGenerateListFileInputThemeNumberTitleNullException() throws IllegalAccessException {
		baseGenerateListFileInputThemeNumberTitleException(null, null, MSG_MISSING_THEME_NUMER_TITLE);
	}
	
	@Test
	void shouldGenerateListFileInputThemeNumberTitleBlankException() throws IllegalAccessException {
		baseGenerateListFileInputThemeNumberTitleException(" ", " ", MSG_MISSING_THEME_NUMER_TITLE);
	}
	
	@Test
	void shouldGenerateListFileInputThemeNumberInvalidTitleNullException() throws IllegalAccessException {
		var message = "05-06-2022 - Numero do Tema invalido. Não é um número válido!";
		baseGenerateListFileInputThemeNumberTitleException("ABC", " ", message);
	}
	
	@Test
	void shouldGenerateListFileInputThemeNumberNotFoundException() throws IllegalAccessException {
		var message = "Nenhum tema encontrada para o Número: 1234";
		baseGenerateListFileInputThemeNumberTitleException("1234", null, message);
	}
	
	private void baseGenerateListFileInputThemeNumberTitleException(String themeNumber, String themeTitle, String message)
			throws IllegalAccessException {
		String fileName = "data-discurso-theme-number-null.json";
		var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
		dto.getReceive().get(0).setThemeNumber(themeNumber);
		dto.getReceive().get(0).setThemeTitle(themeTitle);
		writeFileInputDiscursoFromDto(fileName, dto);
		baseGenerateListException(fileName, message);
	}
	
	@Test
	void shouldGenerateListFileInputSendAndReceiveNull() throws IllegalAccessException {
		String fileName = "data-discurso-send-receive-null.json";
		var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
		dto.setReceive(null);
		dto.setSend(null);
		writeFileInputDiscursoFromDto(fileName, dto);
		baseGenerateListException(fileName, MessageConfig.LIST_SEND_REVEICE_NULL);		
	}
	
	@Test
	void shouldGenerateListSendReceiveSuccessByThemeNumberWithThemeTitleNull() throws IllegalAccessException {
		var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
		dto.getReceive().get(0).setThemeNumber("1");
		dto.getReceive().get(0).setThemeTitle(null);
		baseShouldGenerateListSuccess(dto);
	}

	@Test
	void shouldGenerateListReceiveSuccessByThemeNumberWithThemeTitleNull() throws IllegalAccessException {
		var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
		dto.setSend(null);
		dto.getReceive().get(0).setThemeNumber("1");
		dto.getReceive().get(0).setThemeTitle(null);
		baseShouldGenerateListSuccess(dto);
	}
	
	@Test
	void shouldGenerateListSendSuccessByThemeNumberWithThemeTitleNull() throws IllegalAccessException {
		var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
		dto.setReceive(null);
		dto.getSend().get(0).setThemeNumber("1");
		dto.getSend().get(0).setThemeTitle(null);
		baseShouldGenerateListSuccess(dto);
	}
	
	@Test
	void shouldGenerateListFileInputSuccessByThemeNumberWithThemeTitleEmpty() throws IllegalAccessException {
		var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
		dto.getReceive().get(0).setThemeNumber("1");
		dto.getReceive().get(0).setThemeTitle("");
		baseShouldGenerateListSuccess(dto);
	}
	
	@Test
	void shouldGenerateListFileInputSuccessByThemeTitle() throws IllegalAccessException {
		var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
		dto.getReceive().get(0).setThemeNumber(null);
		assertNotNull(dto.getReceive().get(0).getThemeTitle());
		baseShouldGenerateListSuccess(dto);
		assertFalse(dto.getReceive().get(0).toString().isBlank());
	}
	
	void baseShouldGenerateListSuccess(FileInputDataDiscursoDTO dto) throws IllegalAccessException {
		String fileName = "data-discurso-send-ok.json";
		writeFileInputDiscursoFromDto(fileName, dto);
		FieldUtils.writeField(properties, "inputFileNameDiscursos", fileName, true);
		FieldUtils.writeField(properties, "inputFileNameDiscursosTemas", FILE_NAME_DISCURSO_ALL_THEMES, true);
		assertDoesNotThrow(() -> service.generateList());		
	}

	void baseGenerateListException(String file, String message) throws IllegalAccessException {
		baseGenerateListException(file, FILE_NAME_DISCURSO_ALL_THEMES, message);
	}

	void baseGenerateListException(String file, String fileAllThemes, String message) throws IllegalAccessException {
		FieldUtils.writeField(properties, "inputFileNameDiscursos", file, true);
		FieldUtils.writeField(properties, "inputFileNameDiscursosTemas", fileAllThemes, true);
		var ex = assertThrows(ListBuilderException.class, () -> service.generateList());
		String baseMessage = String.format("Erro ao gerar lista '%s': %s", MODE_EXECUTION, message);
		assertEquals(ex.getMessage(), baseMessage);
	}

}
