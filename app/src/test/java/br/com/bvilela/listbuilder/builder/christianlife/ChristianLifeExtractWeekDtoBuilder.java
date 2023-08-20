package br.com.bvilela.listbuilder.builder.christianlife;

import br.com.bvilela.listbuilder.dto.christianlife.extract.ChristianLifeExtractWeekDTO;
import br.com.bvilela.listbuilder.dto.christianlife.extract.ChristianLifeExtractWeekItemDTO;
import br.com.bvilela.listbuilder.enuns.ChristianLifeExtractItemTypeEnum;
import java.time.LocalDate;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;

public class ChristianLifeExtractWeekDtoBuilder {

    private ChristianLifeExtractWeekDTO target;

    public ChristianLifeExtractWeekDtoBuilder() {
        this.target = new ChristianLifeExtractWeekDTO();
    }

    public static ChristianLifeExtractWeekDtoBuilder create() {
        return new ChristianLifeExtractWeekDtoBuilder();
    }

    public ChristianLifeExtractWeekDTO build() {
        return target;
    }

    public ChristianLifeExtractWeekDtoBuilder withRandomData() {
        return base(null, null);
    }

    public ChristianLifeExtractWeekDtoBuilder withRandomDataOneMonth() {
        return base(LocalDate.of(2022, 6, 6), LocalDate.of(2022, 6, 12));
    }

    public ChristianLifeExtractWeekDtoBuilder withRandomDataTwoMonths() {
        return base(LocalDate.of(2022, 6, 27), LocalDate.of(2022, 7, 3));
    }

    private ChristianLifeExtractWeekDtoBuilder base(LocalDate date1, LocalDate date2) {
        this.withLink(RandomStringUtils.randomAlphabetic(30));
        this.withLabelDate(RandomStringUtils.randomAlphabetic(15));
        this.withInitialDate(date1);
        this.withEndDate(date2);
        this.withItems(
                List.of(
                        ChristianLifeExtractWeekItemDtoBuilder.create()
                                .withRandomData(ChristianLifeExtractItemTypeEnum.READ_OF_WEEK)
                                .build(),
                        ChristianLifeExtractWeekItemDtoBuilder.create()
                                .withRandomData(ChristianLifeExtractItemTypeEnum.PRESIDENT)
                                .build(),
                        ChristianLifeExtractWeekItemDtoBuilder.create()
                                .withRandomData(ChristianLifeExtractItemTypeEnum.LABEL)
                                .withTitle("TESOUROS DA PALAVRA")
                                .build(),
                        ChristianLifeExtractWeekItemDtoBuilder.create()
                                .withRandomData(ChristianLifeExtractItemTypeEnum.LABEL)
                                .withTitle("FAÇA SEU MELHOR")
                                .build(),
                        ChristianLifeExtractWeekItemDtoBuilder.create()
                                .withRandomData(ChristianLifeExtractItemTypeEnum.LABEL)
                                .withTitle("NOSSA VIDA CRISTÃ")
                                .build(),
                        ChristianLifeExtractWeekItemDtoBuilder.create()
                                .withRandomData(ChristianLifeExtractItemTypeEnum.NO_PARTICIPANTS)
                                .build(),
                        ChristianLifeExtractWeekItemDtoBuilder.create()
                                .withRandomData(ChristianLifeExtractItemTypeEnum.WITH_PARTICIPANTS)
                                .build()));
        return this;
    }

    private ChristianLifeExtractWeekDtoBuilder withLink(String link) {
        this.target.setLink(link);
        return this;
    }

    private ChristianLifeExtractWeekDtoBuilder withLabelDate(String labelDate) {
        this.target.setLabelDate(labelDate);
        return this;
    }

    public ChristianLifeExtractWeekDtoBuilder withInitialDate(LocalDate date) {
        this.target.setInitialDate(date);
        return this;
    }

    public ChristianLifeExtractWeekDtoBuilder withEndDate(LocalDate date) {
        this.target.setEndDate(date);
        return this;
    }

    private ChristianLifeExtractWeekDtoBuilder withItems(List<ChristianLifeExtractWeekItemDTO> items) {
        this.target.setItems(items);
        return this;
    }
}
