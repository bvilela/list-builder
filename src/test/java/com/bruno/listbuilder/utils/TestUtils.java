package com.bruno.listbuilder.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.function.Executable;

import com.bruno.listbuilder.dto.discurso.DiscursoAllThemesDTO;
import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bvilela.utils.GsonUtils;

import lombok.Getter;

public class TestUtils {

	private static final String FILE_INPUT_DISCURSO_ALL_THEMES = "dados-discursos-temas.json";

	private final ListTypeEnum listType;

	@Getter
	private final String resourceDirectory;

	public TestUtils(ListTypeEnum listType) {
		this.listType = listType;
		this.resourceDirectory = getResourceDirectoryByListType();
	}

	private String getResourceDirectoryByListType() {
		return Paths.get("src", "test", "resources", listType.toString().toLowerCase()).toFile().getAbsolutePath();
	}

	public void createDirectory() throws ListBuilderException {
		FileUtils.createDirectories(this.resourceDirectory);
	}

	public void writeFileInputSyntaxError() {
		this.writeFileInputFromDto(this.listType.getInputFileName(), "syntax-error");
	}

	public void writeFileInputDiscursoAllThemesSyntaxError() {
		this.writeFileInputFromDto(FILE_INPUT_DISCURSO_ALL_THEMES, "syntax-error");
	}

	public void writeFileInputFromDto(Object dto) {
		this.writeFileInputFromDto(this.listType.getInputFileName(), GsonUtils.getGson().toJson(dto));
	}

	public void writeFileInputFromDto(String fileName, Object dto) {
		this.writeFileInputFromDto(fileName, GsonUtils.getGson().toJson(dto));
	}

	public void writeFileInputDiscursoAllThemes(DiscursoAllThemesDTO dto) {
		this.writeFileInputFromDto(FILE_INPUT_DISCURSO_ALL_THEMES, GsonUtils.getGson().toJson(dto));
	}

	private void writeFileInputFromDto(String fileName, String content) {
		String file = Paths.get(this.resourceDirectory, fileName).toString();
		try (FileWriter fileWriter = new FileWriter(new File(file))) {
			fileWriter.write(content);
			fileWriter.flush();
		} catch (IOException e) {
			// do nothing
		}
	}

	public void cleanDirectory() {
		Arrays.stream(new File(Paths.get(this.resourceDirectory).toString()).listFiles()).forEach(File::delete);
	}

	public <T> void validateException(Class<T> expectedType, Executable executable, String expectedMessageError)
			throws IllegalAccessException {
		var ex = assertThrows(ListBuilderException.class, executable);
		String expectedMessage = String.format("Erro ao gerar lista '%s': %s", this.listType, expectedMessageError);
		assertEquals(expectedMessage, ex.getMessage());
	}

	public static void cleanResourceDir() {
		var files = Paths.get("src", "test", "resources").toFile().listFiles((f, p) -> p.endsWith(".pdf") || p.endsWith(".png") || p.endsWith(".docx"));
		Arrays.stream(files).forEach(File::delete);
	}
}
