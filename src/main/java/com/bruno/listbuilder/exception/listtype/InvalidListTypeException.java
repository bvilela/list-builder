package com.bruno.listbuilder.exception.listtype;

import com.bruno.listbuilder.enuns.ListTypeEnum;

public class InvalidListTypeException extends Exception {

	private static final long serialVersionUID = 2653958924425254811L;

	public InvalidListTypeException() {
		super(String.format("Parametro 'tipo.lista' NÃO é um valor válido! Valores aceitos: %s",
				ListTypeEnum.valuesList()));
	}

}
