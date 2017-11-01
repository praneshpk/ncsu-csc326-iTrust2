package edu.ncsu.csc.itrust2.controllers.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.forms.admin.DiagnosisForm;
import edu.ncsu.csc.itrust2.models.persistent.Diagnosis;

/**
 * Class that provides REST API endpoints for the Diagnosis model. In all
 * requests made to this controller, the {id} provided is a String that is the
 * name of the diagnosis desired.
 *
 * @author Cameron Estroff
 *
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIDiagnosisController extends APIController {

    /**
     * Retrieves a list of all Diagnoses in the database
     *
     * @return list of diagnoses
     */
    @GetMapping ( BASE_PATH + "/diagnoses" )
    public List<Diagnosis> getDiagnoses () {
        return Diagnosis.getDiagnoses();
    }

    /**
     * Creates and saves a new Diagnosis from the RequestBody provided.
     *
     * @param diagnosisF
     *            The diagnosis to be validated and saved
     * @return response
     */
    @PostMapping ( BASE_PATH + "/creatediagnosis" )
    public ResponseEntity createDiagnosis ( @RequestBody final DiagnosisForm diagnosisF ) {
        try {
            final Diagnosis diagnosis = new Diagnosis( diagnosisF );
            if ( Diagnosis.getByName( diagnosisF.getName() ) != null ) {
                return new ResponseEntity( "Diagnosis with the name " + diagnosis.getName() + " already exists",
                        HttpStatus.CONFLICT );
            }
            diagnosis.save();
            return new ResponseEntity( diagnosis, HttpStatus.OK );

        }
        catch ( final Exception e ) {
            return new ResponseEntity( "Could not validate or save the Diagnosis provided due to " + e.getMessage(),
                    HttpStatus.BAD_REQUEST );
        }
    }
}
