package org.realtors.rets.server.metadata;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_metadata_resource"
 */
public class Resource implements Serializable
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
     * @hibernate.column name="resourceID"
     *   not-null="true"
     *   unique="true"
     *   index="resource_rets_id_index"
     *   length="32"
     */
    public String getResourceID()
    {
        return mResourceID;
    }

    public void setResourceID(String resourceID)
    {
        mResourceID = resourceID;
    }

    /**
     *
     * @return
     *
     * @hibernate.property
     * @hibernate.column name="standardName"
     *   not-null="false"
     *   unique="false"
     *   index="resource_standard_name_index"
     */
    public ResourceStandardNameEnum getStandardName()
    {
        return mStandardName;
    }

    public void setStandardName(ResourceStandardNameEnum standardName)
    {
        mStandardName = standardName;
    }

    /**
     *
     * @return
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
     * @return
     *
     * @hibernate.property length="64"
     */
    public String getDescription()
    {
        return mDescription;
    }

    public void setDescription(String description)
    {
        mDescription = description;
    }

    /**
     *
     * @return
     *
     * @hibernate.property length="32"
     */
    public String getKeyField()
    {
        return mKeyField;
    }

    public void setKeyField(String keyField)
    {
        mKeyField = keyField;
    }

    /**
     *
     * @return
     *
     * @hibernate.property length="11"
     */
    public String getClassVersion()
    {
        return mClassVersion;
    }

    public void setClassVersion(String classVersion)
    {
        mClassVersion = classVersion;
    }

    /**
     *
     * @return
     *
     * @hibernate.property type="date"
     */
    public Date getClassDate()
    {
        return mClassDate;
    }

    public void setClassDate(Date classDate)
    {
        mClassDate = classDate;
    }

    /**
     *
     * @return
     *
     * @hibernate.property length="11"
     */
    public String getObjectVersion()
    {
        return mObjectVersion;
    }

    public void setObjectVersion(String objectVersion)
    {
        mObjectVersion = objectVersion;
    }

    /**
     *
     * @return
     *
     * @hibernate.property type="date"
     */
    public Date getObjectDate()
    {
        return mObjectDate;
    }

    public void setObjectDate(Date objectDate)
    {
        mObjectDate = objectDate;
    }

    /**
     *
     * @return
     *
     * @hibernate.property length="11"
     */
    public String getSearchHelpVersion()
    {
        return mSearchHelpVersion;
    }

    public void setSearchHelpVersion(String searchHelpVersion)
    {
        mSearchHelpVersion = searchHelpVersion;
    }

    /**
     *
     * @return
     *
     * @hibernate.property type="date"
     */
    public Date getSearchHelpDate()
    {
        return mSearchHelpDate;
    }

    public void setSearchHelpDate(Date searchHelpDate)
    {
        mSearchHelpDate = searchHelpDate;
    }

    /**
     *
     * @return
     *
     * @hibernate.property length="11"
     */
    public String getEditMaskVersion()
    {
        return mEditMaskVersion;
    }

    public void setEditMaskVersion(String editMaskVersion)
    {
        mEditMaskVersion = editMaskVersion;
    }

    /**
     *
     * @return
     *
     * @hibernate.property type="date"
     */
    public Date getEditMaskDate()
    {
        return mEditMaskDate;
    }

    public void setEditMaskDate(Date editMaskDate)
    {
        mEditMaskDate = editMaskDate;
    }

    /**
     *
     * @return
     *
     * @hibernate.property length="11"
     */
    public String getLookupVersion()
    {
        return mLookupVersion;
    }

    public void setLookupVersion(String lookupVersion)
    {
        mLookupVersion = lookupVersion;
    }

    /**
     *
     * @return
     *
     * @hibernate.property type="date"
     */
    public Date getLookupDate()
    {
        return mLookupDate;
    }

    public void setLookupDate(Date lookupDate)
    {
        mLookupDate = lookupDate;
    }

    /**
     *
     * @return
     *
     * @hibernate.property length="11"
     */
    public String getUpdateHelpVersion()
    {
        return mUpdateHelpVersion;
    }

    public void setUpdateHelpVersion(String updateHelpVersion)
    {
        mUpdateHelpVersion = updateHelpVersion;
    }

    /**
     *
     * @return
     *
     * @hibernate.property type="date"
     */
    public Date getUpdateHelpDate()
    {
        return mUpdateHelpDate;
    }

    public void setUpdateHelpDate(Date updateHelpDate)
    {
        mUpdateHelpDate = updateHelpDate;
    }

    /**
     *
     * @return
     *
     * @hibernate.property length="11"
     */
    public String getValidationExpressionVersion()
    {
        return mValidationExpressionVersion;
    }

    public void setValidationExpressionVersion(String validationExpressionVersion)
    {
        mValidationExpressionVersion = validationExpressionVersion;
    }

    /**
     *
     * @return
     *
     * @hibernate.property type="date"
     */
    public Date getValidationExpressionDate()
    {
        return mValidationExpressionDate;
    }

    public void setValidationExpressionDate(Date validationExpressionDate)
    {
        mValidationExpressionDate = validationExpressionDate;
    }

    /**
     *
     * @return
     *
     * @hibernate.property length="11"
     */
    public String getValidationLookupVersion()
    {
        return mValidationLookupVersion;
    }

    public void setValidationLookupVersion(String validationLookupVersion)
    {
        mValidationLookupVersion = validationLookupVersion;
    }

    /**
     *
     * @return
     *
     * @hibernate.property type="date"
     */
    public Date getValidationLookupDate()
    {
        return mValidationLookupDate;
    }

    public void setValidationLookupDate(Date validationLookupDate)
    {
        mValidationLookupDate = validationLookupDate;
    }

    /**
     *
     * @return
     *
     * @hibernate.property length="11"
     */
    public String getValidationExternalVersion()
    {
        return mValidationExternalVersion;
    }

    public void setValidationExternalVersion(String validationExternalVersion)
    {
        mValidationExternalVersion = validationExternalVersion;
    }

    /**
     *
     * @return
     *
     * @hibernate.property type="date"
     */
    public Date getValidationExternalDate()
    {
        return mValidationExternalDate;
    }

    public void setValidationExternalDate(Date validationExternalDate)
    {
        mValidationExternalDate = validationExternalDate;
    }

    /**
     *
     * @return
     *
     * @hibernate.many-to-one
     */
    public MSystem getSystemid()
    {
        return mSystemid;
    }

    public void setSystemid(MSystem systemid)
    {
        mSystemid = systemid;
    }

    /**
     *
     * @return
     *
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="resourceid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.MClass"
     */
    public Set getClasses()
    {
        return mClasses;
    }

    public void setClasses(Set classes)
    {
        mClasses = classes;
    }

    /**
     *
     * @return
     *
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="resourceid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.MObject"
     */
    public Set getObjects()
    {
        return mObjects;
    }

    public void setObjects(Set objects)
    {
        mObjects = objects;
    }

    /**
     *
     * @return
     *
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="resourceid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.SearchHelp"
     */
    public Set getSearchHelps()
    {
        return mSearchHelps;
    }

    public void setSearchHelps(Set searchHelps)
    {
        mSearchHelps = searchHelps;
    }

    /**
     *
     * @return
     *
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="resourceid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.EditMask"
     */
    public Set getEditMasks()
    {
        return mEditMasks;
    }

    public void setEditMasks(Set editMasks)
    {
        mEditMasks = editMasks;
    }

    /**
     *
     * @return
     *
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="resourceid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.Lookup"
     */
    public Set getLookups()
    {
        return mLookups;
    }

    public void setLookups(Set lookups)
    {
        mLookups = lookups;
    }

    /**
     *
     * @return
     *
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="resourceid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.ValidationLookup"
     */
    public Set getValidationLookups()
    {
        return mValidationLookups;
    }

    public void setValidationLookups(Set validationLookups)
    {
        mValidationLookups = validationLookups;
    }

    /**
     *
     * @return
     *
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="resourceid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.ValidationExternal"
     */
    public Set getValidationExternals()
    {
        return mValidationExternals;
    }

    public void setValidationExternals(Set validationExternals)
    {
        mValidationExternals = validationExternals;
    }

    /**
     *
     * @return
     *
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="resourceid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.ValidationExpression"
     */
    public Set getValidationExpressions()
    {
        return mValidationExpressions;
    }

    public void setValidationExpressions(Set validationExpressions)
    {
        mValidationExpressions = validationExpressions;
    }

    /**
     *
     * @return
     *
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="resourceid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.UpdateHelp"
     */
    public Set getUpdateHelps()
    {
        return mUpdateHelps;
    }

    public void setUpdateHelps(Set updateHelps)
    {
        mUpdateHelps = updateHelps;
    }

    /**
     * Returns the path to the metadata object.  Similar to how the
     * RETS Client does it.
     *
     * @return a string with the path.
     */
    public String getPath()
    {
        String systemId = mSystemid.getPath();
        if (StringUtils.isEmpty(systemId))
        {
            return mResourceID;
        }
        else
        {
            return systemId + ":" + mResourceID;
        }
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof Resource)) return false;
        Resource castOther = (Resource) other;
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
    private String mResourceID;

    /** nullable persistent field */
    private ResourceStandardNameEnum mStandardName;

    /** nullable persistent field */
    private String mVisibleName;

    /** nullable persistent field */
    private String mDescription;

    /** nullable persistent field */
    private String mKeyField;

    /** nullable persistent field */
    private String mClassVersion;

    /** nullable persistent field */
    private Date mClassDate;

    /** nullable persistent field */
    private String mObjectVersion;

    /** nullable persistent field */
    private Date mObjectDate;

    /** nullable persistent field */
    private String mSearchHelpVersion;

    /** nullable persistent field */
    private Date mSearchHelpDate;

    /** nullable persistent field */
    private String mEditMaskVersion;

    /** nullable persistent field */
    private Date mEditMaskDate;

    /** nullable persistent field */
    private String mLookupVersion;

    /** nullable persistent field */
    private Date mLookupDate;

    /** nullable persistent field */
    private String mUpdateHelpVersion;

    /** nullable persistent field */
    private Date mUpdateHelpDate;

    /** nullable persistent field */
    private String mValidationExpressionVersion;

    /** nullable persistent field */
    private Date mValidationExpressionDate;

    /** nullable persistent field */
    private String mValidationLookupVersion;

    /** nullable persistent field */
    private Date mValidationLookupDate;

    /** nullable persistent field */
    private String mValidationExternalVersion;

    /** nullable persistent field */
    private Date mValidationExternalDate;

    /** nullable persistent field */
    private MSystem mSystemid;

    /** persistent field */
    private Set mClasses;

    /** persistent field */
    private Set mObjects;

    /** persistent field */
    private Set mSearchHelps;

    /** persistent field */
    private Set mEditMasks;

    /** persistent field */
    private Set mLookups;

    /** persistent field */
    private Set mValidationLookups;

    /** persistent field */
    private Set mValidationExternals;

    /** persistent field */
    private Set mValidationExpressions;

    /** persistent field */
    private Set mUpdateHelps;
}
