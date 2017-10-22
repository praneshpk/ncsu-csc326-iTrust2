package edu.ncsu.csc.itrust2.forms.hcp;

import java.io.Serializable;

/**
 * PrescriptionForm class
 *
 * @author Will Glas
 */
public class PrescriptionForm implements Serializable {

    /**
     * Serial Version of the Form. For the Serializable
     */
    private static final long serialVersionUID = 1L;

    /**
     * Empty constructor for serializer
     */
    public PrescriptionForm () {
    }

    /** Id of prescription (if it already exists) */
    private String id;

    /** Patient prescribed to */
    private String patient;

    /** Dosage of prescription */
    private String dosage;

    /** Start date of prescription */
    private String start;

    /** End date of prescription */
    private String end;

    /** Number of renewals */
    private String renewals;

    /** NDC code of prescription */
    private String ndcCode;

    /** OfficeVisit visit - can be null or empty string */
    private String officeVisitId;

    /**
     * Get the id of the form
     *
     * @return id
     */
    public String getId () {
        return id;
    }

    /**
     * Set the id of the form
     *
     * @param id
     *            id to set
     */
    public void setId ( final String id ) {
        this.id = id;
    }

    /**
     * Get the patient
     *
     * @return the patient
     */
    public String getPatient () {
        return patient;
    }

    /**
     * Set the patient
     *
     * @param patient
     *            the patient to set
     */
    public void setPatient ( final String patient ) {
        this.patient = patient;
    }

    /**
     * Get the dosage
     *
     * @return the dosage
     */
    public String getDosage () {
        return dosage;
    }

    /**
     * Set the dosage
     *
     * @param dosage
     *            the dosage to set
     */
    public void setDosage ( final String dosage ) {
        this.dosage = dosage;
    }

    /**
     * Get the start date
     *
     * @return the start
     */
    public String getStart () {
        return start;
    }

    /**
     * Set the start date
     *
     * @param start
     *            the start to set
     */
    public void setStart ( final String start ) {
        this.start = start;
    }

    /**
     * Get the end date
     *
     * @return the end
     */
    public String getEnd () {
        return end;
    }

    /**
     * Set the end date
     *
     * @param end
     *            the end to set
     */
    public void setEnd ( final String end ) {
        this.end = end;
    }

    /**
     * Get the renewals
     *
     * @return the renewals
     */
    public String getRenewals () {
        return renewals;
    }

    /**
     * Set the renewals
     *
     * @param renewals
     *            the renewals to set
     */
    public void setRenewals ( final String renewals ) {
        this.renewals = renewals;
    }

    /**
     * Get the NDC code
     *
     * @return the ndcCode
     */
    public String getNdcCode () {
        return ndcCode;
    }

    /**
     * Set the NDC code
     *
     * @param ndcCode
     *            the ndcCode to set
     */
    public void setNdcCode ( final String ndcCode ) {
        this.ndcCode = ndcCode;
    }

    /**
     * Get officeVisitId
     *
     * @return the officeVisitId
     */
    public String getOfficeVisitId () {
        return officeVisitId;
    }

    /**
     * Set officeVisitId
     *
     * @param officeVisitId
     *            the officeVisitId to set
     */
    public void setOfficeVisitId ( final String officeVisitId ) {
        this.officeVisitId = officeVisitId;
    }
}
