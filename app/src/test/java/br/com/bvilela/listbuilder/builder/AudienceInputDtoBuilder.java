package br.com.bvilela.listbuilder.builder;

import br.com.bvilela.listbuilder.dto.audience.AudienceInputDTO;

public class AudienceInputDtoBuilder {

    private static final String LAST_DATE_DEFAULT = "29-03-2022";

    private AudienceInputDTO target;

    public AudienceInputDtoBuilder() {
        this.target = new AudienceInputDTO();
    }

    public static AudienceInputDtoBuilder create() {
        return new AudienceInputDtoBuilder();
    }

    public AudienceInputDTO build() {
        return target;
    }

    public AudienceInputDtoBuilder withSuccess() {
        return base(LAST_DATE_DEFAULT, "terça", "sábado");
    }

    private AudienceInputDtoBuilder baseWeekendDay(String weekendDay) {
        return base(LAST_DATE_DEFAULT, "terça", weekendDay);
    }

    private AudienceInputDtoBuilder base(
            String lastDate, String dayMidweek, String dayWeekend) {
        this.withLastDate(lastDate);
        this.withMeetingDayMidweek(dayMidweek);
        this.withMeetingDayWeekend(dayWeekend);
        return this;
    }

    public AudienceInputDtoBuilder withLastDate(String lastDate) {
        this.target.setLastDate(lastDate);
        return this;
    }

    public AudienceInputDtoBuilder withMeetingDayMidweek(String meetingDayMidweek) {
        this.target.setMidweekMeetingDay(meetingDayMidweek);
        return this;
    }

    public AudienceInputDtoBuilder withMeetingDayWeekend(String meetingDayWeekend) {
        this.target.setWeekendMeetingDay(meetingDayWeekend);
        return this;
    }
}
