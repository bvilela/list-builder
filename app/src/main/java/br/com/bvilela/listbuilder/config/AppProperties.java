package br.com.bvilela.listbuilder.config;

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
	
	@Value("${layout.limpeza}")
	private int layoutLimpeza;
	
}
