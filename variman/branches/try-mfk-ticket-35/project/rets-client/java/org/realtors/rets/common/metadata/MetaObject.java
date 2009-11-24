package org.realtors.rets.common.metadata;

import java.io.Serializable;
import java.util.*;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.realtors.rets.common.metadata.attrib.AttrAlphanum;
import org.realtors.rets.common.metadata.attrib.AttrBoolean;
import org.realtors.rets.common.metadata.attrib.AttrCsvRetsNames;
import org.realtors.rets.common.metadata.attrib.AttrDate;
import org.realtors.rets.common.metadata.attrib.AttrEnum;
import org.realtors.rets.common.metadata.attrib.AttrGenericText;
import org.realtors.rets.common.metadata.attrib.AttrIDAlphanum;
import org.realtors.rets.common.metadata.attrib.AttrNumeric;
import org.realtors.rets.common.metadata.attrib.AttrPlaintext;
import org.realtors.rets.common.metadata.attrib.AttrPositiveNumeric;
import org.realtors.rets.common.metadata.attrib.AttrText;
import org.realtors.rets.common.metadata.attrib.AttrTimeZone;
import org.realtors.rets.common.metadata.attrib.AttrVersion;
import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MEditMask;
import org.realtors.rets.common.metadata.types.MForeignKey;
import org.realtors.rets.common.metadata.types.MLookup;
import org.realtors.rets.common.metadata.types.MLookupType;
import org.realtors.rets.common.metadata.types.MObject;
import org.realtors.rets.common.metadata.types.MResource;
import org.realtors.rets.common.metadata.types.MSearchHelp;
import org.realtors.rets.common.metadata.types.MSystem;
import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.common.metadata.types.MUpdate;
import org.realtors.rets.common.metadata.types.MUpdateHelp;
import org.realtors.rets.common.metadata.types.MUpdateType;
import org.realtors.rets.common.metadata.types.MValidationExpression;
import org.realtors.rets.common.metadata.types.MValidationExternal;
import org.realtors.rets.common.metadata.types.MValidationExternalType;
import org.realtors.rets.common.metadata.types.MValidationLookup;
import org.realtors.rets.common.metadata.types.MValidationLookupType;

public abstract class MetaObject implements Serializable {
	private static final Log LOG = LogFactory.getLog(MetaObject.class);

	/** a standard parser used by different child types */
	protected static final AttrType sAlphanum = new AttrAlphanum(0, 0, "Alpha Numeric.");
	protected static final AttrType sAlphanum64 = new AttrAlphanum(1, 64, "Alpha Numeric, 1 to 64 characters.");
	protected static final AttrType sAlphanum32 = new AttrAlphanum(1, 32, "Alpha Numeric, 1 to 32 characters.");
	protected static final AttrType sAlphanum24 = new AttrAlphanum(1, 24, "Alpha Numeric, 1 to 24 characters.");
	protected static final AttrType sAlphanum10 = new AttrAlphanum(1, 10, "Alpha Numeric, 1 to 10 characters.");
	protected static final AttrType sPlaintext = new AttrPlaintext(0, 0, "Any printable ASCII character.");
	protected static final AttrType sPlaintext1024 = new AttrPlaintext(1, 1024, "Any printable ASCII character, 1 to 1024 characters.");
	protected static final AttrType sPlaintext512 = new AttrPlaintext(1, 512, "Any printable ASCII character, 1 to 512 characters.");
	protected static final AttrType sPlaintext128 = new AttrPlaintext(1, 128, "Any printable ASCII character, 1 to 128 characters.");
	protected static final AttrType sPlaintext64 = new AttrPlaintext(1, 64, "Any printable ASCII character, 1 to 64 characters.");
	protected static final AttrType sPlaintext32 = new AttrPlaintext(1, 32, "Any printable ASCII character, 1 to 32 characters.");
	protected static final AttrType sText = new AttrText(0, 0, "Any printable ASCII including CRLF, SP and HT.");
	protected static final AttrType sText1024 = new AttrText(1, 1024, "Any printable ASCII including CRLF, SP and HT - 1 to 1024 characters.");
	protected static final AttrType sText512 = new AttrText(1, 512, "Any printable ASCII including CRLF, SP and HT - 1 to 512 characters.");
	protected static final AttrType sText256 = new AttrText(1, 256, "Any printable ASCII including CRLF, SP and HT - 1 to 256 characters.");
	protected static final AttrType sText128 = new AttrText(1, 128, "Any printable ASCII including CRLF, SP and HT - 1 to 128 characters.");
	protected static final AttrType sText64 = new AttrText(1, 64, "Any printable ASCII including CRLF, SP and HT - 1 to 64 characters.");
	protected static final AttrType sText32 = new AttrText(1, 32, "Any printable ASCII including CRLF, SP and HT - 1 to 32 characters.");
	protected static final AttrType sAttrBoolean = new AttrBoolean("0 for FALSE, 1 for TRUE.");
	protected static final AttrType sAttrDate = new AttrDate("A Date/Timestamp in yyyy-mm-ddThh:mm:ssZ format.");
	protected static final AttrType sAttrNumeric = new AttrNumeric("A positive or negative whole number.");
	protected static final AttrType sAttrVersion = new AttrVersion("A RETS version in MM.mm.rrrrr format.");
	// RETS 1.7.2
	public    static final AttrType sPOSITIVENUM = new AttrPositiveNumeric("A positive whole number.");
	public    static final AttrType sRETSNAME = new AttrIDAlphanum(1,64, "Alpha Numeric including an underscore.");
	public    static final AttrType sCSV_RETSNAMES = new AttrCsvRetsNames();
	public    static final AttrType sRETSID = new AttrAlphanum(1,32, "Alpha Numeric, 1 to 32 characters.");
	public    static final AttrType sTIMEZONEOFFSET = new AttrTimeZone("Z for UTC, [+-]00:00 for offset to UTC.");

	public    static final AttrType sAttrMetadataEntryId = sRETSID;
	// Used by MUpdateType.
	public    static final AttrType sAttributes1to5 = new AttrGenericText(0, 10, "12345,", "1,2,3,4,5 singly, or in combination.");
	// Used by MValidationExpression
	public    static final String[] VALIDATIONEXPRESSIONTYPES = "ACCEPT,REJECT,SET".split(",");
	public    static final AttrType sExpressionType = new AttrEnum(VALIDATIONEXPRESSIONTYPES, "'ACCEPT' or 'REJECT' or 'SET'.");

	protected static final Map<String, AttrType> sNameToAttributeMap =
		new LinkedHashMap<String,AttrType>()
	{{
		put("ALPHANUM", sAlphanum);
		put("1*64ALPHANUM", sAlphanum64);
		put("1*32ALPHANUM", sAlphanum32);
		put("1*24ALPHANUM", sAlphanum24);
		put("1*10ALPHANUM", sAlphanum10);
		put("PLAINTEXT", sPlaintext);
		put("1*1024PLAINTEXT", sPlaintext1024);
		put("1*512PLAINTEXT", sPlaintext512);
		put("1*128PLAINTEXT", sPlaintext128);
		put("1*64PLAINTEXT", sPlaintext64);
		put("1*32PLAINTEXT", sPlaintext32);
		put("TEXT", sText);
		put("1*1024TEXT", sText1024);
		put("1*512TEXT", sText512);
		put("1*256TEXT", sText256);
		put("1*128TEXT", sText128);
		put("1*64TEXT", sText64);
		put("1*32TEXT", sText32);
		put("BOOLEAN", sAttrBoolean);
		put("RETSDATE", sAttrDate);
		put("NUMERIC", sAttrNumeric);
		put("RETSVERSION", sAttrVersion);
		put("1to5", sAttributes1to5);
		put("EXPRESSIONTYPE", sExpressionType);
		// RETS 1.7.2
		put("POSITIVENUM", sPOSITIVENUM);
		put("RETSNAME", sRETSNAME);
		put("RETSID", sRETSID);
		put("TIMEZONEOFFSET", sTIMEZONEOFFSET);
	}};

	/*
	 * This is used to map the attribute type to the standard RETS "name".
	 * It is a linked hashmap that can be randomly accessed by attribute type,
	 * or enumerated in the order you see it below. The purpose of this
	 * ordering is to order the more common usages for potential display
	 * in a combo box.
	 */
	protected static final Map<AttrType<?>, String> sAttributeToNameMap =
		new LinkedHashMap<AttrType<?>,String>()
	{{
		/*
		 * Most used attribute types.
		 */
		put(sRETSNAME, "RETSNAME");
		put(sRETSID, "RETSID");
		put(sAttrBoolean, "BOOLEAN");
		put(sAttrDate, "RETSDATE");
		put(sAttrVersion, "RETSVERSION");
		put(sAttrNumeric, "NUMERIC");
		
		/*
		 * Alpha numerics.
		 */
		put(sAlphanum, "ALPHANUM");
		put(sAlphanum64, "1*64ALPHANUM");
		put(sAlphanum32, "1*32ALPHANUM");
		put(sAlphanum24, "1*24ALPHANUM");
		put(sAlphanum10, "1*10ALPHANUM");
		
		/*
		 * Plain text.
		 */
		put(sPlaintext, "PLAINTEXT");
		put(sPlaintext1024, "1*1024PLAINTEXT");
		put(sPlaintext512, "1*512PLAINTEXT");
		put(sPlaintext128, "1*128PLAINTEXT");
		put(sPlaintext64, "1*64PLAINTEXT");
		put(sPlaintext32, "1*32PLAINTEXT");
		
		/*
		 * Text.
		 */
		put(sText, "TEXT");
		put(sText1024, "1*1024TEXT");
		put(sText512, "1*512TEXT");
		put(sText256, "1*256TEXT");
		put(sText128, "1*128TEXT");
		put(sText64, "1*64TEXT");
		put(sText32, "1*32TEXT");
		
		/*
		 * Miscellaneous
		 */
		put(sAttributes1to5, "1to5");
		put(sExpressionType, "EXPRESSIONTYPE");
		put(sPOSITIVENUM, "POSITIVENUM");
		put(sTIMEZONEOFFSET, "TIMEZONEOFFSET");
	}};

	protected static final MetadataType[] sNO_CHILDREN = {};

	public static final boolean STRICT_PARSING = true;
	public static final boolean LOOSE_PARSING = false;
	/*
	 * We need a default parsing level of STRICT for the admin tool in order to display
	 * the elements in the panels in the familiar order of the RETS spec. If we use
	 * LOOSE parsing, then everything is alpabetical (e.g. SYSTEM NAME is near the bottom
	 * of the dialog instead of at the top).
	 */
	public static final boolean DEFAULT_PARSING = STRICT_PARSING;

	/*
	 * Used by subordinate classes to indicate that an attribute is required or
	 * optional.
	 */
	public static final boolean sREQUIRED = true;
	public static final boolean sOPTIONAL = false;

	/** A unique ID for this metadata object */
	private Long uniqueId;
	/** the metadata path to this object */
	private String path;
	/** A reference to the parent metadata object */
	private MetaObject parent;
	/** map of child type to map of child id to child object */
	private Map<MetadataType, Map<String, MetaObject>> childTypes;
	/** map of attribute name to attribute object (as parsed by attrtype) */
	private Map<String, Object> attributes;
	/** map of attribute name to AttrType parser */
	private Map<String, AttrType<?>> attrTypes;

	private static Map<CacheKey,Map<String, AttrType<?>>> sAttributeMapCache = new HashMap<CacheKey,Map<String, AttrType<?>>>();
	private MetaCollector mCollector;
	private boolean strict;

	public MetaObject(boolean strictParsing) {
		this.strict = strictParsing;
		if (strictParsing) {
			this.attributes = new LinkedHashMap<String, Object>();
		} else {
			this.attributes = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
		}
		this.attrTypes = getAttributeMap(strictParsing);
		MetadataType[] types = getChildTypes();
		this.childTypes = new LinkedHashMap<MetadataType, Map<String, MetaObject>>();
		for (int i = 0; i < types.length; i++) {
			this.childTypes.put(types[i], null);
		}
	}

	private Map<String, AttrType<?>> getAttributeMap(boolean strictParsing) {
		synchronized (sAttributeMapCache) {
			Map<String, AttrType<?>> map = sAttributeMapCache.get(new CacheKey(this, strictParsing));
			if (map == null) {
				if (strictParsing) {
					map = new LinkedHashMap<String, AttrType<?>>();
				} else {
					map = new TreeMap<String, AttrType<?>>(String.CASE_INSENSITIVE_ORDER);
				}
				addAttributesToMap(map);
				// Let's make sure no one mucks with the map later
				// FIXME: Re-enable this if the metadata migrator is ever removed/obsoleted.
				// map = Collections.unmodifiableMap(map);
				sAttributeMapCache.put(new CacheKey(this, strictParsing), map);
				if (LOG.isDebugEnabled()) {
					LOG.debug("Adding to attribute cache: " + this.getClass().getName() + ", " + strictParsing);
				}
			}
			return map;
		}
	}

	public static void clearAttributeMapCache() {
		synchronized (sAttributeMapCache) {
			sAttributeMapCache.clear();
		}
	}

	public Long getUniqueId() {
		return this.uniqueId;
	}

	public void setUniqueId(Long uniqueId) {
		this.uniqueId = uniqueId;
	}

	public MetaObject getParent() {
		return parent;
	}

	public void setParent(MetaObject parent) {
		this.parent = parent;
	}

	public Collection<MetaObject> getChildren(MetadataType type) {
		if (!this.childTypes.containsKey(type)) {
			// throw new IllegalArgumentException?
			return null;
		}
		Map<String, MetaObject> o = this.childTypes.get(type);
		if (o == null) {
			if (!fetchChildren(type)) {
				return Collections.emptySet();
			}
			o = this.childTypes.get(type);
		}
		if (o != null) {
			return o.values();
		}
		return Collections.emptySet();
	}

	public Set<? extends MetaObject> getChildrenSet(MetadataType type) {
		Collection<MetaObject> children = getChildren(type);
		if (children == null) {
			return null;
		}
		Set<MetaObject> childrenSet = new LinkedHashSet<MetaObject>(children);
		return childrenSet;
	}

	private boolean fetchChildren(MetadataType type) {
		this.childTypes.put(type, new LinkedHashMap<String, MetaObject>());
		try {
			MetaObject[] children = null;
			if (this.mCollector != null) {
				children = this.mCollector.getMetadata(type, getPath());
			}
			if (children == null) {
				return false;
			}
			for (int i = 0; i < children.length; i++) {
				MetaObject child = children[i];
				addChild(type, child);
			}
		} catch (MetadataException e) {
			LOG.error(toString() + " unable to fetch " + type.name() + " children");
			return false;
		}
		return true;
	}

	public MetaObject getChild(MetadataType type, String id) {
		if (id == null) {
			return null;
		}
		try {
			if (this.childTypes.get(type) == null && this.mCollector != null) {
				if (!fetchChildren(type)) {
					return null;
				}
			}
			Map<String, MetaObject> m = this.childTypes.get(type);
			if (m == null) {
				return null;
			}
			return m.get(id);
		} catch (ClassCastException e) {
			return null;
		}
	}
	/**
	 * Obtain the attribute for the given name. Note that this is used to determine
	 * attributes for the subclass identified attributes.
	 * @param key A String containing the name of the attribute.
	 * @return The attribute as an Object.
	 */
	public Object getAttribute(String key) {
		return this.attributes.get(key);
	}

	/**
	 * Returns the known attributes for the subclass.
	 * @return A set of the known attributes.
	 */
	public Set<String> getKnownAttributes() {
		return this.attrTypes.keySet();
	}

	/**
	 * Returns the attribute type for the given attribute.
	 * @param name A string containing the attribute name.
	 * @return The attribute type handler.
	 */
	public AttrType<?> getAttributeType(String name)
	{
		return this.attrTypes.get(name);
	}

	/**
	 * Returns the names of all of the known attributes for this subclass.
	 * @return A String array containing the attribute names.
	 */
	public String [] getAttributeNames() {
		ArrayList<String> names = new ArrayList<String>();
		Iterator<String> iter = getKnownAttributes().iterator();
		
		while (iter.hasNext()) {
			String key = iter.next();
			names.add(key);
		}
		String [] stringArray = new String[names.size()];
		stringArray = names.toArray(stringArray);
		
		return stringArray;
	}

	/**
	 * Returns the attribute given the name. This is from the set of all
	 * known attributes.
	 * @param name A String containing the known name of the attribute.
	 * @return The Attribute
	 */
	public static AttrType<?> getAttributeFromName(String name)
	{
		return sNameToAttributeMap.get(name);
	}

	/**
	 * Given an attribute, return it's name. This is from the set of all
	 * known attributes.
	 * @param attribute The attribute
	 * @return A string containing the attribute's name.
	 */
	public static String getNameFromAttribute(AttrType<?> attribute)
	{
		return sAttributeToNameMap.get(attribute);
	}

	/**
	 * Get standard known attribute names.
	 * @return A string array of the standard attribute names.
	 */
	public static String [] getStandardAttributeNames()
	{
		ArrayList<String> names = new ArrayList<String>();
		Iterator<String> iter = sAttributeToNameMap.values().iterator();
		
		while (iter.hasNext()) {
			String key = iter.next();
			names.add(key);
		}
		String [] stringArray = new String[names.size()];
		stringArray = names.toArray(stringArray);
		
		return stringArray;
	}

	public String getAttributeAsString(String key) {
		Object value = this.attributes.get(key);
		if (value == null) {
			return null;
		}
		if (this.attrTypes.containsKey(key)) {
			AttrType type = this.attrTypes.get(key);
			return type.render(value);
		}
		return value.toString();
	}

	protected Object getTypedAttribute(String key, Class type) {
		AttrType atype = this.attrTypes.get(key);
		if (atype == null) {
			return null;
		}
		if (atype.getType() == type) {
			return this.attributes.get(key);
		} 
		LOG.warn("type mismatch, expected " + type.getName() + " but" + " got " + atype.getType().getName());
		return null;
	}

	public Date getDateAttribute(String key) {
		return (Date) getTypedAttribute(key, Date.class);
	}

	public String getStringAttribute(String key) {
		return (String) getTypedAttribute(key, String.class);
	}

	public Integer getIntegerAttribute(String key) {
		Integer i = (Integer) getTypedAttribute(key, Integer.class);
		return i;
	}

	public int getIntAttribute(String key) {
		Integer i = (Integer) getTypedAttribute(key, Integer.class);
		if (i == null) {
			return 0;
		}
		return i.intValue();
	}

	public boolean getBooleanAttribute(String key) {
		Boolean b = (Boolean) getTypedAttribute(key, Boolean.class);
		if (b == null) {
			return false;
		}
		return b.booleanValue();
	}

	/**
	 * Parse and set the attribute value.
	 * @param key A string containing the name of the attribute.
	 * @param value A String containing the value of the attribute to be parsed.
	 * @throws MetaParseException If value cannot be properly parsed.
	 */
	public void setAttribute(String key, String value) 
	{
		setAttribute(key, value, this.strict);
	}

	/**
	 * Set the attribute and override the strict parsing setting.
	 * @param key A String containing the name of the attribute.
	 * @param value A String containing the value of the attribute to be parsed.
	 * @param strictParsing A Boolean indicating whether or not to use strict parsing.
	 * @throws MetaParseException If value cannot be properly parsed.
	 */
	public void setAttribute(String key, String value, boolean strictParsing) 
	{
		if (value == null) 
		{
			// LOG.warning()
			return;
		}
		/*
		 * See if we know about this attribute already. If so, parse it using the corresponding
		 * type parser.
		 */
		if (this.attrTypes.containsKey(key)) 
		{
			AttrType type = this.attrTypes.get(key);
			try 
			{
				this.attributes.put(key, type.parse(value,strictParsing));
			} catch (MetaParseException e) 
			{
				LOG.warn(toString() + " couldn't parse attribute " + key + ", value " + value + ": " + e.getMessage());
				if (strictParsing) {
					throw e;
				}
			}
		} 
		else 
		{
			this.attributes.put(key, value);
			updateClassAttribute(this, key, sRETSNAME, false); // FIXME: We should preserve the metadata model in an XML file too!
			if (!key.startsWith("X-")) {
				LOG.warn("Unknown key (" + toString() + "): " + key);
			}
		}
	}

	public void addChildren(MetadataType type, MetaObject[] children) {
		if (children != null) {
			for (MetaObject child : children) {
				addChild(type, child);
			}
		}
	}

	public void addChild(MetadataType type, MetaObject child) {
		if (this.childTypes.containsKey(type)) {
			Map<String, MetaObject> map = this.childTypes.get(type);
			if (map == null) {
				map = new LinkedHashMap();
				this.childTypes.put(type, map);
			}
			if (child == null) {
				return;
			}
			String id = child.getId();
			
			child.setParent(this);
			child.setPath(getPath());
			child.setCollector(this.mCollector);
			if (id != null) {
				map.put(id, child);
			}
			return;
		}
	}

	public void deleteChild(MetadataType type, MetaObject child) 
	{
		if (this.childTypes.containsKey(type)) 
		{
			Map<String, MetaObject> map = this.childTypes.get(type);
			
			if (map == null || child == null) {
				return;
			}
			
			String id = child.getId();
			
			if (id != null) 
			{
				map.remove(id);
			}
			return;
		}
	}

	public void deleteAllChildren(MetadataType type) 
	{
		if (this.childTypes.containsKey(type)) 
		{
			Map<String, MetaObject> map = this.childTypes.get(type);
			
			if (map == null) {
				return;
			}
			
			this.childTypes.remove(type);
			map = new LinkedHashMap<String, MetaObject>();
			this.childTypes.put(type, map);
		}
	}

	public String getId() {
		String idAttr = getIdAttr();
		if (idAttr == null) {
			return getMetadataTypeName();
		}
		return getAttributeAsString(idAttr);
	}

	public String getPath() {
		return this.path;
	}

	protected void setPath(String level) {
		if (level == null || level.equals("")) {
			this.path = getId();
		} else {
			this.path = level + ":" + getId();
		}
	}

	@Override
	public String toString() {
		ToStringBuilder tsb = new ToStringBuilder(this);
		Iterator iter = getKnownAttributes().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			tsb.append(key, getAttributeAsString(key));
		}
		return tsb.toString();
	}

	public void setCollector(MetaCollector c) {
		this.mCollector = c;
		Iterator<MetadataType> iterator = this.childTypes.keySet().iterator();
		while (iterator.hasNext()) {
			MetadataType type = iterator.next();
			Map<String, MetaObject> map = this.childTypes.get(type);
			if (map == null) {
				continue;
			}
			Collection<MetaObject> children = map.values();
			for (Iterator<MetaObject> iter = children.iterator(); iter.hasNext();) {
				MetaObject object = iter.next();
				object.setCollector(c);
			}
		}
	}

	/**
	 * Replace the attribute type for the given attribute.
	 * @param name A string containing the attribute name.
	 * @param attrType An attribute type.
	 * @return The prior attribute type.
	 */
	public AttrType<?> replaceAttributeType(String name, AttrType<?> attrType)
	{
		AttrType<?> oldType = this.attrTypes.get(name);
		this.attrTypes.put(name, attrType);
		return oldType;
	}

	/**
	 * Remove the attribute from the attribute and type maps.
	 * @param key A String containing the attribute name.
	 */
	// FIXME: Disable this if the unmodifiable map is re-enabled above.
	public void removeAttribute(String key) 
	{
		if (this.attributes.get(key) != null)
		{
			this.attributes.remove(key);
			this.attrTypes.remove(key);
		}
	}

	/**
	 * Update the class static attributes. This is used to adjust
	 * the metadata model for custom attributes.
	 * @param metaObject The Metadata Object
	 * @param key A String containing the attribute name
	 * @param attrType The Attribute Type
	 * @param required A boolean indicating whether or not the attribute is required
	 */
	public static void updateClassAttribute(MetaObject metaObject, String key, 
											AttrType<?> attrType, boolean required)
	{
		if (metaObject instanceof MResource) {
			MResource.updateAttribute(key, attrType, required);
		} else
		if (metaObject instanceof MClass) {
			MClass.updateAttribute(key, attrType, required);
		} else
		if (metaObject instanceof MEditMask) {
			MEditMask.updateAttribute(key, attrType, required);
		} else
		if (metaObject instanceof MForeignKey) {
			MForeignKey.updateAttribute(key, attrType, required);
		} else
		if (metaObject instanceof MLookup) {
			MLookup.updateAttribute(key, attrType, required);
		} else
		if (metaObject instanceof MLookupType) {
			MLookupType.updateAttribute(key, attrType, required);
		} else
		if (metaObject instanceof MObject) {
			MObject.updateAttribute(key, attrType, required);
		} else
		if (metaObject instanceof MSearchHelp) {
			MSearchHelp.updateAttribute(key, attrType, required);
		} else
		if (metaObject instanceof MSystem) {
			MSystem.updateAttribute(key, attrType, required);
		} else
		if (metaObject instanceof MTable) {
			MTable.updateAttribute(key, attrType, required);
		} else
		if (metaObject instanceof MUpdate) {
			MUpdate.updateAttribute(key, attrType, required);
		} else
		if (metaObject instanceof MUpdateHelp) {
			MUpdateHelp.updateAttribute(key, attrType, required);
		} else
		if (metaObject instanceof MUpdateType) {
			MUpdateType.updateAttribute(key, attrType, required);
		} else
		if (metaObject instanceof MValidationExpression) {
			MValidationExpression.updateAttribute(key, attrType, required);
		} else
		if (metaObject instanceof MValidationExternal) {
			MValidationExternal.updateAttribute(key, attrType, required);
		} else
		if (metaObject instanceof MValidationExternalType) {
			MValidationExternalType.updateAttribute(key, attrType, required);
		} else
		if (metaObject instanceof MValidationLookup) {
			MValidationLookup.updateAttribute(key, attrType, required);
		} else
		if (metaObject instanceof MValidationLookupType) {
			MValidationLookupType.updateAttribute(key, attrType, required);
		}
	}

	public abstract MetadataType[] getChildTypes();

	protected abstract String getIdAttr();

	/**
	 * Adds attributes to an attribute map.  This is called by the MetaObject
	 * constructor to initialize a map of attributes.  This map may be cached,
	 * so this method may not be called for every object construction.
	 *
	 * @param attributeMap Map to add attributes to
	 */
	protected abstract void addAttributesToMap(Map<String, AttrType<?>> attributeMap);

	/**
	 * @return The metadata type display name.
	 */
	public abstract String getMetadataTypeName();

	public abstract MetadataType getMetadataType();

	/**
	 * @return The metadata level for this metadata object.
	 */
	public String getLevel() {
		String level = "";
		MetaObject parent = getParent();
		if (parent != null) {
			level = parent.getPath();
		}
		return level;
	}

	/**
	 * Returns whether or not the attribute is required.
	 * @param name Name of the attribute.
	 * @return TRUE if the attribute is required, FALSE otherwise.
	 */
	public abstract boolean isAttributeRequired(String name);
}

class CacheKey {
	private Class mClass;
	private boolean strictParsing;

	public CacheKey(MetaObject metaObject, boolean strictParsing) {
		this.mClass = metaObject.getClass();
		this.strictParsing = strictParsing;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CacheKey)) {
			return false;
		}
		CacheKey rhs = (CacheKey) obj;
		return new EqualsBuilder().append(this.mClass, rhs.mClass).append(this.strictParsing, rhs.strictParsing).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.mClass).append(this.strictParsing).toHashCode();
	}

}

