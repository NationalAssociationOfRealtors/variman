package org.realtors.rets.common.metadata.types;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataElement;
import org.realtors.rets.common.metadata.MetadataType;

public class MValidationExternal extends MetaObject {
	private static final String METADATATYPENAME = "ValidationExternal";

	private static final MetadataType[] CHILDREN = { MetadataType.VALIDATION_EXTERNAL_TYPE };
	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String VALIDATIONEXTERNALNAME = "ValidationExternalName";
	public static final String SEARCHRESOURCE = "SearchResource";
	public static final String SEARCHCLASS = "SearchClass";
	public static final String VERSION = "Version";
	public static final String DATE = "Date";

    private static final List<MetadataElement> sAttributes =
    	new ArrayList<MetadataElement>()
        {{
    		add(new MetadataElement(METADATAENTRYID, sRETSID, sREQUIRED));
    		add(new MetadataElement(VALIDATIONEXTERNALNAME, sRETSNAME, sREQUIRED));
    		add(new MetadataElement(SEARCHRESOURCE, sRETSNAME, sREQUIRED));
    		add(new MetadataElement(SEARCHCLASS, sRETSNAME, sREQUIRED));
    		add(new MetadataElement(VERSION, sAttrVersion, sREQUIRED));
    		add(new MetadataElement(DATE, sAttrDate, sREQUIRED));
        }};    
	
    public static void addAttributes(String name, AttrType type)
	{
		sAttributes.add(new MetadataElement(name, type));
	}
        
	public MValidationExternal() {
		this(DEFAULT_PARSING);
	}

	public MValidationExternal(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getValidationExternalName() {
		return getStringAttribute(VALIDATIONEXTERNALNAME);
	}

	public String getSearchResource() {
		return getStringAttribute(SEARCHRESOURCE);
	}

	public String getSearchClass() {
		return getStringAttribute(SEARCHCLASS);
	}

	public int getVersion() {
		return getIntAttribute(VERSION);
	}

	public Date getDate() {
		return getDateAttribute(DATE);
	}

	public MValidationExternalType[] getMValidationExternalTypes() {
		MValidationExternalType[] tmpl = new MValidationExternalType[0];
		return (MValidationExternalType[]) getChildren(MetadataType.VALIDATION_EXTERNAL_TYPE).toArray(tmpl);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return CHILDREN;
	}

	@Override
	protected String getIdAttr() {
		return VALIDATIONEXTERNALNAME;
	}

	@Override
	public final String getMetadataTypeName() {
		return METADATATYPENAME;
	}
	
	@Override
	public final MetadataType getMetadataType() {
		return MetadataType.VALIDATION_EXTERNAL;
	}

	@Override
	protected void addAttributesToMap(Map attributeMap) {
		for (MetadataElement element : sAttributes)
		{
			attributeMap.put(element.getName(), element.getType());
		}
	}

}
