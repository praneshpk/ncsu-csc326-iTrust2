package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Random;

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
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;
import io.github.bonigarcia.wdm.PhantomJsDriverManager;

@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class DocumentOfficeVisitStepDefs {

    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    private WebDriver             driver;
    private final String          baseUrl      = "http://localhost:8080/iTrust2";

    private final String          hospitalName = "Office Visit Hospital" + ( new Random() ).nextInt();
    BasicHealthMetrics            expectedBhm;

    @Before
    public void setUp () {
        PhantomJsDriverManager.getInstance().setup();
        driver = new PhantomJSDriver();
    }

    @After
    public void teardown () {
        driver.quit();
    }

    @Given ( "The required facilities exist" )
    public void personnelExists () throws Exception {
        // OfficeVisit.deleteAll( OfficeVisit.class );
        // BasicHealthMetrics.deleteAll( BasicHealthMetrics.class );

        // All tests can safely assume the existence of the 'hcp', 'admin', and
        // 'patient' users

        /* Make sure we create a Hospital and Patient record */

        /* Create a Hospital */
        driver.get( baseUrl );
        WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "admin" );
        WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('addhospital').click();" );
        try {
            final WebElement name = driver.findElement( By.id( "name" ) );
            name.clear();
            name.sendKeys( hospitalName );

            final WebElement address = driver.findElement( By.id( "address" ) );
            address.clear();
            address.sendKeys( "Bialystok" );

            final WebElement state = driver.findElement( By.id( "state" ) );
            final Select dropdown = new Select( state );
            dropdown.selectByVisibleText( "NJ" );

            final WebElement zip = driver.findElement( By.id( "zip" ) );
            zip.clear();
            zip.sendKeys( "10101" );
        }
        catch ( final Exception e ) {
            /* Assume the hospital already exists & carry on */
        }
        finally {
            driver.findElement( By.id( "logout" ) ).click();
        }

        /* Create patient record */

        driver.get( baseUrl );
        username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "patient" );
        password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        submit = driver.findElement( By.className( "btn" ) );
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

    @When ( "I log in to iTrust2 as a HCP" )
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

    @When ( "I navigate to the Document Office Visit page" )
    public void navigateDocumentOV () {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('documentOfficeVisit').click();" );
    }

    @When ( "^I fill in information on the office visit$" )
    public void documentOV () {

        final WebElement notes = driver.findElement( By.name( "notes" ) );
        notes.clear();
        notes.sendKeys( "Patient appears pretty much alive" );

        final WebElement patient = driver.findElement( By.id( "patient" ) );
        //final WebElement patient = driver.findElement(By.xpath("//*[text()[contains(.,'AliceThirteen')]]"));
        patient.click();

        final WebElement type = driver.findElement( By.name( "type" ) );
        type.click();

        final WebElement hospital = driver.findElement( By.name( "hospital" ) );
        hospital.click();

        final WebElement date = driver.findElement( By.name( "date" ) );
        date.clear();
        date.sendKeys( "12/19/2027" );

        final WebElement time = driver.findElement( By.name( "time" ) );
        time.clear();
        time.sendKeys( "9:30 AM" );

        final WebElement heightElement = driver.findElement( By.name( "height" ) );
        heightElement.clear();
        heightElement.sendKeys( "120" );

        final WebElement weightElement = driver.findElement( By.name( "weight" ) );
        weightElement.clear();
        weightElement.sendKeys( "120" );

        final WebElement systolicElement = driver.findElement( By.name( "systolic" ) );
        ( (JavascriptExecutor) driver ).executeScript( "arguments[0].value='100';", systolicElement );
        //systolicElement.clear();
        //systolicElement.sendKeys( "100" );

        final WebElement diastolicElement = driver.findElement( By.name( "diastolic" ) );
        ( (JavascriptExecutor) driver ).executeScript( "arguments[0].value='100';", diastolicElement );
        // diastolicElement.clear();
        //diastolicElement.sendKeys( "100" );

        final WebElement hdlElement = driver.findElement( By.name( "hdl" ) );
        if ( hdlElement.isDisplayed() ) {
        	 ( (JavascriptExecutor) driver ).executeScript( "arguments[0].value='90';", hdlElement );
           // hdlElement.clear();
           // hdlElement.sendKeys( "90" );
        }

        final WebElement ldlElement = driver.findElement( By.name( "ldl" ) );
        if ( ldlElement.isDisplayed() ) {
        	( (JavascriptExecutor) driver ).executeScript( "arguments[0].value='100';", ldlElement );
           // ldlElement.clear();
           // ldlElement.sendKeys( "100" );
        }

        final WebElement triElement = driver.findElement( By.name( "tri" ) );
        if ( triElement.isDisplayed() ) {
        	( (JavascriptExecutor) driver ).executeScript( "arguments[0].value='100';", triElement );
           // triElement.clear();
           // triElement.sendKeys( "100" );
        }

        final WebElement houseSmokeElement = driver.findElement(
                By.cssSelector( "input[value=\"" + HouseholdSmokingStatus.NONSMOKING.toString() + "\"]" ) );
        houseSmokeElement.click();

        final WebElement patientSmokeElement = driver
                .findElement( By.cssSelector( "input[value=\"" + PatientSmokingStatus.NEVER.toString() + "\"]" ) );
        if ( patientSmokeElement.isDisplayed() ) {
        	( (JavascriptExecutor) driver ).executeScript( "arguments[0].click();", patientSmokeElement );
            patientSmokeElement.click();
        }

        final WebElement submit = driver.findElement( By.name( "submit" ) );
        submit.click();

    }

    @Then ( "The office visit is documented successfully" )
    public void documentedSuccessfully () {

        final WebElement message = driver.findElement( By.name( "success" ) );

        assertFalse( message.getText().contains( "Error occurred creating office visit" ) );

    }

    /**
     * Ensures that the correct health metrics have been entered
     *
     * @throws InterruptedException
     */
    @Then ( "The basic health metrics for the infant are correct" )
    public void healthMetricsCorrectInfant () throws InterruptedException {
        BasicHealthMetrics actualBhm = null;
        for ( int i = 1; i <= 10; i++ ) {
            try {
                final List<BasicHealthMetrics> list = BasicHealthMetrics.getBasicHealthMetrics();
                actualBhm = list.get( list.size() - 1 );
                break;
            }
            catch ( final Exception e ) {
                if ( i == 10 && actualBhm == null ) {
                    fail( "Could not get basic health metrics out of database" );
                }
                Thread.sleep( 1000 );
            }
        }
        assertEquals( expectedBhm.getWeight(), actualBhm.getWeight() );
        assertEquals( expectedBhm.getHeight(), actualBhm.getHeight() );
        assertEquals( expectedBhm.getHeadCircumference(), actualBhm.getHeadCircumference() );
        assertEquals( expectedBhm.getHouseSmokingStatus(), actualBhm.getHouseSmokingStatus() );
    }

    /**
     * Ensures that the correct health metrics have been entered
     *
     * @throws InterruptedException
     */
    @Then ( "The basic health metrics for the child are correct" )
    public void healthMetricsCorrectChild () throws InterruptedException {
        BasicHealthMetrics actualBhm = null;
        for ( int i = 1; i <= 10; i++ ) {
            try {
                final List<BasicHealthMetrics> list = BasicHealthMetrics.getBasicHealthMetrics();
                actualBhm = list.get( list.size() - 1 );
            }
            catch ( final Exception e ) {
                if ( i == 10 && actualBhm == null ) {
                    fail( "Could not get basic health metrics out of database" );
                }
                Thread.sleep( 1000 );
            }
        }
        assertEquals( expectedBhm.getWeight(), actualBhm.getWeight() );
        assertEquals( expectedBhm.getHeight(), actualBhm.getHeight() );
        assertEquals( expectedBhm.getSystolic(), actualBhm.getSystolic() );
        assertEquals( expectedBhm.getDiastolic(), actualBhm.getDiastolic() );
        assertEquals( expectedBhm.getHouseSmokingStatus(), actualBhm.getHouseSmokingStatus() );
    }

    /**
     * Ensures that the correct health metrics have been entered
     *
     * @throws InterruptedException
     */
    @Then ( "The basic health metrics for the adult are correct" )
    public void healthMetricsCorrectAdult () throws InterruptedException {
        BasicHealthMetrics actualBhm = null;
        for ( int i = 1; i <= 10; i++ ) {
            try {
                final List<BasicHealthMetrics> list = BasicHealthMetrics.getBasicHealthMetrics();
                actualBhm = list.get( list.size() - 1 );
            }
            catch ( final Exception e ) {
                if ( i == 10 && actualBhm == null ) {
                    fail( "Could not get basic health metrics out of database" );
                }
                Thread.sleep( 1000 );
            }
        }
        assertEquals( expectedBhm.getWeight(), actualBhm.getWeight() );
        assertEquals( expectedBhm.getHeight(), actualBhm.getHeight() );
        assertEquals( expectedBhm.getSystolic(), actualBhm.getSystolic() );
        assertEquals( expectedBhm.getDiastolic(), actualBhm.getDiastolic() );
        assertEquals( expectedBhm.getHouseSmokingStatus(), actualBhm.getHouseSmokingStatus() );
        assertEquals( expectedBhm.getPatientSmokingStatus(), actualBhm.getPatientSmokingStatus() );
        assertEquals( expectedBhm.getHdl(), actualBhm.getHdl() );
        assertEquals( expectedBhm.getLdl(), actualBhm.getLdl() );
        assertEquals( expectedBhm.getTri(), actualBhm.getTri() );
    }

    /**
     * Ensures that the office visit was not recorded.
     */
    @Then ( "The office visit is not documented" )
    public void notDocumented () {

        final WebElement message = driver.findElement( By.name( "success" ) );

        assertTrue( message.getText().contains( "Error occurred creating office visit" ) );
    }

    /**
     * Ensures that a patient exists with the given name and birthday
     *
     * @param name
     *            The name of the patient.
     * @param birthday
     *            The birthday of the patient.
     */
    @Given ( "^A patient exists with name: (.+) and date of birth: (.+)$" )
    public void patientExistsWithName ( final String name, final String birthday ) {
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
            firstName.sendKeys( name.split( " " )[0] );

            final WebElement lastName = driver.findElement( By.id( "lastName" ) );
            lastName.clear();
            lastName.sendKeys( name.split( " " )[1] );

            final WebElement preferredName = driver.findElement( By.id( "preferredName" ) );
            preferredName.clear();

            final WebElement mother = driver.findElement( By.id( "mother" ) );
            mother.clear();

            final WebElement father = driver.findElement( By.id( "father" ) );
            father.clear();

            final WebElement email = driver.findElement( By.id( "email" ) );
            email.clear();
            email.sendKeys( "email@mail.com" );

            final WebElement address1 = driver.findElement( By.id( "address1" ) );
            address1.clear();
            address1.sendKeys( "address place, address" );

            final WebElement city = driver.findElement( By.id( "city" ) );
            city.clear();
            city.sendKeys( "citytown" );

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
            dob.sendKeys( birthday );

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

    /**
     * Documents an office visit with specific information.
     *
     * @param dateString
     *            The current date.
     * @param weightString
     *            The weight of the patient.
     * @param lengthString
     *            The length of the patient.
     * @param headString
     *            The head circumference of the patient.
     * @param smokingStatus
     *            The smoking status of the patient's household.
     * @param note
     *            The note that the doctor includes.
     * @throws InterruptedException
     */
    @When ( "^I fill in information on the office visit for an infant with date: (.+), weight: (.+), length: (.+), head circumference: (.+), household smoking status: (.+), and note: (.+)$" )
    public void documentOVWithSpecificInformation ( final String dateString, final String weightString,
            final String lengthString, final String headString, final String smokingStatus, final String note )
            throws InterruptedException {

        final WebElement notes = driver.findElement( By.name( "notes" ) );
        notes.clear();
        notes.sendKeys( note );

        final WebElement patient = driver.findElement( By.cssSelector( "input[value=\"patient\"]" ) );
        patient.click();

        final WebElement type = driver.findElement( By.name( "type" ) );
        type.click();

        final WebElement hospital = driver.findElement( By.name( "hospital" ) );
        hospital.click();

        final WebElement date = driver.findElement( By.name( "date" ) );
        date.clear();
        date.sendKeys( dateString );
        date.click();

        final WebElement time = driver.findElement( By.name( "time" ) );
        time.clear();
        time.sendKeys( "9:30 AM" );

        expectedBhm = new BasicHealthMetrics();

        final WebElement head = driver.findElement( By.name( "head" ) );
        ( (JavascriptExecutor) driver ).executeScript( "arguments[0].value='" + headString + "';", head);
       // head.clear();
       // head.sendKeys( headString );
        try {
            expectedBhm.setHeadCircumference( Float.parseFloat( headString ) );
        }
        catch ( final IllegalArgumentException e ) {
            /*
             * This means that the test data provided was intentionally invalid,
             * which is okay
             */
        }

        final WebElement heightLength = driver.findElement( By.name( "height" ) );
        heightLength.clear();
        heightLength.sendKeys( lengthString );
        try {
            expectedBhm.setHeight( Float.parseFloat( lengthString ) );
        }
        catch ( final IllegalArgumentException e ) {
            /*
             * This means that the test data provided was intentionally invalid,
             * which is okay
             */
        }

        final WebElement weight = driver.findElement( By.name( "weight" ) );
        weight.clear();
        weight.sendKeys( weightString );
        try {
            expectedBhm.setWeight( Float.parseFloat( weightString ) );
        }
        catch ( final IllegalArgumentException e ) {
            /*
             * This means that the test data provided was intentionally invalid,
             * which is okay
             */
        }
        try {
            final WebElement smoking = driver.findElement( By.cssSelector(
                    "input[value=\"" + HouseholdSmokingStatus.getName( Integer.parseInt( smokingStatus ) ) + "\"]" ) );
            smoking.click();
            expectedBhm.setHouseSmokingStatus( HouseholdSmokingStatus.parseValue( Integer.parseInt( smokingStatus ) ) );
        }
        catch ( final Exception e ) {
            /*
             * This means that the element wasn't found, which is expected if we
             * enter an invalid value (as one of the test cases does).
             * Intentionally ignoring.
             */
        }

        final WebElement submit = driver.findElement( By.name( "submit" ) );
        submit.click();
        // Give the data time to save to the database
        Thread.sleep( 2000 );
    }

    /**
     * Documents an office visit with specific information for patients between
     * 3 and 12
     *
     * @param dateString
     *            The current date.
     * @param weightString
     *            The weight of the patient.
     * @param heightString
     *            The height of the patient.
     * @param sys
     *            The systolic blood pressure of the patient.
     * @param dia
     *            The diastolic blood pressure of the patient.
     * @param smokingStatus
     *            The smoking status of the patient's household.
     * @param note
     *            The note that the doctor includes.
     * @throws InterruptedException
     */
    @When ( "^I fill in information on the office visit for patients of age 3 to 12 with date: (.+), weight: (.+), height: (.+), systolic blood pressure: (.+), diastolic blood pressure: (.+), household smoking status: (.+), and note: (.+)$" )
    public void documentOVWithSpecificInformation3To12 ( final String dateString, final String weightString,
            final String heightString, final String sys, final String dia, final String smokingStatus,
            final String note ) throws InterruptedException {

        final WebElement notes = driver.findElement( By.name( "notes" ) );
        notes.clear();
        notes.sendKeys( note );

        final WebElement patient = driver.findElement( By.cssSelector( "input[value=\"patient\"]" ) );
        patient.click();

        final WebElement type = driver.findElement( By.name( "type" ) );
        type.click();

        final WebElement hospital = driver.findElement( By.name( "hospital" ) );
        hospital.click();

        final WebElement date = driver.findElement( By.name( "date" ) );
        date.clear();
        date.sendKeys( dateString );
        date.click();

        final WebElement time = driver.findElement( By.name( "time" ) );
        time.clear();
        time.sendKeys( "9:30 AM" );

        expectedBhm = new BasicHealthMetrics();

        final WebElement sysElem = driver.findElement( By.name( "systolic" ) );
        ( (JavascriptExecutor) driver ).executeScript( "arguments[0].value='" + sys + "';", sysElem );
       // sysElem.clear();
      //  sysElem.sendKeys( sys );
        try {
            expectedBhm.setSystolic( Integer.parseInt( sys ) );
        }
        catch ( final IllegalArgumentException e ) {
            /*
             * This means that the test data provided was intentionally invalid,
             * which is okay
             */
        }

        final WebElement diaElem = driver.findElement( By.name( "diastolic" ) );
        ( (JavascriptExecutor) driver ).executeScript( "arguments[0].value='" + dia + "';", diaElem );
       // diaElem.clear();
       // diaElem.sendKeys( dia );
        try {
            expectedBhm.setDiastolic( Integer.parseInt( dia ) );
        }
        catch ( final IllegalArgumentException e ) {
            /*
             * This means that the test data provided was intentionally invalid,
             * which is okay
             */
        }

        final WebElement heightLength = driver.findElement( By.name( "height" ) );
        heightLength.clear();
        heightLength.sendKeys( heightString );
        try {
            expectedBhm.setHeight( Float.parseFloat( heightString ) );
        }
        catch ( final IllegalArgumentException e ) {
            /*
             * This means that the test data provided was intentionally invalid,
             * which is okay
             */
        }

        final WebElement weight = driver.findElement( By.name( "weight" ) );
        weight.clear();
        weight.sendKeys( weightString );
        try {
            expectedBhm.setWeight( Float.parseFloat( weightString ) );
        }
        catch ( final IllegalArgumentException e ) {
            /*
             * This means that the test data provided was intentionally invalid,
             * which is okay
             */
        }
        try {
            final WebElement smoking = driver.findElement( By.cssSelector(
                    "input[value=\"" + HouseholdSmokingStatus.getName( Integer.parseInt( smokingStatus ) ) + "\"]" ) );
            smoking.click();
            expectedBhm.setHouseSmokingStatus( HouseholdSmokingStatus.parseValue( Integer.parseInt( smokingStatus ) ) );
        }
        catch ( final Exception e ) {
            /*
             * This means that the element wasn't found, which is expected if we
             * enter an invalid value (as one of the test cases does).
             * Intentionally ignoring.
             */
        }

        final WebElement submit = driver.findElement( By.name( "submit" ) );
        submit.click();
        // Give the data time to save to the database
        Thread.sleep( 2000 );
    }

    /**
     * Documents an office visit with specific information for patients 12 and
     * over.
     *
     * @param dateString
     *            The current date.
     * @param weightString
     *            The weight of the patient.
     * @param heightString
     *            The height of the patient.
     * @param sys
     *            The systolic blood pressure of the patient.
     * @param dia
     *            The diastolic blood pressure of the patient.
     * @param houseSmoke
     *            The smoking status of the patient's household.
     * @param patientSmoke
     *            The smoking status of the patient.
     * @param hdl
     *            The patient's HDL levels.
     * @param ldl
     *            The patient's LDL levels.
     * @param tri
     *            The patient's triglycerides levels.
     * @param note
     *            The note entered by the HCP
     * @throws InterruptedException
     */
    @When ( "^I fill in information on the office visit for people 12 and over with date: (.+), weight: (.+), height: (.+), systolic blood pressure: (.+), diastolic blood pressure: (.+), household smoking status: (.+), patient smoking status: (.+), HDL cholesterol: (.+), LDL cholesterol: (.+), triglycerides: (.+), and note: (.+)$" )
    public void documentOVWithSpecificInformation12Over ( final String dateString, final String weightString,
            final String heightString, final String sys, final String dia, final String houseSmoke,
            final String patientSmoke, final String hdl, final String ldl, final String tri, final String note )
            throws InterruptedException {

        final WebElement notes = driver.findElement( By.name( "notes" ) );
        notes.clear();
        notes.sendKeys( note );

        final WebElement patient = driver.findElement( By.cssSelector( "input[value=\"patient\"]" ) );
        patient.click();

        final WebElement type = driver.findElement( By.name( "type" ) );
        type.click();

        final WebElement hospital = driver.findElement( By.name( "hospital" ) );
        hospital.click();

        final WebElement date = driver.findElement( By.name( "date" ) );
        date.clear();
        date.sendKeys( dateString );
        date.click();

        final WebElement time = driver.findElement( By.name( "time" ) );
        time.clear();
        time.sendKeys( "9:30 AM" );

        expectedBhm = new BasicHealthMetrics();

        final WebElement sysElem = driver.findElement( By.name( "systolic" ) );
        ( (JavascriptExecutor) driver ).executeScript( "arguments[0].value='" + sys + "';", sysElem );
       // sysElem.clear();
       // sysElem.sendKeys( sys );
        try {
            expectedBhm.setSystolic( Integer.parseInt( sys ) );
        }
        catch ( final IllegalArgumentException e ) {
            /*
             * This means that the test data provided was intentionally invalid,
             * which is okay
             */
        }

        final WebElement diaElem = driver.findElement( By.name( "diastolic" ) );
        ( (JavascriptExecutor) driver ).executeScript( "arguments[0].value='" + dia + "';", diaElem );
       // diaElem.clear();
       // diaElem.sendKeys( dia );
        try {
            expectedBhm.setDiastolic( Integer.parseInt( dia ) );
        }
        catch ( final IllegalArgumentException e ) {
            /*
             * This means that the test data provided was intentionally invalid,
             * which is okay
             */
        }

        final WebElement heightLength = driver.findElement( By.name( "height" ) );
        heightLength.clear();
        heightLength.sendKeys( heightString );
        try {
            expectedBhm.setHeight( Float.parseFloat( heightString ) );
        }
        catch ( final IllegalArgumentException e ) {
            /*
             * This means that the test data provided was intentionally invalid,
             * which is okay
             */
        }

        final WebElement weight = driver.findElement( By.name( "weight" ) );
        weight.clear();
        weight.sendKeys( weightString );
        try {
            expectedBhm.setWeight( Float.parseFloat( weightString ) );
        }
        catch ( final IllegalArgumentException e ) {
            /*
             * This means that the test data provided was intentionally invalid,
             * which is okay
             */
        }
        try {
            final WebElement smoking = driver.findElement( By.cssSelector(
                    "input[value=\"" + HouseholdSmokingStatus.getName( Integer.parseInt( houseSmoke ) ) + "\"]" ) );
            smoking.click();
            expectedBhm.setHouseSmokingStatus( HouseholdSmokingStatus.parseValue( Integer.parseInt( houseSmoke ) ) );
        }
        catch ( final Exception e ) {
            /*
             * This means that the element wasn't found, which is expected if we
             * enter an invalid value (as one of the test cases does).
             * Intentionally ignoring.
             */
        }
        try {
            final WebElement smoking = driver.findElement( By.cssSelector(
                    "input[value=\"" + PatientSmokingStatus.getName( Integer.parseInt( patientSmoke ) ) + "\"]" ) );
            //smoking.click();
            ( (JavascriptExecutor) driver ).executeScript( "arguments[0].click();", smoking );
            expectedBhm.setPatientSmokingStatus( PatientSmokingStatus.parseValue( Integer.parseInt( patientSmoke ) ) );
        }
        catch ( final Exception e ) {
            /*
             * This means that the element wasn't found, which is expected if we
             * enter an invalid value (as one of the test cases does).
             * Intentionally ignoring.
             */
        }

        final WebElement hdlElem = driver.findElement( By.name( "hdl" ) );
        ( (JavascriptExecutor) driver ).executeScript( "arguments[0].value='" + hdl + "';", hdlElem );
        //hdlElem.clear();
       // hdlElem.sendKeys( hdl );
        try {
            expectedBhm.setHdl( Integer.parseInt( hdl ) );
        }
        catch ( final IllegalArgumentException e ) {
            /*
             * This means that the test data provided was intentionally invalid,
             * which is okay
             */
        }

        final WebElement ldlElem = driver.findElement( By.name( "ldl" ) );
        ( (JavascriptExecutor) driver ).executeScript( "arguments[0].value='" + ldl + "';", ldlElem );
        //ldlElem.clear();
       // ldlElem.sendKeys( ldl );
        try {
            expectedBhm.setLdl( Integer.parseInt( ldl ) );
        }
        catch ( final IllegalArgumentException e ) {
            /*
             * This means that the test data provided was intentionally invalid,
             * which is okay
             */
        }

        final WebElement triElem = driver.findElement( By.name( "tri" ) );
        ( (JavascriptExecutor) driver ).executeScript( "arguments[0].value='" + tri + "';", triElem );
        //triElem.clear();
        //triElem.sendKeys( tri );
        try {
            expectedBhm.setTri( Integer.parseInt( tri ) );
        }
        catch ( final IllegalArgumentException e ) {
            /*
             * This means that the test data provided was intentionally invalid,
             * which is okay
             */
        }

        final WebElement submit = driver.findElement( By.name( "submit" ) );
        submit.click();
        // Give the data time to save to the database
        Thread.sleep( 2000 );
    }
}
