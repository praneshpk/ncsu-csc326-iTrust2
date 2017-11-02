package edu.ncsu.csc.itrust2.forms.admin;

import java.io.Serializable;

/**
 * CodeForm class for creating ICD-10 and NDC code objects
 *
 * @author WillGlas
 */
public class CodeForm implements Serializable {

    /** Serializing ID */
    private static final long serialVersionUID = 1L;

    /** Unique indentifier found in NDC or ICD-10 database */
    private String            id;

    /** Name of diagnosis or prescription */
    private String            name;

    /**
     * Get the id
     *
     * @return the id
     */
    public String getId () {
        return id;
    }

    /**
     * Set the id
     *
     * @param id
     *            the id to set
     */
    public void setId ( final String id ) {
        this.id = id;
    }

    /**
     * Get the name
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
