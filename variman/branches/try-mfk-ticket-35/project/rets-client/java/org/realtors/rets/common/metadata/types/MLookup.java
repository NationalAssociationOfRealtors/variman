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

public class MLookup extends MetaObject {
	private static final String METADATATYPENAME = "Lookup";

	private static final MLookupType[] EMPTY_LOOKUP_TYPE_ARRAY = {};

	private static final MetadataType[] CHILDREN = { MetadataType.LOOKUP_TYPE };

	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String LOOKUPNAME = "LookupName";
	public static final String VISIBLENAME = "VisibleName";
	public static final String VERSION = "Version";
	public static final String DATE = "Date";

	private static final List<MetadataElement> sAttributes =
		new ArrayList<MetadataElement>()
		{{
			add(new MetadataElement(METADATAENTRYID, sRETSID, RetsVersion.RETS_1_7, sREQUIRED));
			add(new MetadataElement(LOOKUPNAME, sRETSNAME, sREQUIRED));
			add(new MetadataElement(VISIBLENAME, sPlaintext64, sREQUIRED));
			add(new MetadataElement(VERSION, sAttrVersion, sREQUIRED));
			add(new MetadataElement(DATE, sAttrDate, sREQUIRED));
		}};

	public MLookup() {
		this(DEFAULT_PARSING);
	}

	public MLookup(boolean strictParsing) {
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
		for (MetadataElement element : MLookup.sAttributes)
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

	public String getLookupName() {
		return getStringAttribute(LOOKUPNAME);
	}

	public void setLookupName(String lookupName) {
		String lookupNameStr = sRETSNAME.render(lookupName);
		setAttribute(LOOKUPNAME, lookupNameStr);
	}

	public String getVisibleName() {
		return getStringAttribute(VISIBLENAME);
	}

	public void setVisibleName(String visibleName) {
		String visibleNameStr = sPlaintext64.render(visibleName);
		setAttribute(VISIBLENAME, visibleNameStr);
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

	public MLookupType getMLookupType(String value) {
		return (MLookupType) getChild(MetadataType.LOOKUP_TYPE, value);
	}

	public Set<MLookupType> getLookupTypes() {
		MLookupType[] mLookupTypes = getMLookupTypes();
		int numLookupTypes = mLookupTypes.length;
		Set<MLookupType> lookuptypes = new LinkedHashSet<MLookupType>(numLookupTypes);
		for (MLookupType mLookupType : mLookupTypes) {
			lookuptypes.add(mLookupType);
		}
		return lookuptypes;
	}

	public void setLookupTypes(Set<MLookupType> lookuptypes) {
		MLookupType[] mLookupTypes = lookuptypes.toArray(EMPTY_LOOKUP_TYPE_ARRAY);
		setMLookupTypes(mLookupTypes);
	}

	public MLookupType[] getMLookupTypes() {
		return getChildren(MetadataType.LOOKUP_TYPE).toArray(EMPTY_LOOKUP_TYPE_ARRAY);
	}

	public void setMLookupTypes(MLookupType[] lookupTypes) {
		deleteAllChildren(MetadataType.LOOKUP_TYPE);
		if (lookupTypes != null) {
			addChildren(MetadataType.LOOKUP_TYPE, lookupTypes);
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
		return LOOKUPNAME;
	}

	@Override
	public final String getMetadataTypeName() {
		return METADATATYPENAME;
	}

	@Override
	public final MetadataType getMetadataType() {
		return MetadataType.LOOKUP;
	}
}
