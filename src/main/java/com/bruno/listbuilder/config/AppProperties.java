package com.bruno.listbuilder.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class AppProperties {
	
	@Value("${input.dir}")
	private String inputDir;
	
	@Value("${output.dir}")
	private String outputDir;
	
	@Value("${input.file.limpeza}")
	private String inputFileNameLimpeza;
	
	@Value("${layout.limpeza}")
	private int layoutLimpeza;
	
	@Value("${input.file.assistencia}")
	private String inputFileNameAssistencia;
	
	@Value("${input.file.discursos}")
	private String inputFileNameDiscursos;
	
	@Value("${input.file.discursos.temas}")
	private String inputFileNameDiscursosTemas;
	
	@Value("${input.file.vida.crista}")
	private String inputFileNameVidaCrista;
	
	@Value("${input.file.designacao}")
	private String inputFileNameDesignacao;
	
}
