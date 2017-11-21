package edu.ncsu.csc.itrust2.selenium;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import edu.ncsu.csc.itrust2.utils.HibernateDataGenerator;
import io.github.bonigarcia.wdm.PhantomJsDriverManager;

public class LoginIT {

    private WebDriver           driver;
    private String              baseUrl;
    private static final String HOME_URL = "http://localhost:8080/iTrust2/ROLE/index";

    /**
     * Sets up the test
     *
     * @throws Exception
     */
    @Before
    public void setUp () throws Exception {
        PhantomJsDriverManager.getInstance().setup();
        driver = new PhantomJSDriver();
        baseUrl = "http://localhost:8080/iTrust2";
        HibernateDataGenerator.refreshDB();
    }

    @After
    public void teardown () {
        driver.quit()();
    }

    private void testLogin ( final String role ) {
        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( role );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
        assertEquals( HOME_URL.replace( "ROLE", role ), driver.getCurrentUrl() );
    }

    @Test
    public void hcpShouldLogIn () {
        testLogin( "hcp" );
    }

    @Test
    public void patientShouldLogIn () {
        testLogin( "patient" );
    }

    @Test
    public void adminShouldLogIn () {
        testLogin( "admin" );
    }
}
