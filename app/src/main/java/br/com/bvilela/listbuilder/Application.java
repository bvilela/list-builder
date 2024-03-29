package br.com.bvilela.listbuilder;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ExitCodeEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;

import java.util.TimeZone;

@SpringBootApplication
@RequiredArgsConstructor
@ComponentScan({"br.com.bvilela.listbuilder", "br.com.bvilela.lib"})
public class Application {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
        SpringApplication.run(Application.class, args);
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