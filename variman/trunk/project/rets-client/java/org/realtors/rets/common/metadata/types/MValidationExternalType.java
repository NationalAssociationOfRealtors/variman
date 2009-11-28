package org.realtors.rets.common.metadata.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataElement;
import org.realtors.rets.common.metadata.MetadataType;

public class MValidationExternalType extends MetaObject {
	public static final String METADATATYPENAME = "ValidationExternalType";

	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String SEARCHFIELD = "SearchField";
	public static final String DISPLAYFIELD = "DisplayField";
	public static final String RESULTFIELDS = "ResultFields";

	private static final List<MetadataElement> sAttributes =
		new ArrayList<MetadataElement>()
		{{
			add(new MetadataElement(METADATAENTRYID, sRETSID, RetsVersion.RETS_1_7, sREQUIRED));
			add(new MetadataElement(SEARCHFIELD, sPlaintext512, sREQUIRED));
			add(new MetadataElement(DISPLAYFIELD, sPlaintext512, sREQUIRED));
			add(new MetadataElement(RESULTFIELDS, sPlaintext1024, sREQUIRED));
		}};
 
	public MValidationExternalType() {
		this(DEFAULT_PARSING);
	}

	public MValidationExternalType(boolean strictParsing) {
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
		for (MetadataElement element : MValidationExternalType.sAttributes)
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

	public String getSearchField() {
		return getStringAttribute(SEARCHFIELD);
	}

	public void setSearchField(String searchField) {
		String searchFieldStr = sPlaintext512.render(searchField);
		setAttribute(SEARCHFIELD, searchFieldStr);
	}

	public Set<String> getSearchFields() {
		String searchFieldCsv = getSearchField();
		Set<String> searchFieldSet = toSearchFields(searchFieldCsv);
		return searchFieldSet;
	}

	public void setSearchFields(Set<String> searchFields) {
		String searchFieldCsv = toSearchField(searchFields);
		setSearchField(searchFieldCsv);
	}

	public static Set<String> toSearchFields(String searchFieldCsv) {
		if (searchFieldCsv == null) {
			return null;
		}
		Set<String> searchFieldSet = new LinkedHashSet<String>();
		String[] searchFields = StringUtils.split(searchFieldCsv, ",");
		for (String searchField : searchFields) {
			searchFieldSet.add(searchField);
		}
		return searchFieldSet;
	}

	public static String toSearchField(Collection<String> searchFields) {
		if (searchFields == null || searchFields.isEmpty()) {
			return null;
		}
		String searchField = StringUtils.join(searchFields, ",");
		return searchField;
	}

	public String getDisplayField() {
		return getStringAttribute(DISPLAYFIELD);
	}

	public void setDisplayField(String displayField) {
		String displayFieldStr = sPlaintext512.render(displayField);
		setAttribute(DISPLAYFIELD, displayFieldStr);
	}

	public Set<String> getDisplayFields() {
		String displayFieldCsv = getDisplayField();
		Set<String> displayFieldSet = toDisplayFields(displayFieldCsv);
		return displayFieldSet;
	}

	public void setDisplayFields(Set<String> displayFields) {
		String displayFieldCsv = toDisplayField(displayFields);
		setDisplayField(displayFieldCsv);
	}

	public static Set<String> toDisplayFields(String displayFieldCsv) {
		if (displayFieldCsv == null) {
			return null;
		}
		Set<String> displayFieldSet = new LinkedHashSet<String>();
		String[] displayFields = StringUtils.split(displayFieldCsv, ",");
		for (String displayField : displayFields) {
			displayFieldSet.add(displayField);
		}
		return displayFieldSet;
	}

	public static String toDisplayField(Collection<String> displayFields) {
		if (displayFields == null || displayFields.isEmpty()) {
			return null;
		}
		String displayField = StringUtils.join(displayFields, ",");
		return displayField;
	}

	public String getResultFields() {
		return getStringAttribute(RESULTFIELDS);
	}

	public void setResultFields(String resultFields) {
		String resultFieldsStr = sPlaintext1024.render(resultFields);
		setAttribute(RESULTFIELDS, resultFieldsStr);
	}

	public Map<String, String> getResultFieldsMap() {
		String resultFieldsCsv = getResultFields();
		Map<String, String> resultFieldsMap = toResultFieldsMap(resultFieldsCsv);
		return resultFieldsMap;
	}

	public void setResultFieldsMap(Map<String, String> resultFieldsMap) {
		String resultFields = toResultFields(resultFieldsMap);
		setResultFields(resultFields);
	}

	public static Map<String, String> toResultFieldsMap(String resultFieldsCsv) {
		if (resultFieldsCsv == null) {
			return null;
		}
		Map<String, String> resultFieldsMap = new LinkedHashMap<String, String>();
		String[] resultFields = StringUtils.split(resultFieldsCsv, ",");
		for (String resultField : resultFields) {
			String[] resultFieldParts = StringUtils.split(resultField, "=", 2);
			String target = resultFieldParts[0];
			String source = resultFieldParts[1];
			resultFieldsMap.put(target, source);
		}
		return resultFieldsMap;
	}

	public static String toResultFields(Map<String, String> resultFieldsMap) {
		if (resultFieldsMap == null || resultFieldsMap.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		
		String prefix = "";
		Set<Entry<String, String>> entrySet = resultFieldsMap.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String target = entry.getKey();
			String source = entry.getValue();
			sb.append(prefix);
			sb.append(target);
			sb.append("=");
			sb.append(source);
			prefix = ",";
		}
		
		String resultFields = sb.toString();
		return resultFields;
	}

	public MValidationExternal getMValidationExternal() {
		MValidationExternal validationexternal = (MValidationExternal)getParent();
		return validationexternal;
	}

	public void setMValidationExternal(MValidationExternal validationexternal) {
		setParent(validationexternal);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sNO_CHILDREN;
	}

	@Override
	protected String getIdAttr() {
		return null;
	}

	@Override
	public final String getMetadataTypeName() {
		return METADATATYPENAME;
	}

	@Override
	public final MetadataType getMetadataType() {
		return MetadataType.VALIDATION_EXTERNAL_TYPE;
	}
}
