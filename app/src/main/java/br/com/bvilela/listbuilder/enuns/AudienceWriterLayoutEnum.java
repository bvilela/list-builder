package br.com.bvilela.listbuilder.enuns;

import br.com.bvilela.listbuilder.exception.WriterLayoutInvalidTypeException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Getter
@RequiredArgsConstructor
public enum AudienceWriterLayoutEnum {
    FULL(2),
    COMPACT(3);

    private final int numberOfMonth;

    @SneakyThrows
    public static AudienceWriterLayoutEnum getByLayout(String layout) {
        try {
            return AudienceWriterLayoutEnum.valueOf(layout.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new WriterLayoutInvalidTypeException(
                    Arrays.toString(AudienceWriterLayoutEnum.values()));
        }
    }
}
