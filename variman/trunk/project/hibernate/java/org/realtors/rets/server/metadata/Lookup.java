package org.realtors.rets.server.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_metadata_lookup"
 */
public class Lookup extends ServerMetadata implements Serializable
{
    public Lookup()
    {
        mId = null;
        mLookupTypes = Collections.EMPTY_SET;
    }

    public Lookup(long id)
    {
        this();
        mId = new Long(id);
    }

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
     * @hibernate.column name="lookup_name"
     *   not-null="true"
     *   unique="true"
     *   index="rets_metadata_lookup_lookupname_index"
     *   length="32"
     */
    public String getLookupName()
    {
        return mLookupName;
    }

    public void setLookupName(String lookupName)
    {
        mLookupName = lookupName;
    }

    /**
     *
     * @return a String
     *
     * @hibernate.property length="32"
     */
    public String getVisibleName()
    {
        return mVisibleName;
    }

    public void setVisibleName(String visibleName)
    {
        mVisibleName = visibleName;
    }

    /**
     *
     * @return a Resource object
     *
     * @hibernate.many-to-one
     * @hibernate.column name="resourceid"
     *   not-null="true"
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
     *
     * @return a Set of LookupType objects
     *
     * @hibernate.set
     * @hibernate.collection-key column="lookupid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.LookupType"
     */
    public Set getLookupTypes()
    {
        return mLookupTypes;
    }

    public void setLookupTypes(Set lookupTypes)
    {
        mLookupTypes = lookupTypes;
    }

    public void addLookupType(LookupType lookupType)
    {
        lookupType.setLookup(this);
        lookupType.updateLevel();
        mLookupTypes.add(lookupType);
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
        return mLevel + ":" + mLookupName;
    }

    public List getChildren()
    {
        List children = new ArrayList();
        children.add(mLookupTypes.toArray(new LookupType[0]));
        return children;
    }

    public String getTableName()
    {
        return TABLE;
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof Lookup))
        {
            return false;
        }
        Lookup castOther = (Lookup) other;
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
    private String mLookupName;

    /** nullable persistent field */
    private String mVisibleName;

    /** nullable persistent field */
    private Resource mResource;

    /** persistent field */
    private Set mLookupTypes;

    private String mLevel;

    public static final String TABLE = "LOOKUP";
}
