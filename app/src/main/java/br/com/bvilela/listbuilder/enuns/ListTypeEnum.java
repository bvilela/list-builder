package br.com.bvilela.listbuilder.enuns;

import br.com.bvilela.listbuilder.config.MarginBase;
import br.com.bvilela.listbuilder.config.SizeBase;
import br.com.bvilela.listbuilder.config.SizeConfig;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Getter
@Slf4j
public enum ListTypeEnum {
    LIMPEZA,
    DESIGNACAO,
    ASSISTENCIA(SizeConfig.AUDIENCE_MARGIN, SizeConfig.AUDIENCE_HEADER),
    DISCURSO,
    VIDA_CRISTA(
            SizeConfig.CHRISTIAN_LIFE_MARGIN,
            SizeConfig.CHRISTIAN_LIFE_HEADER,
            SizeConfig.CHRISTIAN_LIFE_SUBHEADER);

    private final MarginBase pageMg;
    private final SizeBase header;
    private SizeBase subHeader;

    ListTypeEnum() {
        this.pageMg = SizeConfig.DEFAULT_MARGIN;
        this.header = SizeConfig.DEFAULT_HEADER;
        this.subHeader = SizeConfig.DEFAULT_SUBHEADER;
    }

    ListTypeEnum(MarginBase pageMg, SizeBase... header) {
        this.pageMg = pageMg;
        this.header = header[0];
        if (header.length > 1) {
            this.subHeader = header[1];
        }
    }

    public static ListTypeEnum getByName(String name) {
        return ListTypeEnum.valueOf(name.toUpperCase());
    }

    public static String valuesToString() {
        return Arrays.toString(ListTypeEnum.values());
    }

    public String getInputFileName() {
        return String.format("dados-%s.json", this.toString().toLowerCase()).replace("_", "-");
    }
}
