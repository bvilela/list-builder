package br.com.bvilela.listbuilder.config;

import java.util.List;

import br.com.bvilela.listbuilder.exception.ListBuilderException;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    @Value("${notify.christianlife.midweek.meeting.day:#{null}}")
    private String notifyChristianlifeMidweekMeetingDay;

    @SneakyThrows
    public void checkNotifyNameFilled() {
        if (this.notifyName == null || this.notifyName.isBlank()) {
            throw new ListBuilderException("Defina a propriedade 'notify.name'!");
        }
    }
}
