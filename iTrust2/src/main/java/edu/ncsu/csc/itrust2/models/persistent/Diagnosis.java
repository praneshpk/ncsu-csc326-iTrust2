package edu.ncsu.csc.itrust2.models.persistent;

import java.io.Serializable;
import java.util.List;

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
@Table ( name = "Diagnosis" )
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

    @NotEmpty
    @Length ( max = 255 )
    @Id
    private String                                      name;

    @NotEmpty
    @Length ( max = 255 )
    private String                                      icdCode;

    /**
     * Construct a diagnosis from the given form
     *
     * @param form
     *            a DiagnosisForm to convert to a diagnosis
     */
    public Diagnosis ( final DiagnosisForm form ) {
        setIcdCode( form.getIcdCode() );
        setName( form.getName() );
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
        setName( name );
        setIcdCode( icdCode );
    }

    /**
     * Retrieve a Diagnosis from the database or in-memory cache by name.
     *
     * @param name
     *            Name of the Diagnosis to retrieve
     * @return The Diagnosis found, or null if none was found.
     */
    public static Diagnosis getByName ( final String name ) {
        Diagnosis diagnosis = cache.get( name );
        if ( null == diagnosis ) {
            try {
                diagnosis = getWhere( " name = '" + name + "'" ).get( 0 );
                cache.put( name, diagnosis );
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
     * Retrieves the ID (Description) of this Hospital
     */
    @Override
    public String getId () {
        return getName();
    }

}
