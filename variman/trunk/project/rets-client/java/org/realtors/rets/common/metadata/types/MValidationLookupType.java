package org.realtors.rets.common.metadata.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    		add(new MetadataElement(METADATAENTRYID, sRETSID));
    		add(new MetadataElement(VALIDTEXT, sRETSNAME));
    		add(new MetadataElement(PARENT1VALUE, sRETSNAME));
    		add(new MetadataElement(PARENT2VALUE, sRETSNAME));
        }};
        
	public static void addAttributes(String name, AttrType type)
	{
		sAttributes.add(new MetadataElement(name, type));
	}
	
	public MValidationLookupType() {
		this(DEFAULT_PARSING);
	}

	public MValidationLookupType(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getValidText() {
		return getStringAttribute(VALIDTEXT);
	}

	public String getParent1Value() {
		return getStringAttribute(PARENT1VALUE);
	}

	public String getParent2Value() {
		return getStringAttribute(PARENT2VALUE);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sNoChildren;
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

	@Override
	protected void addAttributesToMap(Map attributeMap) {
		for (MetadataElement element : sAttributes)
		{
			attributeMap.put(element.getName(), element.getType());
		}
	}

}
