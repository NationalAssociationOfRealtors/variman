package org.realtors.rets.common.metadata.types;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataElement;
import org.realtors.rets.common.metadata.MetadataType;

public class MClass extends MetaObject {
	private static final String METADATATYPENAME = "Class";

	private static final MTable[] EMPTY_TABLE_ARRAY = {};
	private static final MUpdate[] EMPTY_UPDATE_ARRAY = {};

	public static final String CLASSNAME = "ClassName";
	public static final String STANDARDNAME = "StandardName";
	public static final String VISIBLENAME = "VisibleName";
	public static final String DESCRIPTION = "Description";
	public static final String TABLEVERSION = "TableVersion";
	public static final String TABLEDATE = "TableDate";
	public static final String UPDATEVERSION = "UpdateVersion";
	public static final String UPDATEDATE = "UpdateDate";
	// 1.7
	public static final String CLASSTIMESTAMP = "ClassTimeStamp";
	public static final String DELETEDFLAGFIELD = "DeletedFlagField";
	public static final String DELETEDFLAGVALUE = "DeletedFlagValue";
	// 1.7.2
	public static final String HASKEYINDEX = "HasKeyIndex";
	// Variman 3.x Extension
	public static final String X_DBNAME = "X-DBName";

	private static final List<MetadataElement> sAttributes =
		new ArrayList<MetadataElement>()
		{{
			add(new MetadataElement(CLASSNAME, sRETSID, sREQUIRED));
			add(new MetadataElement(STANDARDNAME, sPlaintext64));
			add(new MetadataElement(VISIBLENAME, sPlaintext64, sREQUIRED));
			add(new MetadataElement(DESCRIPTION, sPlaintext128));
			add(new MetadataElement(TABLEVERSION, sAttrVersion, sREQUIRED));
			add(new MetadataElement(TABLEDATE, sAttrDate, sREQUIRED));
			add(new MetadataElement(UPDATEVERSION, sAttrVersion));
			add(new MetadataElement(UPDATEDATE, sAttrDate));
			// 1.7
			add(new MetadataElement(CLASSTIMESTAMP, sRETSNAME, RetsVersion.RETS_1_7_2));
			add(new MetadataElement(DELETEDFLAGFIELD, sRETSNAME, RetsVersion.RETS_1_7_2));
			add(new MetadataElement(DELETEDFLAGVALUE, sAlphanum32, RetsVersion.RETS_1_7_2));
			// 1.7.2
			add(new MetadataElement(HASKEYINDEX, sAttrBoolean, RetsVersion.RETS_1_7_2));
			// Variman 3.x Extension
			add(new MetadataElement(X_DBNAME, sRETSNAME, sREQUIRED));
		}};

	private static MetadataType[] CHILDREN = { MetadataType.UPDATE, MetadataType.TABLE };

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
	protected void addAttributesToMap(Map<String, AttrType<?>> attributeMap) 
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
		for (MetadataElement element : MClass.sAttributes)
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
		return CHILDREN;
	}

	public String getClassName() {
		return getStringAttribute(CLASSNAME);
	}

	public void setClassName(String className) {
		String classNameStr = sAlphanum32.render(className);
		setAttribute(CLASSNAME, classNameStr);
	}

	public String getStandardName() {
		return getStringAttribute(STANDARDNAME);
	}

	public void setStandardName(String standardName) {
		String standardNameStr = sPlaintext64.render(standardName);
		setAttribute(STANDARDNAME, standardNameStr);
	}

	public String getVisibleName() {
		return getStringAttribute(VISIBLENAME);
	}

	public void setVisibleName(String visibleName) {
		String visibleNameStr = sPlaintext64.render(visibleName);
		setAttribute(VISIBLENAME, visibleNameStr);
	}

	public String getDescription() {
		return getStringAttribute(DESCRIPTION);
	}

	public void setDescription(String description) {
		String descriptionStr = sPlaintext128.render(description);
		setAttribute(DESCRIPTION, descriptionStr);
	}

	public int getTableVersion() {
		return getIntAttribute(TABLEVERSION);
	}

	public void setTableVersion(int tableVersion) {
		String tableVersionStr = sAttrVersion.render(Integer.valueOf(tableVersion));
		setAttribute(TABLEVERSION, tableVersionStr);
	}

	public Date getTableDate() {
		return getDateAttribute(TABLEDATE);
	}

	public void setTableDate(Date tableDate) {
		String tableDateStr = sAttrDate.render(tableDate);
		setAttribute(TABLEDATE, tableDateStr);
	}

	public int getUpdateVersion() {
		return getIntAttribute(UPDATEVERSION);
	}

	public void setUpdateVersion(int updateVersion) {
		String updateVersionStr = sAttrVersion.render(Integer.valueOf(updateVersion));
		setAttribute(UPDATEVERSION, updateVersionStr);
	}

	public Date getUpdateDate() {
		return getDateAttribute(UPDATEDATE);
	}

	public void setUpdateDate(Date updateDate) {
		String updateDateStr = sAttrDate.render(updateDate);
		setAttribute(UPDATEDATE, updateDateStr);
	}

	public String getClassTimeStamp() {
		return getStringAttribute(CLASSTIMESTAMP);
	}

	public void setClassTimeStamp(String classTimeStamp) {
		String classTimeStampStr = sRETSNAME.render(classTimeStamp);
		setAttribute(CLASSTIMESTAMP, classTimeStampStr);
	}

	public String getDeletedFlagField() {
		return getStringAttribute(DELETEDFLAGFIELD);
	}

	public void setDeletedFlagField(String deletedFlagField) {
		String deletedFlagFieldStr = sRETSNAME.render(deletedFlagField);
		setAttribute(DELETEDFLAGFIELD, deletedFlagFieldStr);
	}

	public String getDeletedFlagValue() {
		return getStringAttribute(DELETEDFLAGVALUE);
	}

	public void setDeletedFlagValue(String deletedFlagValue) {
		String deletedFlagValueStr = sAlphanum32.render(deletedFlagValue);
		setAttribute(DELETEDFLAGVALUE, deletedFlagValueStr);
	}

	public Boolean getHasKeyIndex() {
		return getBooleanAttribute(HASKEYINDEX);
	}

	public void setHasKeyIndex(Boolean hasKeyIndex) {
		String hasKeyIndexStr = sAttrBoolean.render(hasKeyIndex);
		setAttribute(HASKEYINDEX, hasKeyIndexStr);
	}

	public String getXDBName() {
		return getStringAttribute(X_DBNAME);
	}

	public void setXDBName(String xDbName) {
		String xDbNameStr = sRETSNAME.render(xDbName);
		setAttribute(X_DBNAME, xDbNameStr);
	}

	public MTable getMTable(String systemName) {
		return (MTable) getChild(MetadataType.TABLE, systemName);
	}

	public Set<MTable> getTables() {
		MTable[] mTables = getMTables();
		int numTables = mTables.length;
		Set<MTable> tables = new LinkedHashSet<MTable>(numTables);
		for (MTable mTable : mTables) {
			tables.add(mTable);
		}
		return tables;
	}

	public void setTables(Set<MTable> tables) {
		MTable[] mTables = tables.toArray(EMPTY_TABLE_ARRAY);
		setMTables(mTables);
	}

	public MTable[] getMTables() {
		return getChildren(MetadataType.TABLE).toArray(EMPTY_TABLE_ARRAY);
	}

	public void setMTables(MTable[] tables) {
		deleteAllChildren(MetadataType.TABLE);
		if (tables != null) {
			addChildren(MetadataType.TABLE, tables);
		}
	}

	public MUpdate getMUpdate(String updateName) {
		return (MUpdate) getChild(MetadataType.UPDATE, updateName);
	}

	public Set<MUpdate> getUpdates() {
		MUpdate[] mUpdates = getMUpdates();
		int numUpdates = mUpdates.length;
		Set<MUpdate> updates = new LinkedHashSet<MUpdate>(numUpdates);
		for (MUpdate mUpdate : mUpdates) {
			updates.add(mUpdate);
		}
		return updates;
	}

	public void setUpdates(Set<MUpdate> updates) {
		MUpdate[] mUpdates = updates.toArray(EMPTY_UPDATE_ARRAY);
		setMUpdates(mUpdates);
	}

	public MUpdate[] getMUpdates() {
		return getChildren(MetadataType.UPDATE).toArray(EMPTY_UPDATE_ARRAY);
	}

	public void setMUpdates(MUpdate[] updates) {
		deleteAllChildren(MetadataType.UPDATE);
		if (updates != null) {
			addChildren(MetadataType.UPDATE, updates);
		}
	}

	public MResource getMResource() {
		MResource resource = (MResource)getParent();
		return resource;
	}

	public void setMResource(MResource resource) {
		setParent(resource);
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
