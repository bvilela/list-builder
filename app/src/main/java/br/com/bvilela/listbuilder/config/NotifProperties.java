package br.com.bvilela.listbuilder.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Component
public class NotifProperties {

    @Value("${notif.active:false}")
    private boolean notifActive;

    @Value("${notif.name:#{null}}")
    private String notifName;

    @Value("${notif.designation.type.active:#{null}}")
    private List<String> notifDesignationTypeActive;

    @Value("${notif.cleaning.premeeting:false}")
    private boolean notifCleaningPreMeeting;

    @Value("${notif.christianlife.midweek.meeting.day:#{null}}")
    private String notifChristianlifeMidweekMeetingDay;

}
