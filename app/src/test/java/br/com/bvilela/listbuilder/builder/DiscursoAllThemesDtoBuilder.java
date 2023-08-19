package br.com.bvilela.listbuilder.builder;

import br.com.bvilela.listbuilder.dto.discourse.input.InputAllThemesDiscourseDTO;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;

public class DiscursoAllThemesDtoBuilder {

    private InputAllThemesDiscourseDTO target;

    public DiscursoAllThemesDtoBuilder() {
        this.target = new InputAllThemesDiscourseDTO();
    }

    public static DiscursoAllThemesDtoBuilder create() {
        return new DiscursoAllThemesDtoBuilder();
    }

    public InputAllThemesDiscourseDTO build() {
        return target;
    }

    public DiscursoAllThemesDtoBuilder withNullData() {
        this.withThemes(null);
        return this;
    }

    public DiscursoAllThemesDtoBuilder withEmptyData() {
        this.withThemes(Map.of());
        return this;
    }

    public DiscursoAllThemesDtoBuilder withRandomData() {
        this.withThemes(
                Map.of(
                        1, RandomStringUtils.randomAlphabetic(30),
                        2, RandomStringUtils.randomAlphabetic(30),
                        3, RandomStringUtils.randomAlphabetic(30),
                        4, RandomStringUtils.randomAlphabetic(30),
                        5, RandomStringUtils.randomAlphabetic(30),
                        6, RandomStringUtils.randomAlphabetic(30),
                        7, RandomStringUtils.randomAlphabetic(30),
                        8, RandomStringUtils.randomAlphabetic(30),
                        9, RandomStringUtils.randomAlphabetic(30),
                        10, RandomStringUtils.randomAlphabetic(30)));
        return this;
    }

    private DiscursoAllThemesDtoBuilder withThemes(Map<Integer, String> themes) {
        this.target.setThemes(themes);
        return this;
    }
}
