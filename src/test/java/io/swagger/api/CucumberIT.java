package io.swagger.api;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "io.swagger.api.steps",
        plugin = "pretty",
        publish = true)
public class CucumberIT {
}