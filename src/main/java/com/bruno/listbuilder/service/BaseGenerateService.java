package com.bruno.listbuilder.service;

import org.slf4j.Logger;

import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.exception.ListBuilderException;

public interface BaseGenerateService {
	
	ListTypeEnum getExecutionMode();
	
	void generateList() throws ListBuilderException;
	
	default void logInit(Logger log) {
		log.info("Iniciando Geração Lista: '{}'", getExecutionMode());
	}
	
	default void logFinish(Logger log) {
		log.info("Lista de '{}' gerada com Sucesso!", getExecutionMode());
	}
	
	default ListBuilderException defaultListBuilderException(Exception e) {
		return new ListBuilderException("Erro ao gerar lista '%s': %s", getExecutionMode(), e.getMessage());
	}

}
