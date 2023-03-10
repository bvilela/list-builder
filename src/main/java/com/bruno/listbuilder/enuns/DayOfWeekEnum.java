package com.bruno.listbuilder.enuns;

import java.time.DayOfWeek;

import com.bruno.listbuilder.exception.ListBuilderException;

import lombok.Getter;

@Getter
public enum DayOfWeekEnum {

	SEGUNDA("Segunda", DayOfWeek.MONDAY),
	TERCA("Terça", DayOfWeek.TUESDAY),
	QUARTA("Quarta", DayOfWeek.WEDNESDAY),
	QUINTA("Quinta", DayOfWeek.THURSDAY),
	SEXTA("Sexta", DayOfWeek.FRIDAY),
	SABADO("Sábado", DayOfWeek.SATURDAY),
	DOMINGO("Domingo", DayOfWeek.SUNDAY);
	
	private String name;
	private DayOfWeek dayOfWeek;
	
	DayOfWeekEnum(String name, DayOfWeek dayOfWeek) {
		this.name = name;
		this.dayOfWeek = dayOfWeek;
	}
	
	public static DayOfWeekEnum getByDayOfWeek(DayOfWeek dayOfWeek) throws ListBuilderException {
		for (DayOfWeekEnum dia : DayOfWeekEnum.values()) {
			if (dia.getDayOfWeek() == dayOfWeek) {
				return dia;
			}
		}
		throw new ListBuilderException("Nenhum Dia da Semana encontrado para o valor: " + dayOfWeek);
	}

}
