package edu.ncsu.csc.itrust2.forms;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Form for passing an email to the password recovery endpoint
 *
 * @author WillGlas
 */
public class EmailForm {

    /**
     * Empty constructor
     */
    public EmailForm () {
    }

    /**
     * Email of an existing user
     */
    @NotEmpty
    private String email;

    /**
     * Get the email
     * 
     * @return the email
     */
    public String getEmail () {
        return email;
    }

    /**
     * Set the email
     * 
     * @param email
     *            the email to set
     */
    public void setEmail ( final String email ) {
        this.email = email;
    }

}
