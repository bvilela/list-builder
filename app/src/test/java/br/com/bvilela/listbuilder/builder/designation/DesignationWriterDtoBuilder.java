package br.com.bvilela.listbuilder.builder.designation;

import br.com.bvilela.listbuilder.dto.designation.writer.DesignationWriterDTO;
import br.com.bvilela.listbuilder.dto.designation.writer.DesignationWriterItemDTO;
import java.util.ArrayList;
import java.util.List;

public class DesignationWriterDtoBuilder {

    private DesignationWriterDTO target;

    public DesignationWriterDtoBuilder() {
        this.target = new DesignationWriterDTO();
    }

    public static DesignationWriterDtoBuilder create() {
        return new DesignationWriterDtoBuilder();
    }

    public DesignationWriterDTO build() {
        return target;
    }

    private List<DesignationWriterItemDTO> generatedRandomList(int size) {
        List<DesignationWriterItemDTO> list = new ArrayList<DesignationWriterItemDTO>();
        for (int i = 0; i < size; i++) {
            list.add(DesignationWriterItemDtoBuilder.create().withRandomData().build());
        }
        return list;
    }

    private List<DesignationWriterItemDTO> generatedRandomTwoPeopleList(int size) {
        List<DesignationWriterItemDTO> list = new ArrayList<DesignationWriterItemDTO>();
        for (int i = 0; i < size; i++) {
            list.add(DesignationWriterItemDtoBuilder.create().withRandomTwoPeopleData().build());
        }
        return list;
    }

    public DesignationWriterDtoBuilder withRandomData() {
        this.withPresident(generatedRandomList(3));
        this.withReaderWatchtower(generatedRandomList(3));
        this.withReaderBibleStudy(generatedRandomList(3));
        this.withAudioVideo(generatedRandomTwoPeopleList(3));
        this.withIndicator(generatedRandomTwoPeopleList(3));
        this.withMicrophone(generatedRandomTwoPeopleList(3));
        return this;
    }

    public DesignationWriterDtoBuilder withPresident(List<DesignationWriterItemDTO> president) {
        this.target.setPresident(president);
        return this;
    }

    public DesignationWriterDtoBuilder withReaderWatchtower(
            List<DesignationWriterItemDTO> readerWatchtower) {
        this.target.setReaderWatchtower(readerWatchtower);
        return this;
    }

    public DesignationWriterDtoBuilder withReaderBibleStudy(
            List<DesignationWriterItemDTO> readerBibleStudy) {
        this.target.setReaderBibleStudy(readerBibleStudy);
        return this;
    }

    public DesignationWriterDtoBuilder withAudioVideo(List<DesignationWriterItemDTO> audioVideo) {
        this.target.setAudioVideo(audioVideo);
        return this;
    }

    public DesignationWriterDtoBuilder withIndicator(List<DesignationWriterItemDTO> indicator) {
        this.target.setIndicator(indicator);
        return this;
    }

    public DesignationWriterDtoBuilder withMicrophone(List<DesignationWriterItemDTO> microphone) {
        this.target.setMicrophone(microphone);
        return this;
    }
}
