package br.com.bvilela.listbuilder.builder;

import br.com.bvilela.listbuilder.dto.christianlife.extract.ChristianLifeExtractWeekItemDTO;
import br.com.bvilela.listbuilder.enuns.ChristianLifeExtractItemTypeEnum;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;

public class VidaCristaExtractWeekItemDtoBuilder {

    private ChristianLifeExtractWeekItemDTO target;

    public VidaCristaExtractWeekItemDtoBuilder() {
        this.target = new ChristianLifeExtractWeekItemDTO();
    }

    public static VidaCristaExtractWeekItemDtoBuilder create() {
        return new VidaCristaExtractWeekItemDtoBuilder();
    }

    public ChristianLifeExtractWeekItemDTO build() {
        return target;
    }

    public VidaCristaExtractWeekItemDtoBuilder withRandomData(
            ChristianLifeExtractItemTypeEnum type) {
        this.withTitle(RandomStringUtils.randomAlphabetic(15));
        this.withType(type);
        this.withParticipants(
                List.of(
                        RandomStringUtils.randomAlphabetic(15),
                        RandomStringUtils.randomAlphabetic(15),
                        RandomStringUtils.randomAlphabetic(15),
                        RandomStringUtils.randomAlphabetic(15),
                        RandomStringUtils.randomAlphabetic(15)));
        return this;
    }

    public VidaCristaExtractWeekItemDtoBuilder withTitle(String title) {
        this.target.setTitle(title);
        return this;
    }

    private VidaCristaExtractWeekItemDtoBuilder withType(ChristianLifeExtractItemTypeEnum type) {
        this.target.setType(type);
        return this;
    }

    private VidaCristaExtractWeekItemDtoBuilder withParticipants(List<String> participants) {
        this.target.setParticipants(participants);
        return this;
    }
}
