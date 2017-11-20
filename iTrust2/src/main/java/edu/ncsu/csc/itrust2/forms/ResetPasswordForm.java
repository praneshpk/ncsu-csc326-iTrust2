package edu.ncsu.csc.itrust2.forms;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Class to facilitate message from angular webpage to controller method
 *
 * @author WillGlas
 *
 */
public class ResetPasswordForm {

    /**
     * Password to set
     */
    @NotEmpty
    @Length ( min = 6, max = 20 )
    private String password;

    /**
     * Get the password
     *
     * @return the password
     */
    public String getPassword () {
        return password;
    }

    /**
     * Set the password
     *
     * @param password
     *            the password to set
     */
    public void setPassword ( final String password ) {
        this.password = password;
    }
}
