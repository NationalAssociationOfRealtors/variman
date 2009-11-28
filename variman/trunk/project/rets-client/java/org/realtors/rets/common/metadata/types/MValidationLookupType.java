package org.realtors.rets.common.metadata.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataElement;
import org.realtors.rets.common.metadata.MetadataType;

public class MValidationLookupType extends MetaObject {
	private static final String METADATATYPENAME = "ValidationLookupType";

	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String VALIDTEXT = "ValidText";
	public static final String PARENT1VALUE = "Parent1Value";
	public static final String PARENT2VALUE = "Parent2Value";

	private static final List<MetadataElement> sAttributes =
		new ArrayList<MetadataElement>()
		{{
			add(new MetadataElement(METADATAENTRYID, sRETSID, RetsVersion.RETS_1_7));
			add(new MetadataElement(VALIDTEXT, sRETSNAME));
			add(new MetadataElement(PARENT1VALUE, sRETSNAME));
			add(new MetadataElement(PARENT2VALUE, sRETSNAME));
		}};

	public MValidationLookupType() {
		this(DEFAULT_PARSING);
	}

	public MValidationLookupType(boolean strictParsing) {
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
		for (MetadataElement element : MValidationLookupType.sAttributes)
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

	public String getValidText() {
		return getStringAttribute(VALIDTEXT);
	}

	public void setValidText(String validText) {
		String validTextStr = sRETSNAME.render(validText);
		setAttribute(VALIDTEXT, validTextStr);
	}

	public String getParent1Value() {
		return getStringAttribute(PARENT1VALUE);
	}

	public void setParent1Value(String parent1Value) {
		String parent1ValueStr = sRETSNAME.render(parent1Value);
		setAttribute(PARENT1VALUE, parent1ValueStr);
	}

	public String getParent2Value() {
		return getStringAttribute(PARENT2VALUE);
	}

	public void setParent2Value(String parent2Value) {
		String parent2ValueStr = sRETSNAME.render(parent2Value);
		setAttribute(PARENT2VALUE, parent2ValueStr);
	}

	public MValidationLookup getMValidationLookup() {
		MValidationLookup validationlookup = (MValidationLookup)getParent();
		return validationlookup;
	}

	public void setMValidationLookup(MValidationLookup validationlookup) {
		setParent(validationlookup);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sNO_CHILDREN;
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
		return MetadataType.VALIDATION_LOOKUP_TYPE;
	}
}
