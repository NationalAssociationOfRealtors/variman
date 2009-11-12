package org.realtors.rets.common.metadata.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	// 1.7.2
	public static final String MODTIMESTAMP = "ModTimeStamp";
	public static final String FOREIGNKEYNAME = "ForeignKeyName";
	public static final String FOREIGNFIELD = "ForeignField";
	public static final String KEYQUERY = "KeyQuery";
	public static final String KEYSELECT = "KeySelect";
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
    		add(new MetadataElement(METADATAENTRYID, sRETSID, sREQUIRED));
    		add(new MetadataElement(SYSTEMNAME, sRETSNAME, sREQUIRED));
    		add(new MetadataElement(STANDARDNAME, sRETSNAME));
    		add(new MetadataElement(LONGNAME, sText256));
    		add(new MetadataElement(DBNAME, sRETSNAME, sREQUIRED));
    		add(new MetadataElement(SHORTNAME, sText64));
    		add(new MetadataElement(MAXIMUMLENGTH, sPOSITIVENUM, sREQUIRED));
    		add(new MetadataElement(DATATYPE, sDataTypes));
    		add(new MetadataElement(PRECISION, sAttrNumeric));
    		add(new MetadataElement(SEARCHABLE, sAttrBoolean));
    		add(new MetadataElement(INTERPRETATION, sInterpretations));
    		add(new MetadataElement(ALIGNMENT, sAlignments));
    		add(new MetadataElement(USESEPARATOR, sAttrBoolean));
    		add(new MetadataElement(EDITMASKID, sRETSNAME)); // FIXME: but multiples are separated by commas
    		add(new MetadataElement(LOOKUPNAME, sRETSNAME));
    		add(new MetadataElement(MAXSELECT, sAttrNumeric));
    		add(new MetadataElement(UNITS, sUnits));
    		add(new MetadataElement(INDEX, sAttrBoolean));
    		add(new MetadataElement(MINIMUM, sAttrNumeric));
    		add(new MetadataElement(MAXIMUM, sAttrNumeric));
    		add(new MetadataElement(DEFAULT, sAttrNumeric)); // XXX: serial
    		add(new MetadataElement(REQUIRED, sAttrNumeric));
    		add(new MetadataElement(SEARCHHELPID, sRETSNAME));
    		add(new MetadataElement(UNIQUE, sAttrBoolean));
    		// 1.7.2
    		add(new MetadataElement(MODTIMESTAMP, sAttrBoolean, RetsVersion.RETS_1_7_2));
    		add(new MetadataElement(FOREIGNKEYNAME, sRETSID, RetsVersion.RETS_1_7_2));
    		add(new MetadataElement(FOREIGNFIELD, sRETSNAME, RetsVersion.RETS_1_7_2));
    		add(new MetadataElement(KEYQUERY, sAttrBoolean, RetsVersion.RETS_1_7_2));
    		add(new MetadataElement(KEYSELECT, sAttrBoolean, RetsVersion.RETS_1_7_2));
    		add(new MetadataElement(INKEYINDEX, sAttrBoolean, RetsVersion.RETS_1_7_2));
		}};
	 
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

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getSystemName() {
		return getStringAttribute(SYSTEMNAME);
	}

	public String getStandardName() {
		return getStringAttribute(STANDARDNAME);
	}

	public String getLongName() {
		return getStringAttribute(LONGNAME);
	}

	public String getDBName() {
		return getStringAttribute(DBNAME);
	}

	public String getShortName() {
		return getStringAttribute(SHORTNAME);
	}

	public int getMaximumLength() {
		return getIntAttribute(MAXIMUMLENGTH);
	}

	public String getDataType() {
		return getStringAttribute(DATATYPE);
	}

	public int getPrecision() {
		return getIntAttribute(PRECISION);
	}

	public boolean getSearchable() {
		return getBooleanAttribute(SEARCHABLE);
	}

	public String getInterpretation() {
		return getStringAttribute(INTERPRETATION);
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

	public boolean getUseSeparator() {
		return getBooleanAttribute(USESEPARATOR);
	}

	public String getEditMaskID() {
		return getStringAttribute(EDITMASKID);
	}

	public String getLookupName() {
		return getStringAttribute(LOOKUPNAME);
	}

	public int getMaxSelect() {
		return getIntAttribute(MAXSELECT);
	}

	public String getUnits() {
		return getStringAttribute(UNITS);
	}

	public boolean getIndex() {
		return getBooleanAttribute(INDEX);
	}

	public int getMinimum() {
		return getIntAttribute(MINIMUM);
	}

	public int getMaximum() {
		return getIntAttribute(MAXIMUM);
	}

	public int getDefault() {
		return getIntAttribute(DEFAULT);
	}

	public int getRequired() {
		return getIntAttribute(REQUIRED);
	}

	public String getSearchHelpID() {
		return getStringAttribute(SEARCHHELPID);
	}

	public boolean getUnique() {
		return getBooleanAttribute(UNIQUE);
	}
	
	public boolean getModTimeStamp() {
		return getBooleanAttribute(MODTIMESTAMP);
	}
	
	public String getForeignKeyName() {
		return getStringAttribute(FOREIGNKEYNAME);
	}
	
	public String getForeignField() {
		return getStringAttribute(FOREIGNFIELD);
	}
	
	public boolean getKeyQuery() {
		return getBooleanAttribute(KEYQUERY);
	}
	
	public boolean getKeySelect() {
		return getBooleanAttribute(KEYSELECT);
	}
	
	public boolean getInKeyIndex() {
		return getBooleanAttribute(INKEYINDEX);
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
		return MetadataType.TABLE;
	}	
}
