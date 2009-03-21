package org.realtors.rets.common.metadata.types;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataElement;
import org.realtors.rets.common.metadata.MetadataType;

public class MLookup extends MetaObject {
	private static final String METADATATYPENAME = "Lookup";
	
	private static final MetadataType[] CHILDREN = { MetadataType.LOOKUP_TYPE };
	private static final MLookupType[] EMPTYLOOKUPTYPES = {};
	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String LOOKUPNAME = "LookupName";
	public static final String VISIBLENAME = "VisibleName";
	public static final String VERSION = "Version";
	public static final String DATE = "Date";

    private static final List<MetadataElement> sAttributes =
    	new ArrayList<MetadataElement>()
        {{
    		add(new MetadataElement(METADATAENTRYID, sRETSID, sREQUIRED));
    		add(new MetadataElement(LOOKUPNAME, sRETSNAME, sREQUIRED));
    		add(new MetadataElement(VISIBLENAME, sPlaintext64, sREQUIRED));
    		add(new MetadataElement(VERSION, sAttrVersion, sREQUIRED));
    		add(new MetadataElement(DATE, sAttrDate, sREQUIRED));
        }};
        
	public static void addAttributes(String name, AttrType type)
	{
		sAttributes.add(new MetadataElement(name, type));
	}
        
	public MLookup() {
		this(DEFAULT_PARSING);
	}

	public MLookup(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getLookupName() {
		return getStringAttribute(LOOKUPNAME);
	}

	public String getVisibleName() {
		return getStringAttribute(VISIBLENAME);
	}

	public int getVersion() {
		return getIntAttribute(VERSION);
	}

	public Date getDate() {
		return getDateAttribute(DATE);
	}

	public MLookupType getMLookupType(String value) {
		return (MLookupType) getChild(MetadataType.LOOKUP_TYPE, value);
	}

	public MLookupType[] getMLookupTypes() {
		return (MLookupType[]) getChildren(MetadataType.LOOKUP_TYPE).toArray(EMPTYLOOKUPTYPES);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return CHILDREN;
	}

	@Override
	protected String getIdAttr() {
		return LOOKUPNAME;
	}

	@Override
	public final String getMetadataTypeName() {
		return METADATATYPENAME;
	}
	
	@Override
	public final MetadataType getMetadataType() {
		return MetadataType.LOOKUP;
	}
	
	@Override
	protected void addAttributesToMap(Map attributeMap) {
		for (MetadataElement element : sAttributes)
		{
			attributeMap.put(element.getName(), element.getType());
		}
	}

}
