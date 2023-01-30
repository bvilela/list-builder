package com.bruno.listbuilder.exception.listtype;

import javax.validation.constraints.NotNull;

import com.bruno.listbuilder.enuns.ListTypeEnum;

public class ServiceListTypeNotFoundException extends Exception {

	private static final long serialVersionUID = 3052723584627020914L;

	public ServiceListTypeNotFoundException(@NotNull ListTypeEnum listType) {
		super(String.format("Nenhum Processador definido para a lista: %s", listType.toString()));
	}

}
