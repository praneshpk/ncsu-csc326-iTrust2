package edu.ncsu.csc.itrust2.models.persistent;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import edu.ncsu.csc.itrust2.forms.admin.DiagnosisForm;
import edu.ncsu.csc.itrust2.utils.DomainObjectCache;

/**
 * Class representing a Diagnosis object, as represented in the DB.
 *
 * @author Cameron Estroff
 *
 */
@Entity
@Table ( name = "Diagnoses" )
public class Diagnosis extends DomainObject<Hospital> implements Serializable {

    /**
     * Used for serializing the object.
     */
    private static final long                           serialVersionUID = 1L;

    /**
     * In-memory cache that will store instances of the Hospital to avoid
     * retrieval trips to the database.
     */
    static private DomainObjectCache<String, Diagnosis> cache            = new DomainObjectCache<String, Diagnosis>(
            Diagnosis.class );

    /**
     * Name of the diagnosis
     */
    @NotEmpty
    @Length ( max = 255 )
    private String                                      name;

    /**
     * ICD code that is associated with the diagnosis name
     */
    @NotEmpty
    @Length ( max = 255 )
    @Id
    private String                                      icdCode;

    /**
     * Construct a diagnosis from the given form
     *
     * @param form
     *            a DiagnosisForm to convert to a diagnosis
     */
    public Diagnosis ( final DiagnosisForm form ) {
        if ( Pattern.matches( "([A-TV-Z][0-9][A-Z0-9](\\.?[A-Z0-9]{0,4})?)", form.getIcdCode() ) ) {
            setIcdCode( form.getIcdCode() );
            setName( form.getName() );
        }
        else {
            throw new IllegalArgumentException( "The ICD-10 code did not match the required format." );
        }
    }

    /**
     * Construct a diagnosis from the given fields
     *
     * @param name
     *            the name of the code to be created
     * @param icdCode
     *            the icdCode to be added
     */
    public Diagnosis ( final String name, final String icdCode ) {
        if ( Pattern.matches( "([A-TV-Z][0-9][A-Z0-9](\\.?[A-Z0-9]{0,4})?)", icdCode ) ) {
            setName( name );
            setIcdCode( icdCode );
        }
        else {
            throw new IllegalArgumentException( "The ICD-10 code did not match the required format." );
        }
    }

    /**
     * Retrieve a Diagnosis from the database or in-memory cache by id.
     *
     * @param code
     *            Icd-10 code of the Diagnosis to retrieve
     * @return The Diagnosis found, or null if none was found.
     */
    public static Diagnosis getByCode ( final String code ) {
        Diagnosis diagnosis = cache.get( code );
        if ( null == diagnosis ) {
            try {
                diagnosis = getWhere( " name = '" + code + "'" ).get( 0 );
                cache.put( code, diagnosis );
            }
            catch ( final Exception e ) {
                // Exception ignored
            }
        }
        return diagnosis;
    }

    /**
     * Retrieve all matching Diagnoses from the database that match a where
     * clause provided.
     *
     * @param where
     *            Clause to match by
     * @return The matching Diagnoses
     */
    @SuppressWarnings ( "unchecked" )
    private static List<Diagnosis> getWhere ( final String where ) {
        return (List<Diagnosis>) getWhere( Diagnosis.class, where );
    }

    /**
     * Retrieve all Diagnoses from the database
     *
     * @return Diagnoses found
     */
    @SuppressWarnings ( "unchecked" )
    public static List<Diagnosis> getDiagnoses () {
        return (List<Diagnosis>) getAll( Diagnosis.class );
    }

    /**
     * Construct an empty Diagnosis record. Used for Hibernate.
     */
    public Diagnosis () {
    }

    /**
     * Get the ICD code
     *
     * @return the icdCode
     */
    public String getIcdCode () {
        return icdCode;
    }

    /**
     * Set the ICD code
     *
     * @param icdCode
     *            the icdCode to set
     */
    public void setIcdCode ( final String icdCode ) {
        this.icdCode = icdCode;
    }

    /**
     * Return the name
     *
     * @return the name
     */
    public String getName () {
        return name;
    }

    /**
     * Get the name
     *
     * @param name
     *            the name to set
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Retrieves the ID (Description) of this Diagnosis
     *
     * @return code
     */
    @Override
    public String getId () {
        return getIcdCode();
    }

}
