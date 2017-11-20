package edu.ncsu.csc.itrust2.cucumber;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.*;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.HibernateDataGenerator;
import io.github.bonigarcia.wdm.PhantomJsDriverManager;

public class PasswordStepDefs {

	private WebDriver driver;
	private final PasswordEncoder pe    = new BCryptPasswordEncoder();
    private final String    baseUrl     = "http://localhost:8080/iTrust2";
    private final By oldPassword        = By.id("oldPassword");
    private final By newPassword        = By.id("newPassword");
    private final By confirmPassword        = By.id("confirm");
    private String currentPassword;
    private String currentUsername;
	
    
    @Before
    public void setUp () {
        PhantomJsDriverManager.getInstance().setup();
        driver = new PhantomJSDriver();
    }

    @After
    public void teardown () {
        driver.close();
    }
    
    @Given ("^an admin exists in the system$")
    public void ensureAdminUser() {
    	if(User.getByName("admin") == null) {
    		HibernateDataGenerator.generateUsers();
        	HibernateDataGenerator.refreshDB();
    	}
    	this.currentPassword = User.getByName("admin").getPassword();
    	this.currentUsername = "admin";
    }
    
    @Given ("^a patient exists in the system$")
    public void ensurePatientUser() {
    	if(User.getByName("patient") == null) {
    		HibernateDataGenerator.generateUsers();
        	HibernateDataGenerator.refreshDB();
    	}
    	this.currentPassword = User.getByName("patient").getPassword();
    	this.currentUsername = "patient";
    }
    
    @Given ("^a HCP exists in the system$")
    public void ensureHCPUser() {
    	if(User.getByName("hcp") == null) {
    		HibernateDataGenerator.generateUsers();
        	HibernateDataGenerator.refreshDB();
    	}
    	this.currentPassword = User.getByName("hcp").getPassword();
    	this.currentUsername = "hcp";
    }
    
    public void ensureLogin () {
        if ( !driver.getPageSource().contains( currentUsername ) ) {
            driver.get( baseUrl );
            final WebElement usernameField = driver.findElement( By.name( "username" ) );
            usernameField.clear();
            usernameField.sendKeys( this.currentUsername );
            final WebElement passwordField = driver.findElement( By.name( "password" ) );
            passwordField.clear();
            passwordField.sendKeys( this.currentPassword );
            final WebElement submit = driver.findElement( By.className( "btn" ) );
            submit.click();
        }
    }
    
    @When ("^the user changes their password to (.+)$")
    public void adminChangePassword(String password) throws InterruptedException {
    	driver.get(baseUrl);
    	ensureLogin();
    	driver.get(baseUrl + "/changePassword");
    	//Thread.sleep(500);
    	//WebDriverWait wait = new WebDriverWait(driver, 5);
    	//wait.until(ExpectedConditions.visibilityOfElementLocated(oldPassword));
    	final WebElement oldPasswordField = driver.findElement(oldPassword);
    	oldPasswordField.clear();
    	oldPasswordField.sendKeys(this.currentPassword);
    	final WebElement newPasswordField = driver.findElement(newPassword);
    	newPasswordField.clear();
    	newPasswordField.sendKeys(password);
    	final WebElement confirmField = driver.findElement(confirmPassword);
    	confirmField.clear();
    	confirmField.sendKeys(password);
    	this.currentPassword = password;
    	final WebElement submit = driver.findElement(By.id("submit"));
    	submit.click();
    }
    
    @Then ("^their password will be changed$")
    public void passwordChangeSuccess() {
    	Assert.assertTrue(User.getByName(this.currentUsername).getPassword().equals(pe.encode(this.currentPassword)));
    }
}
