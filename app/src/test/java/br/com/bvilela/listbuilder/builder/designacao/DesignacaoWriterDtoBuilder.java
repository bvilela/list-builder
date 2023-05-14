package br.com.bvilela.listbuilder.builder.designacao;

import java.util.ArrayList;
import java.util.List;

import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;
import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterItemDTO;

public class DesignacaoWriterDtoBuilder {

    private DesignacaoWriterDTO target;

    public DesignacaoWriterDtoBuilder() {
        this.target = new DesignacaoWriterDTO();
    }

    public static DesignacaoWriterDtoBuilder create() {
        return new DesignacaoWriterDtoBuilder();
    }

    public DesignacaoWriterDTO build() {
        return target;
    }

    private List<DesignacaoWriterItemDTO> generatedRandomList(int size) {
        List<DesignacaoWriterItemDTO> list = new ArrayList<DesignacaoWriterItemDTO>();
        for (int i = 0; i < size; i++) {
            list.add(DesignacaoWriterItemDtoBuilder.create().withRandomData().build());
        }
        return list;
    }

    private List<DesignacaoWriterItemDTO> generatedRandomTwoPeopleList(int size) {
        List<DesignacaoWriterItemDTO> list = new ArrayList<DesignacaoWriterItemDTO>();
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

    public DesignacaoWriterDtoBuilder withPresident(List<DesignacaoWriterItemDTO> president) {
        this.target.setPresident(president);
        return this;
    }

    public DesignacaoWriterDtoBuilder withReaderWatchtower(
            List<DesignacaoWriterItemDTO> readerWatchtower) {
        this.target.setReaderWatchtower(readerWatchtower);
        return this;
    }

    public DesignacaoWriterDtoBuilder withReaderBibleStudy(
            List<DesignacaoWriterItemDTO> readerBibleStudy) {
        this.target.setReaderBibleStudy(readerBibleStudy);
        return this;
    }

    public DesignacaoWriterDtoBuilder withAudioVideo(List<DesignacaoWriterItemDTO> audioVideo) {
        this.target.setAudioVideo(audioVideo);
        return this;
    }

    public DesignacaoWriterDtoBuilder withIndicator(List<DesignacaoWriterItemDTO> indicator) {
        this.target.setIndicator(indicator);
        return this;
    }

    public DesignacaoWriterDtoBuilder withMicrophone(List<DesignacaoWriterItemDTO> microphone) {
        this.target.setMicrophone(microphone);
        return this;
    }
}
