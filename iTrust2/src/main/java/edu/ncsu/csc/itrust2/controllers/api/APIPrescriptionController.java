package edu.ncsu.csc.itrust2.controllers.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.forms.admin.CodeForm;
import edu.ncsu.csc.itrust2.forms.hcp.PrescriptionForm;
import edu.ncsu.csc.itrust2.models.persistent.NDCCode;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.Prescription;

/**
 * API controller to create and manage prescriptions
 *
 * @author WillGlas
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIPrescriptionController extends APIController {

    /**
     * Get the prescriptions for a specified patient
     *
     * @param patientId
     *            patient to get prescriptions for
     * @return list of prescriptions for patient
     */
    @GetMapping ( BASE_PATH + "/prescriptions/{patientId}" )
    public List<Prescription> getPrescriptionsByPatient ( @PathVariable ( "id" ) final String patientId ) {
        final Patient p = Patient.getPatient( patientId );
        return p.getPrescriptions();
    }

    /**
     * Create a new prescription, add it to the appropriate patient and office
     * visit objects, and save it
     *
     * @param pf
     *            prescription form to create prescription with
     * @return ResponseEntity describing success or failure of operation
     */
    @PostMapping ( BASE_PATH + "/prescriptions" )
    public ResponseEntity createPrescription ( @Valid @RequestBody final PrescriptionForm pf ) {
        try {
            // create prescription and get patient
            final Prescription prescription = new Prescription( pf );
            final Patient patient = Patient.getPatient( pf.getPatient() );
            if ( patient == null ) {
                return new ResponseEntity( "Patient does not exist.", HttpStatus.BAD_REQUEST );
            }
            prescription.save();

            // check if an office visit was specified
            final String visitId = pf.getOfficeVisitId();
            if ( visitId != null && !visitId.equals( "" ) ) {
                final OfficeVisit visit = OfficeVisit.getById( Long.parseLong( visitId ) );
                visit.addPrescription( prescription );
                visit.save();
            }

            patient.addPrescription( prescription );
            patient.save();
            return new ResponseEntity( prescription, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( "Error occured: " + e.getMessage(), HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Renew a prescription
     *
     * @param id
     *            if of prescription to renew
     * @return ResponseEntity detailing success or failure of transaction
     */
    @PutMapping ( BASE_PATH + "/prescriptions/renew/{id}" )
    public ResponseEntity renewPrescription ( @PathVariable final Long id ) {
        final Prescription p = Prescription.getById( id );
        if ( p.renew() ) {
            return new ResponseEntity( p, HttpStatus.OK );
        }
        else {
            return new ResponseEntity( "Could not renew prescription.", HttpStatus.CONFLICT );
        }
    }

    /**
     * Add an NDCCode to the DB
     *
     * @param cf
     *            CodeForm for creating NDCCode
     * @return ResponseEntity indicating success of operation and related
     *         information
     */
    @PostMapping ( BASE_PATH + "ndccodes" )
    public ResponseEntity addNDCCode ( @Valid @RequestBody final CodeForm cf ) {
        try {
            // if ( NDCCode.getByCode( cf.getId() ) != null ) {
            // return new ResponseEntity( "NDC code already exists in the
            // system.", HttpStatus.BAD_REQUEST );
            // }
            final NDCCode code = new NDCCode( cf );
            code.save();
            return new ResponseEntity( code, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( "Failed to create NDC code: " + e.getMessage(), HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Get the NDCCodes
     *
     * @return list of NDCCode objects
     */
    @GetMapping ( BASE_PATH + "ndccodes" )
    public List<NDCCode> getNDCCodes () {
        return NDCCode.getNDCCodes();
    }
}
