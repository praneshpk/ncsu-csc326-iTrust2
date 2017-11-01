package edu.ncsu.csc.itrust2.cucumber;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith ( Cucumber.class ) //changed path for testing - remove BlackBox.feature to test everything
@CucumberOptions ( features = "src/test/resources/edu/ncsu/csc/itrust/cucumber/BlackBox.feature" )
public class ITRunner {
	
}
