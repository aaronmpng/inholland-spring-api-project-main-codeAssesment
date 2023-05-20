package io.inholland.groep4.api.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/io/inholland/groep4/api/cucumber/features", glue = "io.inholland.groep4.api.cucumber.features.steps", plugin = "pretty")
public class CucumberTest {
}
