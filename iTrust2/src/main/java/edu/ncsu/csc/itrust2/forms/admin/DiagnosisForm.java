package edu.ncsu.csc.itrust2.forms.admin;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import edu.ncsu.csc.itrust2.models.persistent.Diagnosis;
import edu.ncsu.csc.itrust2.models.persistent.Patient;

/**
 * Form used for adding an ICD-10 code to iTrust2
 *
 * @author Cameron Estroff
 *
 */
public class DiagnosisForm {

    /**
     * Patient associated with the diagnosis
     */
    @NotEmpty
    @Length ( max = 255 )
    private Patient patient;

    /**
     * ICD-10 code associated with the diagnosis
     */
    @NotEmpty
    @Length ( max = 255 )
    private String  icdCode;

    /**
     * Name associated with the diagnosis
     */
    @NotEmpty
    @Length ( max = 255 )
    private String  name;

    /**
     * Notes associated with the diagnosis
     */
    @NotEmpty
    @Length ( max = 255 )
    private String  notes;

    public DiagnosisForm () {

    }

    public DiagnosisForm ( final Diagnosis d ) {
        setPatient( d.getPatient() );
        setIcdCode( d.getIcdCode() );
        setName( d.getName() );
        setNotes( d.getNotes() );
    }

    /**
     * @return the patient
     */
    public Patient getPatient () {
        return patient;
    }

    /**
     * @param patient
     *            the patient to set
     */
    public void setPatient ( final Patient patient ) {
        this.patient = patient;
    }

    /**
     * @return the icdCode
     */
    public String getIcdCode () {
        return icdCode;
    }

    /**
     * @param icdCode
     *            the icdCode to set
     */
    public void setIcdCode ( final String icdCode ) {
        this.icdCode = icdCode;
    }

    /**
     * @return the name
     */
    public String getName () {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * @return the notes
     */
    public String getNotes () {
        return notes;
    }

    /**
     * @param notes
     *            the notes to set
     */
    public void setNotes ( final String notes ) {
        this.notes = notes;
    }
}
