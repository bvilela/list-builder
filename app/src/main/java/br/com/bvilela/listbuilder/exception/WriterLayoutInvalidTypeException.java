package br.com.bvilela.listbuilder.exception;

import java.io.Serial;

public class WriterLayoutInvalidTypeException extends Exception {

    @Serial private static final long serialVersionUID = 2653958924425254811L;

    public WriterLayoutInvalidTypeException() {
        super("Layout do arquivo inválido!");
    }

}
