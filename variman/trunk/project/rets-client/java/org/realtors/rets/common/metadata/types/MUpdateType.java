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
	    
	public static void addAttributes(String name, AttrType type)
	{
		sAttributes.add(new MetadataElement(name, type));
	}
		
	public MUpdateType() {
		this(DEFAULT_PARSING);

	}

	public MUpdateType(boolean strictParsing) {
		super(strictParsing);
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
	
	@Override
	protected void addAttributesToMap(Map attributeMap) {
		for (MetadataElement element : sAttributes)
		{
			attributeMap.put(element.getName(), element.getType());
		}
	}
}
