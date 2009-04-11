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

public class MUpdate extends MetaObject {
	private static final String METADATATYPENAME = "Update";
	
	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String UPDATENAME = "UpdateName";
	public static final String DESCRIPTION = "Description";
	public static final String KEYFIELD = "KeyField";
	public static final String UPDATETYPEVERSION = "UpdateTypeVersion";
	public static final String UPDATETYPEDATE = "UpdateTypeDate";

    private static final List<MetadataElement> sAttributes =
    	new ArrayList<MetadataElement>()
        {{
    		add(new MetadataElement(METADATAENTRYID, sRETSID, sREQUIRED));
    		add(new MetadataElement(UPDATENAME, sAlphanum24, sREQUIRED));
    		add(new MetadataElement(DESCRIPTION, sPlaintext64));
    		add(new MetadataElement(KEYFIELD, sRETSNAME, sREQUIRED));
    		add(new MetadataElement(UPDATETYPEVERSION, sAttrVersion, sREQUIRED));
    		add(new MetadataElement(UPDATETYPEDATE, sAttrDate, sREQUIRED));
		}};
	 
	public MUpdate() {
		this(DEFAULT_PARSING);
	}

	public MUpdate(boolean strictParsing) {
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

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getUpdateName() {
		return getStringAttribute(UPDATENAME);
	}

	public String getDescription() {
		return getStringAttribute(DESCRIPTION);
	}

	public String getKeyField() {
		return getStringAttribute(KEYFIELD);
	}

	public int getUpdateTypeVersion() {
		return getIntAttribute(UPDATETYPEVERSION);
	}

	public Date getUpdateTypeDate() {
		return getDateAttribute(UPDATETYPEDATE);
	}

	public MUpdateType getMUpdateType(String systemName) {
		return (MUpdateType) getChild(MetadataType.UPDATE_TYPE, systemName);
	}

	public MUpdateType[] getMUpdateTypes() {
		MUpdateType[] tmpl = new MUpdateType[0];
		return (MUpdateType[]) getChildren(MetadataType.UPDATE_TYPE).toArray(tmpl);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sTypes;
	}

	@Override
	protected String getIdAttr() {
		return UPDATENAME;
	}

	@Override
	public final String getMetadataTypeName() {
		return METADATATYPENAME;
	}
	
	@Override
	public final MetadataType getMetadataType() {
		return MetadataType.UPDATE;
	}
	
	private static final MetadataType[] sTypes = { MetadataType.UPDATE_TYPE };
}
