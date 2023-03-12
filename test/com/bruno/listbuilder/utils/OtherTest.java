package com.bruno.listbuilder.utils;

import com.bruno.listbuilder.enuns.DayOfWeekEnum;
import com.bruno.listbuilder.exception.ListBuilderException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

class OtherTest {
	
	@Test
	void sholdDayWeekEnumValid() throws ListBuilderException {
		Assertions.assertEquals(DayOfWeekEnum.SEGUNDA, DayOfWeekEnum.getByDayOfWeek(DayOfWeek.MONDAY));
		Assertions.assertEquals(DayOfWeekEnum.TERCA, DayOfWeekEnum.getByDayOfWeek(DayOfWeek.TUESDAY));
		Assertions.assertEquals(DayOfWeekEnum.QUARTA, DayOfWeekEnum.getByDayOfWeek(DayOfWeek.WEDNESDAY));
		Assertions.assertEquals(DayOfWeekEnum.QUINTA, DayOfWeekEnum.getByDayOfWeek(DayOfWeek.THURSDAY));
		Assertions.assertEquals(DayOfWeekEnum.SEXTA, DayOfWeekEnum.getByDayOfWeek(DayOfWeek.FRIDAY));
		Assertions.assertEquals(DayOfWeekEnum.SABADO, DayOfWeekEnum.getByDayOfWeek(DayOfWeek.SATURDAY));
		Assertions.assertEquals(DayOfWeekEnum.DOMINGO, DayOfWeekEnum.getByDayOfWeek(DayOfWeek.SUNDAY));
	}
	
	@Test
	void sholdDayWeekEnumListBuilderException() {
		var ex = Assertions.assertThrows(ListBuilderException.class, () -> DayOfWeekEnum.getByDayOfWeek(null));
		Assertions.assertEquals("Nenhum Dia da Semana encontrado para o valor: null", ex.getMessage());
	}

}
