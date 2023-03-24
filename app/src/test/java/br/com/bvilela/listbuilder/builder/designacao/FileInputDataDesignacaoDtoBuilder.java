package br.com.bvilela.listbuilder.builder.designacao;

import java.util.List;

import br.com.bvilela.listbuilder.dto.designacao.FileInputDataDesignacaoDTO;
import br.com.bvilela.listbuilder.dto.designacao.FileInputDataDesignacaoListDTO;
import org.apache.commons.lang3.RandomStringUtils;

import br.com.bvilela.listbuilder.dto.designacao.FileInputDataDesignacaoReaderDTO;

public class FileInputDataDesignacaoDtoBuilder {

	private FileInputDataDesignacaoDTO target;

	public FileInputDataDesignacaoDtoBuilder() {
		this.target = new FileInputDataDesignacaoDTO();
	}

	public static FileInputDataDesignacaoDtoBuilder create() {
		return new FileInputDataDesignacaoDtoBuilder();
	}

	public FileInputDataDesignacaoDTO build() {
		return target;
	}

	public FileInputDataDesignacaoDtoBuilder withRandomData() {
		this.withLastDate("01-01-2022");
		this.withMeetingDayMidweek("terça");
		this.withMeetingDayWeekend("sabado");
		this.withPresident(FileInputDataDesignacaoListDtoBuilder.create().withRandomData().build());
		this.withAudioVideo(FileInputDataDesignacaoListDtoBuilder.create().withRandomData().build());
		this.withReader(FileInputDataDesignacaoReaderDtoBuilder.create().withRandomData().build());
		this.withMicrophone(List.of(RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10),
				RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10)));
		this.withIndicator(List.of(RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10),
				RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10)));
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
    
    public FileInputDataDesignacaoDtoBuilder withPresident(FileInputDataDesignacaoListDTO president) {
        this.target.setPresident(president);
        return this;
    }
    
    public FileInputDataDesignacaoDtoBuilder withReader(FileInputDataDesignacaoReaderDTO reader) {
        this.target.setReader(reader);
        return this;
    }
    
    public FileInputDataDesignacaoDtoBuilder withAudioVideo(FileInputDataDesignacaoListDTO audioVideo) {
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
