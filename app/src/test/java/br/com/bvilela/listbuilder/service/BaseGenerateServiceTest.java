package br.com.bvilela.listbuilder.service;

import br.com.bvilela.listbuilder.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;

import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;

public abstract class BaseGenerateServiceTest<T, B> {

    protected static TestUtils testUtils;
    protected B builder;

    public BaseGenerateServiceTest(ListTypeEnum listType, B builder) throws ListBuilderException {
        testUtils = new TestUtils(listType);
        testUtils.createDirectory();
        this.builder = builder;
    }

    @AfterEach
    void setupAfterAll() {
        testUtils.cleanDirectory();
    }

    public void writeFileInputFromDto(T dto) {
        testUtils.writeFileInputFromDto(dto);
    }
}
