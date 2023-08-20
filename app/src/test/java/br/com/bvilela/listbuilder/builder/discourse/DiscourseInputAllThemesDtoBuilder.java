package br.com.bvilela.listbuilder.builder.discourse;

import br.com.bvilela.listbuilder.dto.discourse.input.DiscourseInputAllThemesDTO;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;

public class DiscourseInputAllThemesDtoBuilder {

    private DiscourseInputAllThemesDTO target;

    public DiscourseInputAllThemesDtoBuilder() {
        this.target = new DiscourseInputAllThemesDTO();
    }

    public static DiscourseInputAllThemesDtoBuilder create() {
        return new DiscourseInputAllThemesDtoBuilder();
    }

    public DiscourseInputAllThemesDTO build() {
        return target;
    }

    public DiscourseInputAllThemesDtoBuilder withNullData() {
        this.withThemes(null);
        return this;
    }

    public DiscourseInputAllThemesDtoBuilder withEmptyData() {
        this.withThemes(Map.of());
        return this;
    }

    public DiscourseInputAllThemesDtoBuilder withRandomData() {
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

    private DiscourseInputAllThemesDtoBuilder withThemes(Map<Integer, String> themes) {
        this.target.setThemes(themes);
        return this;
    }
}
