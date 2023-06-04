package br.com.bvilela.listbuilder.config;

import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class NotifyProperties {

    @Value("${notif.active:false}")
    private boolean notifActive;

    @Value("${notif.name:#{null}}")
    private String notifName;

    @Value("${notif.designation.type.active}")
    private List<String> notifDesignationTypeActive;

    @Value("${notif.cleaning.premeeting:false}")
    private boolean notifCleaningPreMeeting;

    @Value("${notif.christianlife.midweek.meeting.day:#{null}}")
    private String notifChristianlifeMidweekMeetingDay;
}
