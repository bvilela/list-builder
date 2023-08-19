package br.com.bvilela.listbuilder.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AppProperties {

    @Value("${input.dir}")
    private String inputDir;

    @Value("${output.dir}")
    private String outputDir;

    @Value("${layout.limpeza}")
    private int layoutLimpeza;

    @Value("${audience.layout:full}")
    private String layoutAudience;

    @Value("${discourse.include-president:false}")
    private boolean discourseIncludePresident;
}
