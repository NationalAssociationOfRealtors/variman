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

public class MUpdate extends MetaObject {
	private static final String METADATATYPENAME = "Update";

	private static final MUpdateType[] EMPTY_UPDATE_TYPE_ARRAY = {};

	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String UPDATENAME = "UpdateName";
	public static final String DESCRIPTION = "Description";
	public static final String KEYFIELD = "KeyField";
	public static final String UPDATETYPEVERSION = "UpdateTypeVersion";
	public static final String UPDATETYPEDATE = "UpdateTypeDate";

	private static final List<MetadataElement> sAttributes =
		new ArrayList<MetadataElement>()
		{{
			add(new MetadataElement(METADATAENTRYID, sRETSID, RetsVersion.RETS_1_7, sREQUIRED));
			add(new MetadataElement(UPDATENAME, sAlphanum24, sREQUIRED));
			add(new MetadataElement(DESCRIPTION, sPlaintext64));
			add(new MetadataElement(KEYFIELD, sRETSNAME, sREQUIRED));
			add(new MetadataElement(UPDATETYPEVERSION, sAttrVersion, sREQUIRED));
			add(new MetadataElement(UPDATETYPEDATE, sAttrDate, sREQUIRED));
		}};

	public MUpdate() {
		this(DEFAULT_PARSING);
	}

	public MUpdate(boolean strictParsing) {
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
		for (MetadataElement element : MUpdate.sAttributes)
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

	public String getUpdateName() {
		return getStringAttribute(UPDATENAME);
	}

	public void setUpdateName(String updateName) {
		String updateNameStr = sAlphanum24.render(updateName);
		setAttribute(UPDATENAME, updateNameStr);
	}

	public String getDescription() {
		return getStringAttribute(DESCRIPTION);
	}

	public void setDescription(String description) {
		String descriptionStr = sPlaintext64.render(description);
		setAttribute(DESCRIPTION, descriptionStr);
	}

	public String getKeyField() {
		return getStringAttribute(KEYFIELD);
	}

	public void setKeyField(String keyField) {
		String keyFieldStr = sRETSNAME.render(keyField);
		setAttribute(KEYFIELD, keyFieldStr);
	}

	public int getUpdateTypeVersion() {
		return getIntAttribute(UPDATETYPEVERSION);
	}

	public void setUpdateTypeVersion(int updateTypeVersion) {
		String updateTypeVersionStr = sAttrVersion.render(Integer.valueOf(updateTypeVersion));
		setAttribute(UPDATETYPEVERSION, updateTypeVersionStr);
	}

	public Date getUpdateTypeDate() {
		return getDateAttribute(UPDATETYPEDATE);
	}

	public void setUpdateTypeDate(Date updateTypeDate) {
		String updateTypeDateStr = sAttrDate.render(updateTypeDate);
		setAttribute(UPDATETYPEDATE, updateTypeDateStr);
	}

	public MUpdateType getMUpdateType(String systemName) {
		return (MUpdateType) getChild(MetadataType.UPDATE_TYPE, systemName);
	}

	public Set<MUpdateType> getUpdateTypes() {
		MUpdateType[] mUpdateTypes = getMUpdateTypes();
		int numUpdateTypes = mUpdateTypes.length;
		Set<MUpdateType> updatetypes = new LinkedHashSet<MUpdateType>(numUpdateTypes);
		for (MUpdateType mUpdateType : mUpdateTypes) {
			updatetypes.add(mUpdateType);
		}
		return updatetypes;
	}

	public void setUpdateTypes(Set<MUpdateType> updatetypes) {
		MUpdateType[] mUpdateTypes = updatetypes.toArray(EMPTY_UPDATE_TYPE_ARRAY);
		setMUpdateTypes(mUpdateTypes);
	}

	public MUpdateType[] getMUpdateTypes() {
		return getChildren(MetadataType.UPDATE_TYPE).toArray(EMPTY_UPDATE_TYPE_ARRAY);
	}

	public void setMUpdateTypes(MUpdateType[] updateTypes) {
		deleteAllChildren(MetadataType.UPDATE_TYPE);
		if (updateTypes != null) {
			addChildren(MetadataType.UPDATE_TYPE, updateTypes);
		}
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

	private static final MetadataType[] sTypes = { MetadataType.UPDATE_TYPE };
}
