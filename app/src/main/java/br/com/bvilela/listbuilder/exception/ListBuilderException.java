package br.com.bvilela.listbuilder.exception;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
public class ListBuilderException extends RuntimeException {

    public ListBuilderException(@NotNull String message, Object... args) {
        super(String.format(message, args));
    }

    public ListBuilderException(@NotNull String message) {
        super(message);
    }

    public ListBuilderException(Throwable throwable) {
        super(throwable);
    }
}
