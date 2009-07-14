package org.realtors.rets.common.metadata.types;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataElement;
import org.realtors.rets.common.metadata.MetadataType;

public class MSystem extends MetaObject {
	private static final String METADATATYPENAME = "System";
	
	public static final String SYSTEMID = "SystemID";
	public static final String SYSTEMDESCRIPTION = "SystemDescription";
	public static final String COMMENTS = "COMMENTS";
	public static final String DATE = "Date";
	public static final String VERSION = "Version";
	// 1.7.2
	public static final String TIMEZONEOFFSET = "TimeZoneOffset";
	
    private static final List<MetadataElement> sAttributes =
    	new ArrayList<MetadataElement>()
        {{
        	add(new MetadataElement(SYSTEMID, sAlphanum10, sREQUIRED));
        	add(new MetadataElement(SYSTEMDESCRIPTION, sPlaintext64));
        	add(new MetadataElement(DATE, sAttrDate, sREQUIRED));
        	add(new MetadataElement(VERSION, sAttrVersion, sREQUIRED));
        	add(new MetadataElement(COMMENTS, sText));
        	add(new MetadataElement(TIMEZONEOFFSET, sTIMEZONEOFFSET, RetsVersion.RETS_1_7_2));
        }};
	
	public MSystem() {
		this(DEFAULT_PARSING);
	}

	public MSystem(boolean strictParsing) {
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
	protected void addAttributesToMap(Map attributeMap) 
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
		for (MetadataElement element : this.sAttributes)
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
		int index = -1;
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

	public String getSystemID() {
		return getStringAttribute(SYSTEMID);
	}

	public String getComment() {
		return getStringAttribute(COMMENTS);
	}

	public String getSystemDescription() {
		return getStringAttribute(SYSTEMDESCRIPTION);
	}

	public Date getDate() {
		return getDateAttribute(DATE);
	}

	public int getVersion() {
		return getIntAttribute(VERSION);
	}
	
	public String getTimeZoneOffset() {
		return getAttributeAsString(TIMEZONEOFFSET);
	}

	public MResource getMResource(String resourceID) {
		return (MResource) getChild(MetadataType.RESOURCE, resourceID);
	}

	public MResource[] getMResources() {
		MResource[] tmpl = new MResource[0];
		return (MResource[]) getChildren(MetadataType.RESOURCE).toArray(tmpl);
	}

	public MForeignKey getMForeignKey(String foreignKeyID) {
		return (MForeignKey) getChild(MetadataType.FOREIGNKEYS, foreignKeyID);
	}

	public MForeignKey[] getMForeignKeys() {
		MForeignKey[] tmpl = new MForeignKey[0];
		return (MForeignKey[]) getChildren(MetadataType.FOREIGNKEYS).toArray(tmpl);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return CHILDREN;
	}

	@Override
	protected String getIdAttr() {
		return null;
	}

	@Override
	public final String getMetadataTypeName() {
		return METADATATYPENAME;
	}
	
	@Override
	public final MetadataType getMetadataType() {
		return MetadataType.SYSTEM;
	}	

	public static final MetadataType[] CHILDREN = { MetadataType.RESOURCE, MetadataType.FOREIGNKEYS };
}
