package br.com.bvilela.listbuilder.utils;

import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;

public interface WriterUtils<T> {

    T getDocument(ListTypeEnum listTypeEnum);

    void addImageHeader(T document, ListTypeEnum listType) throws ListBuilderException;
}
