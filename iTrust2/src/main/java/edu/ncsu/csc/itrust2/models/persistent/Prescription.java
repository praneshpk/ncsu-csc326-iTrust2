package edu.ncsu.csc.itrust2.models.persistent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import edu.ncsu.csc.itrust2.forms.hcp.PrescriptionForm;
import edu.ncsu.csc.itrust2.utils.DomainObjectCache;

/**
 * Prescription class
 *
 * @author Will Glas
 */
@Entity
@Table ( name = "Prescriptions" )
public class Prescription extends DomainObject<Prescription> {

    /**
     * The cache representation of the Prescriptions in the database
     */
    static private DomainObjectCache<Long, Prescription> cache = new DomainObjectCache<Long, Prescription>(
            Prescription.class );

    /**
     * Retrieve an Prescriptions by its numerical ID.
     *
     * @param id
     *            The ID (as assigned by the DB) of the Prescriptions
     * @return The Prescriptions, if found, or null if not found.
     */
    public static Prescription getById ( final Long id ) {
        Prescription request = cache.get( id );
        if ( null == request ) {
            try {
                request = getWhere( "id = '" + id + "'" ).get( 0 );
                cache.put( id, request );
            }
            catch ( final Exception e ) {
                // Exception ignored
            }
        }
        return request;
    }

    /**
     * Retrieve a List of Prescriptions that meets the given where clause.
     * Clause is expected to be valid SQL.
     *
     * @param where
     *            The WhereClause to find Prescriptions by
     * @return The matching list
     */
    @SuppressWarnings ( "unchecked" )
    private static List<Prescription> getWhere ( final String where ) {
        return (List<Prescription>) getWhere( Prescription.class, where );
    }

    /**
     * Get all the prescriptions for a specific patient
     *
     * @param patientName
     *            name of the patient
     * @return List of all the prescriptions for the patient
     */
    public static List<Prescription> getForPatient ( final String patientName ) {
        return getWhere( " patient_id = '" + patientName + "'" );
    }

    /**
     * The id of this patient
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long        id;

    /**
     * The patient associated with the prescription
     */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "patient_id" )
    private Patient     patient;

    /**
     * The office visit associated with the prescription - can be null
     */
    @ManyToOne
    @JoinColumn ( name = "officevisit_id" )
    private OfficeVisit officeVisit;

    /**
     * NDC code specifying prescription type
     */
    @ManyToOne
    @JoinColumn ( name = "ndccode_id" )
    private NDCCode     ndcCode;

    /**
     * The dosage of the medicine
     */
    private double      dosage;

    /**
     * Start date of the prescription
     */
    private Date        start;

    /**
     * End date of the prescription
     */
    private Date        end;

    /**
     * Number of renewals of the prescription
     */
    private int         renewals;

    /**
     * Empty constructor necessary for Hibernate.
     */
    public Prescription () {
    }

    /**
     * Takes PrescriptionForm object and constructs a Prescription
     *
     * @param pf
     *            PrescriptionForm object
     * @throws ParseException
     */
    public Prescription ( final PrescriptionForm pf ) throws ParseException {
        final String formId = pf.getId();
        if ( formId != null && !formId.equals( "" ) ) {
            setId( Long.parseLong( formId ) );
        }

        final Patient patient = Patient.getPatient( pf.getPatient() );
        if ( patient == null ) {
            throw new IllegalArgumentException( "Patient does not exist in the system." );
        }
        setPatient( patient );
        setDosage( Double.parseDouble( pf.getDosage() ) );

        final SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy", Locale.ENGLISH );
        final Date parsedStartDate = sdf.parse( pf.getStart() );
        final Date parsedEndDate = sdf.parse( pf.getEnd() );
        if ( !parsedStartDate.before( parsedEndDate ) ) {
            throw new IllegalArgumentException( "Start date came after end date." );
        }
        setStart( parsedStartDate );
        setEnd( parsedEndDate );
        setRenewals( Integer.parseInt( pf.getRenewals() ) );

        final NDCCode code = NDCCode.getByCode( pf.getNdcCode() );
        if ( code == null ) {
            throw new IllegalArgumentException( "NDCCode specified was not in the system." );
        }
        setNdcCode( code );
    }

    /**
     * Renew a prescription
     *
     * @return true if the prescription was successfully renewed.
     */
    public boolean renew () {
        final Date now = Date.from( Instant.now() );
        if ( renewals > 0 && start.before( now ) && end.after( now ) ) {
            renewals--;
            this.save();
            return true;
        }

        return false;
    }

    /**
     * Get the id of the object
     *
     * @return the object's id
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the Id
     *
     * @param id
     *            the id to set
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Get the patient
     *
     * @return the patient
     */
    public Patient getPatient () {
        return patient;
    }

    /**
     * Set the patient
     *
     * @param patient
     *            the patient to set
     */
    public void setPatient ( final Patient patient ) {
        this.patient = patient;
    }

    /**
     * Get the dosage
     *
     * @return the dosage
     */
    public double getDosage () {
        return dosage;
    }

    /**
     * Set the dosage
     *
     * @param dosage
     *            the dosage to set
     */
    public void setDosage ( final double dosage ) {
        this.dosage = dosage;
    }

    /**
     * Get the start date
     *
     * @return the start
     */
    public Date getStart () {
        return start;
    }

    /**
     * Set the start date
     *
     * @param start
     *            the start to set
     */
    public void setStart ( final Date start ) {
        this.start = start;
    }

    /**
     * Get the end date
     *
     * @return the end
     */
    public Date getEnd () {
        return end;
    }

    /**
     * Set the end date
     *
     * @param end
     *            the end to set
     */
    public void setEnd ( final Date end ) {
        this.end = end;
    }

    /**
     * Get the number of renewals
     *
     * @return the renewals
     */
    public int getRenewals () {
        return renewals;
    }

    /**
     * Set the number of renewals
     *
     * @param renewals
     *            the renewals to set
     */
    public void setRenewals ( final int renewals ) {
        this.renewals = renewals;
    }

    /**
     * Get the NDC code
     *
     * @return the ndcCode
     */
    public NDCCode getNdcCode () {
        return ndcCode;
    }

    /**
     * Set the NDC code
     *
     * @param ndcCode
     *            the ndcCode to set
     */
    public void setNdcCode ( final NDCCode ndcCode ) {
        this.ndcCode = ndcCode;
    }

    /**
     * Get the office visit
     *
     * @return the officeVisit
     */
    public OfficeVisit getOfficeVisit () {
        return officeVisit;
    }

    /**
     * Set the office visit
     *
     * @param officeVisit
     *            the officeVisit to set
     */
    public void setOfficeVisit ( final OfficeVisit officeVisit ) {
        this.officeVisit = officeVisit;
    }
}
