package org.realtors.rets.server.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @hibernate.class table="rets_metadata_resource"
 * @hibernate.jcs-cache usage="read-write"
 */
public class Resource extends ServerMetadata implements Serializable
{
    public Resource(long id)
    {
        this();
        mId = new Long(id);
    }

    public Resource()
    {
        mId = null;
        mClasses = Collections.EMPTY_SET;
        mObjects = Collections.EMPTY_SET;
        mSearchHelps = Collections.EMPTY_SET;
        mEditMasks  = Collections.EMPTY_SET;
        mLookups = Collections.EMPTY_SET;
        mValidationLookups = Collections.EMPTY_SET;
        mValidationExternals = Collections.EMPTY_SET;
        mValidationExpressions = Collections.EMPTY_SET;
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
     * @return a ResourceStandardNameEnum
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
     * @return a String
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
     * @return a String
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
     * @return a MSystem object
     *
     * @hibernate.many-to-one
     * @hibernate.column name="systemid"
     *   not-null="true"
     * 
     */
    public MSystem getSystem()
    {
        return mSystem;
    }

    public void setSystem(MSystem system)
    {
        mSystem = system;
    }

    /**
     *
     * @return a Set of MClass objects
     *
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="resourceid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.MClass"
     * @hibernate.collection-jcs-cache usage="read-write"
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
     * @return a Set of MObject objects
     *
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="resourceid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.MObject"
     * @hibernate.collection-jcs-cache usage="read-write"
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
     * @return a Set of SearchHelp objects
     *
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="resourceid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.SearchHelp"
     * @hibernate.collection-jcs-cache usage="read-write"
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
     * @return a Set of EditMasks
     *
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="resourceid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.EditMask"
     * @hibernate.collection-jcs-cache usage="read-write"
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
     * @return a Set of Lookup objects
     *
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="resourceid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.Lookup"
     * @hibernate.collection-jcs-cache usage="read-write"
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
     * @return a Set of ValidationLookups
     *
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="resourceid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.ValidationLookup"
     * @hibernate.collection-jcs-cache usage="read-write"
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
     * @return a Set of ValidationExternals
     *
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="resourceid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.ValidationExternal"
     * @hibernate.collection-jcs-cache usage="read-write"
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
     * @return a Set of ValidationExpressions
     *
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="resourceid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.ValidationExpression"
     * @hibernate.collection-jcs-cache usage="read-write"
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
     * @return a Set of UpdateHelp objects
     *
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="resourceid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.UpdateHelp"
     * @hibernate.collection-jcs-cache usage="read-write"
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
     * Returns the level of the metadata object.
     *
     * @return a string of level
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
        mLevel = mSystem.getPath();
    }

    public String getPath()
    {
        if (StringUtils.isEmpty(mLevel))
        {
            return mResourceID;
        }
        else
        {
            return mLevel + ":" + mResourceID;
        }
    }

    public List getChildren()
    {
        List children = new ArrayList();
        children.add(mClasses.toArray(new MClass[0]));
        children.add(mObjects.toArray(new MObject[0]));
        children.add(mSearchHelps.toArray(new SearchHelp[0]));
        children.add(mEditMasks.toArray(new EditMask[0]));
        children.add(mLookups.toArray(new Lookup[0]));
        children.add(mValidationLookups.toArray(new ValidationLookup[0]));
        children.add(mValidationExternals.toArray(new ValidationExternal[0]));
        children.add(
            mValidationExpressions.toArray(new ValidationExpression[0]));
        return children;
    }

    public String getRetsId()
    {
        return mResourceID;
    }

    public String getTableName()
    {
        return TABLE;
    }

    public String toString()
    {
        return mResourceID;
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
    private MSystem mSystem;

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

    private String mLevel;

    public static final String TABLE = "RESOURCE";
}
