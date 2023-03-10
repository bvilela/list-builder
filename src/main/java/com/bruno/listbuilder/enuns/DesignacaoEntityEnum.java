package com.bruno.listbuilder.enuns;

import lombok.Getter;

@Getter
public enum DesignacaoEntityEnum {

	PRESIDENT("Presidente"),
	READER_WATCHTOWER("Leitor de A Sentinela"),
	READER_BIBLESTUDY("Leitor do Estudo Bíblico"),
	AUDIOVIDEO("Aúdio e Vídeo"),
	INDICATOR("Indicador"),
	MICROPHONE("Microfone");
	
	private String label;
	
	DesignacaoEntityEnum(String label) {
		this.label = label;
	}

}
