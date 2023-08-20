package br.com.bvilela.listbuilder.builder;

import br.com.bvilela.listbuilder.dto.audience.AudienceInputDTO;

public class FileInputDataAudienceDtoBuilder {

    private static final String LAST_DATE_DEFAULT = "29-03-2022";

    private AudienceInputDTO target;

    public FileInputDataAudienceDtoBuilder() {
        this.target = new AudienceInputDTO();
    }

    public static FileInputDataAudienceDtoBuilder create() {
        return new FileInputDataAudienceDtoBuilder();
    }

    public AudienceInputDTO build() {
        return target;
    }

    public FileInputDataAudienceDtoBuilder withLastDateInvalid() {
        return baseLastDate("01-13-2022");
    }

    public FileInputDataAudienceDtoBuilder withMidweekInvalid() {
        return baseMidweekDay("tercaaa");
    }

    public FileInputDataAudienceDtoBuilder withWeekendInvalid() {
        return baseWeekendDay("sabadooo");
    }

    public FileInputDataAudienceDtoBuilder withSuccess() {
        return base(LAST_DATE_DEFAULT, "terça", "sábado");
    }

    private FileInputDataAudienceDtoBuilder baseMidweekDay(String midweekDay) {
        return base(LAST_DATE_DEFAULT, midweekDay, "sábado");
    }

    private FileInputDataAudienceDtoBuilder baseWeekendDay(String weekendDay) {
        return base(LAST_DATE_DEFAULT, "terça", weekendDay);
    }

    private FileInputDataAudienceDtoBuilder baseLastDate(String lastDate) {
        return base(lastDate, "terça", "sábado");
    }

    private FileInputDataAudienceDtoBuilder base(
            String lastDate, String dayMidweek, String dayWeekend) {
        this.withLastDate(lastDate);
        this.withMeetingDayMidweek(dayMidweek);
        this.withMeetingDayWeekend(dayWeekend);
        return this;
    }

    public FileInputDataAudienceDtoBuilder withLastDate(String lastDate) {
        this.target.setLastDate(lastDate);
        return this;
    }

    public FileInputDataAudienceDtoBuilder withMeetingDayMidweek(String meetingDayMidweek) {
        this.target.setMeetingDayMidweek(meetingDayMidweek);
        return this;
    }

    public FileInputDataAudienceDtoBuilder withMeetingDayWeekend(String meetingDayWeekend) {
        this.target.setMeetingDayWeekend(meetingDayWeekend);
        return this;
    }
}
