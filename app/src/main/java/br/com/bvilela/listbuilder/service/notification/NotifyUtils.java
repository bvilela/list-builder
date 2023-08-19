package br.com.bvilela.listbuilder.service.notification;

import br.com.bvilela.lib.enuns.ColorEnum;
import br.com.bvilela.lib.model.CalendarEvent;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.util.DateUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotifyUtils {

    public static boolean containsName(List<String> list, String nameNotify) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        return list.stream().anyMatch(e -> e.toLowerCase().contains(nameNotify.toLowerCase()));
    }

    public static boolean containsName(String item, String nameNotify) {
        if (item == null) {
            return false;
        }
        return item.toLowerCase().contains(nameNotify.toLowerCase());
    }

    public static CalendarEvent createDoNextListEvent(
            ListTypeEnum executionModeEnum, LocalDate lastDateList) {
        var dateEvent = lastDateList.minusDays(7);
        var listName = StringUtils.capitalize(executionModeEnum.toString());
        return CalendarEvent.builder()
                .setSummary(listName.concat(" Fazer Lista"))
                .setDescription(
                        String.format(
                                "Fazer a próxima lista de %s. Atual termina em: %s",
                                listName, DateUtils.format(lastDateList)))
                .setDateTimeStart(LocalDateTime.of(dateEvent, LocalTime.of(18, 0, 0)))
                .setDateTimeEnd(LocalDateTime.of(dateEvent, LocalTime.of(18, 30, 0)))
                .setColor(ColorEnum.GRAFITE)
                .build();
    }
}
