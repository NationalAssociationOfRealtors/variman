package org.realtors.rets.common.metadata.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataElement;
import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.attrib.AttrGenericText;

public class MUpdateType extends MetaObject {
	private static final String METADATATYPENAME = "UpdateType";
	
	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String SYSTEMNAME = "SystemName";
	public static final String SEQUENCE = "Sequence";
	public static final String ATTRIBUTES = "Attributes";
	public static final String DEFAULT = "Default";
	public static final String VALIDATIONEXPRESSIONID = "ValidationExpressionID";
	public static final String UPDATEHELPID = "UpdateHelpID";
	public static final String VALIDATIONLOOKUPNAME = "ValidationLookupName";
	public static final String VALIDATIONEXTERNALNAME = "ValidationExternalName";
	public static final String MAXUPDATE = "MaxUpdate";
	
    private static final List<MetadataElement> sAttributes =
    	new ArrayList<MetadataElement>()
        {{
    		add(new MetadataElement(METADATAENTRYID, sRETSID, sREQUIRED));
    		add(new MetadataElement(SYSTEMNAME, sRETSNAME, sREQUIRED));
    		add(new MetadataElement(SEQUENCE, sAttrNumeric));
    		add(new MetadataElement(ATTRIBUTES, sAttributes1to5, sREQUIRED));
    		add(new MetadataElement(DEFAULT, sPlaintext));
    		add(new MetadataElement(VALIDATIONEXPRESSIONID, sAlphanum64));
    		add(new MetadataElement(UPDATEHELPID, sRETSNAME));
    		add(new MetadataElement(VALIDATIONLOOKUPNAME, sRETSNAME));
    		add(new MetadataElement(VALIDATIONEXTERNALNAME, sRETSNAME));
    		add(new MetadataElement(MAXUPDATE, sAttrNumeric));
		}};
	 
	public MUpdateType() {
		this(DEFAULT_PARSING);

	}

	public MUpdateType(boolean strictParsing) {
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

	public String getSystemName() {
		return getStringAttribute(SYSTEMNAME);
	}

	public int getSequence() {
		return getIntAttribute(SEQUENCE);
	}

	public String getAttributes() {
		return getStringAttribute(ATTRIBUTES);
	}

	public String getDefault() {
		return getStringAttribute(DEFAULT);
	}

	public String getValidationExpressionID() {
		return getStringAttribute(VALIDATIONEXPRESSIONID);
	}

	public String getUpdateHelpID() {
		return getStringAttribute(UPDATEHELPID);
	}

	public String getValidationLookupName() {
		return getStringAttribute(VALIDATIONLOOKUPNAME);
	}

	public String getValidationExternalName() {
		return getStringAttribute(VALIDATIONEXTERNALNAME);
	}

	public int getMaxUpdate() {
		return getIntAttribute(MAXUPDATE);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sNoChildren;
	}

	@Override
	protected String getIdAttr() {
		return SYSTEMNAME;
	}

	@Override
	public final String getMetadataTypeName() {
		return METADATATYPENAME;
	}
	
	@Override
	public final MetadataType getMetadataType() {
		return MetadataType.UPDATE_TYPE;
	}
}
