package com.bruno.listbuilder.service;

import org.junit.jupiter.api.AfterEach;

import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.utils.TestUtils;

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
