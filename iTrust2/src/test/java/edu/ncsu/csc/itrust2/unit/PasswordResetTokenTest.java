package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        final User user = User.getByName( "patient" );
        final String t = PasswordResetToken.generateToken( user );
        assertNotNull( t );

        assertTrue( PasswordResetToken.validateToken( t, "patient" ) );
    }
}
