package edu.ncsu.csc.itrust2.controllers.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class APIDiagnosisController extends APIController {

    /**
     * Retrieves a list of all Diagnoses in the database
     *
     * @return list of diagnoses
     */
    @GetMapping ( BASE_PATH + "/diagnosis" )
    public List<Diagnosis> getDiagnoses () {
        return Diagnosis.getDiagnoses();
    }
}
