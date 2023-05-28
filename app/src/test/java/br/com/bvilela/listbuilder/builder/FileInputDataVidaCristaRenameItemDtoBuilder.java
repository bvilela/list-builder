package br.com.bvilela.listbuilder.builder;

import br.com.bvilela.listbuilder.dto.vidacrista.FileInputDataVidaCristaRenameItemDTO;

public class FileInputDataVidaCristaRenameItemDtoBuilder {

    private FileInputDataVidaCristaRenameItemDTO target;

    public FileInputDataVidaCristaRenameItemDtoBuilder() {
        this.target = new FileInputDataVidaCristaRenameItemDTO();
    }

    public static FileInputDataVidaCristaRenameItemDtoBuilder create() {
        return new FileInputDataVidaCristaRenameItemDtoBuilder();
    }

    public FileInputDataVidaCristaRenameItemDTO build() {
        return target;
    }

    public FileInputDataVidaCristaRenameItemDtoBuilder withData(
            Integer weekIndex, String originalName, String newName) {
        this.withWeekIndex(weekIndex);
        this.withOriginalName(originalName);
        this.withNewName(newName);
        return this;
    }

    private FileInputDataVidaCristaRenameItemDtoBuilder withWeekIndex(Integer weekIndex) {
        this.target.setWeekIndex(weekIndex);
        return this;
    }

    private FileInputDataVidaCristaRenameItemDtoBuilder withOriginalName(String originalName) {
        this.target.setOriginalName(originalName);
        return this;
    }

    private FileInputDataVidaCristaRenameItemDtoBuilder withNewName(String newName) {
        this.target.setNewName(newName);
        return this;
    }
}
