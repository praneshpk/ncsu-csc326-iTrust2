package edu.ncsu.csc.itrust2.cucumber;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import cucumber.api.java.en.*;
import edu.ncsu.csc.itrust2.forms.admin.CodeForm;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.NDCCode;
import edu.ncsu.csc.itrust2.models.persistent.Prescription;
import edu.ncsu.csc.itrust2.models.persistent.User;

public class PrescriptionStepDefs {
	
	private final By codeInput       = By.name( "codeInput" );
    private final By nameInput           = By.name( "nameInput" );
    private final By submitBtn     = By.name( "submitBtn" );
    private final By editInput     = By.name("updateNameInput");
    private final By editBtn       = By.name("submitUpdateBtn");

	  private final WebDriver driver        = new HtmlUnitDriver( true );
	  private final String    baseUrl       = "http://localhost:8080/iTrust2";
	  private String currentCode = "";
	  private String currentName = "";
	  private String newname = "";
	  
	  private void addAdminHelper() {
	    	User u = User.getByName("admin");
	    	if(u == null) {
	    		final User admin = new User( "hcp", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_ADMIN,
	                    1 );
	            admin.save();
	    	}
	    }
	  
	  @Given("^an admin has logged into the system$")
	  public void ensureNonExistance() {
		  if(!driver.getPageSource().contains("admin")) {
	    		addAdminHelper();
	        	driver.get( baseUrl );
	            final WebElement username = driver.findElement( By.name( "username" ) );
	            username.clear();
	            username.sendKeys( "admin" );
	            final WebElement password = driver.findElement( By.name( "password" ) );
	            password.clear();
	            password.sendKeys( "123456" );
	            final WebElement submit = driver.findElement( By.className( "btn" ) );
	            submit.click();
	    	}
	  }
	  
	  @When("^an admin attempts to create a new prescription with the NDC code (.+) and name (.+)$")
	  public void createPrescription(String code, String name) {
		  this.currentCode = code;
		  this.currentName = name;
		  driver.get(baseUrl + "/admin/updateNDC");
		  driver.findElement(codeInput).sendKeys(code);
		  driver.findElement(nameInput).sendKeys(name);
		  driver.findElement(submitBtn).click();
	  }
	  
	  @And("^the prescription with NDC code (.+) and name (.+) already exists$")
	  public void ensureNDCCodeExists(String code, String name) {
		  if(NDCCode.getByCode(code) == null || NDCCode.getByCode(code).getName() != name) {//doesn't exist
			  CodeForm form = new CodeForm();
			  form.setId(code);
			  form.setName(name);
			  NDCCode n = new NDCCode(form);
			  n.save();
			  driver.navigate().refresh();
			  try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		  this.currentCode = code;
		  this.currentName = name;
	  }
	  
	  @When("^an admin attempts to edit to code's name to (.+)$")
	  public void editNDCCode(String name) {
		  driver.get(baseUrl + "/admin/updateNDC");
		  final WebElement code = driver.findElement(By.xpath("//input[@value='" + currentCode + "']"));
		  code.click();
		  driver.findElement(editInput).sendKeys(name);
		  driver.findElement(editBtn).click();
		  this.newname = name;
	  }
	  
	  @Then("^the prescription is successfully created with the given information$")
	  public void prescriptionCreationSuccess() {
		  try {
			Thread.sleep(500);
			Assert.assertTrue(driver.getPageSource().contains("Successfully updated. Refresh to see changes."));
			driver.navigate().refresh();
			Thread.sleep(500);
			Assert.assertTrue(driver.getPageSource().contains(currentCode + " - " + currentName));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	  }
	  
	  @Then("^the prescription is not successfully created$")
	  public void prescriptionCreationFailure() {
		  try {
			Thread.sleep(500);
			Assert.assertTrue(driver.getPageSource().contains("Error: Incorrect NDC code format."));
			driver.navigate().refresh();
			Thread.sleep(500);
			Assert.assertTrue(!driver.getPageSource().contains(currentCode + " - " + currentName));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	  }
	  
	  @Then("^the prescription is successfully updated with the given information$")
	  public void editSuccess() {
		  try {
			Thread.sleep(500);
			Assert.assertTrue(driver.getPageSource().contains("Successfully updated. Refresh to see changes."));
			driver.navigate().refresh();
			Thread.sleep(500);
			Assert.assertTrue(!driver.getPageSource().contains(currentCode + " - " + currentName)); //these were the current name/code before update
			Assert.assertTrue(driver.getPageSource().contains(currentCode + " - " + newname));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	
	
}
