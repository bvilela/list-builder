package br.com.bvilela.listbuilder.service;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.util.FileUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface BaseGenerateService {

    ListTypeEnum getListType();

    void generateList();

    default String logInitMessage() {
        return "Iniciando Geração Lista: '" + getListType() + "'";
    }

    default String logFinishMessage() {
        return "Lista '" + getListType() + "' gerada com Sucesso!";
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
