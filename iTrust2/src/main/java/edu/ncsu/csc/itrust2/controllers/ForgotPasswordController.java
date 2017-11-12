package edu.ncsu.csc.itrust2.controllers;

import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import edu.ncsu.csc.itrust2.forms.PasswordResetForm;
import edu.ncsu.csc.itrust2.models.persistent.PasswordResetToken;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.Personnel;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Controller for Forgot Password
 *
 * @author Pranesh Kamalakanthan
 *
 */
@Controller
@SuppressWarnings ( { "rawtypes", "unchecked" } )
public class ForgotPasswordController {
    /**
     * Returns the forgot my password page
     *
     * @param model
     *            Data from the front end
     * @return Page to display to the user
     */
    @GetMapping ( "/forgotPassword" )
    public String forgotPassword ( final Model model ) {
        return "/forgotPassword";
    }

    /**
     * Returns the change my password page
     *
     * @param model
     *            Data from the front end
     * @return Page to display to the user
     */
    @GetMapping ( "/changePassword" )
    public String changePassword ( final Model model ) {
        return "/changePassword";
    }

    /**
     * Send a password reset email to the specified email
     *
     * @param request
     *            information about the http request
     * @param pForm
     *            contains email to recover
     * @return a response entity detailing success of operation
     */
    @PostMapping ( "/passwordrecovery" )
    public ResponseEntity passwordRecovery ( final HttpServletRequest request,
            @Valid @RequestBody final PasswordResetForm pForm ) {
        final String email = pForm.getEmail();
        final User user = getUserByEmail( email );
        if ( user == null ) {
            return new ResponseEntity( "No user exists for the given email.", HttpStatus.BAD_REQUEST );
        }
        final String token = PasswordResetToken.generateToken( user );

        try {
            final Session session = getGmailSession();
            final MimeMessage msg = createEmail( session, user, token, email );
            Transport.send( msg );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( "Email failed to send: " + e.getMessage(), HttpStatus.CONFLICT );
        }

        return new ResponseEntity( "Email sent.", HttpStatus.OK );
    }

    /**
     * Create a password reset email ready to be sent
     *
     * @param session
     *            session object containing properties for sending
     * @param user
     *            user object
     * @param token
     *            reset token
     * @param request
     *            request containing information about the post request
     * @param recipientEmail
     *            email of the intended recipient
     * @return email ready to be sent
     * @throws AddressException
     * @throws MessagingException
     */
    private MimeMessage createEmail ( final Session session, final User user, final String token,
            final String recipientEmail ) throws AddressException, MessagingException {
        // ideally the context path shouldn't be hardcoded
        final String link = "http://localhost:8080/iTrust2/changePassword?id=" + user.getId() + "&token=" + token;
        final MimeMessage msg = new MimeMessage( session );
        msg.setFrom( new InternetAddress( "iTrustSupTeam@gmail.com" ) );
        msg.addRecipient( RecipientType.TO, new InternetAddress( recipientEmail ) );
        msg.setSubject( "iTrust2 Password Reset" );
        msg.setText( "A password reset was requested from your Account. "
                + "If you requested this password reset, please click the following link: " + link );
        return msg;
    }

    /**
     * Create a session with a gmail server
     *
     * @return the session
     */
    private Session getGmailSession () {
        final Properties props = new Properties();
        props.put( "mail.smtp.host", "smtp.gmail.com" );
        props.put( "mail.smtp.socketFactory.port", "465" );
        props.put( "mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory" );
        props.put( "mail.smtp.auth", "true" );
        props.put( "mail.smtp.port", "805" );

        return Session.getDefaultInstance( props, new Authenticator() {
            /**
             * Authentication information for gmail server
             */
            @Override
            protected PasswordAuthentication getPasswordAuthentication () {
                return new PasswordAuthentication( "iTrustSupTeam@gmail.com", "team1rocks" );
            }
        } );
    }

    /**
     * Get a user by email (this is made more complicated by users not having
     * emails)
     *
     * @param email
     *            email to search for
     * @return User object
     */
    private User getUserByEmail ( final String email ) {
        User user = null;
        final List<Patient> patients = Patient.getWhere( "email = '" + email + "'" );
        if ( !patients.isEmpty() ) {
            user = patients.get( 0 ).getSelf();
        }
        else {
            final List<Personnel> personnel = Personnel.getWhere( "email = '" + email + "'" );
            if ( !personnel.isEmpty() ) {
                user = personnel.get( 0 ).getSelf();
            }
        }
        return user;
    }
}
