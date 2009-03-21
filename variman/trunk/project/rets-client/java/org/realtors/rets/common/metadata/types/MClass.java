package org.realtors.rets.common.metadata.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataElement;
import org.realtors.rets.common.metadata.MetadataType;

public class MClass extends MetaObject {
	private static final String METADATATYPENAME = "Class";
	
	public static final String CLASSNAME = "ClassName";
	public static final String VISIBLENAME = "VisibleName";
	public static final String STANDARDNAME = "StandardName";
	public static final String DESCRIPTION = "Description";
	public static final String TABLEVERSION = "TableVersion";
	public static final String TABLEDATE = "TableDate";
	public static final String UPDATEVERSION = "UpdateVersion";
	public static final String UPDATEDATE = "UpdateDate";
	// 1.7.2
	public static final String CLASSTIMESTAMP = "ClassTimeStamp";
	public static final String DELETEDFLAGFIELD = "DeletedFlagField";
	public static final String DELETEDFLAGVALUE = "DeletedFlagValue";
	public static final String HASKEYINDEX = "HasKeyIndex";
	
    private static final List<MetadataElement> sAttributes =
    	new ArrayList<MetadataElement>()
        {{
    		add(new MetadataElement(CLASSNAME, sAlphanum32, sREQUIRED));
    		add(new MetadataElement(VISIBLENAME, sPlaintext32, sREQUIRED));
    		add(new MetadataElement(STANDARDNAME, sAlphanum32));
    		add(new MetadataElement(DESCRIPTION, sPlaintext128));
    		add(new MetadataElement(TABLEVERSION, sAttrVersion, sREQUIRED));
    		add(new MetadataElement(TABLEDATE, sAttrDate, sREQUIRED));
    		add(new MetadataElement(UPDATEVERSION, sAttrVersion));
    		add(new MetadataElement(UPDATEDATE, sAttrDate));
    		// RETS 1.7.2
    		add(new MetadataElement(CLASSTIMESTAMP, sRETSNAME, RetsVersion.RETS_1_7_2));
    		add(new MetadataElement(DELETEDFLAGFIELD, sRETSNAME, RetsVersion.RETS_1_7_2));
    		add(new MetadataElement(DELETEDFLAGVALUE, sAlphanum32, RetsVersion.RETS_1_7_2));
    		add(new MetadataElement(HASKEYINDEX, sAttrBoolean, RetsVersion.RETS_1_7_2));
        }};
	
	
	private static MetadataType[] sTypes = { MetadataType.UPDATE, MetadataType.TABLE };

	public MClass() {
		this(DEFAULT_PARSING);
	}

	public MClass(boolean strictParsing) {
		super(strictParsing);
	}

	public static void addAttributes(String name, AttrType type)
	{
		sAttributes.add(new MetadataElement(name, type));
	}
	
	@Override
	public MetadataType[] getChildTypes() {
		return sTypes;
	}

	public String getClassName() {
		return getStringAttribute(CLASSNAME);
	}

	public String getVisibleName() {
		return getStringAttribute(VISIBLENAME);
	}

	public String getStandardName() {
		return getStringAttribute(STANDARDNAME);
	}

	public String getDescription() {
		return getStringAttribute(DESCRIPTION);
	}

	public int getTableVersion() {
		return getIntAttribute(TABLEVERSION);
	}

	public Date getTableDate() {
		return getDateAttribute(TABLEDATE);
	}

	public int getUpdateVersion() {
		return getIntAttribute(UPDATEVERSION);
	}

	public Date getUpdateDate() {
		return getDateAttribute(UPDATEDATE);
	}
	
	public String getClassTimeStamp() {
		return getStringAttribute(CLASSTIMESTAMP);
	}

	public String getDeletedFlagField() {
		return getStringAttribute(DELETEDFLAGFIELD);
	}
	
	public String getDeletedFlagValue() {
		return getStringAttribute(DELETEDFLAGVALUE);
	}
	
	public Boolean getHasKeyIndex() {
		return getBooleanAttribute(HASKEYINDEX);
	}
	
	public MUpdate getMUpdate(String updateName) {
		return (MUpdate) getChild(MetadataType.UPDATE, updateName);
	}

	public MUpdate[] getMUpdates() {
		MUpdate[] tmpl = new MUpdate[0];
		return (MUpdate[]) getChildren(MetadataType.UPDATE).toArray(tmpl);
	}

	public MTable getMTable(String systemName) {
		return (MTable) getChild(MetadataType.TABLE, systemName);
	}

	public MTable[] getMTables() {
		Collection children = getChildren(MetadataType.TABLE);
		return (MTable[]) children.toArray(new MTable[0]);
	}

	@Override
	protected String getIdAttr() {
		return CLASSNAME;
	}

	@Override
	public final String getMetadataTypeName() {
		return METADATATYPENAME;
	}
	
	@Override
	public final MetadataType getMetadataType() {
		return MetadataType.CLASS;
	}

	@Override
	protected void addAttributesToMap(Map attributeMap) {
		for (MetadataElement element : sAttributes)
		{
			attributeMap.put(element.getName(), element.getType());
		}
	}

}
