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

public class MUpdate extends MetaObject {
	private static final String METADATATYPENAME = "Update";
	
	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String UPDATENAME = "UpdateName";
	public static final String DESCRIPTION = "Description";
	public static final String KEYFIELD = "KeyField";
	public static final String UPDATETYPEVERSION = "UpdateTypeVersion";
	public static final String UPDATETYPEDATE = "UpdateTypeDate";

    private static final List<MetadataElement> sAttributes =
    	new ArrayList<MetadataElement>()
        {{
    		add(new MetadataElement(METADATAENTRYID, sRETSID, sREQUIRED));
    		add(new MetadataElement(UPDATENAME, sAlphanum24, sREQUIRED));
    		add(new MetadataElement(DESCRIPTION, sPlaintext64));
    		add(new MetadataElement(KEYFIELD, sRETSNAME, sREQUIRED));
    		add(new MetadataElement(UPDATETYPEVERSION, sAttrVersion, sREQUIRED));
    		add(new MetadataElement(UPDATETYPEDATE, sAttrDate, sREQUIRED));
		}};
	    
	public static void addAttributes(String name, AttrType type)
	{
		sAttributes.add(new MetadataElement(name, type));
	}
		
	public MUpdate() {
		this(DEFAULT_PARSING);
	}

	public MUpdate(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getUpdateName() {
		return getStringAttribute(UPDATENAME);
	}

	public String getDescription() {
		return getStringAttribute(DESCRIPTION);
	}

	public String getKeyField() {
		return getStringAttribute(KEYFIELD);
	}

	public int getUpdateTypeVersion() {
		return getIntAttribute(UPDATETYPEVERSION);
	}

	public Date getUpdateTypeDate() {
		return getDateAttribute(UPDATETYPEDATE);
	}

	public MUpdateType getMUpdateType(String systemName) {
		return (MUpdateType) getChild(MetadataType.UPDATE_TYPE, systemName);
	}

	public MUpdateType[] getMUpdateTypes() {
		MUpdateType[] tmpl = new MUpdateType[0];
		return (MUpdateType[]) getChildren(MetadataType.UPDATE_TYPE).toArray(tmpl);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sTypes;
	}

	@Override
	protected String getIdAttr() {
		return UPDATENAME;
	}

	@Override
	public final String getMetadataTypeName() {
		return METADATATYPENAME;
	}
	
	@Override
	public final MetadataType getMetadataType() {
		return MetadataType.UPDATE;
	}
	
	@Override
	protected void addAttributesToMap(Map attributeMap) {
		for (MetadataElement element : sAttributes)
		{
			attributeMap.put(element.getName(), element.getType());
		}
	}

	private static final MetadataType[] sTypes = { MetadataType.UPDATE_TYPE };
}
