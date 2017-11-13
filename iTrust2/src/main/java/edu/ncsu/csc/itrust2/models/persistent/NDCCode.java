package edu.ncsu.csc.itrust2.models.persistent;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import edu.ncsu.csc.itrust2.forms.admin.CodeForm;
import edu.ncsu.csc.itrust2.utils.DomainObjectCache;

/**
 * NDC class to persist codes added by the admin
 *
 * @author WillGlas
 */
@Entity
@Table ( name = "NDCCodes" )
public class NDCCode extends DomainObject<NDCCode> implements Serializable {

    /** Serial version id */
    private static final long                         serialVersionUID = 1L;

    /**
     * The cache representation of the Prescriptions in the database
     */
    static private DomainObjectCache<String, NDCCode> cache            = new DomainObjectCache<String, NDCCode>(
            NDCCode.class );

    /** Unique code as determined by online NDC database */
    @Id
    private String                                    code;

    /** Name of the drug */
    @NotNull
    private String                                    name;

    /**
     * Get the list of all NDCCode objects in the system.
     * 
     * @return NDCCodes
     */
    @SuppressWarnings ( "unchecked" )
    public static List<NDCCode> getNDCCodes () {
        return (List<NDCCode>) getAll( NDCCode.class );
    }

    /**
     * Retrieve an NDCCode by its ID.
     *
     * @param code
     *            The code of the NDCCode
     * @return The NDCCode, if found, or null if not found.
     */
    public static NDCCode getByCode ( final String code ) {
        NDCCode request = cache.get( code );
        if ( null == request ) {
            try {
                request = getWhere( "code = '" + code + "'" ).get( 0 );
                cache.put( code, request );
            }
            catch ( final Exception e ) {
                // Exception ignored
            }
        }
        return request;
    }

    /**
     * Retrieve a List of NDCCode that meets the given where clause. Clause is
     * expected to be valid SQL.
     *
     * @param where
     *            The WhereClause to find Prescriptions by
     * @return The matching list
     */
    @SuppressWarnings ( "unchecked" )
    private static List<NDCCode> getWhere ( final String where ) {
        return (List<NDCCode>) getWhere( NDCCode.class, where );
    }

    /**
     * Empty constructor
     */
    public NDCCode () {
    }

    /**
     * Create an NDCCode object from a CodeForm
     *
     * @param cf
     *            form to create code with
     */
    public NDCCode ( final CodeForm cf ) {
        final String ndcRegEx = "\\d{4}-\\d{4}-\\d{2}|\\d{5}-\\d{3}-\\d{2}|\\d{5}-\\d{4}-\\d{1}|\\d{5}-\\*\\d{3}-\\d{2}";
        if ( Pattern.matches( ndcRegEx, cf.getId() ) ) {
            setCode( cf.getId() );
            setName( cf.getName() );
        }
        else {
            throw new IllegalArgumentException( "The NDC did not match the required format." );
        }
    }

    /**
     * Get the id
     *
     * @return the id
     */
    @Override
    public String getId () {
        return code;
    }

    /**
     * Set the code
     *
     * @param code
     *            the code to set
     */
    public void setCode ( final String code ) {
        this.code = code;
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
     * Set the name
     *
     * @param name
     *            the name to set
     */
    public void setName ( final String name ) {
        this.name = name;
    }

}
