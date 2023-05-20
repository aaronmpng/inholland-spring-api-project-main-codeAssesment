package io.inholland.groep4.api.cucumber.features.steps;

import io.cucumber.spring.CucumberContextConfiguration;
import io.inholland.groep4.Application;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@CucumberContextConfiguration
@SpringBootTest()
@ContextConfiguration(classes = Application.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class CucumberContextConfig {
}
