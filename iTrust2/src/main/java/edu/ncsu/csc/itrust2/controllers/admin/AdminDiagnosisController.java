package edu.ncsu.csc.itrust2.controllers.admin;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.ncsu.csc.itrust2.forms.admin.DiagnosisForm;
import edu.ncsu.csc.itrust2.models.persistent.Diagnosis;

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

    /**
     * Parses the DiagnosisForm from the User
     *
     * @param form
     *            DiagnosisForm to validate and save
     * @param result
     *            Validation result
     * @param model
     *            Data from the front end
     * @return The page to show to the user
     */
    @PostMapping ( "/admin/createDiagnosis" )
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    public String createDiagnosisSubmit ( @Valid @ModelAttribute ( "DiagnosisForm" ) final DiagnosisForm form,
            final BindingResult result, final Model model ) {
        Diagnosis d = null;
        try {
            d = new Diagnosis( form );
            if ( Diagnosis.getByName( d.getName() ) != null ) {
                result.rejectValue( "name", "name.notvalid", "A diagnosis with this name already exists" );
            }
        }
        catch ( final Exception e ) {
            throw new IllegalArgumentException(
                    e ); /* This should never happen */
        }

        if ( result.hasErrors() ) {
            model.addAttribute( "DiagnosisForm", form );
            return "/admin/createDiagnosis";
        }
        else {
            d.save();
            return "admin/createDiagnosisResult";
        }
    }
}
