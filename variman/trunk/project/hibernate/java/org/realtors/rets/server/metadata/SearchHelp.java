package org.realtors.rets.server.metadata;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_metadata_searchhelp"
 */
public class SearchHelp implements Serializable
{
    /**
     *
     * @return
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
     * @return
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
     * @return
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
     * @return
     *
     * @hibernate.many-to-one 
     */
    public Resource getResourceid()
    {
        return mResourceid;
    }

    public void setResourceid(Resource resourceid)
    {
        mResourceid = resourceid;
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
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
    private Resource mResourceid;
}
