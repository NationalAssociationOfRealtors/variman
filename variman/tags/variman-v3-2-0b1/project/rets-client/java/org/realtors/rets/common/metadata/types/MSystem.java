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

public class MSystem extends MetaObject {
	private static final String METADATATYPENAME = "System";

	private static final MResource[] EMPTY_RESOURCE_ARRAY = {};
	private static final MForeignKey[] EMPTY_FOREIGN_KEY_ARRAY = {};

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
		for (MetadataElement element : MSystem.sAttributes)
		{
			if (element.getName().equals(name)) {
				return element.isRequired();
			}
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
		if (sAttributes == null) {
			return;
		}
		
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

	public void setSystemID(String systemId) {
		String systemIdStr = sAlphanum10.render(systemId);
		setAttribute(SYSTEMID, systemIdStr);
	}

	public String getComments() {
		return getStringAttribute(COMMENTS);
	}

	public void setComments(String comment) {
		String commentStr = sText.render(comment);
		setAttribute(COMMENTS, commentStr);
	}

	public String getSystemDescription() {
		return getStringAttribute(SYSTEMDESCRIPTION);
	}

	public void setSystemDescription(String systemDescription) {
		String systemDescriptionStr = sPlaintext64.render(systemDescription);
		setAttribute(SYSTEMDESCRIPTION, systemDescriptionStr);
	}

	public Date getDate() {
		return getDateAttribute(DATE);
	}

	public void setDate(Date date) {
		String dateStr = sAttrDate.render(date);
		setAttribute(DATE, dateStr);
	}

	public int getVersion() {
		return getIntAttribute(VERSION);
	}

	public void setVersion(int version) {
		String versionStr = sAttrVersion.render(Integer.valueOf(version));
		setAttribute(VERSION, versionStr);
	}

	public String getTimeZoneOffset() {
		return getAttributeAsString(TIMEZONEOFFSET);
	}

	public void setTimeZoneOffset(String timeZoneOffset) {
//		String timeZoneOffsetStr = sTIMEZONEOFFSET.render((Integer)null); // The AttrTimeZone.render method expects an Integer.
		setAttribute(TIMEZONEOFFSET, timeZoneOffset);
	}

	public MResource getMResource(String resourceID) {
		return (MResource) getChild(MetadataType.RESOURCE, resourceID);
	}

	public Set<MResource> getResources() {
		MResource[] mResources = getMResources();
		int numResources = mResources.length;
		Set<MResource> resources = new LinkedHashSet<MResource>(numResources);
		for (MResource mResource : mResources) {
			resources.add(mResource);
		}
		return resources;
	}

	public void setResources(Set<MResource> resources) {
		MResource[] mResources = resources.toArray(EMPTY_RESOURCE_ARRAY);
		setMResources(mResources);
	}

	public MResource[] getMResources() {
		return getChildren(MetadataType.RESOURCE).toArray(EMPTY_RESOURCE_ARRAY);
	}

	public void setMResources(MResource[] resources) {
		deleteAllChildren(MetadataType.RESOURCE);
		if (resources != null) {
			addChildren(MetadataType.RESOURCE, resources);
		}
	}

	public Set<MForeignKey> getForeignKeys() {
		MForeignKey[] mForeignKeys = getMForeignKeys();
		int numForeignKeys = mForeignKeys.length;
		Set<MForeignKey> foreignkeys = new LinkedHashSet<MForeignKey>(numForeignKeys);
		for (MForeignKey mForeignKey : mForeignKeys) {
			foreignkeys.add(mForeignKey);
		}
		return foreignkeys;
	}

	public void setForeignKeys(Set<MForeignKey> foreignkeys) {
		MForeignKey[] mForeignKeys = foreignkeys.toArray(EMPTY_FOREIGN_KEY_ARRAY);
		setMForeignKeys(mForeignKeys);
	}

	public MForeignKey getMForeignKey(String foreignKeyID) {
		return (MForeignKey) getChild(MetadataType.FOREIGN_KEYS, foreignKeyID);
	}

	public MForeignKey[] getMForeignKeys() {
		return getChildren(MetadataType.FOREIGN_KEYS).toArray(EMPTY_FOREIGN_KEY_ARRAY);
	}

	public void setMForeignKeys(MForeignKey[] foreignKeys) {
		deleteAllChildren(MetadataType.FOREIGN_KEYS);
		if (foreignKeys != null) {
			addChildren(MetadataType.FOREIGN_KEYS, foreignKeys);
		}
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

	private static final MetadataType[] CHILDREN = { MetadataType.RESOURCE, MetadataType.FOREIGN_KEYS };

	@Override
	public String getLevel() {
		return "";
	}
}
