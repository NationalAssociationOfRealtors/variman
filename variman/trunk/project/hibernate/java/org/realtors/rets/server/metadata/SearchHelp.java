package org.realtors.rets.server.metadata;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @hibernate.class table="rets_metadata_searchhelp"
 */
public class SearchHelp implements Serializable
{
    /**
     *
     * @return a Long object
     *
     * @hibernate.id generator-class="native"
     */
    public Long getId()
    {
        return mId;
    }

    public void setId(Long id)
    {
        mId = id;
    }

    /**
     *
     * @return a String
     *
     * @hibernate.property
     * @hibernate.column name="searchHelpID"
     *   not-null="false"
     *   unique="false"
     *   index="rets_metadata_searchhelp_index"
     *   length="32"
     */
    public String getSearchHelpID()
    {
        return mSearchHelpID;
    }

    public void setSearchHelpID(String searchHelpID)
    {
        mSearchHelpID = searchHelpID;
    }

    /**
     *
     * @return a String
     *
     * @hibernate.property length="256"
     */
    public String getValue()
    {
        return mValue;
    }

    public void setValue(String value)
    {
        mValue = value;
    }

    /**
     *
     * @return a Resource object
     *
     * @hibernate.many-to-one 
     */
    public Resource getResource()
    {
        return mResource;
    }

    public void setResource(Resource resource)
    {
        mResource = resource;
    }

    /**
     * Returns the hierarchy level for this metadata object.
     *
     * @return the hierarchy level for this metadata object.
     *
     * @hibernate.property length="64"
     */
    public String getLevel()
    {
        return mLevel;
    }

    public void setLevel(String level)
    {
        mLevel = level;
    }

    public void updateLevel()
    {
        mLevel = mResource.getPath();
    }

    public String getPath()
    {
        return mLevel + ":" + mSearchHelpID;
    }

    public String toString()
    {
        return mSearchHelpID;
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof SearchHelp)) return false;
        SearchHelp castOther = (SearchHelp) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
    }

    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(getId())
            .toHashCode();
    }

    /** identifier field */
    private Long mId;

    /** nullable persistent field */
    private String mSearchHelpID;

    /** nullable persistent field */
    private String mValue;

    /** nullable persistent field */
    private Resource mResource;

    private String mLevel;
}
