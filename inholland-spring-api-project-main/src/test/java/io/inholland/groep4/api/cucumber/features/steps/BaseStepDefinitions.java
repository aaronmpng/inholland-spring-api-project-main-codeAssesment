package io.inholland.groep4.api.cucumber.features.steps;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(classes = CucumberContextConfig.class)
@Slf4j
public class BaseStepDefinitions {

    @LocalServerPort
    private int port;

    @Value("${nl.inholland.groep4.api.baseurl}")
    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl + port;
    }
}
