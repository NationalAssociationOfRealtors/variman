package org.realtors.rets.common.metadata.types;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataElement;
import org.realtors.rets.common.metadata.MetadataType;

public class MResource extends MetaObject {
	private static final String METADATATYPENAME = "Resource";

	private static final MClass[] EMPTY_CLASS_ARRAY = {};
	private static final MObject[] EMPTY_OBJECT_ARRAY = {};
	private static final MSearchHelp[] EMPTY_SEARCH_HELP_ARRAY = {};
	private static final MEditMask[] EMPTY_EDIT_MASK_ARRAY = {};
	private static final MLookup[] EMPTY_LOOKUP_ARRAY = {};
	private static final MUpdateHelp[] EMPTY_UPDATE_HELP_ARRAY = {};
	private static final MValidationExpression[] EMPTY_VALIDATION_EXPRESSION_ARRAY = {};
	private static final MValidationLookup[] EMPTY_VALIDATION_LOOKUP_ARRAY = {};
	private static final MValidationExternal[] EMPTY_VALIDATION_EXTERNAL_ARRAY = {};

	private static final MetadataType[] CHILDREN = {
		MetadataType.CLASS,
		MetadataType.OBJECT,
		MetadataType.SEARCH_HELP,
		MetadataType.EDITMASK,
		MetadataType.LOOKUP,
		MetadataType.UPDATE_HELP,
		MetadataType.VALIDATION_EXPRESSION,
		MetadataType.VALIDATION_LOOKUP,
		MetadataType.VALIDATION_EXTERNAL,
	};

	public static final String RESOURCEID = "ResourceID";
	public static final String STANDARDNAME = "StandardName";
	public static final String VISIBLENAME = "VisibleName";
	public static final String DESCRIPTION = "Description";
	public static final String KEYFIELD = "KeyField";
	public static final String CLASSCOUNT = "ClassCount";
	public static final String CLASSVERSION = "ClassVersion";
	public static final String CLASSDATE = "ClassDate";
	public static final String OBJECTVERSION = "ObjectVersion";
	public static final String OBJECTDATE = "ObjectDate";
	public static final String SEARCHHELPVERSION = "SearchHelpVersion";
	public static final String SEARCHHELPDATE = "SearchHelpDate";
	public static final String EDITMASKVERSION = "EditMaskVersion";
	public static final String EDITMASKDATE = "EditMaskDate";
	public static final String LOOKUPVERSION = "LookupVersion";
	public static final String LOOKUPDATE = "LookupDate";
	public static final String UPDATEHELPVERSION = "UpdateHelpVersion";
	public static final String UPDATEHELPDATE = "UpdateHelpDate";
	public static final String VALIDATIONEXPRESSIONVERSION = "ValidationExpressionVersion";
	public static final String VALIDATIONEXPRESSIONDATE = "ValidationExpressionDate";
	public static final String VALIDATIONLOOKUPVERSION = "ValidationLookupVersion";
	public static final String VALIDATIONLOOKUPDATE = "ValidationLookupDate";
	public static final String VALIDATIONEXTERNALVERSION = "ValidationExternalVersion";
	public static final String VALIDATIONEXTERNALDATE = "ValidationExternalDate";

	private static final List<MetadataElement> sAttributes =
		new ArrayList<MetadataElement>()
		{{
			add(new MetadataElement(RESOURCEID, sRETSID, sREQUIRED));
			add(new MetadataElement(STANDARDNAME, sAlphanum64));
			add(new MetadataElement(VISIBLENAME, sPlaintext64, sREQUIRED));
			add(new MetadataElement(DESCRIPTION, sPlaintext64));
			/*
			 * This following should be a RETSNAME. The spec does say it
			 * suppose to be a RETSID. However, the spec's description says it
			 * is suppose to be a SystemName which is defined as a RETSNAME.
			 * Maybe the description meant MetadataEntryID which is a RETSID.
			 */
			add(new MetadataElement(KEYFIELD, sRETSID));
			add(new MetadataElement(CLASSCOUNT, sPOSITIVENUM));
			add(new MetadataElement(CLASSVERSION, sAttrVersion, sREQUIRED));
			add(new MetadataElement(CLASSDATE, sAttrDate, sREQUIRED));
			add(new MetadataElement(OBJECTVERSION, sAttrVersion));
			add(new MetadataElement(OBJECTDATE, sAttrDate));
			add(new MetadataElement(SEARCHHELPVERSION, sAttrVersion));
			add(new MetadataElement(SEARCHHELPDATE, sAttrDate));
			add(new MetadataElement(EDITMASKVERSION, sAttrVersion));
			add(new MetadataElement(EDITMASKDATE, sAttrDate));
			add(new MetadataElement(LOOKUPVERSION, sAttrVersion));
			add(new MetadataElement(LOOKUPDATE, sAttrDate));
			add(new MetadataElement(UPDATEHELPVERSION, sAttrVersion));
			add(new MetadataElement(UPDATEHELPDATE, sAttrDate));
			add(new MetadataElement(VALIDATIONEXPRESSIONVERSION, sAttrVersion));
			add(new MetadataElement(VALIDATIONEXPRESSIONDATE, sAttrDate));
			add(new MetadataElement(VALIDATIONLOOKUPVERSION, sAttrVersion));
			add(new MetadataElement(VALIDATIONLOOKUPDATE, sAttrDate));
			add(new MetadataElement(VALIDATIONEXTERNALVERSION, sAttrVersion));
			add(new MetadataElement(VALIDATIONEXTERNALDATE, sAttrDate));
		}};
		
	public MResource() {
		this(DEFAULT_PARSING);
	}

	public MResource(boolean strictParsing) {
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
		for (MetadataElement element : MResource.sAttributes)
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

	public String getResourceID() {
		return getStringAttribute(RESOURCEID);
	}

	public void setResourceID(String resourceId) {
		String resourceIdStr = sRETSID.render(resourceId);
		setAttribute(RESOURCEID, resourceIdStr);
	}

	public String getStandardName() {
		return getStringAttribute(STANDARDNAME);
	}

	public void setStandardName(String standardName) {
		String standardNameStr = sAlphanum64.render(standardName);
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
		String descriptionStr = sPlaintext64.render(description);
		setAttribute(DESCRIPTION, descriptionStr);
	}

	public String getKeyField() {
		return getStringAttribute(KEYFIELD);
	}

	public void setKeyField(String keyField) {
		String keyFieldStr = sRETSID.render(keyField);
		setAttribute(KEYFIELD, keyFieldStr);
	}

	public int getClassCount() {
		int classCount = getMClasses().length;
		return classCount;
		//return getIntAttribute(CLASSCOUNT);
	}

	public int getClassVersion() {
		return getIntAttribute(CLASSVERSION);
	}

	public void setClassVersion(int classVersion) {
		String classVersionStr = sAttrVersion.render(classVersion);
		setAttribute(CLASSVERSION, classVersionStr);
	}

	public Date getClassDate() {
		return getDateAttribute(CLASSDATE);
	}

	public void setClassDate(Date classDate) {
		String classDateStr = sAttrDate.render(classDate);
		setAttribute(CLASSDATE, classDateStr);
	}

	public int getObjectVersion() {
		return getIntAttribute(OBJECTVERSION);
	}

	public void setObjectVersion(int objectVersion) {
		String objectVersionStr = sAttrVersion.render(objectVersion);
		setAttribute(OBJECTVERSION, objectVersionStr);
	}

	public Date getObjectDate() {
		return getDateAttribute(OBJECTDATE);
	}

	public void setObjectDate(Date objectDate) {
		String objectDateStr = sAttrDate.render(objectDate);
		setAttribute(OBJECTDATE, objectDateStr);
	}

	public int getSearchHelpVersion() {
		return getIntAttribute(SEARCHHELPVERSION);
	}

	public void setSearchHelpVersion(int searchHelpVersion) {
		String searchHelpVersionStr = sAttrVersion.render(searchHelpVersion);
		setAttribute(SEARCHHELPVERSION, searchHelpVersionStr);
	}

	public Date getSearchHelpDate() {
		return getDateAttribute(SEARCHHELPDATE);
	}

	public void setSearchHelpDate(Date searchHelpDate) {
		String searchHelpDateStr = sAttrDate.render(searchHelpDate);
		setAttribute(SEARCHHELPDATE, searchHelpDateStr);
	}

	public int getEditMaskVersion() {
		return getIntAttribute(EDITMASKVERSION);
	}

	public void setEditMaskVersion(int editMaskVersion) {
		String editMaskVersionStr = sAttrVersion.render(editMaskVersion);
		setAttribute(EDITMASKVERSION, editMaskVersionStr);
	}

	public Date getEditMaskDate() {
		return getDateAttribute(EDITMASKDATE);
	}

	public void setEditMaskDate(Date editMaskDate) {
		String editMaskDateStr = sAttrDate.render(editMaskDate);
		setAttribute(EDITMASKDATE, editMaskDateStr);
	}

	public int getLookupVersion() {
		return getIntAttribute(LOOKUPVERSION);
	}

	public void setLookupVersion(int lookupVersion) {
		String lookupVersionStr = sAttrVersion.render(lookupVersion);
		setAttribute(LOOKUPVERSION, lookupVersionStr);
	}

	public Date getLookupDate() {
		return getDateAttribute(LOOKUPDATE);
	}

	public void setLookupDate(Date lookupDate) {
		String lookupDateStr = sAttrDate.render(lookupDate);
		setAttribute(LOOKUPDATE, lookupDateStr);
	}

	public int getUpdateHelpVersion() {
		return getIntAttribute(UPDATEHELPVERSION);
	}

	public void setUpdateHelpVersion(int updateHelpVersion) {
		String updateHelpVersionStr = sAttrVersion.render(updateHelpVersion);
		setAttribute(UPDATEHELPVERSION, updateHelpVersionStr);
	}

	public Date getUpdateHelpDate() {
		return getDateAttribute(UPDATEHELPDATE);
	}

	public void setUpdateHelpDate(Date updateHelpDate) {
		String updateHelpDateStr = sAttrDate.render(updateHelpDate);
		setAttribute(UPDATEHELPDATE, updateHelpDateStr);
	}

	public int getValidationExpressionVersion() {
		return getIntAttribute(VALIDATIONEXPRESSIONVERSION);
	}

	public void setValidationExpressionVersion(int validationExpressionVersion) {
		String validationExpressionVersionStr = sAttrVersion.render(validationExpressionVersion);
		setAttribute(VALIDATIONEXPRESSIONVERSION, validationExpressionVersionStr);
	}

	public Date getValidationExpressionDate() {
		return getDateAttribute(VALIDATIONEXPRESSIONDATE);
	}

	public void setValidationExpressionDate(Date validationExpressionDate) {
		String validationExpressionDateStr = sAttrDate.render(validationExpressionDate);
		setAttribute(VALIDATIONEXPRESSIONDATE, validationExpressionDateStr);
	}

	public int getValidationLookupVersion() {
		return getIntAttribute(VALIDATIONLOOKUPVERSION);
	}

	public void setValidationLookupVersion(int validationLookupVersion) {
		String validationLookupVersionStr = sAttrVersion.render(validationLookupVersion);
		setAttribute(VALIDATIONLOOKUPVERSION, validationLookupVersionStr);
	}

	public Date getValidationLookupDate() {
		return getDateAttribute(VALIDATIONLOOKUPDATE);
	}

	public void setValidationLookupDate(Date validationLookupDate) {
		String validationLookupDateStr = sAttrDate.render(validationLookupDate);
		setAttribute(VALIDATIONLOOKUPDATE, validationLookupDateStr);
	}

	public int getValidationExternalVersion() {
		return getIntAttribute(VALIDATIONEXTERNALVERSION);
	}

	public void setValidationExternalVersion(int validationExternalVersion) {
		String validationExternalVersionStr = sAttrVersion.render(validationExternalVersion);
		setAttribute(VALIDATIONEXTERNALVERSION, validationExternalVersionStr);
	}

	public Date getValidationExternalDate() {
		return getDateAttribute(VALIDATIONEXTERNALDATE);
	}

	public void setValidationExternalDate(Date validationExternalDate) {
		String validationExternalDateStr = sAttrDate.render(validationExternalDate);
		setAttribute(VALIDATIONEXTERNALDATE, validationExternalDateStr);
	}

	public MClass getMClass(String className) {
		return (MClass) getChild(MetadataType.CLASS, className);
	}

	public Set<MClass> getClasses() {
		MClass[] mClasses = getMClasses();
		int numClasses = mClasses.length;
		Set<MClass> classes = new LinkedHashSet<MClass>(numClasses);
		for (MClass mClass : mClasses) {
			classes.add(mClass);
		}
		return classes;
	}

	public void setClasses(Set<MClass> classes) {
		MClass[] mClasses = classes.toArray(EMPTY_CLASS_ARRAY);
		setMClasses(mClasses);
	}

	public MClass[] getMClasses() {
		return getChildren(MetadataType.CLASS).toArray(EMPTY_CLASS_ARRAY);
	}

	public void setMClasses(MClass[] classes) {
		deleteAllChildren(MetadataType.CLASS);
		if (classes != null) {
			addChildren(MetadataType.CLASS, classes);
		}
	}

	public MObject getMObject(String objectType) {
		return (MObject) getChild(MetadataType.OBJECT, objectType);
	}

	public Set<MObject> getObjects() {
		MObject[] mObjects = getMObjects();
		int numObjects = mObjects.length;
		Set<MObject> objects = new LinkedHashSet<MObject>(numObjects);
		for (MObject mObject : mObjects) {
			objects.add(mObject);
		}
		return objects;
	}

	public void setObjects(Set<MObject> objects) {
		MObject[] mObjects = objects.toArray(EMPTY_OBJECT_ARRAY);
		setMObjects(mObjects);
	}

	public MObject[] getMObjects() {
		return getChildren(MetadataType.OBJECT).toArray(EMPTY_OBJECT_ARRAY);
	}

	public void setMObjects(MObject[] objects) {
		deleteAllChildren(MetadataType.OBJECT);
		if (objects != null) {
			addChildren(MetadataType.OBJECT, objects);
		}
	}

	public MSearchHelp getMSearchHelp(String searchHelpID) {
		return (MSearchHelp) getChild(MetadataType.SEARCH_HELP, searchHelpID);
	}

	public Set<MSearchHelp> getSearchHelps() {
		MSearchHelp[] mSearchHelps = getMSearchHelps();
		int numSearchHelps = mSearchHelps.length;
		Set<MSearchHelp> searchhelps = new LinkedHashSet<MSearchHelp>(numSearchHelps);
		for (MSearchHelp mSearchHelp : mSearchHelps) {
			searchhelps.add(mSearchHelp);
		}
		return searchhelps;
	}

	public void setSearchHelps(Set<MSearchHelp> searchhelps) {
		MSearchHelp[] mSearchHelps = searchhelps.toArray(EMPTY_SEARCH_HELP_ARRAY);
		setMSearchHelps(mSearchHelps);
	}

	public MSearchHelp[] getMSearchHelps() {
		return getChildren(MetadataType.SEARCH_HELP).toArray(EMPTY_SEARCH_HELP_ARRAY);
	}

	public void setMSearchHelps(MSearchHelp[] searchhelps) {
		deleteAllChildren(MetadataType.SEARCH_HELP);
		if (searchhelps != null) {
			addChildren(MetadataType.SEARCH_HELP, searchhelps);
		}
	}

	public MEditMask getMEditMask(String editMaskID) {
		return (MEditMask) getChild(MetadataType.EDITMASK, editMaskID);
	}

	public Set<MEditMask> getEditMasks() {
		MEditMask[] mEditMasks = getMEditMasks();
		int numEditMasks = mEditMasks.length;
		Set<MEditMask> editmasks = new LinkedHashSet<MEditMask>(numEditMasks);
		for (MEditMask mEditMask : mEditMasks) {
			editmasks.add(mEditMask);
		}
		return editmasks;
	}

	public void setEditMasks(Set<MEditMask> editmasks) {
		MEditMask[] mEditMasks = editmasks.toArray(EMPTY_EDIT_MASK_ARRAY);
		setMEditMasks(mEditMasks);
	}

	public MEditMask[] getMEditMasks() {
		return getChildren(MetadataType.EDITMASK).toArray(EMPTY_EDIT_MASK_ARRAY);
	}

	public void setMEditMasks(MEditMask[] editmasks) {
		deleteAllChildren(MetadataType.EDITMASK);
		if (editmasks != null) {
			addChildren(MetadataType.EDITMASK, editmasks);
		}
	}

	public MLookup getMLookup(String lookupName) {
		return (MLookup) getChild(MetadataType.LOOKUP, lookupName);
	}

	public Set<MLookup> getLookups() {
		MLookup[] mLookups = getMLookups();
		int numLookups = mLookups.length;
		Set<MLookup> lookups = new LinkedHashSet<MLookup>(numLookups);
		for (MLookup mLookup : mLookups) {
			lookups.add(mLookup);
		}
		return lookups;
	}

	public void setLookups(Set<MLookup> lookups) {
		MLookup[] mLookups = lookups.toArray(EMPTY_LOOKUP_ARRAY);
		setMLookups(mLookups);
	}

	public MLookup[] getMLookups() {
		return getChildren(MetadataType.LOOKUP).toArray(EMPTY_LOOKUP_ARRAY);
	}

	public void setMLookups(MLookup[] lookups) {
		deleteAllChildren(MetadataType.LOOKUP);
		if (lookups != null) {
			addChildren(MetadataType.LOOKUP, lookups);
		}
	}

	public MUpdateHelp getMUpdateHelp(String updateHelpID) {
		return (MUpdateHelp) getChild(MetadataType.UPDATE_HELP, updateHelpID);
	}

	public Set<MUpdateHelp> getUpdateHelps() {
		MUpdateHelp[] mUpdateHelps = getMUpdateHelps();
		int numUpdateHelps = mUpdateHelps.length;
		Set<MUpdateHelp> updatehelps = new LinkedHashSet<MUpdateHelp>(numUpdateHelps);
		for (MUpdateHelp mUpdateHelp : mUpdateHelps) {
			updatehelps.add(mUpdateHelp);
		}
		return updatehelps;
	}

	public void setUpdateHelps(Set<MUpdateHelp> updatehelps) {
		MUpdateHelp[] mUpdateHelps = updatehelps.toArray(EMPTY_UPDATE_HELP_ARRAY);
		setMUpdateHelps(mUpdateHelps);
	}

	public MUpdateHelp[] getMUpdateHelps() {
		return getChildren(MetadataType.UPDATE_HELP).toArray(EMPTY_UPDATE_HELP_ARRAY);
	}

	public void setMUpdateHelps(MUpdateHelp[] updatehelps) {
		deleteAllChildren(MetadataType.UPDATE_HELP);
		if (updatehelps != null) {
			addChildren(MetadataType.UPDATE_HELP, updatehelps);
		}
	}

	public MValidationExpression getMValidationExpression(String validationExpressionID) {
		return (MValidationExpression) getChild(MetadataType.VALIDATION_EXPRESSION, validationExpressionID);
	}

	public Set<MValidationExpression> getValidationExpressions() {
		MValidationExpression[] mValidationExpressions = getMValidationExpressions();
		int numValidationExpressions = mValidationExpressions.length;
		Set<MValidationExpression> validationexpressions = new LinkedHashSet<MValidationExpression>(numValidationExpressions);
		for (MValidationExpression mValidationExpression : mValidationExpressions) {
			validationexpressions.add(mValidationExpression);
		}
		return validationexpressions;
	}

	public void setValidationExpressions(Set<MValidationExpression> validationexpressions) {
		MValidationExpression[] mValidationExpressions = validationexpressions.toArray(EMPTY_VALIDATION_EXPRESSION_ARRAY);
		setMValidationExpressions(mValidationExpressions);
	}

	public MValidationExpression[] getMValidationExpressions() {
		return getChildren(MetadataType.VALIDATION_EXPRESSION).toArray(EMPTY_VALIDATION_EXPRESSION_ARRAY);
	}

	public void setMValidationExpressions(MValidationExpression[] validationexpressions) {
		deleteAllChildren(MetadataType.VALIDATION_EXPRESSION);
		if (validationexpressions != null) {
			addChildren(MetadataType.VALIDATION_EXPRESSION, validationexpressions);
		}
	}

	public MValidationLookup getMValidationLookup(String validationLookupName) {
		return (MValidationLookup) getChild(MetadataType.VALIDATION_LOOKUP, validationLookupName);
	}

	public Set<MValidationLookup> getValidationLookups() {
		MValidationLookup[] mValidationLookups = getMValidationLookups();
		int numValidationLookups = mValidationLookups.length;
		Set<MValidationLookup> validationlookups = new LinkedHashSet<MValidationLookup>(numValidationLookups);
		for (MValidationLookup mValidationLookup : mValidationLookups) {
			validationlookups.add(mValidationLookup);
		}
		return validationlookups;
	}

	public void setValidationLookups(Set<MValidationLookup> validationlookups) {
		MValidationLookup[] mValidationLookups = validationlookups.toArray(EMPTY_VALIDATION_LOOKUP_ARRAY);
		setMValidationLookups(mValidationLookups);
	}

	public MValidationLookup[] getMValidationLookups() {
		return getChildren(MetadataType.VALIDATION_LOOKUP).toArray(EMPTY_VALIDATION_LOOKUP_ARRAY);
	}

	public void setMValidationLookups(MValidationLookup[] validationlookups) {
		deleteAllChildren(MetadataType.VALIDATION_LOOKUP);
		if (validationlookups != null) {
			addChildren(MetadataType.VALIDATION_LOOKUP, validationlookups);
		}
	}

	public MValidationExternal getMValidationExternal(String validationExternalName) {
		return (MValidationExternal) getChild(MetadataType.VALIDATION_EXTERNAL, validationExternalName);
	}

	public Set<MValidationExternal> getValidationExternals() {
		MValidationExternal[] mValidationExternals = getMValidationExternals();
		int numValidationExternals = mValidationExternals.length;
		Set<MValidationExternal> validationexternals = new LinkedHashSet<MValidationExternal>(numValidationExternals);
		for (MValidationExternal mValidationExternal : mValidationExternals) {
			validationexternals.add(mValidationExternal);
		}
		return validationexternals;
	}

	public void setValidationExternals(Set<MValidationExternal> validationexternals) {
		MValidationExternal[] mValidationExternals = validationexternals.toArray(EMPTY_VALIDATION_EXTERNAL_ARRAY);
		setMValidationExternals(mValidationExternals);
	}

	public MValidationExternal[] getMValidationExternals() {
		return getChildren(MetadataType.VALIDATION_EXTERNAL).toArray(EMPTY_VALIDATION_EXTERNAL_ARRAY);
	}

	public void setMValidationExternals(MValidationExternal[] validationexternals) {
		deleteAllChildren(MetadataType.VALIDATION_EXTERNAL);
		if (validationexternals != null) {
			addChildren(MetadataType.VALIDATION_EXTERNAL, validationexternals);
		}
	}

	public MSystem getMSystem() {
		MSystem system = (MSystem)getParent();
		return system;
	}

	public void setMSystem(MSystem system) {
		setParent(system);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return CHILDREN;
	}

	@Override
	protected String getIdAttr() {
		return RESOURCEID;
	}

	@Override
	public final String getMetadataTypeName() {
		return METADATATYPENAME;
	}

	@Override
	public final MetadataType getMetadataType() {
		return MetadataType.RESOURCE;
	}

	@Override
	public String getLevel() {
		return "";
	}
}
