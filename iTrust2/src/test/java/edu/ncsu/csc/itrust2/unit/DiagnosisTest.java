package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.admin.DiagnosisForm;
import edu.ncsu.csc.itrust2.models.persistent.Diagnosis;

/**
 * Tests the Diagnosis class
 *
 * @author Cameron Estroff
 *
 */
public class DiagnosisTest {

    /**
     * Tests the constructors and getters and setters
     */
    @Test
    public void testValidDiagnosis () {
        Diagnosis d = null;
        assertNull( d );
        d = new Diagnosis( "name", "H40.0" );
        assertNotNull( d );
        assertEquals( "name", d.getName() );
        assertEquals( "H40.0", d.getIcdCode() );
        d.setName( "changed" );
        assertEquals( "changed", d.getName() );
        d.setIcdCode( "H40.021" );
        assertEquals( "H40.021", d.getIcdCode() );

        final DiagnosisForm df = new DiagnosisForm();
        df.setName( "name" );
        df.setIcdCode( "H40.0" );
        final Diagnosis d1 = new Diagnosis( df );
        assertEquals( "name", d1.getName() );
        assertEquals( "H40.0", d1.getIcdCode() );
        assertEquals( "H40.0", d1.getId() );
    }

    /**
     * Tests the constructors and getters and setters with invalid data
     */
    @Test
    public void testInvalidDiagnosis () {
        Diagnosis d = null;
        assertNull( d );
        try {
            d = new Diagnosis( "name", "HHHHH" );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertNull( d );
        }

        final DiagnosisForm df = new DiagnosisForm();
        df.setName( "name" );
        df.setIcdCode( "HHHHH" );
        Diagnosis d1 = null;
        try {
            d1 = new Diagnosis( df );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertNull( d1 );
        }
    }
}
