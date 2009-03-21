package org.realtors.rets.common.metadata.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataElement;
import org.realtors.rets.common.metadata.MetadataType;

public class MEditMask extends MetaObject {
	private static final String METADATATYPENAME = "EditMask";
	
	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String EDITMASKID = "EditMaskID";
	public static final String VALUE = "Value";

    private static final List<MetadataElement> sAttributes =
    	new ArrayList<MetadataElement>()
        {{
        	add(new MetadataElement(METADATAENTRYID, sRETSID, sREQUIRED));
        	add(new MetadataElement(EDITMASKID, sRETSNAME, sREQUIRED));
        	add(new MetadataElement(VALUE, sText256, sREQUIRED));
        }};

	public static void addAttributes(String name, AttrType type)
	{
		sAttributes.add(new MetadataElement(name, type));
	}
    	
	public MEditMask() {
		this(DEFAULT_PARSING);
	}

	public MEditMask(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getEditMaskID() {
		return getStringAttribute(EDITMASKID);
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
		return EDITMASKID;
	}

	@Override
	public final String getMetadataTypeName() {
		return METADATATYPENAME;
	}
	
	@Override
	public final MetadataType getMetadataType() {
		return MetadataType.EDITMASK;
	}

	@Override
	protected void addAttributesToMap(Map attributeMap) {
		for (MetadataElement element : sAttributes)
		{
			attributeMap.put(element.getName(), element.getType());
		}
	}

}
