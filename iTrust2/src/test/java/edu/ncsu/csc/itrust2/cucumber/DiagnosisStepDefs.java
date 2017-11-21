package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.PatientSmokingStatus;
import edu.ncsu.csc.itrust2.models.persistent.BasicHealthMetrics;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;
import edu.ncsu.csc.itrust2.utils.HibernateDataGenerator;
import io.github.bonigarcia.wdm.PhantomJsDriverManager;

@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class DiagnosisStepDefs {

    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    private WebDriver             driver;
    private final String          baseUrl = "http://localhost:8080/iTrust2";

    BasicHealthMetrics            expectedBhm;

    @Before
    public void setUp () {
        PhantomJsDriverManager.getInstance().setup();
        driver = new PhantomJSDriver();
    }

    @After
    public void teardown () {
        driver.close();
    }

    @Given ( "^Facilities exist$" )
    public void personnelExists () throws Exception {

        // All tests can safely assume the existence of the 'hcp', 'admin', and
        // 'patient' users

        /* Make sure we create a Hospital and Patient record */

        HibernateDataGenerator.refreshDB();

        /* Create patient record */
        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "patient" );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
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

    @Given ( "^The user is logged in to iTrust2 as HCP$" )
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

    @Given ( "^The user is logged in to iTrust2 as patient$" )
    public void loginAsPatient () {
        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "patient" );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
    }

    @Given ( "^The user is logged in to iTrust2 as admin$" )
    public void loginAsAdmin () {
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

    /**
     * Puts a diagnosis in the database
     *
     * @param name
     *            of the diagnosis
     * @param code
     *            of the diagnosis
     */
    @Given ( "^The diagnosis exists with the name (.+) and the code (.+)" )
    public void insetDiagnosis ( final String name, final String code ) {
        HibernateDataGenerator.generateDiagnosis( name, code );
    }

    @Given ( "^An office visit has been created with a diagnosis of (.+)$" )
    public void createOfficeVisit ( final String name ) {
        HibernateDataGenerator.generateOfficeVist( name );
    }

    @Given ( "^HCP navigated to the Document Office Visit page$" )
    public void navigateDocumentOV () {
        //( (JavascriptExecutor) driver ).executeScript( "document.getElementById('documentOfficeVisit').click();" );
    	driver.get(baseUrl + "/hcp/documentOfficeVisit.html");
    }

    @Given ( "^HCP filled in information on the office visit$" )
    public void documentOV () throws InterruptedException {
    	
    	HibernateDataGenerator.generateUsers();
    	driver.navigate().refresh();
    	
    	final WebElement patient = driver.findElement(By.name("AliceThirteen"));
    	//final WebElement patient = driver.findElement(By.xpath("//*[contains(text(),'AliceThirteen')]"));
        patient.click();
        
        Thread.sleep(200);
    	
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
        
        Thread.sleep(200);

        final WebElement heightElement = driver.findElement( By.name( "height" ) );
        heightElement.clear();
        heightElement.sendKeys( "120" );

        final WebElement weightElement = driver.findElement( By.name( "weight" ) );
        weightElement.clear();
        weightElement.sendKeys( "120" );

        final WebElement systolicElement = driver.findElement( By.name( "systolic" ) );
        systolicElement.clear();
        systolicElement.sendKeys( "100" );

        final WebElement diastolicElement = driver.findElement( By.name( "diastolic" ) );
        diastolicElement.clear();
        diastolicElement.sendKeys( "100" );

        final WebElement hdlElement = driver.findElement( By.name( "hdl" ) );
        hdlElement.clear();
        hdlElement.sendKeys( "90" );

        final WebElement ldlElement = driver.findElement( By.name( "ldl" ) );
        ldlElement.clear();
        ldlElement.sendKeys( "100" );

        final WebElement triElement = driver.findElement( By.name( "tri" ) );
        triElement.clear();
        triElement.sendKeys( "100" );

        final WebElement houseSmokeElement = driver.findElement(
                By.cssSelector( "input[value=\"" + HouseholdSmokingStatus.NONSMOKING.toString() + "\"]" ) );
        houseSmokeElement.click();

        final WebElement patientSmokeElement = driver
                .findElement( By.cssSelector( "input[value=\"" + PatientSmokingStatus.NEVER.toString() + "\"]" ) );
        patientSmokeElement.click();

    }

    @When ( "^The HCP selects the diagnosis with name (.+) and code (.+)$" )
    public void fillInDiagnosis ( final String diagnosisName, final String diagnosisCode ) {
        final Select diagnosis = new Select( driver.findElement( By.id( "repeatDiagnoses" ) ) );
        diagnosis.selectByVisibleText( diagnosisName + " - " + diagnosisCode );

        final WebElement submit = driver.findElement( By.name( "submit" ) );
        submit.click();
    }

    @When ( "^The HCP submits the office visit$" )
    public void submitDiagnosis () {

        final WebElement submit = driver.findElement( By.name( "submit" ) );
        submit.click();
    }

    @When ( "^The patient navigated to the View Office Visits page$" )
    public void navigateViewOV () {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('viewOfficeVisits').click();" );
    }

    @When ( "^The admin creates a diagnosis with the name (.*) and the code (.*)$" )
    public void createDiagnosis ( final String diagnosisName, final String diagnosisCode ) {

        final WebElement nameOfDiagnosis = driver.findElement( By.name( "nameInput" ) );
        nameOfDiagnosis.clear();
        nameOfDiagnosis.sendKeys( diagnosisName );

        final WebElement codeOfDiagnosis = driver.findElement( By.name( "codeInput" ) );
        codeOfDiagnosis.clear();
        codeOfDiagnosis.sendKeys( diagnosisCode );

        final WebElement submit = driver.findElement( By.name( "submitUpdateBtn" ) );
        submit.click();
    }

    @When ( "^The admin navigates to the Create Diagnosis page$" )
    public void navigateCreateDiagnosis () {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('createDiagnosis').click();" );
    }

    @Then ( "^The patient can successfully see their diagnosis of (.+)$" )
    public void seeCorrectDiagnosis ( final String correctDiagnosis ) {

        final WebElement visits = driver.findElement( By.name( "11/11/2200" ) );
        visits.click();

        try {
            Thread.sleep( 500 );
        }
        catch ( final InterruptedException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        assertTrue( driver.getPageSource().contains( correctDiagnosis ) );
    }

    @Then ( "^Office visit is documented successfully$" )
    public void documentedSuccessfully () {

        final WebElement message = driver.findElement( By.name( "success" ) );

        assertFalse( message.getText().contains( "Error occurred creating office visit" ) );
    }

    @Then ( "^The diagnosis is successfully added to the system$" )
    public void diagnosisCreationSuccessful () {
        try {
            Thread.sleep( 500 );
            assertTrue( driver.getPageSource().contains( "Successfully updated." ) );

        }
        catch ( final InterruptedException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Then ( "^The diagnosis is not added to the system$" )
    public void diagnosisCreationUnsuccessful () {
        try {
            Thread.sleep( 500 );
            assertTrue( driver.getPageSource().contains( "Error" )
                    || driver.getPageSource().contains( "Please input a name" )
                    || driver.getPageSource().contains( "Please input an idc code" ) );

        }
        catch ( final InterruptedException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
