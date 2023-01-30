package com.bruno.listbuilder.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.bruno.listbuilder.enuns.ListTypeEnum;

class FileUtilsTest {

	@Test
	void shouldGenerateOutputFileNameAssistenciaPDF() {
		var name = FileUtils.generateOutputFileNamePDF(ListTypeEnum.ASSISTENCIA, LocalDate.of(2022, 4, 1), LocalDate.of(2022, 6, 7));
		assertEquals("Assistencia_04.Abr_2022-06.Jun_2022.pdf", name);
	}
	
	@Test
	void shouldGenerateOutputFileNameAssistenciaDocx() {
		var name = FileUtils.generateOutputFileNameDocx(ListTypeEnum.ASSISTENCIA, LocalDate.of(2022, 12, 1), LocalDate.of(2023, 1, 30));
		assertEquals("Assistencia_12.Dez_2022-01.Jan_2023.docx", name);
	}
	
	@Test
	void shouldGenerateOutputFileNameDesignacaoPDF() {
		var name = FileUtils.generateOutputFileNamePDF(ListTypeEnum.DESIGNACAO, LocalDate.of(2022, 11, 1));
		assertEquals("Designacao_11.Nov_2022.pdf", name);
	}
	
	@Test
	void shouldGenerateOutputFileNameDesignacaoDocx() {
		var name = FileUtils.generateOutputFileNameDocx(ListTypeEnum.DESIGNACAO, LocalDate.of(2022, 12, 1));
		assertEquals("Designacao_12.Dez_2022.docx", name);
	}
	
	@Test
	void shouldGenerateOutputFileNameDiscursoPDF() {
		var name = FileUtils.generateOutputFileNamePDF(ListTypeEnum.DISCURSO, LocalDate.of(2022, 1, 10));
		assertEquals("Discurso_01.Jan_2022.pdf", name);
	}
	
	@Test
	void shouldGenerateOutputFileNameDiscursoDocx() {
		var name = FileUtils.generateOutputFileNameDocx(ListTypeEnum.DISCURSO, LocalDate.of(2022, 2, 10));
		assertEquals("Discurso_02.Fev_2022.docx", name);
	}
	
	@Test
	void shouldGenerateOutputFileNameLimpezaPDF() {
		var name = FileUtils.generateOutputFileNamePDF(ListTypeEnum.LIMPEZA, LocalDate.of(2022, 3, 1));
		assertEquals("Limpeza_03.Mar_2022.pdf", name);
	}
	
	@Test
	void shouldGenerateOutputFileNameLimpezaDocx() {
		var name = FileUtils.generateOutputFileNameDocx(ListTypeEnum.LIMPEZA, LocalDate.of(2022, 3, 1));
		assertEquals("Limpeza_03.Mar_2022.docx", name);
	}
	
	@Test
	void shouldGenerateOutputFileNameVidaCristaPDF() {
		var name = FileUtils.generateOutputFileNamePDF(ListTypeEnum.VIDA_CRISTA, LocalDate.of(2022, 3, 10));
		assertEquals("Vida_crista_03.Mar_2022.pdf", name);
	}
	
	@Test
	void shouldGenerateOutputFileNameVidaCristaDocx() {
		var name = FileUtils.generateOutputFileNameDocx(ListTypeEnum.VIDA_CRISTA, LocalDate.of(2022, 4, 10));
		assertEquals("Vida_crista_04.Abr_2022.docx", name);
	}
	
}
