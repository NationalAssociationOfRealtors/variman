package org.realtors.rets.common.metadata.types;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataElement;
import org.realtors.rets.common.metadata.MetadataType;

public class MValidationExternal extends MetaObject {
	private static final String METADATATYPENAME = "ValidationExternal";

	private static final MValidationExternalType[] EMPTY_VALIDATION_EXTERNAL_TYPE_ARRAY = {};

	private static final MetadataType[] CHILDREN = { MetadataType.VALIDATION_EXTERNAL_TYPE };

	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String VALIDATIONEXTERNALNAME = "ValidationExternalName";
	public static final String SEARCHRESOURCE = "SearchResource";
	public static final String SEARCHCLASS = "SearchClass";
	public static final String VERSION = "Version";
	public static final String DATE = "Date";

	private static final List<MetadataElement> sAttributes =
		new ArrayList<MetadataElement>()
		{{
			add(new MetadataElement(METADATAENTRYID, sRETSID, RetsVersion.RETS_1_7, sREQUIRED));
			add(new MetadataElement(VALIDATIONEXTERNALNAME, sRETSNAME, sREQUIRED));
			add(new MetadataElement(SEARCHRESOURCE, sRETSNAME, sREQUIRED));
			add(new MetadataElement(SEARCHCLASS, sRETSNAME, sREQUIRED));
			add(new MetadataElement(VERSION, sAttrVersion, sREQUIRED));
			add(new MetadataElement(DATE, sAttrDate, sREQUIRED));
		}};

	private MClass clazz;

	public MValidationExternal() {
		this(DEFAULT_PARSING);
	}

	public MValidationExternal(boolean strictParsing) {
		super(strictParsing);
	}

	/**
	 * Add an attribute to the class static attributes.
	 * @param name Attribute Name
	 * @param type Attribute Type
	 * @param required TRUE, the attribute is required. FALSE otherwise.
	 */
	public static void addAttribute(String name, AttrType<?> type, boolean required)
	{
		MetadataElement element = new MetadataElement(name, type, required);
		sAttributes.add(element);
	}

	/*
	 * Add the attributes to the map. This must be done here to
	 * make sure static initialization properly takes place.
	 */
	@Override
	protected void addAttributesToMap(Map<String, AttrType<?>> attributeMap) 
	{
		for (MetadataElement element : sAttributes)
		{
			attributeMap.put(element.getName(), element.getType());
		}
	}

	/**
	 * Returns whether or not the attribute is required.
	 * @param name Name of the attribute.
	 * @return TRUE if the attribute is required, FALSE otherwise.
	 */
	@Override
	public boolean isAttributeRequired(String name)
	{
		for (MetadataElement element : MValidationExternal.sAttributes)
		{
			if (element.getName().equals(name))
				return element.isRequired();
		}
		
		return false;
	}

	/**
	 * Update (or add) the attribute. This is intended for use where the 
	 * metadata model is being changed or expanded.
	 * @param name Attribute Name
	 * @param type Attribute Type
	 * @param required TRUE, the attribute is required. FALSE otherwise.
	 */
	public static void updateAttribute(String name, AttrType<?> type, boolean required)
	{
		boolean found = false;
		if (sAttributes == null)
			return;
		
		clearAttributeMapCache();
		MetadataElement element = new MetadataElement(name, type, required);
		
		for (int i = 0; i < sAttributes.size(); i++)
		{
			if (sAttributes.get(i).getName().equals(name))
			{
				found = true;
				sAttributes.set(i, element);
				break;
			}
		}
		if (!found)
		{
			sAttributes.add(element);
		}
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public void setMetadataEntryID(String metadataEntryId) {
		String metadataEntryIdStr = sRETSID.render(metadataEntryId);
		setAttribute(METADATAENTRYID, metadataEntryIdStr);
	}

	public String getValidationExternalName() {
		return getStringAttribute(VALIDATIONEXTERNALNAME);
	}

	public void setValidationExternalName(String validationExternalName) {
		String validationExternalNameStr = sRETSNAME.render(validationExternalName);
		setAttribute(VALIDATIONEXTERNALNAME, validationExternalNameStr);
	}

	public String getSearchResource() {
		return getStringAttribute(SEARCHRESOURCE);
	}

	public void setSearchResource(String searchResource) {
		String searchResourceStr = sRETSNAME.render(searchResource);
		setAttribute(SEARCHRESOURCE, searchResourceStr);
	}

	public String getSearchClass() {
		return getStringAttribute(SEARCHCLASS);
	}

	public void setSearchClass(String searchClass) {
		String searchClassStr = sRETSNAME.render(searchClass);
		setAttribute(SEARCHCLASS, searchClassStr);
	}

	public MClass getMClass() {
		return this.clazz;
	}

	public void setMClass(MClass clazz) {
		this.clazz = clazz;
		boolean nullAll = true;
		if (this.clazz != null) {
			MResource resource = this.clazz.getMResource();
			if (resource != null) {
				String resourceId = resource.getResourceID();
				String className = this.clazz.getClassName();
				if (StringUtils.isNotBlank(resourceId) &&
					StringUtils.isNotBlank(className)
				) {
					setSearchResource(resourceId);
					setSearchClass(className);
					nullAll = false;
				}
			}
		}
		if (nullAll) {
			setSearchResource(null);
			setSearchClass(null);
		}
	}

	public int getVersion() {
		return getIntAttribute(VERSION);
	}

	public void setVersion(int version) {
		String versionStr = sAttrVersion.render(Integer.valueOf(version));
		setAttribute(VERSION, versionStr);
	}

	public Date getDate() {
		return getDateAttribute(DATE);
	}

	public void setDate(Date date) {
		String dateStr = sAttrDate.render(date);
		setAttribute(DATE, dateStr);
	}

    public Set<MValidationExternalType> getValidationExternalTypes() {
        MValidationExternalType[] mValidationExternalTypes = getMValidationExternalTypes();
        int numValidationExternalTypes = mValidationExternalTypes.length;
        Set<MValidationExternalType> validationexternaltypes = new LinkedHashSet<MValidationExternalType>(numValidationExternalTypes);
        for (MValidationExternalType mValidationExternalType : mValidationExternalTypes) {
            validationexternaltypes.add(mValidationExternalType);
        }
        return validationexternaltypes;
    }

    public void setValidationExternalTypes(Set<MValidationExternalType> validationexternaltypes) {
        MValidationExternalType[] mValidationExternalTypes = validationexternaltypes.toArray(EMPTY_VALIDATION_EXTERNAL_TYPE_ARRAY);
        setMValidationExternalTypes(mValidationExternalTypes);
    }

	public MValidationExternalType[] getMValidationExternalTypes() {
		return getChildren(MetadataType.VALIDATION_EXTERNAL_TYPE).toArray(EMPTY_VALIDATION_EXTERNAL_TYPE_ARRAY);
	}

	public void setMValidationExternalTypes(MValidationExternalType[] validationExternalTypes) {
		deleteAllChildren(MetadataType.VALIDATION_EXTERNAL_TYPE);
		if (validationExternalTypes != null) {
			addChildren(MetadataType.VALIDATION_EXTERNAL_TYPE, validationExternalTypes);
		}
	}

	public MResource getMResource() {
		MResource resource = (MResource)getParent();
		return resource;
	}

	public void setMResource(MResource resource) {
		setParent(resource);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return CHILDREN;
	}

	@Override
	protected String getIdAttr() {
		return VALIDATIONEXTERNALNAME;
	}

	@Override
	public final String getMetadataTypeName() {
		return METADATATYPENAME;
	}

	@Override
	public final MetadataType getMetadataType() {
		return MetadataType.VALIDATION_EXTERNAL;
	}
}
