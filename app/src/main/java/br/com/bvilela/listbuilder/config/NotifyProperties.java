package br.com.bvilela.listbuilder.config;

import br.com.bvilela.listbuilder.enuns.DayOfWeekEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.utils.AppUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Component
public class NotifyProperties {

    @Value("${notify.active:false}")
    private boolean notifyActive;

    @Value("${notify.name:#{null}}")
    private String notifyName;

    @Value("${notify.designation.type.active}")
    private List<String> notifyDesignationTypeActive;

    @Value("${notify.cleaning.premeeting:false}")
    private boolean notifyCleaningPreMeeting;

    @Value("${notify.christianlife.meeting.day:#{null}}")
    private String notifyChristianLifeMeetingDay;

    public boolean notifyInactive() {
        return !this.notifyActive;
    }

    @SneakyThrows
    public void checkNotifyNameFilled() {
        if (this.notifyName == null || this.notifyName.isBlank()) {
            throw new ListBuilderException("Defina a propriedade 'notify.name'!");
        }
    }

    @SneakyThrows
    public DayOfWeekEnum getChristianLifeMeetingDayEnum() {
        if (this.notifyChristianLifeMeetingDay == null
                || this.notifyChristianLifeMeetingDay.isBlank()) {
            throw new ListBuilderException(
                    "Defina a propriedade 'notify.christianlife.meeting.day'!");
        }

        var meetingDay = AppUtils.removeAccents(this.notifyChristianLifeMeetingDay);
        DayOfWeekEnum meetingDayEnum;
        try {
            meetingDayEnum = DayOfWeekEnum.valueOf(meetingDay.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ListBuilderException(
                    "Propriedade 'notify.christianlife.midweek.meeting.day' não é um Dia da Semana Válido!");
        }

        return meetingDayEnum;
    }
}
