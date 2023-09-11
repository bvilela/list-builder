package br.com.bvilela.listbuilder.util;

import br.com.bvilela.listbuilder.dto.christianlife.extract.ChristianLifeExtractWeekItemDTO;
import br.com.bvilela.listbuilder.enuns.ChristianLifeExtractItemTypeEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChristianLifeUtils {

    public static boolean isBibleStudy(ChristianLifeExtractWeekItemDTO item) {
        return ChristianLifeExtractItemTypeEnum.BIBLE_STUDY.equals(item.getType());
    }

    public static boolean isBibleStudy(String text) {
        text = AppUtils.removeAccents(text).toLowerCase();
        return text.contains("estudo biblico");
    }
}

