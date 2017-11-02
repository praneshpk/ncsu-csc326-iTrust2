package edu.ncsu.csc.itrust2.controllers.hcp;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.ncsu.csc.itrust2.forms.hcp.PrescriptionForm;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Class for managing Prescriptions
 *
 * @author Pranesh Kamalakanthan
 *
 */
@Controller
public class PrescriptionController {

    /**
     * Returns the form page for a HCP to add a prescription
     *
     * @param model
     *            The data for the front end
     * @return Page to display to the user
     */
    @GetMapping ( "/hcp/addPrescription" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public String addPrescription ( final Model model ) {
        model.addAttribute( "PrescriptionForm", new PrescriptionForm() );
        model.addAttribute( "officevisits", OfficeVisit.getOfficeVisits() );
        model.addAttribute( "patients", User.getPatients() );
        return "/hcp/addPrescription";
    }

    /**
     * Returns the form page for a HCP to manage prescriptions
     *
     * @param model
     *            The data for the front end
     * @return Page to display to the user
     */
    @GetMapping ( "/hcp/managePrescriptions" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public String managePrescriptions ( final Model model ) {
        model.addAttribute( "patients", User.getPatients() );
        return "/hcp/managePrescriptions";
    }

}
