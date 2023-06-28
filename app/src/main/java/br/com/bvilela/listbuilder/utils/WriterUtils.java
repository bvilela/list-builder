package br.com.bvilela.listbuilder.utils;

public interface WriterUtils<T> {

    T getDocument();

    void addImageHeader(T document);

}
