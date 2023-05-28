package br.com.bvilela.listbuilder.enuns;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum VidaCristaExtractItemType {
    READ_OF_WEEK(false),
    PRESIDENT(true),
    LABEL(false),
    NO_PARTICIPANTS(false),
    WITH_PARTICIPANTS(true);

    @Getter private final boolean hasParticipants;

}
