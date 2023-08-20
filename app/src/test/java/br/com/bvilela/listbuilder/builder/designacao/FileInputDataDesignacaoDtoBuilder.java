package br.com.bvilela.listbuilder.builder.designacao;

import br.com.bvilela.listbuilder.dto.designation.input.DesignationInputDTO;
import br.com.bvilela.listbuilder.dto.designation.input.DesignationInputReaderDTO;
import br.com.bvilela.listbuilder.dto.util.InputListDTO;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;

public class FileInputDataDesignacaoDtoBuilder {

    private DesignationInputDTO target;

    public FileInputDataDesignacaoDtoBuilder() {
        this.target = new DesignationInputDTO();
    }

    public static FileInputDataDesignacaoDtoBuilder create() {
        return new FileInputDataDesignacaoDtoBuilder();
    }

    public DesignationInputDTO build() {
        return target;
    }

    public FileInputDataDesignacaoDtoBuilder withRandomData() {
        this.withLastDate("01-01-2022");
        this.withMeetingDayMidweek("terça");
        this.withMeetingDayWeekend("sabado");
        this.withPresident(FileInputDataDesignacaoListDtoBuilder.create().withRandomData().build());
        this.withAudioVideo(
                FileInputDataDesignacaoListDtoBuilder.create().withRandomData().build());
        this.withReader(FileInputDataDesignacaoReaderDtoBuilder.create().withRandomData().build());
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

    public FileInputDataDesignacaoDtoBuilder withLastDate(String lastDate) {
        this.target.setLastDate(lastDate);
        return this;
    }

    public FileInputDataDesignacaoDtoBuilder withMeetingDayMidweek(String meetingDayMidweek) {
        this.target.setMeetingDayMidweek(meetingDayMidweek);
        return this;
    }

    public FileInputDataDesignacaoDtoBuilder withMeetingDayWeekend(String meetingDayWeekend) {
        this.target.setMeetingDayWeekend(meetingDayWeekend);
        return this;
    }

    public FileInputDataDesignacaoDtoBuilder withPresident(InputListDTO president) {
        this.target.setPresident(president);
        return this;
    }

    public FileInputDataDesignacaoDtoBuilder withReader(DesignationInputReaderDTO reader) {
        this.target.setReader(reader);
        return this;
    }

    public FileInputDataDesignacaoDtoBuilder withAudioVideo(InputListDTO audioVideo) {
        this.target.setAudioVideo(audioVideo);
        return this;
    }

    public FileInputDataDesignacaoDtoBuilder withMicrophone(List<String> microphone) {
        this.target.setMicrophone(microphone);
        return this;
    }

    public FileInputDataDesignacaoDtoBuilder withIndicator(List<String> indicator) {
        this.target.setIndicator(indicator);
        return this;
    }
}
