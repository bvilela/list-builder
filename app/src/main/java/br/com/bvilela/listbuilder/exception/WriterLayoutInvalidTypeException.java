package br.com.bvilela.listbuilder.exception;

import br.com.bvilela.listbuilder.enuns.AudienceWriterLayoutEnum;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;

import java.io.Serial;
import java.util.Arrays;

public class WriterLayoutInvalidTypeException extends Exception {

    @Serial private static final long serialVersionUID = 2653958924425254811L;

    public WriterLayoutInvalidTypeException() {
        super("Layout do arquivo inválido!");
    }

    public WriterLayoutInvalidTypeException(String allowValues) {
        super("Layout do arquivo inválido! Valores aceitos: " + allowValues);
    }
}
