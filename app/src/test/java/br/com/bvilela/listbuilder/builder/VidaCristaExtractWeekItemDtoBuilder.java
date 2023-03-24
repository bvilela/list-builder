package br.com.bvilela.listbuilder.builder;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekItemDTO;
import br.com.bvilela.listbuilder.enuns.VidaCristaExtractItemType;

public class VidaCristaExtractWeekItemDtoBuilder {

	private VidaCristaExtractWeekItemDTO target;

    public VidaCristaExtractWeekItemDtoBuilder() {
        this.target = new VidaCristaExtractWeekItemDTO();
    }

    public static VidaCristaExtractWeekItemDtoBuilder create() {
        return new VidaCristaExtractWeekItemDtoBuilder();
    }

    public VidaCristaExtractWeekItemDTO build() {
        return target;
    }
    
    public VidaCristaExtractWeekItemDtoBuilder withRandomData(VidaCristaExtractItemType type) {
    	this.withTitle(RandomStringUtils.randomAlphabetic(15));
    	this.withType(type);
    	this.withParticipants(List.of(
    			RandomStringUtils.randomAlphabetic(15),
    			RandomStringUtils.randomAlphabetic(15),
    			RandomStringUtils.randomAlphabetic(15),
    			RandomStringUtils.randomAlphabetic(15),
    			RandomStringUtils.randomAlphabetic(15)
    			));
        return this;
    }

    public VidaCristaExtractWeekItemDtoBuilder withTitle(String title) {
        this.target.setTitle(title);
        return this;
    }
    
    private VidaCristaExtractWeekItemDtoBuilder withType(VidaCristaExtractItemType type) {
        this.target.setType(type);
        return this;
    }
    
    private VidaCristaExtractWeekItemDtoBuilder withParticipants(List<String> participants) {
        this.target.setParticipants(participants);
        return this;
    }
	
}
