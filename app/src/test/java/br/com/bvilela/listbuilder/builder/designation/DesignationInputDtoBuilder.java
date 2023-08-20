package br.com.bvilela.listbuilder.builder.designation;

import br.com.bvilela.listbuilder.dto.designation.input.DesignationInputDTO;
import br.com.bvilela.listbuilder.dto.designation.input.DesignationInputReaderDTO;
import br.com.bvilela.listbuilder.dto.util.InputListDTO;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;

public class DesignationInputDtoBuilder {

    private DesignationInputDTO target;

    public DesignationInputDtoBuilder() {
        this.target = new DesignationInputDTO();
    }

    public static DesignationInputDtoBuilder create() {
        return new DesignationInputDtoBuilder();
    }

    public DesignationInputDTO build() {
        return target;
    }

    public DesignationInputDtoBuilder withRandomData() {
        this.withLastDate("01-01-2022");
        this.withMeetingDayMidweek("terça");
        this.withMeetingDayWeekend("sabado");
        this.withPresident(InputListDtoBuilder.create().withRandomData().build());
        this.withAudioVideo(
                InputListDtoBuilder.create().withRandomData().build());
        this.withReader(DesignationInputReaderDtoBuilder.create().withRandomData().build());
        this.withMicrophone(
                List.of(
                        RandomStringUtils.randomAlphabetic(10),
                        RandomStringUtils.randomAlphabetic(10),
                        RandomStringUtils.randomAlphabetic(10),
                        RandomStringUtils.randomAlphabetic(10)));
        this.withIndicator(
                List.of(
                        RandomStringUtils.randomAlphabetic(10),
                        RandomStringUtils.randomAlphabetic(10),
                        RandomStringUtils.randomAlphabetic(10),
                        RandomStringUtils.randomAlphabetic(10)));
        return this;
    }

    public DesignationInputDtoBuilder withLastDate(String lastDate) {
        this.target.setLastDate(lastDate);
        return this;
    }

    public DesignationInputDtoBuilder withMeetingDayMidweek(String meetingDayMidweek) {
        this.target.setMeetingDayMidweek(meetingDayMidweek);
        return this;
    }

    public DesignationInputDtoBuilder withMeetingDayWeekend(String meetingDayWeekend) {
        this.target.setMeetingDayWeekend(meetingDayWeekend);
        return this;
    }

    public DesignationInputDtoBuilder withPresident(InputListDTO president) {
        this.target.setPresident(president);
        return this;
    }

    public DesignationInputDtoBuilder withReader(DesignationInputReaderDTO reader) {
        this.target.setReader(reader);
        return this;
    }

    public DesignationInputDtoBuilder withAudioVideo(InputListDTO audioVideo) {
        this.target.setAudioVideo(audioVideo);
        return this;
    }

    public DesignationInputDtoBuilder withMicrophone(List<String> microphone) {
        this.target.setMicrophone(microphone);
        return this;
    }

    public DesignationInputDtoBuilder withIndicator(List<String> indicator) {
        this.target.setIndicator(indicator);
        return this;
    }
}
