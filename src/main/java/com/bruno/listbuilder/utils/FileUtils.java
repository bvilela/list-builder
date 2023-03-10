package com.bruno.listbuilder.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bvilela.utils.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class FileUtils {

	private FileUtils() {
	}
	
	private static final String PDF_EXTENSION = ".pdf";
	private static final String DOCX_EXTENSION = ".docx";

	public static void createDirectories(String path) throws ListBuilderException {
		try {
			Files.createDirectories(Paths.get(path));
		} catch (Exception e) {
			throw new ListBuilderException("Erro ao criar o diretorio '%d' - Erro: %d", path, e.getMessage());
		}
	}

	public static <T> T readInputFile(Path pathInputFile, Class<T> clazz) throws ListBuilderException {

		log.info("Inciando Leitura do Arquivo '{}'", pathInputFile.toString());

		try (JsonReader reader = new JsonReader(new FileReader(pathInputFile.toString(), StandardCharsets.UTF_8))) {
			Gson gson = GsonUtils.getGson();
			T data = gson.fromJson(reader, clazz);

			log.info("Arquivo lido com Sucesso!");

			return data;

		} catch (IOException e) {
			log.error("Error ao ler arquivo: {}", e.getMessage());
			throw new ListBuilderException("Erro ao ler arquivo - Arquivo não encontrado");
		} catch (JsonSyntaxException e) {
			log.error("Error ao ler JSON: {}", e.getMessage());
			throw new ListBuilderException("Erro ao ler arquivo - Arquivo não é um JSON válido");
		}
	}
	
	public static void writeTxtFile(File file, StringBuilder sb) throws ListBuilderException {
		try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
			writer.append(sb);	
		} catch (IOException e) {
			log.error("Error Escrever Arquivo TXT: {}", e.getMessage());
			throw new ListBuilderException("Erro ao escrever arquivo TXT");			
		}
	}
	
	public static ClassPathResource getClassPathImageHeader(ListTypeEnum listType) {
		return getClassPathImage(listType, "header.jpg");
	}
	
	public static ClassPathResource getClassPathImage(ListTypeEnum listType, String imageName) {
		var path = Paths.get("images", listType.toString().toLowerCase(), imageName);
		return new ClassPathResource(path.toString());
	}
	
	public static String generateOutputFileNamePDF(ListTypeEnum listType, LocalDate date) {
		return generateOutputFileName(listType, date, PDF_EXTENSION);
	}
	
	public static String generateOutputFileNamePDF(ListTypeEnum listType, LocalDate date1, LocalDate date2) {
		return generateOutputFileName(listType, date1, date2, PDF_EXTENSION);
	}
	
	public static String generateOutputFileNameDocx(ListTypeEnum listType, LocalDate date) {
		return generateOutputFileName(listType, date, DOCX_EXTENSION);
	}
	
	public static String generateOutputFileNameDocx(ListTypeEnum listType, LocalDate date1, LocalDate date2) {
		return generateOutputFileName(listType, date1, date2, DOCX_EXTENSION);
	}

	private static String generateOutputFileName(ListTypeEnum listType, LocalDate date, String extension) {
		var listName = getListName(listType);
		var year = String.valueOf(date.getYear());
		var monthNumber = monthNumber(date);
		var nameMonthShort = getMonthNameShort(date);
		return String.format("%s_%s.%s_%s%s", listName, monthNumber, nameMonthShort, year, extension);
	}

	private static String generateOutputFileName(ListTypeEnum listType, LocalDate date1, LocalDate date2, String extension) {
		var listName = getListName(listType);
		var year1 = String.valueOf(date1.getYear());
		var monthNumber1 = monthNumber(date1);
		var monthNameShort1 = getMonthNameShort(date1);
		var year2 = String.valueOf(date2.getYear());
		var monthNumber2 = monthNumber(date2);
		var monthNameShort2 = getMonthNameShort(date2);
		return String.format("%s_%s.%s_%s-%s.%s_%s%s", listName, monthNumber1, monthNameShort1, year1, monthNumber2,
				monthNameShort2, year2, extension);
	}
	
	private static String getListName(ListTypeEnum listType) {
		return StringUtils.capitalize(listType.toString().toLowerCase());
	}

	private static String getMonthNameShort(LocalDate date1) {
		return DateUtils.getNameMonthShortCapitalize(date1).replace(".", "");
	}
	
	private static String monthNumber(LocalDate date) {
		return StringUtils.leftPad(String.valueOf(date.getMonthValue()), 2, "0");
	}
	
}
