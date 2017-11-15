package edu.ncsu.csc.itrust2.models.persistent;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import edu.ncsu.csc.itrust2.utils.DomainObjectCache;

/**
 * PasswordResetToken class to allow users to reset their password if they
 * forget it
 *
 * @author WillGlas
 */
@Entity
@Table ( name = "PasswordResetTokens" )
public class PasswordResetToken extends DomainObject<PasswordResetToken> {

    private static final int                                   ALLOWED_MINS = 120;

    /**
     * Id of the PasswordResetToken
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long                                               id;

    /**
     * UUID
     */
    @NotNull
    private String                                             token;

    /**
     * User who requested password reset
     */
    @NotNull
    @OneToOne
    @JoinColumn ( name = "self_id" )
    private User                                               user;

    /**
     * Expiration time of token
     */
    @NotNull
    private Calendar                                           expiration;

    /**
     * The cache representation of the patients in the database
     */
    static private DomainObjectCache<Long, PasswordResetToken> cache        = new DomainObjectCache<Long, PasswordResetToken>(
            PasswordResetToken.class );

    /**
     * Get a specific office visit by the database ID
     *
     * @param id
     *            the database ID
     * @return the specific office visit with the desired ID
     */
    public static PasswordResetToken getById ( final Long id ) {
        PasswordResetToken token = cache.get( id );
        if ( null == token ) {
            try {
                token = getWhere( "id = '" + id + "'" ).get( 0 );
                cache.put( id, token );
            }
            catch ( final Exception e ) {
                // Exception ignored
            }
        }
        return token;
    }

    /**
     * Helper method to pass to the DomainObject class that performs a specific
     * query on the database.
     *
     * @SuppressWarnings for Unchecked cast from List<capture#1-of ? extends
     *                   DomainObject> to List<PasswordResetToken> Because get
     *                   all just returns a list of DomainObjects, the cast is
     *                   okay.
     *
     * @param where
     *            The specific query on the database
     * @return the result of the query
     */
    @SuppressWarnings ( "unchecked" )
    public static List<PasswordResetToken> getWhere ( final String where ) {
        return (List<PasswordResetToken>) getWhere( PasswordResetToken.class, where );
    }

    /**
     * Create a password reset token and return the UUID
     *
     * @param user
     *            user to create reset token for
     *
     * @return the token String
     */
    public static String generateToken ( final User user ) {
        if ( user == null ) {
            throw new IllegalArgumentException( "User cannot be null." );
        }
        final String uuid = UUID.randomUUID().toString();
        final Calendar exp = Calendar.getInstance();
        exp.add( Calendar.MINUTE, ALLOWED_MINS );
        final PasswordResetToken token = new PasswordResetToken( user, uuid, exp );
        token.save();
        return uuid;
    }

    /**
     * Validate a token
     *
     * @param token
     *            token to validate
     * @param username
     *            user to validate
     * @return whether or not the token was validated
     */
    public static boolean validateToken ( final String token, final String username ) {
        final PasswordEncoder pe = new BCryptPasswordEncoder();
        final List<PasswordResetToken> tokens = getWhere( "self_id = '" + username + "'" );
        if ( !tokens.isEmpty() ) {
            for ( int i = tokens.size() - 1; i >= 0; i-- ) {
                final PasswordResetToken current = tokens.get( i );
                if ( pe.matches( token, current.getToken() )
                        && current.getExpiration().after( Calendar.getInstance() ) ) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Empty constructor
     */
    public PasswordResetToken () {
    }

    /**
     * constructor for reset tokens
     *
     * @param user
     *            user requesting reset
     * @param token
     *            uuid
     * @param expiration
     *            expiration time of token
     */
    public PasswordResetToken ( final User user, final String token, final Calendar expiration ) {
        this.user = user;
        final PasswordEncoder pe = new BCryptPasswordEncoder();
        this.token = pe.encode( token );
        this.expiration = expiration;
    }

    /**
     * Return the id
     *
     * @return id
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the id
     *
     * @param id
     *            the id to set
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Get the token
     *
     * @return the token
     */
    public String getToken () {
        return token;
    }

    /**
     * Set the token
     *
     * @param token
     *            the token to set
     */
    public void setToken ( final String token ) {
        this.token = token;
    }

    /**
     * Get the user
     *
     * @return the user
     */
    public User getUser () {
        return user;
    }

    /**
     * Set the user
     *
     * @param user
     *            the user to set
     */
    public void setUser ( final User user ) {
        this.user = user;
    }

    /**
     * Get the expiration time
     *
     * @return the expiration
     */
    public Calendar getExpiration () {
        return expiration;
    }

    /**
     * Set the expiration time
     *
     * @param expiration
     *            the expiration to set
     */
    public void setExpiration ( final Calendar expiration ) {
        this.expiration = expiration;
    }

}
