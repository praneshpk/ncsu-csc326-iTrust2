package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ncsu.csc.itrust2.models.persistent.PasswordResetToken;

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
        final PasswordResetToken t = PasswordResetToken.createToken( "antti@itrust.fi" );
        assertEquals( t.getUser().getId(), "antti" );

        final PasswordResetToken t1 = PasswordResetToken.createToken( "karl_liebknecht@mail.de" );
        assertEquals( t1.getUser().getId(), "patient" );

        // Save a token and get it from the database
        t.save();
        final PasswordResetToken persistedT = PasswordResetToken.getById( t.getId() );
        assertEquals( persistedT.getUser().getId(), "antti" );
    }
}
