package br.com.bvilela.listbuilder.builder;

import br.com.bvilela.listbuilder.dto.discourse.input.InputDiscourseItemDTO;
import br.com.bvilela.listbuilder.utils.DateUtils;
import java.time.LocalDate;
import org.apache.commons.lang3.RandomStringUtils;

public class FileInputDataDiscursoItemDtoBuilder {

    private InputDiscourseItemDTO target;

    public FileInputDataDiscursoItemDtoBuilder() {
        this.target = new InputDiscourseItemDTO();
    }

    public static FileInputDataDiscursoItemDtoBuilder create() {
        return new FileInputDataDiscursoItemDtoBuilder();
    }

    public InputDiscourseItemDTO build() {
        return target;
    }

    public FileInputDataDiscursoItemDtoBuilder withRandomData() {
        this.withDate("05-06-2022");
        this.withThemeNumber(RandomStringUtils.randomNumeric(3));
        this.withThemeTitle(RandomStringUtils.randomAlphanumeric(30));
        this.withSpeaker(RandomStringUtils.randomAlphanumeric(30));
        this.withCongregation(RandomStringUtils.randomAlphanumeric(30));
        this.withDateConverted(DateUtils.parse(this.target.getDate()));
        return this;
    }

    private FileInputDataDiscursoItemDtoBuilder withDate(String date) {
        this.target.setDate(date);
        return this;
    }

    private FileInputDataDiscursoItemDtoBuilder withDateConverted(LocalDate dateConverted) {
        this.target.setDateConverted(dateConverted);
        return this;
    }

    private FileInputDataDiscursoItemDtoBuilder withThemeNumber(String themeNumber) {
        this.target.setThemeNumber(themeNumber);
        return this;
    }

    private FileInputDataDiscursoItemDtoBuilder withThemeTitle(String themeTitle) {
        this.target.setThemeTitle(themeTitle);
        return this;
    }

    private FileInputDataDiscursoItemDtoBuilder withSpeaker(String speaker) {
        this.target.setSpeaker(speaker);
        return this;
    }

    private FileInputDataDiscursoItemDtoBuilder withCongregation(String congregation) {
        this.target.setCongregation(congregation);
        return this;
    }
}
