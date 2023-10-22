package br.com.bvilela.listbuilder;

import org.springframework.boot.ExitCodeEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;

import java.util.TimeZone;

@SpringBootApplication
@ComponentScan({"br.com.bvilela.listbuilder", "br.com.bvilela.lib"})
public class ListBuilderApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
        SpringApplication.run(ListBuilderApplication.class, args);
    }

    @Bean
    ExitCodeEventListener exitCodeEventListenerBean() {
        return new ExitCodeEventListener();
    }

    public static class ExitCodeEventListener {
        @EventListener
        public void exitEvent(ExitCodeEvent event) {
            System.exit(event.getExitCode());
        }
    }
}