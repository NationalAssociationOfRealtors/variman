package org.realtors.rets.common.metadata.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataElement;
import org.realtors.rets.common.metadata.MetadataType;

public class MObject extends MetaObject {
	private static final String METADATATYPENAME = "Object";

	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String OBJECTTYPE = "ObjectType";
	public static final String MIMETYPE = "MIMEType";
	public static final String VISIBLENAME = "VisibleName";
	public static final String DESCRIPTION = "Description";
	public static final String OBJECTTIMESTAMP = "ObjectTimeStamp";
	public static final String OBJECTCOUNT = "ObjectCount";
	
    private static final List<MetadataElement> sAttributes =
    	new ArrayList<MetadataElement>()
        {{
    		add(new MetadataElement(METADATAENTRYID, sRETSNAME, sREQUIRED));
    		add(new MetadataElement(OBJECTTYPE, sAlphanum24, sREQUIRED));
    		add(new MetadataElement(MIMETYPE, sText64, sREQUIRED));
    		add(new MetadataElement(VISIBLENAME, sPlaintext64, sREQUIRED));
    		add(new MetadataElement(DESCRIPTION, sPlaintext128));
    		add(new MetadataElement(OBJECTTIMESTAMP, sRETSNAME));
    		add(new MetadataElement(OBJECTCOUNT, sRETSNAME));
        }};
        
	public static void addAttributes(String name, AttrType type)
	{
		sAttributes.add(new MetadataElement(name, type));
	}
	
	public MObject() {
		this(DEFAULT_PARSING);
	}

	public MObject(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getObjectType() {
		return getStringAttribute(OBJECTTYPE);
	}

	public String getMIMEType() {
		return getStringAttribute(MIMETYPE);
	}

	public String getVisibleName() {
		return getStringAttribute(VISIBLENAME);
	}

	public String getDescription() {
		return getStringAttribute(DESCRIPTION);
	}
	
	public String getObjectTimeStamp() {
		return getStringAttribute(OBJECTTIMESTAMP);
	}
	
	public String getObjectCount() {
		return getStringAttribute(OBJECTCOUNT);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sNoChildren;
	}

	@Override
	protected String getIdAttr() {
		return OBJECTTYPE;
	}

	@Override
	public final String getMetadataTypeName() {
		return METADATATYPENAME;
	}
	
	@Override
	public final MetadataType getMetadataType() {
		return MetadataType.OBJECT;
	}

	@Override
	protected void addAttributesToMap(Map attributeMap) {
		for (MetadataElement element : sAttributes)
		{
			attributeMap.put(element.getName(), element.getType());
		}
	}
}
