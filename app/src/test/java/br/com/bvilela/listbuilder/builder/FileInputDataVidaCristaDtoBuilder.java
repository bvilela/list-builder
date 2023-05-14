package br.com.bvilela.listbuilder.builder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;

import br.com.bvilela.listbuilder.dto.vidacrista.FileInputDataVidaCristaDTO;
import br.com.bvilela.listbuilder.dto.vidacrista.FileInputDataVidaCristaRenameItemDTO;
import br.com.bvilela.listbuilder.utils.DateUtils;

public class FileInputDataVidaCristaDtoBuilder {

    private FileInputDataVidaCristaDTO target;

    public FileInputDataVidaCristaDtoBuilder() {
        this.target = new FileInputDataVidaCristaDTO();
    }

    public static FileInputDataVidaCristaDtoBuilder create() {
        return new FileInputDataVidaCristaDtoBuilder();
    }

    public FileInputDataVidaCristaDTO build() {
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
        this.withLastDate(DateUtils.format(LocalDate.now(), "dd-MM-yyyy"));
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
            List<FileInputDataVidaCristaRenameItemDTO> renameItems) {
        this.target.setRenameItems(renameItems);
        return this;
    }
}
