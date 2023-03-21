package com.bruno.listbuilder.utils;

import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.exception.ListBuilderException;

public interface WriterUtils<T> {
	
	T getDocument(ListTypeEnum listTypeEnum);
	
	void addImageHeader(T document, ListTypeEnum listType) throws ListBuilderException;

}
