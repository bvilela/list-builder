package br.com.bvilela.listbuilder.service;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.util.FileUtils;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;

public interface BaseGenerateService {

    ListTypeEnum getListType();

    void generateList();

    // TODO: remover
    default void logInit(Logger log) {
        log.info("Iniciando Geração Lista: '{}'", getListType());
    }

    default String logInitMessage() {
        return "Iniciando Geração Lista: '" + getListType() + "'";
    }

    // TODO: remover
    default void logFinish(Logger log) {
        log.info("Lista '{}' gerada com Sucesso!", getListType());
    }

    default String logFinishMessage() {
        return "Lista '" + getListType() + "' gerada com Sucesso!";
    }

    // TODO: remover
    default ListBuilderException defaultListBuilderException(Logger log, Exception exception) {
        log.error("Erro ao gerar lista '{}': {}", getListType(), exception.getMessage());
        return new ListBuilderException(exception);
    }

    default String logErrorMessage(Exception ex) {
        return String.format("Erro ao gerar lista '%s': %s", getListType(), ex.getMessage());
    }

    default <T> T getFileInputDataDTO(AppProperties appProperties, Class<T> clazz) {
        Path pathInputFile =
                Paths.get(appProperties.getInputDir(), getListType().getInputFileName());
        return FileUtils.readInputFile(pathInputFile, clazz);
    }
}
