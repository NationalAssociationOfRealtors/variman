package org.realtors.rets.common.metadata.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataElement;
import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.attrib.AttrEnum;

public class MTable extends MetaObject {
	private static final String METADATATYPENAME = "Table";

	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String SYSTEMNAME = "SystemName";
	public static final String STANDARDNAME = "StandardName";
	public static final String LONGNAME = "LongName";
	public static final String DBNAME = "DBName";
	public static final String SHORTNAME = "ShortName";
	public static final String MAXIMUMLENGTH = "MaximumLength";
	public static final String DATATYPE = "DataType";
	public static final String PRECISION = "Precision";
	public static final String SEARCHABLE = "Searchable";
	public static final String INTERPRETATION = "Interpretation";
	public static final String ALIGNMENT = "Alignment";
	public static final String USESEPARATOR = "UseSeparator";
	public static final String EDITMASKID = "EditMaskID";
	public static final String LOOKUPNAME = "LookupName";
	public static final String MAXSELECT = "MaxSelect";
	public static final String UNITS = "Units";
	public static final String INDEX = "Index";
	public static final String MINIMUM = "Minimum";
	public static final String MAXIMUM = "Maximum";
	public static final String DEFAULT = "Default";
	public static final String REQUIRED = "Required";
	public static final String SEARCHHELPID = "SearchHelpID";
	public static final String UNIQUE = "Unique";
	// 1.7
	public static final String MODTIMESTAMP = "ModTimeStamp";
	public static final String FOREIGNKEYNAME = "ForeignKeyName";
	public static final String FOREIGNFIELD = "ForeignField";
	public static final String KEYQUERY = "KeyQuery";
	public static final String KEYSELECT = "KeySelect";
	// 1.7.2
	public static final String INKEYINDEX = "InKeyIndex";

	private static final String[] DATATYPES = "Boolean,Character,Date,DateTime,Time,Tiny,Small,Int,Long,Decimal".split(",");
	private static final AttrType sDataTypes = new AttrEnum(DATATYPES);
	private static final String[] INTERPRETATIONS = "Number,Currency,Lookup,LookupMulti,LookupBitstring,LookupBitmask".split(",");
	private static final AttrType sInterpretations = new AttrEnum(INTERPRETATIONS);
	private static final String[] ALIGNMENTS = "Left,Right,Center,Justify".split(",");
	private static final AttrType sAlignments = new AttrEnum(ALIGNMENTS);
	private static final String[] UNITSS = "Feet,Meters,SqFt,SqMeters,Acres,Hectares".split(",");
	private static final AttrType sUnits = new AttrEnum(UNITSS);

	private static final List<MetadataElement> sAttributes =
		new ArrayList<MetadataElement>()
		{{
			add(new MetadataElement(METADATAENTRYID, sRETSID, RetsVersion.RETS_1_7, sREQUIRED));
			add(new MetadataElement(SYSTEMNAME, sRETSNAME, sREQUIRED));
			add(new MetadataElement(STANDARDNAME, sRETSNAME));
			add(new MetadataElement(LONGNAME, sText256));
			/*
			 * The following should use sAlphanum10 but many existing users have
			 * column names greater than 10 characters. Hence the reason
			 * sRETSNAME is used.
			 */
			add(new MetadataElement(DBNAME, sRETSNAME, sREQUIRED));
			add(new MetadataElement(SHORTNAME, sText64));
			add(new MetadataElement(MAXIMUMLENGTH, sPOSITIVENUM, sREQUIRED));
			add(new MetadataElement(DATATYPE, sDataTypes));
			add(new MetadataElement(PRECISION, sAttrNumeric)); // TODO: Make an AttrType for OPTNONNEGATIVENUM.
			add(new MetadataElement(SEARCHABLE, sAttrBoolean));
			add(new MetadataElement(INTERPRETATION, sInterpretations));
			add(new MetadataElement(ALIGNMENT, sAlignments));
			add(new MetadataElement(USESEPARATOR, sAttrBoolean));
			add(new MetadataElement(EDITMASKID, sCSV_RETSNAMES));
			add(new MetadataElement(LOOKUPNAME, sRETSNAME));
			add(new MetadataElement(MAXSELECT, sAttrNumeric));
			add(new MetadataElement(UNITS, sUnits));
			add(new MetadataElement(INDEX, sAttrBoolean));
			add(new MetadataElement(MINIMUM, sAttrNumeric));
			add(new MetadataElement(MAXIMUM, sAttrNumeric));
			add(new MetadataElement(DEFAULT, sAttrNumeric)); // XXX: Make an AttrType for SERIAL.
			add(new MetadataElement(REQUIRED, sAttrNumeric));
			add(new MetadataElement(SEARCHHELPID, sRETSNAME));
			add(new MetadataElement(UNIQUE, sAttrBoolean));
			// 1.7
			add(new MetadataElement(MODTIMESTAMP, sAttrBoolean, RetsVersion.RETS_1_7));
			add(new MetadataElement(FOREIGNKEYNAME, sRETSID, RetsVersion.RETS_1_7));
			add(new MetadataElement(FOREIGNFIELD, sRETSNAME, RetsVersion.RETS_1_7));
			add(new MetadataElement(KEYQUERY, sAttrBoolean, RetsVersion.RETS_1_7));
			add(new MetadataElement(KEYSELECT, sAttrBoolean, RetsVersion.RETS_1_7));
			// 1.7.2
			add(new MetadataElement(INKEYINDEX, sAttrBoolean, RetsVersion.RETS_1_7_2));
		}};

	private MForeignKey foreignKey;
	private MLookup lookup;
	private MSearchHelp searchHelp;
	private Set<MEditMask> editMasks;

	public MTable() {
		this(DEFAULT_PARSING);
	}

	public MTable(boolean strictParsing) {
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
		for (MetadataElement element : MTable.sAttributes)
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

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public void setMetadataEntryID(String metadataEntryId) {
		String metadataEntryIdStr = sRETSID.render(metadataEntryId);
		setAttribute(METADATAENTRYID, metadataEntryIdStr);
	}

	public String getSystemName() {
		return getStringAttribute(SYSTEMNAME);
	}

	public void setSystemName(String systemName) {
		String systemNameStr = sRETSNAME.render(systemName);
		setAttribute(SYSTEMNAME, systemNameStr);
	}

	public String getStandardName() {
		return getStringAttribute(STANDARDNAME);
	}

	public void setStandardName(String standardName) {
		String standardNameStr = sRETSNAME.render(standardName);
		setAttribute(STANDARDNAME, standardNameStr);
	}

	public String getLongName() {
		return getStringAttribute(LONGNAME);
	}

	public void setLongName(String longName) {
		String longNameStr = sText256.render(longName);
		setAttribute(LONGNAME, longNameStr);
	}

	public String getDBName() {
		return getStringAttribute(DBNAME);
	}

	public void setDBName(String dbName) {
		String dbNameStr = sAlphanum10.render(dbName);
		setAttribute(DBNAME, dbNameStr);
	}

	public String getShortName() {
		return getStringAttribute(SHORTNAME);
	}

	public void setShortName(String shortName) {
		String shortNameStr = sText64.render(shortName);
		setAttribute(SHORTNAME, shortNameStr);
	}

	public int getMaximumLength() {
		return getIntAttribute(MAXIMUMLENGTH);
	}

	public void setMaximumLength(int maximumLength) {
		String maximumLengthStr = sPOSITIVENUM.render(Integer.valueOf(maximumLength));
		setAttribute(MAXIMUMLENGTH, maximumLengthStr);
	}

	public String getDataType() {
		return getStringAttribute(DATATYPE);
	}

	public void setDataType(String dataType) {
		String dataTypeStr = sDataTypes.render(dataType);
		setAttribute(DATATYPE, dataTypeStr);
	}

	public int getPrecision() {
		return getIntAttribute(PRECISION);
	}

	public void setPrecision(int precision) {
		String precisionStr = sAttrNumeric.render(Integer.valueOf(precision));
		setAttribute(PRECISION, precisionStr);
	}

	public boolean getSearchable() {
		return getBooleanAttribute(SEARCHABLE);
	}

	public void setSearchable(boolean searchable) {
		String searchableStr = sAttrBoolean.render(Boolean.valueOf(searchable));
		setAttribute(SEARCHABLE, searchableStr);
	}

	public String getInterpretation() {
		return getStringAttribute(INTERPRETATION);
	}

	public void setInterpretation(String interpretation) {
		String interpretationStr = sInterpretations.render(interpretation);
		setAttribute(INTERPRETATION, interpretationStr);
	}

	public boolean isLookup() {
		String interp = getInterpretation();
		if (interp != null && interp.startsWith("Lookup")) {
			return true;
		}
		if (getSystemName().equalsIgnoreCase("status")) {
			System.out.println("Field is " + getSystemName() + " and interp " + "is " + interp
					+ " but isLookup() is false");
		}
		return false;
	}

	public String getAlignment() {
		return getStringAttribute(ALIGNMENT);
	}

	public void setAlignment(String alignment) {
		String alignmentStr = sAlignments.render(alignment);
		setAttribute(ALIGNMENT, alignmentStr);
	}

	public boolean getUseSeparator() {
		return getBooleanAttribute(USESEPARATOR);
	}

	public void setUseSeparator(boolean useSeparator) {
		String useSeparatorStr = sAttrBoolean.render(Boolean.valueOf(useSeparator));
		setAttribute(USESEPARATOR, useSeparatorStr);
	}

	public String getEditMaskID() {
		return getStringAttribute(EDITMASKID);
	}

	public void setEditMaskID(String editMaskId) {
		String editMaskIdStr = sCSV_RETSNAMES.render(editMaskId);
		setAttribute(EDITMASKID, editMaskIdStr);
	}

	public Set<MEditMask> getEditMasks() {
		return this.editMasks;
	}

	public void setEditMasks(Set<MEditMask> editMasks) {
		this.editMasks = editMasks;
		if (this.editMasks == null || this.editMasks.isEmpty()) {
			setEditMaskID(null);
		} else {
			String editMaskId = toEditMaskId(this.editMasks);
			setEditMaskID(editMaskId);
		}
	}

	public static Set<String> toEditMaskIds(String editMaskIdCsv) {
		if (editMaskIdCsv == null) {
			return null;
		}
		Set<String> editMaskIdSet = new LinkedHashSet<String>();
		String[] editMaskIds = StringUtils.split(editMaskIdCsv, ",");
		for (String editMaskId : editMaskIds) {
			editMaskIdSet.add(editMaskId);
		}
		return editMaskIdSet;
	}

	public static String toEditMaskId(Collection<MEditMask> editMasks) {
		if (editMasks == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		String prefix = "";
		for (MEditMask editMask : editMasks) {
			String editMaskId = editMask.getEditMaskID();
			sb.append(prefix);
			sb.append(editMaskId);
			prefix = ",";
		}
		String editMaskId = sb.toString();
		return editMaskId;
	}

	public String getLookupName() {
		return getStringAttribute(LOOKUPNAME);
	}

	public void setLookupName(String lookupName) {
		String lookupNameStr = sRETSNAME.render(lookupName);
		setAttribute(LOOKUPNAME, lookupNameStr);
	}

	public MLookup getMLookup() {
		return this.lookup;
	}

	public void setMLookup(MLookup lookup) {
		this.lookup = lookup;
		if (this.lookup == null) {
			setLookupName(null);
		} else {
			String lookupName = this.lookup.getLookupName();
			setLookupName(lookupName);
		}
	}

	public int getMaxSelect() {
		return getIntAttribute(MAXSELECT);
	}

	public void setMaxSelect(int maxSelect) {
		String maxSelectStr = sAttrNumeric.render(Integer.valueOf(maxSelect));
		setAttribute(MAXSELECT, maxSelectStr);
	}

	public String getUnits() {
		return getStringAttribute(UNITS);
	}

	public void setUnits(String units) {
		String unitsStr = sUnits.render(units);
		setAttribute(UNITS, unitsStr);
	}

	public boolean getIndex() {
		return getBooleanAttribute(INDEX);
	}

	public void setIndex(boolean index) {
		String indexStr = sAttrBoolean.render(Boolean.valueOf(index));
		setAttribute(INDEX, indexStr);
	}

	public int getMinimum() {
		return getIntAttribute(MINIMUM);
	}

	public void setMinimum(int minimum) {
		String minimumStr = sAttrNumeric.render(Integer.valueOf(minimum));
		setAttribute(MINIMUM, minimumStr);
	}

	public int getMaximum() {
		return getIntAttribute(MAXIMUM);
	}

	public void setMaximum(int maximum) {
		String maximumStr = sAttrNumeric.render(Integer.valueOf(maximum));
		setAttribute(MAXIMUM, maximumStr);
	}

	public int getDefault() {
		return getIntAttribute(DEFAULT);
	}

	public void setDefault(int defaultValue) {
		String defaultStr = sAttrNumeric.render(Integer.valueOf(defaultValue));
		setAttribute(DEFAULT, defaultStr);
	}

	public int getRequired() {
		return getIntAttribute(REQUIRED);
	}

	public void setRequired(int required) {
		String requiredStr = sAttrNumeric.render(Integer.valueOf(required));
		setAttribute(REQUIRED, requiredStr);
	}

	public String getSearchHelpID() {
		return getStringAttribute(SEARCHHELPID);
	}

	public void setSearchHelpID(String searchHelpId) {
		String searchHelpIdStr = sRETSNAME.render(searchHelpId);
		setAttribute(SEARCHHELPID, searchHelpIdStr);
	}

	public MSearchHelp getMSearchHelp() {
		return this.searchHelp;
	}

	public void setMSearchHelp(MSearchHelp searchHelp) {
		this.searchHelp = searchHelp;
		if (this.searchHelp == null) {
			setSearchHelpID(null);
		} else {
			String searchHelpId = this.searchHelp.getSearchHelpID();
			setSearchHelpID(searchHelpId);
		}
	}

	public boolean getUnique() {
		return getBooleanAttribute(UNIQUE);
	}

	public void setUnique(boolean unique) {
		String uniqueStr = sAttrBoolean.render(Boolean.valueOf(unique));
		setAttribute(UNIQUE, uniqueStr);
	}

	public boolean getModTimeStamp() {
		return getBooleanAttribute(MODTIMESTAMP);
	}

	public void setModTimeStamp(boolean modTimeStamp) {
		String modTimeStampStr = sAttrBoolean.render(Boolean.valueOf(modTimeStamp));
		setAttribute(MODTIMESTAMP, modTimeStampStr);
	}

	public String getForeignKeyName() {
		return getStringAttribute(FOREIGNKEYNAME);
	}

	public void setForeignKeyName(String foreignKeyName) {
		String foreignKeyNameStr = sRETSID.render(foreignKeyName);
		setAttribute(FOREIGNKEYNAME, foreignKeyNameStr);
	}

	public MForeignKey getMForeignKey() {
		return this.foreignKey;
	}

	public void setMForeignKey(MForeignKey foreignKey) {
		this.foreignKey = foreignKey;
		if (this.foreignKey == null) {
			setForeignKeyName(null);
			setForeignField(null);
		} else {
			String foreignKeyName = this.foreignKey.getForeignKeyID();
			// TODO: Confirm this is the correct meaning of foreignField.
			String foreignField = this.foreignKey.getChildSystemName();
			setForeignKeyName(foreignKeyName);
			setForeignField(foreignField);
		}
	}

	public String getForeignField() {
		return getStringAttribute(FOREIGNFIELD);
	}

	public void setForeignField(String foreignField) {
		String foreignFieldStr = sRETSNAME.render(foreignField);
		setAttribute(FOREIGNFIELD, foreignFieldStr);
	}

	public boolean getKeyQuery() {
		return getBooleanAttribute(KEYQUERY);
	}

	public void setKeyQuery(boolean keyQuery) {
		String keyQueryStr = sAttrBoolean.render(Boolean.valueOf(keyQuery));
		setAttribute(KEYQUERY, keyQueryStr);
	}

	public boolean getKeySelect() {
		return getBooleanAttribute(KEYSELECT);
	}

	public void setKeySelect(boolean keySelect) {
		String keySelectStr = sAttrBoolean.render(Boolean.valueOf(keySelect));
		setAttribute(KEYSELECT, keySelectStr);
	}

	public boolean getInKeyIndex() {
		return getBooleanAttribute(INKEYINDEX);
	}

	public void setInKeyIndex(boolean inKeyIndex) {
		String inKeyIndexStr = sAttrBoolean.render(Boolean.valueOf(inKeyIndex));
		setAttribute(INKEYINDEX, inKeyIndexStr);
	}

	public MClass getMClass() {
		MClass clazz = (MClass)getParent();
		return clazz;
	}

	public void setMClass(MClass clazz) {
		setParent(clazz);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sNO_CHILDREN;
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
		return MetadataType.TABLE;
	}
}
