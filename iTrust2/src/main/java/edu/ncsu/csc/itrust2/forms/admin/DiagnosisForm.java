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
     * ICD-10 code associated with the diagnosis
     */
    @NotEmpty
    @Length ( max = 255 )
    private String icdCode;

    /**
     * Name associated with the diagnosis
     */
    @NotEmpty
    @Length ( max = 255 )
    private String name;

    public DiagnosisForm () {

    }

    public DiagnosisForm ( final Diagnosis d ) {
        setIcdCode( d.getIcdCode() );
        setName( d.getName() );
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
     * @param description
     *            the description to set
     */
    public void setName ( final String name ) {
        this.name = name;
    }
}
