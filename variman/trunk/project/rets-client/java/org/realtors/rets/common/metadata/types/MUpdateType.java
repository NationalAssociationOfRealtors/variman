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

public class MUpdateType extends MetaObject {
	private static final String METADATATYPENAME = "UpdateType";

	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String SYSTEMNAME = "SystemName";
	public static final String SEQUENCE = "Sequence";
	public static final String ATTRIBUTES = "Attributes";
	public static final String DEFAULT = "Default";
	public static final String VALIDATIONEXPRESSIONID = "ValidationExpressionID";
	public static final String UPDATEHELPID = "UpdateHelpID";
	public static final String VALIDATIONLOOKUPNAME = "ValidationLookupName";
	public static final String VALIDATIONEXTERNALNAME = "ValidationExternalName";
	public static final String MAXUPDATE = "MaxUpdate";

	private static final List<MetadataElement> sAttributes =
		new ArrayList<MetadataElement>()
		{{
			add(new MetadataElement(METADATAENTRYID, sRETSID, RetsVersion.RETS_1_7, sREQUIRED));
			add(new MetadataElement(SYSTEMNAME, sRETSNAME, sREQUIRED));
			add(new MetadataElement(SEQUENCE, sAttrNumeric));
			add(new MetadataElement(ATTRIBUTES, sAttributes1to5, sREQUIRED));
			add(new MetadataElement(DEFAULT, sPlaintext));
			add(new MetadataElement(VALIDATIONEXPRESSIONID, sCSV_RETSNAMES));
			add(new MetadataElement(UPDATEHELPID, sRETSNAME));
			add(new MetadataElement(VALIDATIONLOOKUPNAME, sRETSNAME));
			add(new MetadataElement(VALIDATIONEXTERNALNAME, sRETSNAME));
			add(new MetadataElement(MAXUPDATE, sAttrNumeric));
		}};

	private MTable table;
	private MUpdateHelp updateHelp;
	private Set<MValidationExpression> validationExpressions;
	private MValidationLookup validationLookup;
	private MValidationExternal validationExternal;

	public MUpdateType() {
		this(DEFAULT_PARSING);
	}

	public MUpdateType(boolean strictParsing) {
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
		for (MetadataElement element : MUpdateType.sAttributes)
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

	public MTable getMTable() {
		return this.table;
	}

	public void setMTable(MTable table) {
		this.table = table;
		if (this.table == null) {
			setSystemName(null);
		} else {
			String systemName = this.table.getSystemName();
			setSystemName(systemName);
		}
	}

	public String getSystemName() {
		return getStringAttribute(SYSTEMNAME);
	}

	public void setSystemName(String systemName) {
		String systemNameStr = sRETSNAME.render(systemName);
		setAttribute(SYSTEMNAME, systemNameStr);
	}

	public int getSequence() {
		return getIntAttribute(SEQUENCE);
	}

	public void setSequence(int sequence) {
		String sequenceStr = sAttrNumeric.render(Integer.valueOf(sequence));
		setAttribute(SEQUENCE, sequenceStr);
	}

	public String getAttributes() {
		return getStringAttribute(ATTRIBUTES);
	}

	public void setAttributes(String attributes) {
		String attributesStr = sAttributes1to5.render(attributes);
		setAttribute(ATTRIBUTES, attributesStr);
	}

	public Set<String> getMAttributes() {
		String attributesCsv = getAttributes();
		Set<String> attributeSet = toAttributes(attributesCsv);
		return attributeSet;
	}

	public void setMAttributes(Set<String> attributeSet) {
		String MAttributeCsv = toAttributes(attributeSet);
		setAttributes(MAttributeCsv);
	}

	public static Set<String> toAttributes(String attributesCsv) {
		if (attributesCsv == null) {
			return null;
		}
		Set<String> attributeSet = new LinkedHashSet<String>();
		String[] attributes = StringUtils.split(attributesCsv, ",");
		for (String attribute : attributes) {
			attributeSet.add(attribute);
		}
		return attributeSet;
	}

	public static String toAttributes(Collection<String> attributeSet) {
		if (attributeSet == null || attributeSet.isEmpty()) {
			return null;
		}
		String attributesCsv = StringUtils.join(attributeSet, ",");
		return attributesCsv;
	}

	public String getDefault() {
		return getStringAttribute(DEFAULT);
	}

	public void setDefault(String defaultValue) {
		String defaultStr = sPlaintext.render(defaultValue);
		setAttribute(DEFAULT, defaultStr);
	}

	public String getValidationExpressionID() {
		return getStringAttribute(VALIDATIONEXPRESSIONID);
	}

	public void setValidationExpressionID(String validationExpressionId) {
		String validationExpressionIdStr = sCSV_RETSNAMES.render(validationExpressionId);
		setAttribute(VALIDATIONEXPRESSIONID, validationExpressionIdStr);
	}

	public Set<MValidationExpression> getMValidationExpressions() {
		return this.validationExpressions;
	}

	public void setMValidationExpressions(Set<MValidationExpression> validationExpressions) {
		this.validationExpressions = validationExpressions;
		if (this.validationExpressions == null) {
			setValidationExpressionID(null);
		} else {
			String validationExpressionId = toValidationExpressionId(this.validationExpressions);
			setValidationExpressionID(validationExpressionId);
		}
	}

	public static Set<String> toValidationExpressionIds(String validationExpressionIdCsv) {
		if (validationExpressionIdCsv == null) {
			return null;
		}
		Set<String> validationExpressionIdSet = new LinkedHashSet<String>();
		String[] validationExpressionIds = StringUtils.split(validationExpressionIdCsv, ",");
		for (String validationExpressionId : validationExpressionIds) {
			validationExpressionIdSet.add(validationExpressionId);
		}
		return validationExpressionIdSet;
	}

	public static String toValidationExpressionId(Collection<MValidationExpression> validationExpressions) {
		if (validationExpressions == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		String prefix = "";
		for (MValidationExpression validationExpression : validationExpressions) {
			String validationExpressionId = validationExpression.getValidationExpressionID();
			sb.append(prefix);
			sb.append(validationExpressionId);
			prefix = ",";
		}
		String validationExpressionId = sb.toString();
		return validationExpressionId;
	}

	public MUpdateHelp getMUpdateHelp() {
		return this.updateHelp;
	}

	public void setMUpdateHelp(MUpdateHelp updateHelp) {
		this.updateHelp = updateHelp;
		if (this.updateHelp == null) {
			setUpdateHelpID(null);
		} else {
			String updateHelpId = this.updateHelp.getUpdateHelpID();
			setUpdateHelpID(updateHelpId);
		}
	}

	public String getUpdateHelpID() {
		return getStringAttribute(UPDATEHELPID);
	}

	public void setUpdateHelpID(String updateHelpId) {
		String updateHelpIdStr = sRETSNAME.render(updateHelpId);
		setAttribute(UPDATEHELPID, updateHelpIdStr);
	}

	public MValidationLookup getMValidationLookup() {
		return this.validationLookup;
	}

	public void setMValidationLookup(MValidationLookup validationLookup) {
		this.validationLookup = validationLookup;
		if (this.validationLookup == null) {
			setValidationLookupName(null);
		} else {
			String validationLookupName = this.validationLookup.getValidationLookupName();
			setValidationLookupName(validationLookupName);
		}
	}

	public String getValidationLookupName() {
		return getStringAttribute(VALIDATIONLOOKUPNAME);
	}

	public void setValidationLookupName(String validationLookupName) {
		String validationLookupNameStr = sRETSNAME.render(validationLookupName);
		setAttribute(VALIDATIONLOOKUPNAME, validationLookupNameStr);
	}

	public MValidationExternal getMValidationExternal() {
		return this.validationExternal;
	}

	public void setMValidationExternal(MValidationExternal validationExternal) {
		this.validationExternal = validationExternal;
		if (this.validationExternal == null) {
			setValidationExternalName(null);
		} else {
			String validationExternalName = this.validationExternal.getValidationExternalName();
			setValidationExternalName(validationExternalName);
		}
	}

	public String getValidationExternalName() {
		return getStringAttribute(VALIDATIONEXTERNALNAME);
	}

	public void setValidationExternalName(String validationExternalName) {
		String validationExternalNameStr = sRETSNAME.render(validationExternalName);
		setAttribute(VALIDATIONEXTERNALNAME, validationExternalNameStr);
	}

	public int getMaxUpdate() {
		return getIntAttribute(MAXUPDATE);
	}

	public void setMaxUpdate(int maxUpdate) {
		String maxUpdateStr = sAttrNumeric.render(Integer.valueOf(maxUpdate));
		setAttribute(MAXUPDATE, maxUpdateStr);
	}

	public MUpdate getMUpdate() {
		MUpdate update = (MUpdate)getParent();
		return update;
	}

	public void setMUpdate(MUpdate update) {
		setParent(update);
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
		return MetadataType.UPDATE_TYPE;
	}
}
