package br.com.bvilela.listbuilder.config;

import br.com.bvilela.listbuilder.enuns.DayOfWeekEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import java.util.List;

import br.com.bvilela.listbuilder.util.AppUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Getter
@Configuration
public class NotifyProperties {

    @Value("${notifications.active:false}")
    private boolean active;

    @Value("${notifications.name:#{null}}")
    private String name;

    @Value("${notifications.designation.type-active:#{null}}")
    private List<String> designationTypeActive;

    @Value("${notifications.cleaning.premeeting:false}")
    private boolean cleaningPreMeeting;

    @Value("${notifications.christianlife.meeting-day:#{null}}")
    private String christianLifeMeetingDay;

    public boolean notifyInactive() {
        return !this.active;
    }

    @SneakyThrows
    public void checkNotifyNameFilled() {
        if (AppUtils.valueIsNullOrBlank(this.name)) {
            throw new ListBuilderException("Defina a propriedade 'notifications.name'!");
        }
    }

    @SneakyThrows
    public DayOfWeekEnum getChristianLifeMeetingDayEnum() {
        if (AppUtils.valueIsNullOrBlank(this.christianLifeMeetingDay)) {
            throw new ListBuilderException(
                    "Defina a propriedade 'notifications.christianlife.meeting.day'!");
        }

        var meetingDay = AppUtils.removeAccents(this.christianLifeMeetingDay);
        DayOfWeekEnum meetingDayEnum;
        try {
            meetingDayEnum = DayOfWeekEnum.valueOf(meetingDay.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ListBuilderException(
                    "Propriedade 'notifications.christianlife.midweek.meeting.day' não é um Dia da Semana Válido!");
        }

        return meetingDayEnum;
    }
}