package br.com.bvilela.listbuilder.builder;

import br.com.bvilela.listbuilder.dto.assistencia.FileInputDataAssistenciaDTO;

public class FileInputDataAssistenciaDtoBuilder {

    private static final String LAST_DATE_DEFAULT = "29-03-2022";

    private FileInputDataAssistenciaDTO target;

    public FileInputDataAssistenciaDtoBuilder() {
        this.target = new FileInputDataAssistenciaDTO();
    }

    public static FileInputDataAssistenciaDtoBuilder create() {
        return new FileInputDataAssistenciaDtoBuilder();
    }

    public FileInputDataAssistenciaDTO build() {
        return target;
    }

    public FileInputDataAssistenciaDtoBuilder withLastDateInvalid() {
        return baseLastDate("01-13-2022");
    }

    public FileInputDataAssistenciaDtoBuilder withMidweekInvalid() {
        return baseMidweekDay("tercaaa");
    }

    public FileInputDataAssistenciaDtoBuilder withWeekendInvalid() {
        return baseWeekendDay("sabadooo");
    }

    public FileInputDataAssistenciaDtoBuilder withSuccess() {
        return base(LAST_DATE_DEFAULT, "terça", "sábado");
    }

    private FileInputDataAssistenciaDtoBuilder baseMidweekDay(String midweekDay) {
        return base(LAST_DATE_DEFAULT, midweekDay, "sábado");
    }

    private FileInputDataAssistenciaDtoBuilder baseWeekendDay(String weekendDay) {
        return base(LAST_DATE_DEFAULT, "terça", weekendDay);
    }

    private FileInputDataAssistenciaDtoBuilder baseLastDate(String lastDate) {
        return base(lastDate, "terça", "sábado");
    }

    private FileInputDataAssistenciaDtoBuilder base(
            String lastDate, String dayMidweek, String dayWeekend) {
        this.withLastDate(lastDate);
        this.withMeetingDayMidweek(dayMidweek);
        this.withMeetingDayWeekend(dayWeekend);
        return this;
    }

    public FileInputDataAssistenciaDtoBuilder withLastDate(String lastDate) {
        this.target.setLastDate(lastDate);
        return this;
    }

    public FileInputDataAssistenciaDtoBuilder withMeetingDayMidweek(String meetingDayMidweek) {
        this.target.setMeetingDayMidweek(meetingDayMidweek);
        return this;
    }

    public FileInputDataAssistenciaDtoBuilder withMeetingDayWeekend(String meetingDayWeekend) {
        this.target.setMeetingDayWeekend(meetingDayWeekend);
        return this;
    }
}
