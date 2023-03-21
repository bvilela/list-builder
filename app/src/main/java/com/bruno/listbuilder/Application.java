package com.bruno.listbuilder;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ExitCodeEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;

import com.bruno.listbuilder.service.ApplicationService;

@SpringBootApplication
@ComponentScan({"com.bruno.listbuilder", "com.bvilela.lib"})
public class Application implements ApplicationRunner {

	private ApplicationService applicationService;

	public Application(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		applicationService.runApplication();
	}
	
	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
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
