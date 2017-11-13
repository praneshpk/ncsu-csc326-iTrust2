package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertFalse;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.PatientSmokingStatus;
import edu.ncsu.csc.itrust2.models.persistent.BasicHealthMetrics;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;
import edu.ncsu.csc.itrust2.utils.HibernateDataGenerator;

@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class EditPatientDemographicsStepDefs {

    @Autowired
    private WebApplicationContext context;

    private final WebDriver       driver  = new HtmlUnitDriver( true );
    private final String          baseUrl = "http://localhost:8080/iTrust2";

    BasicHealthMetrics            expectedBhm;

    WebDriverWait                 wait    = new WebDriverWait( driver, 2 );

    @Given ( "^All the Facilities exist$" )
    public void personnelExists () throws Exception {

        // All tests can safely assume the existence of the 'hcp', 'admin', and
        // 'patient' users

        /* Make sure we create a Hospital and Patient record */

        HibernateDataGenerator.refreshDB();
        HibernateDataGenerator.generateDiagnosis( "Sick", "S00" );

        /* Create patient record */

        driver.get( baseUrl );
        WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "patient" );
        WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('editdemographics').click();" );
        try {
            final WebElement firstName = driver.findElement( By.id( "firstName" ) );
            firstName.clear();
            firstName.sendKeys( "Karl" );

            final WebElement lastName = driver.findElement( By.id( "lastName" ) );
            lastName.clear();
            lastName.sendKeys( "Liebknecht" );

            final WebElement preferredName = driver.findElement( By.id( "preferredName" ) );
            preferredName.clear();

            final WebElement mother = driver.findElement( By.id( "mother" ) );
            mother.clear();

            final WebElement father = driver.findElement( By.id( "father" ) );
            father.clear();

            final WebElement email = driver.findElement( By.id( "email" ) );
            email.clear();
            email.sendKeys( "karl_liebknecht@mail.de" );

            final WebElement address1 = driver.findElement( By.id( "address1" ) );
            address1.clear();
            address1.sendKeys( "Karl-Liebknecht-Haus, Alexanderplatz" );

            final WebElement city = driver.findElement( By.id( "city" ) );
            city.clear();
            city.sendKeys( "Berlin" );

            final WebElement state = driver.findElement( By.id( "state" ) );
            final Select dropdown = new Select( state );
            dropdown.selectByVisibleText( "CA" );

            final WebElement zip = driver.findElement( By.id( "zip" ) );
            zip.clear();
            zip.sendKeys( "91505" );

            final WebElement phone = driver.findElement( By.id( "phone" ) );
            phone.clear();
            phone.sendKeys( "123-456-7890" );

            final WebElement dob = driver.findElement( By.id( "dateOfBirth" ) );
            dob.clear();
            dob.sendKeys( "08/13/1871" );

            final WebElement submit2 = driver.findElement( By.className( "btn" ) );
            submit2.click();

        }
        catch ( final Exception e ) {
            /*  */
        }
        finally {
            driver.findElement( By.id( "logout" ) ).click();
        }

    }

    @Given ( "^The user really is logged in to iTrust2 as HCP$" )
    public void loginAsHCP () {
        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "hcp" );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
    }

    @Given ( "^HCP really has navigated to the Document Office Visit page$" )
    public void navigateDocumentOV () {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('documentOfficeVisit').click();" );
    }

    @When ( "^The HCP modifies the (.+) demographics to (.+) in the office visit$" )
    public void documentOV ( final String demNumber, final String newValue ) {

        final WebElement patient = driver.findElement( By.name( "name" ) );
        patient.click();

        if ( demNumber.equals( "1" ) ) {

            final WebElement email = driver.findElement( By.id( "email" ) );
            email.clear();
            email.sendKeys( newValue );

        }
        else if ( demNumber.equals( "2" ) ) {

            final WebElement preferredName = driver.findElement( By.id( "preferredName" ) );
            preferredName.clear();
            preferredName.sendKeys( newValue );

        }
        else if ( demNumber.equals( "3" ) ) {

            final WebElement dob = driver.findElement( By.id( "dateOfBirth" ) );
            dob.clear();
            dob.sendKeys( newValue );

        }

        /**
         * final WebElement firstName = driver.findElement( By.id( "firstName" )
         * ); firstName.clear(); firstName.sendKeys( "Karl" );
         * 
         * final WebElement lastName = driver.findElement( By.id( "lastName" )
         * ); lastName.clear(); lastName.sendKeys( "Liebknecht" );
         * 
         * final WebElement preferredName = driver.findElement( By.id(
         * "preferredName" ) ); preferredName.clear();
         * 
         * final WebElement mother = driver.findElement( By.id( "mother" ) );
         * mother.clear();
         * 
         * final WebElement father = driver.findElement( By.id( "father" ) );
         * father.clear();
         * 
         * final WebElement email = driver.findElement( By.id( "email" ) );
         * email.clear(); email.sendKeys( "karl_liebknecht@mail.de" );
         * 
         * final WebElement address1 = driver.findElement( By.id( "address1" )
         * ); address1.clear(); address1.sendKeys( "Karl-Liebknecht-Haus,
         * Alexanderplatz" );
         * 
         * final WebElement city = driver.findElement( By.id( "city" ) );
         * city.clear(); city.sendKeys( "Berlin" );
         * 
         * final WebElement state = driver.findElement( By.id( "state" ) );
         * final Select dropdown = new Select( state );
         * dropdown.selectByVisibleText( "CA" );
         * 
         * final WebElement zip = driver.findElement( By.id( "zip" ) );
         * zip.clear(); zip.sendKeys( "91505" );
         * 
         * final WebElement phone = driver.findElement( By.id( "phone" ) );
         * phone.clear(); phone.sendKeys( "123-456-7890" );
         * 
         * final WebElement dob = driver.findElement( By.id( "dateOfBirth" ) );
         * dob.clear(); dob.sendKeys( "08/13/1871" );
         **/

    }

    @And ( "^The HCP completes the office visit$" )
    public void fillInDiagnosis () {

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "notes" ) ) );
        final WebElement notes = driver.findElement( By.name( "notes" ) );
        notes.clear();
        notes.sendKeys( "Patient appears pretty much alive" );

        final WebElement type = driver.findElement( By.name( "type" ) );
        type.click();

        final WebElement hospital = driver.findElement( By.name( "hospital" ) );
        hospital.click();

        final WebElement date = driver.findElement( By.name( "date" ) );
        date.clear();
        date.sendKeys( "12/19/2050" );

        final WebElement time = driver.findElement( By.name( "time" ) );
        time.clear();
        time.sendKeys( "9:30 AM" );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "height" ) ) );
        final WebElement heightElement = driver.findElement( By.name( "height" ) );
        heightElement.clear();
        heightElement.sendKeys( "120" );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "weight" ) ) );
        final WebElement weightElement = driver.findElement( By.name( "weight" ) );
        weightElement.clear();
        weightElement.sendKeys( "120" );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "systolic" ) ) );
        final WebElement systolicElement = driver.findElement( By.name( "systolic" ) );
        systolicElement.clear();
        systolicElement.sendKeys( "100" );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "diastolic" ) ) );
        final WebElement diastolicElement = driver.findElement( By.name( "diastolic" ) );
        diastolicElement.clear();
        diastolicElement.sendKeys( "100" );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "hdl" ) ) );
        final WebElement hdlElement = driver.findElement( By.name( "hdl" ) );
        hdlElement.clear();
        hdlElement.sendKeys( "90" );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "ldl" ) ) );
        final WebElement ldlElement = driver.findElement( By.name( "ldl" ) );
        ldlElement.clear();
        ldlElement.sendKeys( "100" );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "tri" ) ) );
        final WebElement triElement = driver.findElement( By.name( "tri" ) );
        triElement.clear();
        triElement.sendKeys( "100" );

        wait.until( ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector( "input[value=\"" + HouseholdSmokingStatus.NONSMOKING.toString() + "\"]" ) ) );
        final WebElement houseSmokeElement = driver.findElement(
                By.cssSelector( "input[value=\"" + HouseholdSmokingStatus.NONSMOKING.toString() + "\"]" ) );
        houseSmokeElement.click();

        wait.until( ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector( "input[value=\"" + PatientSmokingStatus.NEVER.toString() + "\"]" ) ) );
        final WebElement patientSmokeElement = driver
                .findElement( By.cssSelector( "input[value=\"" + PatientSmokingStatus.NEVER.toString() + "\"]" ) );
        patientSmokeElement.click();

        final Select diagnosis = new Select( driver.findElement( By.id( "repeatDiagnoses" ) ) );
        diagnosis.selectByVisibleText( "Sick - S00" );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "submit" ) ) );
        final WebElement submit = driver.findElement( By.name( "submit" ) );
        submit.click();
    }

    @Then ( "^Office visit really has been documented successfully$" )
    public void documentedSuccessfully () {
        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "success" ) ) );
        final WebElement message = driver.findElement( By.name( "success" ) );

        assertFalse( message.getText().contains( "Error occurred creating office visit" ) );
    }
}