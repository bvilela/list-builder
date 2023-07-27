package br.com.bvilela.listbuilder.utils;

import br.com.bvilela.lib.utils.GsonUtils;
import br.com.bvilela.listbuilder.dto.discurso.DiscursoAllThemesDTO;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

public class TestUtils {

    private static final String FILE_INPUT_DISCURSO_ALL_THEMES = "dados-discurso-temas.json";

    @Getter private final ListTypeEnum listType;

    @Getter private final String resourceDirectory;

    public TestUtils(ListTypeEnum listType) {
        this.listType = listType;
        this.resourceDirectory = getResourceDirectoryByListType();
    }

    private String getResourceDirectoryByListType() {
        return Paths.get("src", "test", "resources", listType.toString().toLowerCase())
                .toFile()
                .getAbsolutePath();
    }

    @SneakyThrows
    public void createDirectory() {
        FileUtils.createDirectories(this.resourceDirectory);
    }

    public void writeFileInputSyntaxError() {
        this.writeFileInputFromDto(this.listType.getInputFileName(), "syntax-error");
    }

    public void writeFileInputDiscursoAllThemesSyntaxError() {
        this.writeFileInputFromDto(FILE_INPUT_DISCURSO_ALL_THEMES, "syntax-error");
    }

    public void writeFileInputFromDto(Object dto) {
        this.writeFileInputFromDto(
                this.listType.getInputFileName(), GsonUtils.getGson().toJson(dto));
    }

    public void writeFileInputDiscursoAllThemes(DiscursoAllThemesDTO dto) {
        this.writeFileInputFromDto(FILE_INPUT_DISCURSO_ALL_THEMES, GsonUtils.getGson().toJson(dto));
    }

    private void writeFileInputFromDto(String fileName, String content) {
        String file = Paths.get(this.resourceDirectory, fileName).toString();
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write(content);
            fileWriter.flush();
        } catch (IOException ignored) {
        }
    }

    public void cleanDirectory() {
        Arrays.stream(new File(Paths.get(this.resourceDirectory).toString()).listFiles())
                .forEach(File::delete);
    }

    public <T> void validateException(Executable executable, String expectedMessageError) {
        var exception = Assertions.assertThrows(ListBuilderException.class, executable);
        String expectedMessage =
                String.format("Erro ao gerar lista '%s': %s", this.listType, expectedMessageError);
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    public static void cleanResourceDir() {
        var files =
                Paths.get("src", "test", "resources")
                        .toFile()
                        .listFiles(
                                (f, p) ->
                                        p.endsWith(".pdf")
                                                || p.endsWith(".png")
                                                || p.endsWith(".docx"));
        Arrays.stream(files).forEach(File::delete);
    }

    public static List<LocalDate> createListLocalDates(List<String> datesDDMM, int year) {
        var list = new ArrayList<LocalDate>();
        for (String date : datesDDMM) {
            var split = date.split("/");
            var localDate =
                    LocalDate.of(year, Integer.parseInt(split[1]), Integer.parseInt(split[0]));
            list.add(localDate);
        }
        return list;
    }
}
