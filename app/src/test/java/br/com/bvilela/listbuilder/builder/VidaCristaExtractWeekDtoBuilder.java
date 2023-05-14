package br.com.bvilela.listbuilder.builder;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekItemDTO;
import br.com.bvilela.listbuilder.enuns.VidaCristaExtractItemType;

public class VidaCristaExtractWeekDtoBuilder {

    private VidaCristaExtractWeekDTO target;

    public VidaCristaExtractWeekDtoBuilder() {
        this.target = new VidaCristaExtractWeekDTO();
    }

    public static VidaCristaExtractWeekDtoBuilder create() {
        return new VidaCristaExtractWeekDtoBuilder();
    }

    public VidaCristaExtractWeekDTO build() {
        return target;
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
        this.withDate1(date1);
        this.withDate2(date2);
        this.withItems(
                List.of(
                        VidaCristaExtractWeekItemDtoBuilder.create()
                                .withRandomData(VidaCristaExtractItemType.READ_OF_WEEK)
                                .build(),
                        VidaCristaExtractWeekItemDtoBuilder.create()
                                .withRandomData(VidaCristaExtractItemType.PRESIDENT)
                                .build(),
                        VidaCristaExtractWeekItemDtoBuilder.create()
                                .withRandomData(VidaCristaExtractItemType.LABEL)
                                .withTitle("TESOUROS DA PALAVRA")
                                .build(),
                        VidaCristaExtractWeekItemDtoBuilder.create()
                                .withRandomData(VidaCristaExtractItemType.LABEL)
                                .withTitle("FAÇA SEU MELHOR")
                                .build(),
                        VidaCristaExtractWeekItemDtoBuilder.create()
                                .withRandomData(VidaCristaExtractItemType.LABEL)
                                .withTitle("NOSSA VIDA CRISTÃ")
                                .build(),
                        VidaCristaExtractWeekItemDtoBuilder.create()
                                .withRandomData(VidaCristaExtractItemType.NO_PARTICIPANTS)
                                .build(),
                        VidaCristaExtractWeekItemDtoBuilder.create()
                                .withRandomData(VidaCristaExtractItemType.WITH_PARTICIPANTS)
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

    private VidaCristaExtractWeekDtoBuilder withDate1(LocalDate date) {
        this.target.setDate1(date);
        return this;
    }

    private VidaCristaExtractWeekDtoBuilder withDate2(LocalDate date) {
        this.target.setDate2(date);
        return this;
    }

    private VidaCristaExtractWeekDtoBuilder withItems(List<VidaCristaExtractWeekItemDTO> items) {
        this.target.setItems(items);
        return this;
    }
}
