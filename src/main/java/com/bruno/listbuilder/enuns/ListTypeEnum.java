package com.bruno.listbuilder.enuns;

import com.bruno.listbuilder.config.MarginBase;
import com.bruno.listbuilder.config.SizeBase;
import com.bruno.listbuilder.config.SizeConfig;
import com.bruno.listbuilder.exception.listtype.InvalidListTypeException;

import lombok.Getter;

@Getter
public enum ListTypeEnum {
	
	LIMPEZA,
	DESIGNACAO,
	ASSISTENCIA(SizeConfig.AUDIENCE_MARGIN, SizeConfig.AUDIENCE_HEADER),
	DISCURSO,
	VIDA_CRISTA(SizeConfig.CHRISTIAN_LIFE_MARGIN, SizeConfig.CHRISTIAN_LIFE_HEADER, SizeConfig.CHRISTIAN_LIFE_SUBHEADER);
	
	private MarginBase pageMg;
	private SizeBase header;
	private SizeBase subHeader;
	
	private ListTypeEnum() {
		this.pageMg = SizeConfig.DEFAULT_MARGIN;
		this.header = SizeConfig.DEFAULT_HEADER;
		this.subHeader = SizeConfig.DEFAULT_SUBHEADER;
	}
	
	private ListTypeEnum(MarginBase pageMg, SizeBase... header) {
		this.pageMg = pageMg;
		this.header = header[0];
		if (header.length > 1) {
			this.subHeader = header[1];	
		}
	}
	
	public static ListTypeEnum getByName(String name) throws InvalidListTypeException {
		try {
			return ListTypeEnum.valueOf(name.toUpperCase());
			
		} catch (IllegalArgumentException e) {
			throw new InvalidListTypeException();
		}
	}
	
	public String getInputFileName() {
		return String.format("dados-%s.json", this.toString().toLowerCase());
	}
	
}
