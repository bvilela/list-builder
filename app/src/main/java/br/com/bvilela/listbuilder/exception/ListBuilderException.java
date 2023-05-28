package br.com.bvilela.listbuilder.exception;

import java.io.Serial;
import javax.validation.constraints.NotNull;

public class ListBuilderException extends Exception {

    @Serial private static final long serialVersionUID = 4682053954814519272L;

    public ListBuilderException(@NotNull String message, Object... args) {
        super(String.format(message, args));
    }

    public ListBuilderException(@NotNull String message) {
        super(message);
    }
}
