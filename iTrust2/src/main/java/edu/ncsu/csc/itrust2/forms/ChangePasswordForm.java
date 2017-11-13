package edu.ncsu.csc.itrust2.forms;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * ChangePasswordForm for users to change password
 *
 * @author WillGlas
 */
public class ChangePasswordForm {

    /**
     * Old password
     */
    @NotEmpty
    @Length ( min = 6, max = 20 )
    private String oldPassword;

    /**
     * Password to set
     */
    @NotEmpty
    @Length ( min = 6, max = 20 )
    private String newPassword;

    /**
     * Get the oldPassword
     *
     * @return the oldPassword
     */
    public String getOldPassword () {
        return oldPassword;
    }

    /**
     * Set the password
     *
     * @param password
     *            the password to set
     */
    public void setOldPassword ( final String password ) {
        this.oldPassword = password;
    }

    /**
     * Get the newPassword field
     *
     * @return the newPassword
     */
    public String getNewPassword () {
        return newPassword;
    }

    /**
     * Set the newPassword field
     *
     * @param newPassword
     *            the newPassword to set
     */
    public void setNewPassword ( final String newPassword ) {
        this.newPassword = newPassword;
    }
}
