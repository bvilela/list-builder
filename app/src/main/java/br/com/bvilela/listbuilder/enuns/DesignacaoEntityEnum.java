package br.com.bvilela.listbuilder.enuns;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DesignacaoEntityEnum {
    PRESIDENT("Presidente"),
    READER_WATCHTOWER("Leitor de A Sentinela"),
    READER_BIBLESTUDY("Leitor do Estudo Bíblico"),
    AUDIO_VIDEO("Aúdio e Vídeo"),
    INDICATOR("Indicador"),
    MICROPHONE("Microfone");

    private final String label;
}
