package org.realtors.rets.common.metadata.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataElement;
import org.realtors.rets.common.metadata.MetadataType;

public class MLookupType extends MetaObject {
	private static final String METADATATYPENAME = "LookupType";
	
	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String LONGVALUE = "LongValue";
	public static final String SHORTVALUE = "ShortValue";
	public static final String VALUE = "Value";

    private static final List<MetadataElement> sAttributes =
    	new ArrayList<MetadataElement>()
        {{
        	add(new MetadataElement(METADATAENTRYID, sRETSID, sREQUIRED));
    		add(new MetadataElement(LONGVALUE, sText128, sREQUIRED));
    		add(new MetadataElement(SHORTVALUE, sText32, sREQUIRED));
    		add(new MetadataElement(VALUE, sText128, sREQUIRED));
        }};
	    
	public static void addAttributes(String name, AttrType type)
	{
		sAttributes.add(new MetadataElement(name, type));
	}
	
	public MLookupType() {
		this(DEFAULT_PARSING);
	}

	public MLookupType(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getLongValue() {
		return getStringAttribute(LONGVALUE);
	}

	public String getShortValue() {
		return getStringAttribute(SHORTVALUE);
	}

	public String getValue() {
		return getStringAttribute(VALUE);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sNoChildren;
	}

	@Override
	protected String getIdAttr() {
		return VALUE;
	}

	@Override
	public final String getMetadataTypeName() {
		return METADATATYPENAME;
	}
	
	@Override
	public final MetadataType getMetadataType() {
		return MetadataType.LOOKUP_TYPE;
	}
	
	@Override
	protected void addAttributesToMap(Map attributeMap) {
		for (MetadataElement element : sAttributes)
		{
			attributeMap.put(element.getName(), element.getType());
		}
	}

}
