package br.com.bvilela.listbuilder.service;

import java.nio.file.Path;
import java.nio.file.Paths;

import br.com.bvilela.listbuilder.config.AppProperties;
import org.slf4j.Logger;

import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.utils.FileUtils;

public interface BaseGenerateService {

	ListTypeEnum getListType();

	void generateList() throws ListBuilderException;

	default void logInit(Logger log) {
		log.info("Iniciando Geração Lista: '{}'", getListType());
	}

	default void logFinish(Logger log) {
		log.info("Lista '{}' gerada com Sucesso!", getListType());
	}

	default ListBuilderException defaultListBuilderException(Exception exception) {
		return new ListBuilderException("Erro ao gerar lista '%s': %s", getListType(), exception.getMessage());
	}

	default <T> T getFileInputDataDTO(AppProperties appProperties, Class<T> clazz)
			throws ListBuilderException {
		Path pathInputFile = Paths.get(appProperties.getInputDir(), getListType().getInputFileName());
		return FileUtils.readInputFile(pathInputFile, clazz);
	}

}
