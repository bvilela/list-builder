package br.com.bvilela.listbuilder.utils;

import br.com.bvilela.listbuilder.enuns.ListTypeEnum;

public interface WriterUtils<T> {

    T getDocument(ListTypeEnum listTypeEnum);

    void addImageHeader(T document, ListTypeEnum listType);
}
