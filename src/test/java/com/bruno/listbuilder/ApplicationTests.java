package com.bruno.listbuilder;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bruno.listbuilder.service.impl.ApplicationServiceImpl;

class ApplicationTests {
	
	@InjectMocks
	private Application application;
	
	@Mock
	private ApplicationServiceImpl service;
	
	@BeforeEach
	public void setup() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		application = new Application(service);
	}

	@Test
	void shouldBeMain() {
		assertDoesNotThrow(() -> application.run(null));
	}
	
	@Test
	void shouldPostConstruct() {
		assertDoesNotThrow(() -> application.init());
	}
	
	@Test
	void shouldBeExitCodeEventListenerBean() {
		assertDoesNotThrow(() -> application.exitCodeEventListenerBean());
	}

}
