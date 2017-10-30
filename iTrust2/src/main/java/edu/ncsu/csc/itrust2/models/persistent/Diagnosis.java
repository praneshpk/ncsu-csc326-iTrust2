package edu.ncsu.csc.itrust2.models.persistent;

public class Diagnosis {

    private Patient patient;
    private String  icdCode;
    private String  name;
    private String  notes;

    public Diagnosis () {

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
