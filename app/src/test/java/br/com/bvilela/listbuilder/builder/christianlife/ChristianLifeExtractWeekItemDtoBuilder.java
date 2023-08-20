package br.com.bvilela.listbuilder.builder.christianlife;

import br.com.bvilela.listbuilder.dto.christianlife.extract.ChristianLifeExtractWeekItemDTO;
import br.com.bvilela.listbuilder.enuns.ChristianLifeExtractItemTypeEnum;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;

public class ChristianLifeExtractWeekItemDtoBuilder {

    private ChristianLifeExtractWeekItemDTO target;

    public ChristianLifeExtractWeekItemDtoBuilder() {
        this.target = new ChristianLifeExtractWeekItemDTO();
    }

    public static ChristianLifeExtractWeekItemDtoBuilder create() {
        return new ChristianLifeExtractWeekItemDtoBuilder();
    }

    public ChristianLifeExtractWeekItemDTO build() {
        return target;
    }

    public ChristianLifeExtractWeekItemDtoBuilder withRandomData(
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

    public ChristianLifeExtractWeekItemDtoBuilder withTitle(String title) {
        this.target.setTitle(title);
        return this;
    }

    private ChristianLifeExtractWeekItemDtoBuilder withType(ChristianLifeExtractItemTypeEnum type) {
        this.target.setType(type);
        return this;
    }

    private ChristianLifeExtractWeekItemDtoBuilder withParticipants(List<String> participants) {
        this.target.setParticipants(participants);
        return this;
    }
}
