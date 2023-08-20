package br.com.bvilela.listbuilder.builder.designacao;

import br.com.bvilela.listbuilder.dto.designation.writer.DesignationWriterDTO;
import br.com.bvilela.listbuilder.dto.designation.writer.DesignationWriterItemDTO;
import java.util.ArrayList;
import java.util.List;

public class DesignacaoWriterDtoBuilder {

    private DesignationWriterDTO target;

    public DesignacaoWriterDtoBuilder() {
        this.target = new DesignationWriterDTO();
    }

    public static DesignacaoWriterDtoBuilder create() {
        return new DesignacaoWriterDtoBuilder();
    }

    public DesignationWriterDTO build() {
        return target;
    }

    private List<DesignationWriterItemDTO> generatedRandomList(int size) {
        List<DesignationWriterItemDTO> list = new ArrayList<DesignationWriterItemDTO>();
        for (int i = 0; i < size; i++) {
            list.add(DesignacaoWriterItemDtoBuilder.create().withRandomData().build());
        }
        return list;
    }

    private List<DesignationWriterItemDTO> generatedRandomTwoPeopleList(int size) {
        List<DesignationWriterItemDTO> list = new ArrayList<DesignationWriterItemDTO>();
        for (int i = 0; i < size; i++) {
            list.add(DesignacaoWriterItemDtoBuilder.create().withRandomTwoPeopleData().build());
        }
        return list;
    }

    public DesignacaoWriterDtoBuilder withRandomData() {
        this.withPresident(generatedRandomList(3));
        this.withReaderWatchtower(generatedRandomList(3));
        this.withReaderBibleStudy(generatedRandomList(3));
        this.withAudioVideo(generatedRandomTwoPeopleList(3));
        this.withIndicator(generatedRandomTwoPeopleList(3));
        this.withMicrophone(generatedRandomTwoPeopleList(3));
        return this;
    }

    public DesignacaoWriterDtoBuilder withPresident(List<DesignationWriterItemDTO> president) {
        this.target.setPresident(president);
        return this;
    }

    public DesignacaoWriterDtoBuilder withReaderWatchtower(
            List<DesignationWriterItemDTO> readerWatchtower) {
        this.target.setReaderWatchtower(readerWatchtower);
        return this;
    }

    public DesignacaoWriterDtoBuilder withReaderBibleStudy(
            List<DesignationWriterItemDTO> readerBibleStudy) {
        this.target.setReaderBibleStudy(readerBibleStudy);
        return this;
    }

    public DesignacaoWriterDtoBuilder withAudioVideo(List<DesignationWriterItemDTO> audioVideo) {
        this.target.setAudioVideo(audioVideo);
        return this;
    }

    public DesignacaoWriterDtoBuilder withIndicator(List<DesignationWriterItemDTO> indicator) {
        this.target.setIndicator(indicator);
        return this;
    }

    public DesignacaoWriterDtoBuilder withMicrophone(List<DesignationWriterItemDTO> microphone) {
        this.target.setMicrophone(microphone);
        return this;
    }
}
