package edu.ncsu.csc.itrust2.cucumber;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.en.*;
import edu.ncsu.csc.itrust2.forms.admin.CodeForm;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.NDCCode;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
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
	  private Prescription currentPres;
	  
	  
	  private void addUserHelper(String username, Role role) {
	    	User u = User.getByName(username);
	    	if(u == null) {
	    		final User admin = new User( username, "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", role,
	                    1 );
	            admin.save();
	    	}
	    }
	  
	  @Given("^an admin has logged into the system$")
	  public void ensureAdminExistance() {
		  if(!driver.getPageSource().contains("admin")) {
//			  final WebElement logout =  driver.findElement(By.id("logout"));
//			  logout.click();
			  addUserHelper("admin", Role.ROLE_ADMIN);
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
	  
	  @Given("^an HCP has logged into the system and a patient exists$")
	  public void ensureHCPExistance() {
		  if(!driver.getPageSource().contains("hcp")) {
//			  final WebElement logout =  driver.findElement(By.id("logout"));
//			  logout.click();
			  addUserHelper("hcp", Role.ROLE_HCP);
			  driver.get( baseUrl );
			  try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  final WebElement username = driver.findElement( By.name( "username" ) );
			  username.clear();
			  username.sendKeys( "hcp" );
			  final WebElement password = driver.findElement( By.name( "password" ) );
			  password.clear();
			  password.sendKeys( "123456" );
			  final WebElement submit = driver.findElement( By.className( "btn" ) );
			  submit.click();
	    	}
		  if(Patient.getPatient("TimTheOneYearOld") == null) {
			  final Patient tim = new Patient();
		      final User timUser = new User( "TimTheOneYearOld",
		                "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_PATIENT, 1 );
		      timUser.save();
		      tim.setSelf( timUser );
		      tim.setFirstName( "TimTheOneYearOld" );
		      tim.setLastName( "Smith" );
		      final Calendar timBirth = Calendar.getInstance();
		      timBirth.add( Calendar.YEAR, -1 ); // tim is one year old
		      tim.setDateOfBirth( timBirth );
		      tim.save();
		  }
	  }
	  
	  @Given("^a patient has logged into the system and has a prescription assigned to them$")
	  public void ensurePatientExistance() {
		  if(!driver.getPageSource().contains("TimTheOneYearOld")) {
//			  final WebElement logout =  driver.findElement(By.id("logout"));
//			  logout.click();
			  addUserHelper("TimTheOneYearOld", Role.ROLE_ADMIN);
			  if(Patient.getPatient("TimTheOneYearOld") == null) {
				  final Patient tim = new Patient();
			      final User timUser = new User( "TimTheOneYearOld",
			                "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_PATIENT, 1 );
			      timUser.save();
			      tim.setSelf( timUser );
			      tim.setFirstName( "TimTheOneYearOld" );
			      tim.setLastName( "Smith" );
			      final Calendar timBirth = Calendar.getInstance();
			      timBirth.add( Calendar.YEAR, -1 ); // tim is one year old
			      tim.setDateOfBirth( timBirth );
			      tim.save();
			  }
			  driver.get( baseUrl );
			  final WebElement username = driver.findElement( By.name( "username" ) );
			  username.clear();
			  username.sendKeys( "TimTheOneYearOld" );
			  final WebElement password = driver.findElement( By.name( "password" ) );
			  password.clear();
			  password.sendKeys( "123456" );
			  final WebElement submit = driver.findElement( By.className( "btn" ) );
			  submit.click();
	    	}
		  Patient p = Patient.getPatient("TimTheOneYearOld");
		  if(p.getPrescriptions().isEmpty()) {
			  Prescription pres = new Prescription();
			  NDCCode code = new NDCCode();
			  code.setCode("6443-469-29");
			  code.setName("irritation");
			  code.save();
			  pres.setNdcCode(code);
			  pres.setDosage(30.0);
			  pres.setPatient(p);
			  pres.setRenewals(5);
			  pres.save();
			  p.addPrescription(pres);
			  p.save();
		  }
		  this.currentPres = p.getPrescriptions().get(0);
		  this.currentName = p.getPrescriptions().get(0).getNdcCode().getName();
		  this.currentCode = p.getPrescriptions().get(0).getNdcCode().getId();
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
		  if(NDCCode.getByCode(code) == null || !NDCCode.getByCode(code).getName().equals(name)) {//doesn't exist
			  NDCCode n = new NDCCode();
			  n.setCode(code);
			  n.setName(name);
			  n.save();
			  try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driver.navigate().refresh();
		  }
		  this.currentCode = code;
		  this.currentName = name;
	  }
	  
	  @When("^an admin attempts to edit to code's name to (.+)$")
	  public void editNDCCode(String name) {
		  driver.get(baseUrl + "/admin/updateNDC");
		  try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  final WebElement code = driver.findElement(By.xpath("//input[@value='" + currentCode + "']"));
		  code.click();
		  driver.findElement(editInput).sendKeys(name);
		  driver.findElement(editBtn).click();
		  this.newname = name;
	  }
	  
	  @When("^the HCP assigns a dosage of (.+), (.+) renewals, a start date of (.+) and end date of (.+)$")
	  public void prescribeMedicine(String dosage, String renewals, String startDate, String endDate) {
		  driver.get(baseUrl + "/hcp/addPrescription");
		  try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Select dropdown = new Select(driver.findElement(By.id("patientSelect")));
		dropdown.selectByVisibleText("TimTheOneYearOld");
		Select drug = new Select(driver.findElement(By.id("prescriptionSelect")));
		drug.selectByVisibleText(this.currentName);
		WebElement dosageBox = driver.findElement(By.id("dosageInput"));
		dosageBox.sendKeys(dosage);
		WebElement renewalBox = driver.findElement(By.id("renewalInput"));
		renewalBox.sendKeys(renewals);
		WebElement startDateBox = driver.findElement(By.id("startDate"));
		startDateBox.sendKeys(startDate);
		WebElement endDateBox = driver.findElement(By.id("endDate"));
		endDateBox.sendKeys(endDate);
		driver.findElement(By.name("submit")).click();
	  }
	  
	  @Then("^the prescription is successfully created with the given information$")
	  public void prescriptionCreationSuccess() {
		  try {
			Thread.sleep(500);
			Assert.assertTrue(driver.getPageSource().contains("Successfully updated. Refresh to see changes."));
			driver.navigate().refresh();
			Thread.sleep(2000);
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
	
	 @Then("^the prescription is successfully prescribed$")
	 public void prescribeSuccess() {
		 try {
			Thread.sleep(2000);
			Assert.assertTrue(driver.getPageSource().contains("Prescription created successfully"));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 
	 @Then("^the prescription is NOT successfully prescribed$")
	 public void prescribeFailure() {
		 try {
				Thread.sleep(2000);
				Assert.assertTrue(driver.getPageSource().contains("must be a number greater than 0"));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 }
	 
//	 @Then("^they are able to view their prescriptions$")
//	 public void viewSuccess() {
//		  try {
//			driver.get(baseUrl + "patient/viewPrescriptions"); 
//			Thread.sleep(2000);
//			//Assert.assertTrue(driver.getPageSource().contains(this.currentPres.getNdcCode().getName()));
//			//The line above and the one below -- it can't seem to find anything.
//			WebElement button = driver.findElement(By.xpath("//input[@value='" + this.currentPres.getId() + "']"));
//			button.click();
//			Thread.sleep(500);
//			Date start = this.currentPres.getStart();
//			@SuppressWarnings("deprecation")
//			String startDate = start.getMonth() + "\\" + start.getDay() + "\\" + start.getYear();
//			Assert.assertTrue(driver.getPageSource().contains(startDate));
//			Date end = this.currentPres.getEnd();
//			@SuppressWarnings("deprecation")
//			String endDate = end.getMonth() + "\\" + end.getDay() + "\\" + end.getYear();
//			Assert.assertTrue(driver.getPageSource().contains(endDate));
//			Assert.assertTrue(driver.getPageSource().contains("Remaining Renewals: " + this.currentPres.getRenewals()));
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	 }
	
}
