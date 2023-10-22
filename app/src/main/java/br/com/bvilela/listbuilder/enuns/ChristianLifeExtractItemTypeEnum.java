package br.com.bvilela.listbuilder.enuns;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChristianLifeExtractItemTypeEnum {
    READ_OF_WEEK(false),
    PRESIDENT(true),
    LABEL(false),
    NO_PARTICIPANTS(false),
    WITH_PARTICIPANTS(true),
    BIBLE_STUDY(true),
    BIBLE_STUDY_READER(true);

    private final boolean hasParticipants;
}
