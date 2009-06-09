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

	/**
	 * Add an attribute to the class static attributes.
	 * @param name Attribute Name
	 * @param type Attribute Type
	 * @param required TRUE, the attribute is required. FALSE otherwise.
	 */
	public static void addAttribute(String name, AttrType<?> type, boolean required)
	{
		MetadataElement element = new MetadataElement(name, type, required);
		sAttributes.add(element);
	}

	/*
	 * Add the attributes to the map. This must be done here to
	 * make sure static initialization properly takes place.
	 */
	@Override
	protected void addAttributesToMap(Map attributeMap) 
	{
		for (MetadataElement element : sAttributes)
		{
			attributeMap.put(element.getName(), element.getType());
		}
	}
	
	/**
	 * Returns whether or not the attribute is required.
	 * @param name Name of the attribute.
	 * @return TRUE if the attribute is required, FALSE otherwise.
	 */
	@Override
	public boolean isAttributeRequired(String name)
	{
		for (MetadataElement element : this.sAttributes)
		{
			if (element.getName().equals(name))
				return element.isRequired();
		}
		
		return false;
	}
	
	/**
	 * Update (or add) the attribute. This is intended for use where the 
	 * metadata model is being changed or expanded.
	 * @param name Attribute Name
	 * @param type Attribute Type
	 * @param required TRUE, the attribute is required. FALSE otherwise.
	 */
	public static void updateAttribute(String name, AttrType<?> type, boolean required)
	{
		boolean found = false;
		int index = -1;
		if (sAttributes == null)
			return;
		
		clearAttributeMapCache();
		MetadataElement element = new MetadataElement(name, type, required);
		
		for (int i = 0; i < sAttributes.size(); i++)
		{
			if (sAttributes.get(i).getName().equals(name))
			{
				found = true;
				sAttributes.set(i, element);
				break;
			}
		}
		if (!found)
		{
			sAttributes.add(element);
		}
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
}
