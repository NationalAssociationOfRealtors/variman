package org.realtors.rets.common.metadata.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataElement;
import org.realtors.rets.common.metadata.MetadataType;

public class MValidationExternalType extends MetaObject {
	public static final String METADATATYPENAME = "ValidationExternalType";

	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String SEARCHFIELD = "SearchField";
	public static final String DISPLAYFIELD = "DisplayField";
	public static final String RESULTFIELDS = "ResultFields";
	
    private static final List<MetadataElement> sAttributes =
    	new ArrayList<MetadataElement>()
        {{
    		add(new MetadataElement(METADATAENTRYID, sRETSID, sREQUIRED));
    		add(new MetadataElement(SEARCHFIELD, sPlaintext512, sREQUIRED));
    		add(new MetadataElement(DISPLAYFIELD, sPlaintext512, sREQUIRED));
    		add(new MetadataElement(RESULTFIELDS, sPlaintext1024, sREQUIRED));
        }};
        
	public static void addAttributes(String name, AttrType type)
	{
		sAttributes.add(new MetadataElement(name, type));
	}

	public MValidationExternalType() {
		this(DEFAULT_PARSING);
	}

	public MValidationExternalType(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getSearchField() {
		return getStringAttribute(SEARCHFIELD);
	}

	public String getDisplayField() {
		return getStringAttribute(DISPLAYFIELD);
	}

	public String getResultFields() {
		return getStringAttribute(RESULTFIELDS);
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
		return MetadataType.VALIDATION_EXTERNAL_TYPE;
	}

	@Override
	protected void addAttributesToMap(Map attributeMap) {
		for (MetadataElement element : sAttributes)
		{
			attributeMap.put(element.getName(), element.getType());
		}
	}

}
