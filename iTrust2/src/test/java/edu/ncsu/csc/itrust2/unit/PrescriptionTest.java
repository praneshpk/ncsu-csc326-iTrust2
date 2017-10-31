package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.admin.CodeForm;
import edu.ncsu.csc.itrust2.forms.hcp.PrescriptionForm;
import edu.ncsu.csc.itrust2.models.persistent.NDCCode;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;
import edu.ncsu.csc.itrust2.models.persistent.Prescription;

/**
 * Unit tests for the Prescription class
 *
 * @author WillGlas
 */
public class PrescriptionTest {

    /**
     * Create a bunch of NDCCode objects to test the regex in the constructor.
     */
    @Test
    public void testCreateNDCCodes () {
        try {
            new NDCCode( createCodeForm( "1111-1111-11", "Oxicodon" ) );
            new NDCCode( createCodeForm( "12345-678-90", "Adderall" ) );
            new NDCCode( createCodeForm( "12345-6789-0", "Ibuprofen" ) );
            new NDCCode( createCodeForm( "49981-*007-01", "Medical Marijuana" ) );
        }
        catch ( final IllegalArgumentException e ) {
            fail( "Regex failed." );
        }

    }

    /**
     * Tests for renewing a prescription
     *
     * @throws ParseException
     */
    @Test
    public void testPrescriptions () throws ParseException {

        final NDCCode c = new NDCCode( createCodeForm( "1111-1111-11", "Oxicodon" ) );
        c.save();

        final NDCCode persistedC = NDCCode.getByCode( "1111-1111-11" );
        assertEquals( "1111-1111-11", persistedC.getId() );
        assertEquals( "Oxicodon", persistedC.getName() );

        final Long ovId = OfficeVisit.getForPatient( "antti" ).get( 0 ).getId();

        final Prescription p = new Prescription( createPrescriptionForm( "10.1", "10/19/2017", "10/31/2017",
                "1111-1111-11", "antti", "20", ovId + "" ) );
        p.save();
        final Long pid = p.getId();

        final Prescription persistedP = Prescription.getById( pid );
        assertEquals( "10.1", persistedP.getDosage() + "" );
        assertEquals( "1111-1111-11", persistedP.getNdcCode().getId() );
        assertEquals( "antti", persistedP.getPatient().getSelf().getId() );
        assertEquals( "20", persistedP.getRenewals() + "" );
        assertEquals( ovId, persistedP.getOfficeVisit().getId() );

        // Create prescription with non-matching patient and office-visit
        try {
            new Prescription( createPrescriptionForm( "10.1", "10/19/2017", "10/31/2017", "1111-1111-11", "patient",
                    "20", ovId + "" ) );
            fail( "Patient username did not match office visit patient." );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Patient id does not match office visit patient id.", e.getMessage() );
        }

        // Create prescription with invalid dates
        try {
            new Prescription( createPrescriptionForm( "10.1", "10/31/2017", "10/29/2017", "1111-1111-11", "patient",
                    "20", null ) );
            fail( "Start date came after end date and no exception was thrown." );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Start date came after end date.", e.getMessage() );
        }

        // Create prescription with invalid code
        try {
            new Prescription( createPrescriptionForm( "10.1", "10/20/2017", "10/29/2017", "8888-1111-11", "patient",
                    "20", null ) );
            fail( "Invalid NDC was used." );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "NDCCode specified was not in the system.", e.getMessage() );
        }

        // Test renewing
        final Prescription p0 = new Prescription(
                createPrescriptionForm( "10.1", "10/19/2017", "10/31/2019", "1111-1111-11", "antti", "1", null ) );
        assertTrue( p0.renew() );
        assertFalse( p0.renew() );
        p0.save();

        final Prescription persistedP0 = Prescription.getById( p0.getId() );
        assertFalse( persistedP0.renew() );

        final Prescription p1 = new Prescription(
                createPrescriptionForm( "10.1", "10/19/2016", "10/31/2016", "1111-1111-11", "antti", "1", null ) );
        p1.save();
        final Prescription persistedP1 = Prescription.getById( p1.getId() );
        assertFalse( persistedP1.renew() );

        final Prescription p2 = new Prescription(
                createPrescriptionForm( "10.1", "10/19/2200", "10/31/2200", "1111-1111-11", "antti", "1", null ) );
        p2.save();
        final Prescription persistedP2 = Prescription.getById( p2.getId() );
        assertFalse( persistedP2.renew() );

    }

    /**
     * Create and return a CodeForm object
     *
     * @param id
     *            id of object
     * @param name
     *            name of object
     * @return CodeForm
     */
    private CodeForm createCodeForm ( final String id, final String name ) {
        final CodeForm cf = new CodeForm();
        cf.setId( id );
        cf.setName( name );
        return cf;
    }

    /**
     * Create and return a PrescriptionForm
     *
     * @param dosage
     *            dosage
     * @param start
     *            start date
     * @param end
     *            end date
     * @param ndcCode
     *            Id of NDCCode object
     * @param patient
     *            Username of patient
     * @param renewals
     *            number of renewals
     * @param ovId
     *            office visit ID
     * @return Prescription object with given properties
     * @throws ParseException
     */
    private PrescriptionForm createPrescriptionForm ( final String dosage, final String start, final String end,
            final String ndcCode, final String patient, final String renewals, final String ovId )
            throws ParseException {
        final PrescriptionForm pf = new PrescriptionForm();
        pf.setDosage( dosage );
        pf.setStart( start );
        pf.setEnd( end );
        pf.setNdcCode( ndcCode );
        pf.setPatient( patient );
        pf.setRenewals( renewals );
        pf.setOfficeVisitId( ovId );
        return pf;
    }

}
