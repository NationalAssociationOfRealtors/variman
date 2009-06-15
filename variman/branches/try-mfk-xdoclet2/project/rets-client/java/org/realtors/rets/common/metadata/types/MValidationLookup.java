package org.realtors.rets.common.metadata.types;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataElement;
import org.realtors.rets.common.metadata.MetadataType;

public class MValidationLookup extends MetaObject {
	private static final String METADATATYPENAME = "ValidationLookup";

	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String VALIDATIONLOOKUPNAME = "ValidationLookupName";
	public static final String PARENT1FIELD = "Parent1Field";
	public static final String PARENT2FIELD = "Parent2Field";
	public static final String VERSION = "Version";
	public static final String DATE = "Date";
	
    private static final List<MetadataElement> sAttributes =
    	new ArrayList<MetadataElement>()
        {{
    		add(new MetadataElement(METADATAENTRYID, sRETSID, sREQUIRED));
    		add(new MetadataElement(VALIDATIONLOOKUPNAME, sRETSNAME, sREQUIRED));
    		add(new MetadataElement(PARENT1FIELD, sRETSNAME));
    		add(new MetadataElement(PARENT2FIELD, sRETSNAME));
    		add(new MetadataElement(VERSION, sAttrVersion, sREQUIRED));
    		add(new MetadataElement(DATE, sAttrDate, sREQUIRED));
        }};
     
	private static final MetadataType[] sChildren = { MetadataType.VALIDATION_LOOKUP_TYPE };

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

	public String getValidationLookupName() {
		return getStringAttribute(VALIDATIONLOOKUPNAME);
	}

	public String getParent1Field() {
		return getStringAttribute(PARENT1FIELD);
	}

	public String getParent2Field() {
		return getStringAttribute(PARENT2FIELD);
	}

	public int getVersion() {
		return getIntAttribute(VERSION);
	}

	public Date getDate() {
		return getDateAttribute(DATE);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sChildren;
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

	public MValidationLookupType[] getMValidationLookupTypes() {
		MValidationLookupType[] tmpl = new MValidationLookupType[0];
		return (MValidationLookupType[]) getChildren(MetadataType.VALIDATION_LOOKUP_TYPE).toArray(tmpl);
	}	
}
