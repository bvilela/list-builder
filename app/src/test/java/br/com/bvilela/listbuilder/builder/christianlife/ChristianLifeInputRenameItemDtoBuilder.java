package br.com.bvilela.listbuilder.builder.christianlife;

import br.com.bvilela.listbuilder.dto.christianlife.input.ChristianLifeInputRenameItemDTO;

public class ChristianLifeInputRenameItemDtoBuilder {

    private ChristianLifeInputRenameItemDTO target;

    public ChristianLifeInputRenameItemDtoBuilder() {
        this.target = new ChristianLifeInputRenameItemDTO();
    }

    public static ChristianLifeInputRenameItemDtoBuilder create() {
        return new ChristianLifeInputRenameItemDtoBuilder();
    }

    public ChristianLifeInputRenameItemDTO build() {
        return target;
    }

    public ChristianLifeInputRenameItemDtoBuilder withData(
            Integer weekIndex, String originalName, String newName) {
        this.withWeekIndex(weekIndex);
        this.withOriginalName(originalName);
        this.withNewName(newName);
        return this;
    }

    private ChristianLifeInputRenameItemDtoBuilder withWeekIndex(Integer weekIndex) {
        this.target.setWeekIndex(weekIndex);
        return this;
    }

    private ChristianLifeInputRenameItemDtoBuilder withOriginalName(String originalName) {
        this.target.setOriginalName(originalName);
        return this;
    }

    private ChristianLifeInputRenameItemDtoBuilder withNewName(String newName) {
        this.target.setNewName(newName);
        return this;
    }
}
