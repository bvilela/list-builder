package br.com.bvilela.listbuilder.builder.discourse;

import br.com.bvilela.listbuilder.dto.discourse.input.DiscourseInputItemDTO;
import br.com.bvilela.listbuilder.util.DateUtils;
import java.time.LocalDate;
import org.apache.commons.lang3.RandomStringUtils;

public class DiscourseInputItemDtoBuilder {

    private DiscourseInputItemDTO target;

    public DiscourseInputItemDtoBuilder() {
        this.target = new DiscourseInputItemDTO();
    }

    public static DiscourseInputItemDtoBuilder create() {
        return new DiscourseInputItemDtoBuilder();
    }

    public DiscourseInputItemDTO build() {
        return target;
    }

    public DiscourseInputItemDtoBuilder withRandomData() {
        this.withDate("05-06-2022");
        this.withThemeNumber(RandomStringUtils.randomNumeric(3));
        this.withThemeTitle(RandomStringUtils.randomAlphanumeric(30));
        this.withSpeaker(RandomStringUtils.randomAlphanumeric(30));
        this.withCongregation(RandomStringUtils.randomAlphanumeric(30));
        this.withDateConverted(DateUtils.parse(this.target.getDate()));
        return this;
    }

    private DiscourseInputItemDtoBuilder withDate(String date) {
        this.target.setDate(date);
        return this;
    }

    private DiscourseInputItemDtoBuilder withDateConverted(LocalDate dateConverted) {
        this.target.setDateConverted(dateConverted);
        return this;
    }

    private DiscourseInputItemDtoBuilder withThemeNumber(String themeNumber) {
        this.target.setThemeNumber(themeNumber);
        return this;
    }

    private DiscourseInputItemDtoBuilder withThemeTitle(String themeTitle) {
        this.target.setThemeTitle(themeTitle);
        return this;
    }

    private DiscourseInputItemDtoBuilder withSpeaker(String speaker) {
        this.target.setSpeaker(speaker);
        return this;
    }

    private DiscourseInputItemDtoBuilder withCongregation(String congregation) {
        this.target.setCongregation(congregation);
        return this;
    }
}
