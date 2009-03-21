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
        
	public static void addAttributes(String name, AttrType type)
	{
		sAttributes.add(new MetadataElement(name, type));
	}
        
	private static final MetadataType[] sChildren = { MetadataType.VALIDATION_LOOKUP_TYPE };

	public MValidationLookup() {
		this(DEFAULT_PARSING);
	}

	public MValidationLookup(boolean strictParsing) {
		super(strictParsing);
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
	
	@Override
	protected void addAttributesToMap(Map attributeMap) {
		for (MetadataElement element : sAttributes)
		{
			attributeMap.put(element.getName(), element.getType());
		}
	}

	public MValidationLookupType[] getMValidationLookupTypes() {
		MValidationLookupType[] tmpl = new MValidationLookupType[0];
		return (MValidationLookupType[]) getChildren(MetadataType.VALIDATION_LOOKUP_TYPE).toArray(tmpl);
	}

}
