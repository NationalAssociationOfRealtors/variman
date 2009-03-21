package org.realtors.rets.common.metadata.types;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataElement;
import org.realtors.rets.common.metadata.MetadataType;

public class MResource extends MetaObject {
	private static final String METADATATYPENAME = "Resource";
	
	private static final MetadataType[] CHILDREN = { 
		MetadataType.VALIDATION_EXPRESSION, 
		MetadataType.LOOKUP,
		MetadataType.CLASS, 
		MetadataType.OBJECT, 
		MetadataType.VALIDATION_EXTERNAL, 
		MetadataType.VALIDATION_LOOKUP,
		MetadataType.EDITMASK, 
		MetadataType.UPDATE_HELP, 
		MetadataType.SEARCH_HELP
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
        
	public static void addAttributes(String name, AttrType type)
	{
		sAttributes.add(new MetadataElement(name, type));
	}
    	
	public MResource() {
		this(DEFAULT_PARSING);
	}

	public MResource(boolean strictParsing) {
		super(strictParsing);
	}

	public String getResourceID() {
		return getStringAttribute(RESOURCEID);
	}

	public String getStandardName() {
		return getStringAttribute(STANDARDNAME);
	}

	public String getVisibleName() {
		return getStringAttribute(VISIBLENAME);
	}

	public String getDescription() {
		return getStringAttribute(DESCRIPTION);
	}

	public String getKeyField() {
		return getStringAttribute(KEYFIELD);
	}

	public int getClassCount() {
		return getMClasses().length;
		//return getIntAttribute(CLASSCOUNT);
	}

	public int getClassVersion() {
		return getIntAttribute(CLASSVERSION);
	}

	public Date getClassDate() {
		return getDateAttribute(CLASSDATE);
	}

	public int getObjectVersion() {
		return getIntAttribute(OBJECTVERSION);
	}

	public Date getObjectDate() {
		return getDateAttribute(OBJECTDATE);
	}

	public int getSearchHelpVersion() {
		return getIntAttribute(SEARCHHELPVERSION);
	}

	public Date getSearchHelpDate() {
		return getDateAttribute(SEARCHHELPDATE);
	}

	public int getEditMaskVersion() {
		return getIntAttribute(EDITMASKVERSION);
	}

	public Date getEditMaskDate() {
		return getDateAttribute(EDITMASKDATE);
	}

	public int getLookupVersion() {
		return getIntAttribute(LOOKUPVERSION);
	}

	public Date getLookupDate() {
		return getDateAttribute(LOOKUPDATE);
	}

	public int getUpdateHelpVersion() {
		return getIntAttribute(UPDATEHELPVERSION);
	}

	public Date getUpdateHelpDate() {
		return getDateAttribute(UPDATEHELPDATE);
	}

	public int getValidationExpressionVersion() {
		return getIntAttribute(VALIDATIONEXPRESSIONVERSION);
	}

	public Date getValidationExpressionDate() {
		return getDateAttribute(VALIDATIONEXPRESSIONDATE);
	}

	public int getValidationLookupVersion() {
		return getIntAttribute(VALIDATIONLOOKUPVERSION);
	}

	public Date getValidationLookupDate() {
		return getDateAttribute(VALIDATIONLOOKUPDATE);
	}

	public int getValidationExternalVersion() {
		return getIntAttribute(VALIDATIONEXTERNALVERSION);
	}

	public Date getValidationExternalDate() {
		return getDateAttribute(VALIDATIONEXTERNALDATE);
	}

	public MValidationExpression getMValidationExpression(String validationExpressionID) {
		return (MValidationExpression) getChild(MetadataType.VALIDATION_EXPRESSION, validationExpressionID);
	}

	public MValidationExpression[] getMValidationExpressions() {
		MValidationExpression[] tmpl = new MValidationExpression[0];
		return (MValidationExpression[]) getChildren(MetadataType.VALIDATION_EXPRESSION).toArray(tmpl);
	}

	public MLookup getMLookup(String lookupName) {
		return (MLookup) getChild(MetadataType.LOOKUP, lookupName);
	}

	public MLookup[] getMLookups() {
		MLookup[] tmpl = new MLookup[0];
		return (MLookup[]) getChildren(MetadataType.LOOKUP).toArray(tmpl);
	}

	public MClass getMClass(String className) {
		return (MClass) getChild(MetadataType.CLASS, className);
	}

	public MClass[] getMClasses() {
		MClass[] tmpl = new MClass[0];
		return (MClass[]) getChildren(MetadataType.CLASS).toArray(tmpl);
	}

	public MObject getMObject(String objectType) {
		return (MObject) getChild(MetadataType.OBJECT, objectType);
	}

	public MObject[] getMObjects() {
		MObject[] tmpl = new MObject[0];
		return (MObject[]) getChildren(MetadataType.OBJECT).toArray(tmpl);
	}

	public MValidationExternal getMValidationExternal(String validationExternalName) {
		return (MValidationExternal) getChild(MetadataType.VALIDATION_EXTERNAL, validationExternalName);
	}

	public MValidationExternal[] getMValidationExternal() {
		MValidationExternal[] tmpl = new MValidationExternal[0];
		return (MValidationExternal[]) getChildren(MetadataType.VALIDATION_EXTERNAL).toArray(tmpl);
	}

	public MValidationLookup getMValidationLookup(String validationLookupName) {
		return (MValidationLookup) getChild(MetadataType.VALIDATION_LOOKUP, validationLookupName);
	}

	public MValidationLookup[] getMValidationLookups() {
		MValidationLookup[] tmpl = new MValidationLookup[0];
		return (MValidationLookup[]) getChildren(MetadataType.VALIDATION_LOOKUP).toArray(tmpl);
	}

	public MEditMask getMEditMask(String editMaskID) {
		return (MEditMask) getChild(MetadataType.EDITMASK, editMaskID);
	}

	public MEditMask[] getMEditMasks() {
		MEditMask[] tmpl = new MEditMask[0];
		return (MEditMask[]) getChildren(MetadataType.EDITMASK).toArray(tmpl);
	}

	public MUpdateHelp getMUpdateHelp(String updateHelpID) {
		return (MUpdateHelp) getChild(MetadataType.UPDATE_HELP, updateHelpID);
	}

	public MUpdateHelp[] getMUpdateHelps() {
		MUpdateHelp[] tmpl = new MUpdateHelp[0];
		return (MUpdateHelp[]) getChildren(MetadataType.UPDATE_HELP).toArray(tmpl);
	}

	public MSearchHelp getMSearchHelp(String searchHelpID) {
		return (MSearchHelp) getChild(MetadataType.SEARCH_HELP, searchHelpID);
	}

	public MSearchHelp[] getMSearchHelps() {
		MSearchHelp[] tmpl = new MSearchHelp[0];
		return (MSearchHelp[]) getChildren(MetadataType.SEARCH_HELP).toArray(tmpl);
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
	protected void addAttributesToMap(Map attributeMap) {
		for (MetadataElement element : sAttributes)
		{
			attributeMap.put(element.getName(), element.getType());
		}
	}

}
