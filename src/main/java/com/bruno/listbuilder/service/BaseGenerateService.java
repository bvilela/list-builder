package com.bruno.listbuilder.service;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;

import com.bruno.listbuilder.config.AppProperties;
import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.utils.FileUtils;

public interface BaseGenerateService {

	ListTypeEnum getListType();

	void generateList() throws ListBuilderException;

	default void logInit(Logger log) {
		log.info("Iniciando Geração Lista: '{}'", getListType());
	}

	default void logFinish(Logger log) {
		log.info("Lista '{}' gerada com Sucesso!", getListType());
	}

	default ListBuilderException defaultListBuilderException(Exception e) {
		return new ListBuilderException("Erro ao gerar lista '%s': %s", getListType(), e.getMessage());
	}

	default <T> T getFileInputDataDTO(AppProperties appProperties, Class<T> clazz)
			throws ListBuilderException {
		Path pathInputFile = Paths.get(appProperties.getInputDir(), getListType().getInputFileName());
		return FileUtils.readInputFile(pathInputFile, clazz);
	}

}
