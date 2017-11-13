package edu.ncsu.csc.itrust2.forms.admin;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import edu.ncsu.csc.itrust2.models.persistent.Diagnosis;

/**
 * Form used for adding an ICD-10 code to iTrust2
 *
 * @author Cameron Estroff
 *
 */
public class DiagnosisForm {

    /**
     * Name associated with the diagnosis
     */
    @NotEmpty
    @Length ( max = 255 )
    private String name;

    /**
     * ICD-10 code associated with the diagnosis
     */
    @NotEmpty
    @Length ( max = 255 )
    private String icdCode;

    /**
     * An empty constuctor for Hibernate
     */
    public DiagnosisForm () {

    }

    /**
     * Creates the diagnosis form from an existing diagnosis
     *
     * @param d
     *            the diagnosis to use
     */
    public DiagnosisForm ( final Diagnosis d ) {
        setIcdCode( d.getIcdCode() );
        setName( d.getName() );
    }

    /**
     * Returns the code of the diagnosis
     *
     * @return the icdCode
     */
    public String getIcdCode () {
        return icdCode;
    }

    /**
     * Sets the code of the diagnosis
     *
     * @param icdCode
     *            the icdCode to set
     */
    public void setIcdCode ( final String icdCode ) {
        this.icdCode = icdCode;
    }

    /**
     * Returns the name of the diagnosis
     *
     * @return the name
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the name of the diagnosis
     *
     * @param name
     *            the name to set
     */
    public void setName ( final String name ) {
        this.name = name;
    }
}
