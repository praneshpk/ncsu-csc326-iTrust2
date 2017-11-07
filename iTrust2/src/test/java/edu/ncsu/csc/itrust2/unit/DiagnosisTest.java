package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
    public void testDiagnosis () {
        Diagnosis d = null;
        assertNull( d );
        d = new Diagnosis( "name", "H01.1" );
        assertNotNull( d );
        assertEquals( "name", d.getName() );
        assertEquals( "H01.1", d.getIcdCode() );
        d.setName( "changed" );
        assertEquals( "changed", d.getName() );
        d.setIcdCode( "H02.1" );
        assertEquals( "H02.1", d.getIcdCode() );

        final DiagnosisForm df = new DiagnosisForm();
        df.setName( "name" );
        df.setIcdCode( "H01.1" );
        final Diagnosis d1 = new Diagnosis( df );
        assertEquals( "name", d1.getName() );
        assertEquals( "H01.1", d1.getIcdCode() );
        assertEquals( "H01.1", d1.getId() );
    }
}
