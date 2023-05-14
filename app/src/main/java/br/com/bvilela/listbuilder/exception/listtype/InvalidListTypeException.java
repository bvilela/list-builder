package br.com.bvilela.listbuilder.exception.listtype;

import java.util.Arrays;

import br.com.bvilela.listbuilder.enuns.ListTypeEnum;

public class InvalidListTypeException extends Exception {

	private static final long serialVersionUID = 2653958924425254811L;

	public InvalidListTypeException() {
		super(String.format("Parametro 'tipo.lista' NÃO é um valor válido! Valores aceitos: %s",
				Arrays.toString(ListTypeEnum.values())));
	}

}