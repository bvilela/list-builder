package br.com.bvilela.listbuilder.builder;

import br.com.bvilela.listbuilder.dto.christianlife.extract.ChristianLifeExtractWeekDTO;
import br.com.bvilela.listbuilder.dto.christianlife.extract.ChristianLifeExtractWeekItemDTO;
import br.com.bvilela.listbuilder.enuns.ChristianLifeExtractItemTypeEnum;
import java.time.LocalDate;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;

public class VidaCristaExtractWeekDtoBuilder {

    private ChristianLifeExtractWeekDTO target;

    public VidaCristaExtractWeekDtoBuilder() {
        this.target = new ChristianLifeExtractWeekDTO();
    }

    public static VidaCristaExtractWeekDtoBuilder create() {
        return new VidaCristaExtractWeekDtoBuilder();
    }

    public ChristianLifeExtractWeekDTO build() {
        return target;
    }

    public VidaCristaExtractWeekDtoBuilder withRandomData() {
        return base(null, null);
    }

    public VidaCristaExtractWeekDtoBuilder withRandomDataOneMonth() {
        return base(LocalDate.of(2022, 6, 6), LocalDate.of(2022, 6, 12));
    }

    public VidaCristaExtractWeekDtoBuilder withRandomDataTwoMonths() {
        return base(LocalDate.of(2022, 6, 27), LocalDate.of(2022, 7, 3));
    }

    private VidaCristaExtractWeekDtoBuilder base(LocalDate date1, LocalDate date2) {
        this.withLink(RandomStringUtils.randomAlphabetic(30));
        this.withLabelDate(RandomStringUtils.randomAlphabetic(15));
        this.withInitialDate(date1);
        this.withEndDate(date2);
        this.withItems(
                List.of(
                        VidaCristaExtractWeekItemDtoBuilder.create()
                                .withRandomData(ChristianLifeExtractItemTypeEnum.READ_OF_WEEK)
                                .build(),
                        VidaCristaExtractWeekItemDtoBuilder.create()
                                .withRandomData(ChristianLifeExtractItemTypeEnum.PRESIDENT)
                                .build(),
                        VidaCristaExtractWeekItemDtoBuilder.create()
                                .withRandomData(ChristianLifeExtractItemTypeEnum.LABEL)
                                .withTitle("TESOUROS DA PALAVRA")
                                .build(),
                        VidaCristaExtractWeekItemDtoBuilder.create()
                                .withRandomData(ChristianLifeExtractItemTypeEnum.LABEL)
                                .withTitle("FAÇA SEU MELHOR")
                                .build(),
                        VidaCristaExtractWeekItemDtoBuilder.create()
                                .withRandomData(ChristianLifeExtractItemTypeEnum.LABEL)
                                .withTitle("NOSSA VIDA CRISTÃ")
                                .build(),
                        VidaCristaExtractWeekItemDtoBuilder.create()
                                .withRandomData(ChristianLifeExtractItemTypeEnum.NO_PARTICIPANTS)
                                .build(),
                        VidaCristaExtractWeekItemDtoBuilder.create()
                                .withRandomData(ChristianLifeExtractItemTypeEnum.WITH_PARTICIPANTS)
                                .build()));
        return this;
    }

    private VidaCristaExtractWeekDtoBuilder withLink(String link) {
        this.target.setLink(link);
        return this;
    }

    private VidaCristaExtractWeekDtoBuilder withLabelDate(String labelDate) {
        this.target.setLabelDate(labelDate);
        return this;
    }

    public VidaCristaExtractWeekDtoBuilder withInitialDate(LocalDate date) {
        this.target.setInitialDate(date);
        return this;
    }

    public VidaCristaExtractWeekDtoBuilder withEndDate(LocalDate date) {
        this.target.setEndDate(date);
        return this;
    }

    private VidaCristaExtractWeekDtoBuilder withItems(List<ChristianLifeExtractWeekItemDTO> items) {
        this.target.setItems(items);
        return this;
    }
}
