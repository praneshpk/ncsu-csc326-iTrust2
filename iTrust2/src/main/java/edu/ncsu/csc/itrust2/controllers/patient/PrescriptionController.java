package edu.ncsu.csc.itrust2.controllers.patient;

import java.util.List;
import java.util.Vector;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.ncsu.csc.itrust2.forms.hcp.PrescriptionForm;
import edu.ncsu.csc.itrust2.models.persistent.Prescription;

public class PrescriptionController {

    /**
     * Creates a page of all prescribed Prescriptions so the patient can view
     * them
     *
     * @param model
     *            Data from the front end
     * @return The page for the patient to view their prescriptions
     */
    @GetMapping ( "/patient/viewPrescriptions" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public String viewPrescriptions ( final Model model ) {
        final List<Prescription> prescription = Prescription
                .getForPatient( SecurityContextHolder.getContext().getAuthentication().getName() );
        final List<PrescriptionForm> prescriptions = new Vector<PrescriptionForm>();
        for ( final Prescription p : prescription ) {
            prescriptions.add( new PrescriptionForm( p ) );
        }
        model.addAttribute( "prescriptions", prescriptions );
        return "patient/viewPrescriptions";
    }
}
