package br.com.bvilela.listbuilder.service;

import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.utils.TestUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;

public abstract class BaseGenerateServiceTest<T, B> {

    protected TestUtils testUtils;
    protected B builder;

    @SneakyThrows
    public BaseGenerateServiceTest(ListTypeEnum listType, B builder) {
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
