package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Calendar;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.hcp.OfficeVisitForm;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.PatientSmokingStatus;
import edu.ncsu.csc.itrust2.models.persistent.BasicHealthMetrics;
import edu.ncsu.csc.itrust2.models.persistent.Diagnosis;
import edu.ncsu.csc.itrust2.models.persistent.Hospital;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;
import edu.ncsu.csc.itrust2.models.persistent.Patient;

/**
 * Tests the OfficeVisit functionality
 *
 * @author Cameron Estroff
 *
 */
public class OfficeVisitTest {

    /**
     * Tests each of the getters and setters in the OfficeVisit.java class
     */
    @Test
    public void testGettersSetters () {
        final OfficeVisit of = new OfficeVisit();
        of.setDate( Calendar.getInstance() );
        assertNotNull( of.getDate() );
        of.setNotes( "Test notes" );
        assertEquals( "Test notes", of.getNotes() );
        final Hospital h = Hospital.getHospitals().get( 0 );
        if ( h != null ) {
            of.setHospital( h );
            assertEquals( h, of.getHospital() );
        }
        final Patient p = Patient.getPatients().get( 0 );
        if ( p != null ) {
            of.setPatient( p.getSelf() );
            assertEquals( p.getSelf(), of.getPatient() );
        }
        of.setType( AppointmentType.GENERAL_CHECKUP );
        assertEquals( AppointmentType.GENERAL_CHECKUP, of.getType() );
        final OfficeVisitForm form = new OfficeVisitForm();
        form.setHeadCircumference( new Float( 10 ) );
        form.setDiastolic( 10 );
        form.setHdl( 10 );
        form.setHeight( new Float( 10 ) );
        form.setHouseSmokingStatus( HouseholdSmokingStatus.INDOOR );
        form.setHeadCircumference( new Float( 10 ) );
        form.setLdl( 10 );
        form.setPatientSmokingStatus( PatientSmokingStatus.CURRENT_BUT_UNKNOWN );
        form.setSystolic( 10 );
        form.setTri( 150 );
        form.setWeight( new Float( 10 ) );

        BasicHealthMetrics bhm = null;
        try {
            bhm = new BasicHealthMetrics( form );
        }
        catch ( final ParseException e ) {
            fail();
        }
        of.setBasicHealthMetrics( bhm );
        assertEquals( bhm, of.getBasicHealthMetrics() );

        Diagnosis d = Diagnosis.getDiagnoses().get( 0 );
        // If no diagnosis exist in database then skip
        if ( d == null ) {
            final Diagnosis dCreated = new Diagnosis( "testName", "H101.1" );
            dCreated.save();
            d = Diagnosis.getDiagnoses().get( 0 );
        }
        if ( d != null ) {
            final String diagnosisName = d.getName();
            of.setDiagnosis( d );
            assertEquals( d, of.getDiagnosis() );
            form.setDiagnosis( diagnosisName );
            assertEquals( d.getName(), diagnosisName );
            // Test getting from db by the name
            final Diagnosis dFinal = Diagnosis.getByName( diagnosisName );
            if ( dFinal == null ) {
                fail();
            }

        }
    }

}
