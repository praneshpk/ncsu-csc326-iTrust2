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
     * Create a password reset token and return it
     *
     * @param email
     *            email of the user (possible that this field hasn't been
     *            entered)
     *
     * @return a PasswordResetToken
     */
    public static PasswordResetToken createToken ( final String email ) {
        User user = null;
        final List<Patient> patients = Patient.getWhere( "email = '" + email + "'" );
        if ( !patients.isEmpty() ) {
            user = patients.get( 0 ).getSelf();
        }
        else {
            final List<Personnel> personnel = Personnel.getWhere( "email = '" + email + "'" );
            user = personnel.isEmpty() ? null : personnel.get( 0 ).getSelf();
        }
        final String uuid = UUID.randomUUID().toString();
        final Calendar exp = Calendar.getInstance();
        exp.add( Calendar.MINUTE, ALLOWED_MINS );
        return user == null ? null : new PasswordResetToken( user, uuid, exp );
    }

    /**
     * Private constructor for reset tokens
     *
     * @param user
     *            user requesting reset
     * @param token
     *            uuid
     * @param expiration
     *            expiration time of token
     */
    private PasswordResetToken ( final User user, final String token, final Calendar expiration ) {
        this.user = user;
        this.token = token;
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
