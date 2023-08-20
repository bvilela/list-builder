package br.com.bvilela.listbuilder.builder;

import br.com.bvilela.listbuilder.dto.christianlife.input.ChristianLifeInputDTO;
import br.com.bvilela.listbuilder.dto.christianlife.input.ChristianLifeInputRenameItemDTO;
import br.com.bvilela.listbuilder.util.DateUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;

public class FileInputDataVidaCristaDtoBuilder {

    private ChristianLifeInputDTO target;

    public FileInputDataVidaCristaDtoBuilder() {
        this.target = new ChristianLifeInputDTO();
    }

    public static FileInputDataVidaCristaDtoBuilder create() {
        return new FileInputDataVidaCristaDtoBuilder();
    }

    public ChristianLifeInputDTO build() {
        return target;
    }

    public FileInputDataVidaCristaDtoBuilder withRandomData() {
        this.withAbbreviations(
                Map.of(
                        RandomStringUtils.randomAlphabetic(2),
                        RandomStringUtils.randomAlphabetic(15),
                        RandomStringUtils.randomAlphabetic(2),
                        RandomStringUtils.randomAlphabetic(15),
                        RandomStringUtils.randomAlphabetic(2),
                        RandomStringUtils.randomAlphabetic(15)));
        this.withLastDate(DateUtils.formatDDMMyyyy(LocalDate.now()));
        this.withParticipants(
                List.of(
                        List.of(RandomStringUtils.randomAlphabetic(2)),
                        List.of(
                                RandomStringUtils.randomAlphabetic(2),
                                RandomStringUtils.randomAlphabetic(2)),
                        List.of(RandomStringUtils.randomAlphabetic(2)),
                        List.of(
                                RandomStringUtils.randomAlphabetic(2),
                                RandomStringUtils.randomAlphabetic(2))));
        return this;
    }

    private FileInputDataVidaCristaDtoBuilder withAbbreviations(Map<String, String> abbreviations) {
        this.target.setAbbreviations(abbreviations);
        return this;
    }

    public FileInputDataVidaCristaDtoBuilder withLastDate(String lastDate) {
        this.target.setLastDate(lastDate);
        return this;
    }

    public FileInputDataVidaCristaDtoBuilder withParticipants(List<List<String>> participants) {
        this.target.setParticipants(participants);
        return this;
    }

    public FileInputDataVidaCristaDtoBuilder withRemoveWeekFromList(
            Map<String, String> removeFromList) {
        this.target.setRemoveWeekFromList(removeFromList);
        return this;
    }

    public FileInputDataVidaCristaDtoBuilder withRenameItems(
            List<ChristianLifeInputRenameItemDTO> renameItems) {
        this.target.setRenameItems(renameItems);
        return this;
    }
}
