package edu.ncsu.csc.itrust2.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for Forgot Password
 *
 * @author Pranesh Kamalakanthan
 *
 */
@Controller
public class ForgotPassword {
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
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_ADMIN', 'ROLE_PATIENT')" )
    public String changePassword ( final Model model ) {
        return "/changePassword";
    }

    /**
     * Returns the change my password page
     *
     * @param model
     *            Data from the front end
     * @return Page to display to the user
     */
    @GetMapping ( "/resetPassword" )
    public String resetPassword ( final Model model ) {
        return "/resetPassword";
    }
}
