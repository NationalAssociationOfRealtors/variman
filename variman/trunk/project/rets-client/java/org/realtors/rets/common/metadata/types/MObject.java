package org.realtors.rets.common.metadata.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataElement;
import org.realtors.rets.common.metadata.MetadataType;

public class MObject extends MetaObject {
	private static final String METADATATYPENAME = "Object";

	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String OBJECTTYPE = "ObjectType";
	public static final String MIMETYPE = "MIMEType";
	public static final String VISIBLENAME = "VisibleName";
	public static final String DESCRIPTION = "Description";
	public static final String OBJECTTIMESTAMP = "ObjectTimeStamp";
	public static final String OBJECTCOUNT = "ObjectCount";

	private static final List<MetadataElement> sAttributes =
		new ArrayList<MetadataElement>()
		{{
			add(new MetadataElement(METADATAENTRYID, sRETSID, RetsVersion.RETS_1_7, sREQUIRED));
			add(new MetadataElement(OBJECTTYPE, sAlphanum24, sREQUIRED));
			add(new MetadataElement(MIMETYPE, sText64, sREQUIRED));
			add(new MetadataElement(VISIBLENAME, sPlaintext64, sREQUIRED));
			add(new MetadataElement(DESCRIPTION, sPlaintext128));
			add(new MetadataElement(OBJECTTIMESTAMP, sRETSNAME, RetsVersion.RETS_1_7));
			add(new MetadataElement(OBJECTCOUNT, sRETSNAME, RetsVersion.RETS_1_7));
		}};

	public MObject() {
		this(DEFAULT_PARSING);
	}

	public MObject(boolean strictParsing) {
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
		for (MetadataElement element : MObject.sAttributes)
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

	public String getObjectType() {
		return getStringAttribute(OBJECTTYPE);
	}

	public void setObjectType(String objectType) {
		String objectTypeStr = sAlphanum24.render(objectType);
		setAttribute(OBJECTTYPE, objectTypeStr);
	}

	public String getMIMEType() {
		return getStringAttribute(MIMETYPE);
	}

	public void setMIMEType(String mimeType) {
		String mimeTypeStr = sText64.render(mimeType);
		setAttribute(MIMETYPE, mimeTypeStr);
	}

	public String getVisibleName() {
		return getStringAttribute(VISIBLENAME);
	}

	public void setVisibleName(String visibleName) {
		String visibleNameStr = sPlaintext64.render(visibleName);
		setAttribute(VISIBLENAME, visibleNameStr);
	}

	public String getDescription() {
		return getStringAttribute(DESCRIPTION);
	}

	public void setDescription(String description) {
		String descriptionStr = sPlaintext128.render(description);
		setAttribute(DESCRIPTION, descriptionStr);
	}

	public String getObjectTimeStamp() {
		return getStringAttribute(OBJECTTIMESTAMP);
	}

	public void setObjectTimeStamp(String objectTimeStamp) {
		String objectTimeStampStr = sRETSNAME.render(objectTimeStamp);
		setAttribute(OBJECTTIMESTAMP, objectTimeStampStr);
	}

	public String getObjectCount() {
		return getStringAttribute(OBJECTCOUNT);
	}

	public void setObjectCount(String objectCount) {
		String objectCountStr = sRETSNAME.render(objectCount);
		setAttribute(OBJECTCOUNT, objectCountStr);
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
		return sNO_CHILDREN;
	}

	@Override
	protected String getIdAttr() {
		return OBJECTTYPE;
	}

	@Override
	public final String getMetadataTypeName() {
		return METADATATYPENAME;
	}

	@Override
	public final MetadataType getMetadataType() {
		return MetadataType.OBJECT;
	}
}
