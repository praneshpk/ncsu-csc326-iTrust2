package edu.ncsu.csc.itrust2.controllers.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.ncsu.csc.itrust2.forms.admin.DiagnosisForm;

/**
 * Class that enables an Admin to create a Diagnosis.
 *
 * @author Cameron Estroff
 *
 */
@Controller
public class AdminDiagnosisController {

    /**
     * Creates the form page for the Create Diagnosis page
     *
     * @param model
     *            Data for the front end
     * @return Page to show to the user
     */
    @RequestMapping ( value = "admin/createDiagnosis" )
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    public String createDiagnosis ( final Model model ) {
        model.addAttribute( "DiagnosisForm", new DiagnosisForm() );
        return "/admin/createDiagnosis";
    }
}
