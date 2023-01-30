package com.bruno.listbuilder.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import com.bruno.listbuilder.dto.assistencia.FileInputDataAssistenciaDTO;
import com.bruno.listbuilder.dto.designacao.FileInputDataDesignacaoDTO;
import com.bruno.listbuilder.dto.discurso.DiscursoAllThemesDTO;
import com.bruno.listbuilder.dto.discurso.FileInputDataDiscursoDTO;
import com.bruno.listbuilder.dto.limpeza.FileInputDataLimpezaDTO;
import com.bruno.listbuilder.dto.vidacrista.FileInputDataVidaCristaDTO;
import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bvilela.utils.GsonUtils;

public class TestFileUtilsWriteFile {

	private static final String RESOURCE_DIRECTORY_LIMPEZA = Paths
			.get("src", "test", "resources", ListTypeEnum.LIMPEZA.toString().toLowerCase()).toFile()
			.getAbsolutePath();
	
	private static final String RESOURCE_DIRECTORY_ASSISTENCIA = Paths
			.get("src", "test", "resources", ListTypeEnum.ASSISTENCIA.toString().toLowerCase()).toFile()
			.getAbsolutePath();
	
	private static final String RESOURCE_DIRECTORY_DISCURSO = Paths
			.get("src", "test", "resources", ListTypeEnum.DISCURSO.toString().toLowerCase()).toFile()
			.getAbsolutePath();
	
	private static final String RESOURCE_DIRECTORY_VIDA_CRISTA = Paths
			.get("src", "test", "resources", ListTypeEnum.VIDA_CRISTA.toString().toLowerCase()).toFile()
			.getAbsolutePath();
	
	private static final String RESOURCE_DIRECTORY_DESIGNACAO = Paths
			.get("src", "test", "resources", ListTypeEnum.DESIGNACAO.toString().toLowerCase()).toFile()
			.getAbsolutePath();	

	private TestFileUtilsWriteFile() {
	}

	public static void writeFileInputLimpezaFromDto(String fileName, FileInputDataLimpezaDTO dto) {
		baseWriteFileInputFromDto(RESOURCE_DIRECTORY_LIMPEZA, fileName, dto);
	}
	
	public static void writeFileInputAssistenciaFromDto(String fileName, FileInputDataAssistenciaDTO dto) {
		baseWriteFileInputFromDto(RESOURCE_DIRECTORY_ASSISTENCIA, fileName, dto);
	}
	
	public static void writeFileInputDiscursoFromDto(String fileName, FileInputDataDiscursoDTO dto) {
		baseWriteFileInputFromDto(RESOURCE_DIRECTORY_DISCURSO, fileName, dto);
	}
	
	public static void writeFileInputDiscursoTemasFromDto(String fileName, DiscursoAllThemesDTO dto) {
		baseWriteFileInputFromDto(RESOURCE_DIRECTORY_DISCURSO, fileName, dto);
	}
	
	public static void writeFileInputVidaCristaFromDto(String fileName, FileInputDataVidaCristaDTO dto) {
		baseWriteFileInputFromDto(RESOURCE_DIRECTORY_VIDA_CRISTA, fileName, dto);
	}
	
	public static void writeFileInputDesignacaoFromDto(String fileName, FileInputDataDesignacaoDTO dto) {
		baseWriteFileInputFromDto(RESOURCE_DIRECTORY_DESIGNACAO, fileName, dto);
	}
	
	public static void baseWriteFileInputFromDto(String directory, String fileName, Object dto) {
		String file = Paths.get(directory, fileName).toString();

		try (FileWriter fileWriter = new FileWriter(new File(file))) {
			fileWriter.write(GsonUtils.getGson().toJson(dto));
			fileWriter.flush();
		} catch (IOException e) {
			// do nothing
		}
	}

	public static void writeFileInputLimpezaSyntaxError(String fileName) {
		baseWriteFileInputSyntaxError(RESOURCE_DIRECTORY_LIMPEZA, fileName);
	}
	
	public static void writeFileInputAssistenciaSyntaxError(String fileName) {
		baseWriteFileInputSyntaxError(RESOURCE_DIRECTORY_ASSISTENCIA, fileName);
	}
	
	public static void writeFileInputDiscursoSyntaxError(String fileName) {
		baseWriteFileInputSyntaxError(RESOURCE_DIRECTORY_DISCURSO, fileName);
	}
	
	public static void writeFileInputVidaCristaSyntaxError(String fileName) {
		baseWriteFileInputSyntaxError(RESOURCE_DIRECTORY_VIDA_CRISTA, fileName);
	}
	
	public static void writeFileInputDesignacaoSyntaxError(String fileName) {
		baseWriteFileInputSyntaxError(RESOURCE_DIRECTORY_DESIGNACAO, fileName);
	}
	
	private static void baseWriteFileInputSyntaxError(String directory, String fileName) {
		String file = Paths.get(directory, fileName).toString();

		try (FileWriter fileWriter = new FileWriter(new File(file))) {
			fileWriter.write("syntax-error");
			fileWriter.flush();
		} catch (IOException e) {
			// do nothing
		}
	}

	public static void cleanDirLimpeza() {
		Arrays.stream(new File(Paths.get(RESOURCE_DIRECTORY_LIMPEZA).toString()).listFiles()).forEach(File::delete);
	}
	
	public static void cleanDirAssistencia() {
		Arrays.stream(new File(Paths.get(RESOURCE_DIRECTORY_ASSISTENCIA).toString()).listFiles()).forEach(File::delete);
	}

	public static void cleanDirDiscurso() {
		Arrays.stream(new File(Paths.get(RESOURCE_DIRECTORY_DISCURSO).toString()).listFiles()).forEach(File::delete);
	}
	
	public static void cleanDirVidaCrista() {
		Arrays.stream(new File(Paths.get(RESOURCE_DIRECTORY_VIDA_CRISTA).toString()).listFiles()).forEach(File::delete);
	}
	
	public static void cleanDirDesignacao() {
		Arrays.stream(new File(Paths.get(RESOURCE_DIRECTORY_DESIGNACAO).toString()).listFiles()).forEach(File::delete);
	}

	public static void cleanResourceDir() {
		var files = Paths.get("src", "test", "resources").toFile().listFiles((f, p) -> p.endsWith(".pdf") || p.endsWith(".png") || p.endsWith(".docx"));
		Arrays.stream(files).forEach(File::delete);
	}
}
