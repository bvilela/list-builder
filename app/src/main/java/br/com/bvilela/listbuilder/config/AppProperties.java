package br.com.bvilela.listbuilder.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Getter
@Configuration
public class AppProperties {

    @Value("${folder.input}")
    private String inputDir;

    @Value("${folder.output}")
    private String outputDir;

    @Value("${layout.limpeza}")
    private int layoutLimpeza;

    @Value("${audience.layout:full}")
    private String layoutAudience;

    @Value("${christianlife.include.bible-study-reader:false}")
    private boolean christianlifeAddBibleStudyReader;

    @Value("${discourse.include-president:false}")
    private boolean discourseIncludePresident;
}