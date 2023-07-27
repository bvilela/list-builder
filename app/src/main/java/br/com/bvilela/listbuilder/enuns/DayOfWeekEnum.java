package br.com.bvilela.listbuilder.enuns;

import br.com.bvilela.listbuilder.exception.ListBuilderException;
import java.time.DayOfWeek;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Getter
@RequiredArgsConstructor
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

    @SneakyThrows
    public static DayOfWeekEnum getByDayOfWeek(DayOfWeek dayOfWeek) {
        for (DayOfWeekEnum day : DayOfWeekEnum.values()) {
            if (day.getDayOfWeek() == dayOfWeek) {
                return day;
            }
        }
        throw new ListBuilderException(
                "Nenhum Dia da Semana encontrado para o valor: " + dayOfWeek);
    }

    public static DayOfWeekEnum getByValue(String value) {
        return DayOfWeekEnum.valueOf(value.toUpperCase());
    }
}
