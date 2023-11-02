package br.com.bvilela.listbuilder.config;

import br.com.bvilela.listbuilder.enuns.DayOfWeekEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.util.AppUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Configuration
public class NotifyProperties {

    @Value("${notify.active:false}")
    private boolean active;

    @Value("${notify.name:#{null}}")
    private String name;

    @Value("${notify.designation.active-type:#{null}}")
    private List<String> designationTypeActive;

    @Value("${notify.cleaning.premeeting:false}")
    private boolean cleaningPreMeeting;

    @Value("${notify.christianlife.meeting-day:#{null}}")
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