package org.realtors.rets.common.metadata.types;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataElement;
import org.realtors.rets.common.metadata.MetadataType;

public class MValidationLookup extends MetaObject {
	private static final String METADATATYPENAME = "ValidationLookup";

	private static final MValidationLookupType[] EMPTY_VALIDATION_LOOKUP_TYPE_ARRAY = {};

	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String VALIDATIONLOOKUPNAME = "ValidationLookupName";
	public static final String PARENT1FIELD = "Parent1Field";
	public static final String PARENT2FIELD = "Parent2Field";
	public static final String VERSION = "Version";
	public static final String DATE = "Date";

	private static final List<MetadataElement> sAttributes =
		new ArrayList<MetadataElement>()
		{{
			add(new MetadataElement(METADATAENTRYID, sRETSID, RetsVersion.RETS_1_7, sREQUIRED));
			add(new MetadataElement(VALIDATIONLOOKUPNAME, sRETSNAME, sREQUIRED));
			add(new MetadataElement(PARENT1FIELD, sRETSNAME));
			add(new MetadataElement(PARENT2FIELD, sRETSNAME));
			add(new MetadataElement(VERSION, sAttrVersion, sREQUIRED));
			add(new MetadataElement(DATE, sAttrDate, sREQUIRED));
		}};

	private static final MetadataType[] CHILDREN = { MetadataType.VALIDATION_LOOKUP_TYPE };

	public MValidationLookup() {
		this(DEFAULT_PARSING);
	}

	public MValidationLookup(boolean strictParsing) {
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
		for (MetadataElement element : MValidationLookup.sAttributes)
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

	public String getValidationLookupName() {
		return getStringAttribute(VALIDATIONLOOKUPNAME);
	}

	public void setValidationLookupName(String validationLookupName) {
		String validationLookupNameStr = sRETSNAME.render(validationLookupName);
		setAttribute(VALIDATIONLOOKUPNAME, validationLookupNameStr);
	}

	public String getParent1Field() {
		return getStringAttribute(PARENT1FIELD);
	}

	public void setParent1Field(String parent1Field) {
		String parent1FieldStr = sRETSNAME.render(parent1Field);
		setAttribute(PARENT1FIELD, parent1FieldStr);
	}

	public String getParent2Field() {
		return getStringAttribute(PARENT2FIELD);
	}

	public void setParent2Field(String parent2Field) {
		String parent2FieldStr = sRETSNAME.render(parent2Field);
		setAttribute(PARENT2FIELD, parent2FieldStr);
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

	public Set<MValidationLookupType> getValidationLookupTypes() {
		MValidationLookupType[] mValidationLookupTypes = getMValidationLookupTypes();
		int numValidationLookupTypes = mValidationLookupTypes.length;
		Set<MValidationLookupType> validationlookuptypes = new LinkedHashSet<MValidationLookupType>(numValidationLookupTypes);
		for (MValidationLookupType mValidationLookupType : mValidationLookupTypes) {
			validationlookuptypes.add(mValidationLookupType);
		}
		return validationlookuptypes;
	}

	public void setValidationLookupTypes(Set<MValidationLookupType> validationlookuptypes) {
		MValidationLookupType[] mValidationLookupTypes = validationlookuptypes.toArray(EMPTY_VALIDATION_LOOKUP_TYPE_ARRAY);
		setMValidationLookupTypes(mValidationLookupTypes);
	}

	public MValidationLookupType[] getMValidationLookupTypes() {
		return getChildren(MetadataType.VALIDATION_LOOKUP_TYPE).toArray(EMPTY_VALIDATION_LOOKUP_TYPE_ARRAY);
	}

	public void setMValidationLookupTypes(MValidationLookupType[] validationLookupTypes) {
		deleteAllChildren(MetadataType.VALIDATION_LOOKUP_TYPE);
		if (validationLookupTypes != null) {
			addChildren(MetadataType.VALIDATION_LOOKUP_TYPE, validationLookupTypes);
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
		return VALIDATIONLOOKUPNAME;
	}

	@Override
	public final String getMetadataTypeName() {
		return METADATATYPENAME;
	}

	@Override
	public final MetadataType getMetadataType() {
		return MetadataType.VALIDATION_LOOKUP;
	}
}
