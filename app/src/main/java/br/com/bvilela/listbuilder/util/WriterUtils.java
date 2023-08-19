package br.com.bvilela.listbuilder.util;

public interface WriterUtils<T> {

    T getDocument();

    void addImageHeader(T document);
}
