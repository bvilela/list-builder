package br.com.bvilela.listbuilder.enuns;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotifyDesignationEntityEnum {
    READER("Leitor"),
    AUDIO_VIDEO("AudioVideo"),
    PRESIDENT("Presidente");

    @Getter private final String label;
}
