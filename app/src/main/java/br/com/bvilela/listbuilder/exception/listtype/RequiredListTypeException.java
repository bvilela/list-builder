package br.com.bvilela.listbuilder.exception.listtype;

import java.io.Serial;

public class RequiredListTypeException extends Exception {

    @Serial private static final long serialVersionUID = 2653958924425254811L;

    public RequiredListTypeException() {
        super("Parametro 'tipo.lista' NÃO definido ou VAZIO!");
    }
}
