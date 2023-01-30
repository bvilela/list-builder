package com.bruno.listbuilder.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;

import org.junit.jupiter.api.Test;

import com.bruno.listbuilder.enuns.DayOfWeekEnum;
import com.bruno.listbuilder.exception.ListBuilderException;

class OtherTest {
	
	@Test
	void sholdDayWeekEnumValid() throws ListBuilderException {
		assertEquals(DayOfWeekEnum.SEGUNDA, DayOfWeekEnum.getByDayOfWeek(DayOfWeek.MONDAY));
		assertEquals(DayOfWeekEnum.TERCA, DayOfWeekEnum.getByDayOfWeek(DayOfWeek.TUESDAY));
		assertEquals(DayOfWeekEnum.QUARTA, DayOfWeekEnum.getByDayOfWeek(DayOfWeek.WEDNESDAY));
		assertEquals(DayOfWeekEnum.QUINTA, DayOfWeekEnum.getByDayOfWeek(DayOfWeek.THURSDAY));
		assertEquals(DayOfWeekEnum.SEXTA, DayOfWeekEnum.getByDayOfWeek(DayOfWeek.FRIDAY));
		assertEquals(DayOfWeekEnum.SABADO, DayOfWeekEnum.getByDayOfWeek(DayOfWeek.SATURDAY));
		assertEquals(DayOfWeekEnum.DOMINGO, DayOfWeekEnum.getByDayOfWeek(DayOfWeek.SUNDAY));
	}
	
	@Test
	void sholdDayWeekEnumListBuilderException() {
		var ex = assertThrows(ListBuilderException.class, () -> DayOfWeekEnum.getByDayOfWeek(null));
		assertEquals("Nenhum Dia da Semana encontrado para o valor: null", ex.getMessage());
	}

}
