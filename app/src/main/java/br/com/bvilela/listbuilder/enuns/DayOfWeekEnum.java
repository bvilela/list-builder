package br.com.bvilela.listbuilder.enuns;

import br.com.bvilela.listbuilder.exception.ListBuilderException;
import java.time.DayOfWeek;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public enum DayOfWeekEnum {
    SEGUNDA("Segunda", DayOfWeek.MONDAY),
    TERCA("Terça", DayOfWeek.TUESDAY),
    QUARTA("Quarta", DayOfWeek.WEDNESDAY),
    QUINTA("Quinta", DayOfWeek.THURSDAY),
    SEXTA("Sexta", DayOfWeek.FRIDAY),
    SABADO("Sábado", DayOfWeek.SATURDAY),
    DOMINGO("Domingo", DayOfWeek.SUNDAY);

    private final String name;
    private final DayOfWeek dayOfWeek;

    DayOfWeekEnum(String name, DayOfWeek dayOfWeek) {
        this.name = name;
        this.dayOfWeek = dayOfWeek;
    }

    @SneakyThrows
    public static DayOfWeekEnum getByDayOfWeek(DayOfWeek dayOfWeek) {
        for (DayOfWeekEnum dia : DayOfWeekEnum.values()) {
            if (dia.getDayOfWeek() == dayOfWeek) {
                return dia;
            }
        }
        throw new ListBuilderException(
                "Nenhum Dia da Semana encontrado para o valor: " + dayOfWeek);
    }
}
