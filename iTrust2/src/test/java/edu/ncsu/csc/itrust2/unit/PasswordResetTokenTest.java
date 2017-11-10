package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import edu.ncsu.csc.itrust2.models.persistent.PasswordResetToken;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Test class for PasswordResetToken
 *
 * @author WillGlas
 */
public class PasswordResetTokenTest {

    /**
     * Test the createPasswordResetToken method
     */
    @Test
    public void testCreatePasswordResetToken () {
        // Test the correct user is found for various roles
        final String t = PasswordResetToken.generateToken( User.getByName( "antti" ) );
        assertNotNull( t );

        final String t1 = PasswordResetToken.generateToken( User.getByName( "patient" ) );
        assertNotNull( t1 );
    }
}
