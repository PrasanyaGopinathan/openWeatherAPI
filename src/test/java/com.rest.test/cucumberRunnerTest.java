package com.rest.test;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/Destination"},
        glue = {"com.rest.test.test.step.definitions"},
        features = {"src/test/java/com.rest.test/resources/features"},
        tags = {"@openWeatherAPITests"})
public class cucumberRunnerTest {}
