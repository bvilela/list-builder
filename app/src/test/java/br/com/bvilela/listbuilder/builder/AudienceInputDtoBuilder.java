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

    public AudienceInputDtoBuilder withLastDateInvalid() {
        return baseLastDate("01-13-2022");
    }

    public AudienceInputDtoBuilder withMidweekInvalid() {
        return baseMidweekDay("tercaaa");
    }

    public AudienceInputDtoBuilder withWeekendInvalid() {
        return baseWeekendDay("sabadooo");
    }

    public AudienceInputDtoBuilder withSuccess() {
        return base(LAST_DATE_DEFAULT, "terça", "sábado");
    }

    private AudienceInputDtoBuilder baseMidweekDay(String midweekDay) {
        return base(LAST_DATE_DEFAULT, midweekDay, "sábado");
    }

    private AudienceInputDtoBuilder baseWeekendDay(String weekendDay) {
        return base(LAST_DATE_DEFAULT, "terça", weekendDay);
    }

    private AudienceInputDtoBuilder baseLastDate(String lastDate) {
        return base(lastDate, "terça", "sábado");
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
        this.target.setMeetingDayMidweek(meetingDayMidweek);
        return this;
    }

    public AudienceInputDtoBuilder withMeetingDayWeekend(String meetingDayWeekend) {
        this.target.setMeetingDayWeekend(meetingDayWeekend);
        return this;
    }
}
