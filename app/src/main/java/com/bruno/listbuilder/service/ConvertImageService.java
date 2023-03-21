package com.bruno.listbuilder.service;

import java.nio.file.Path;

import com.bruno.listbuilder.exception.ListBuilderException;

public interface ConvertImageService {
	
	void convertToImage(Path path) throws ListBuilderException;

}
