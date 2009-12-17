package org.realtors.rets.common.metadata.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataElement;
import org.realtors.rets.common.metadata.MetadataType;

public class MForeignKey extends MetaObject {
	private static final String METADATATYPENAME = "ForeignKey";

	public static final String FOREIGNKEYID = "ForeignKeyID";
	public static final String PARENTRESOURCEID = "ParentResourceID";
	public static final String PARENTCLASSID = "ParentClassID";
	public static final String PARENTSYSTEMNAME = "ParentSystemName";
	public static final String CHILDRESOURCEID = "ChildResourceID";
	public static final String CHILDCLASSID = "ChildClassID";
	public static final String CHILDSYSTEMNAME = "ChildSystemName";
	// 1.7
	public static final String CONDITIONALPARENTFIELD = "ConditionalParentField";
	public static final String CONDITIONALPARENTVALUE = "ConditionalParentValue";

	private static final List<MetadataElement> sAttributes =
		new ArrayList<MetadataElement>()
		{{
			add(new MetadataElement(FOREIGNKEYID, sRETSID, sREQUIRED));
			add(new MetadataElement(PARENTRESOURCEID, sRETSID, sREQUIRED));
			add(new MetadataElement(PARENTCLASSID, sRETSID, sREQUIRED));
			add(new MetadataElement(PARENTSYSTEMNAME, sRETSNAME, sREQUIRED));
			add(new MetadataElement(CHILDRESOURCEID, sRETSID));
			add(new MetadataElement(CHILDCLASSID, sRETSID));
			add(new MetadataElement(CHILDSYSTEMNAME, sRETSNAME));
			
			// 1.7
			add(new MetadataElement(CONDITIONALPARENTFIELD, sRETSNAME, RetsVersion.RETS_1_7));
			add(new MetadataElement(CONDITIONALPARENTVALUE, sRETSNAME, RetsVersion.RETS_1_7));
		}};

	private MTable childTable;
	private MTable parentTable;

	public MForeignKey() {
		this(DEFAULT_PARSING);
	}

	public MForeignKey(boolean strictParsing) {
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
		for (MetadataElement element : MForeignKey.sAttributes)
		{
			if (element.getName().equals(name)) {
				return element.isRequired();
			}
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
		if (sAttributes == null) {
			return;
		}
		
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

	public String getForeignKeyID() {
		return getStringAttribute(FOREIGNKEYID);
	}

	public void setForeignKeyID(String foreignKeyId) {
		String foreignKeyIdStr = sRETSID.render(foreignKeyId);
		setAttribute(FOREIGNKEYID, foreignKeyIdStr);
	}

	public MTable getParentTable() {
		return parentTable;
	}

	public void setParentTable(MTable parentTable) {
		this.parentTable = parentTable;
		boolean nullAll = true;
		if (this.parentTable != null) {
			MClass clazz = this.parentTable.getMClass();
			if (clazz != null) {
				MResource resource = clazz.getMResource();
				if (resource != null) {
					String resourceId = resource.getResourceID();
					String className = clazz.getClassName();
					String systemName = this.parentTable.getSystemName();
					if (StringUtils.isNotBlank(resourceId) &&
						StringUtils.isNotBlank(className) &&
						StringUtils.isNotBlank(systemName)
					) {
						setParentResourceID(resourceId);
						setParentClassID(className);
						setParentSystemName(systemName);
						nullAll = false;
					}
				}
			}
		}
		if (nullAll) {
			setParentResourceID(null);
			setParentClassID(null);
			setParentSystemName(null);
		}
	}

	public String getParentResourceID() {
		String resourceId = getStringAttribute(PARENTRESOURCEID);
		return resourceId;
	}

	public void setParentResourceID(String parentResourceId) {
		String parentResourceIdStr = sRETSID.render(parentResourceId);
		setAttribute(PARENTRESOURCEID, parentResourceIdStr);
	}

	public String getParentClassID() {
		String classId = getStringAttribute(PARENTCLASSID);
		return classId;
	}

	public void setParentClassID(String parentClassId) {
		String parentClassIdStr = sRETSID.render(parentClassId);
		setAttribute(PARENTCLASSID, parentClassIdStr);
	}

	public String getParentSystemName() {
		String systemName = getStringAttribute(PARENTSYSTEMNAME);
		return systemName;
	}

	public void setParentSystemName(String parentSystemName) {
		String parentSystemNameStr = sRETSID.render(parentSystemName);
		setAttribute(PARENTSYSTEMNAME, parentSystemNameStr);
	}

	public MTable getChildTable() {
		return childTable;
	}

	public void setChildTable(MTable childTable) {
		this.childTable = childTable;
		boolean nullAll = true;
		if (this.childTable != null) {
			MClass clazz = this.childTable.getMClass();
			if (clazz != null) {
				MResource resource = clazz.getMResource();
				if (resource != null) {
					String resourceId = resource.getResourceID();
					String className = clazz.getClassName();
					String systemName = this.childTable.getSystemName();
					if (StringUtils.isNotBlank(resourceId) &&
						StringUtils.isNotBlank(className) &&
						StringUtils.isNotBlank(systemName)
					) {
						setChildResourceID(resourceId);
						setChildClassID(className);
						setChildSystemName(systemName);
						nullAll = false;
					}
				}
			}
		}
		if (nullAll) {
			setChildResourceID(null);
			setChildClassID(null);
			setChildSystemName(null);
		}
	}

	public String getChildResourceID() {
		String childResourceId = getStringAttribute(CHILDRESOURCEID);
		return childResourceId;
	}

	public void setChildResourceID(String childResourceId) {
		String childResourceIdStr = sRETSID.render(childResourceId);
		setAttribute(CHILDRESOURCEID, childResourceIdStr);
	}

	public String getChildClassID() {
		String classId = getStringAttribute(CHILDCLASSID);
		return classId;
	}

	public void setChildClassID(String childClassId) {
		String childClassIdStr = sRETSID.render(childClassId);
		setAttribute(CHILDCLASSID, childClassIdStr);
	}

	public String getChildSystemName() {
		String systemName = getStringAttribute(CHILDSYSTEMNAME);
		return systemName;
	}

	public void setChildSystemName(String childSystemName) {
		String childSystemNameStr = sRETSNAME.render(childSystemName);
		setAttribute(CHILDSYSTEMNAME, childSystemNameStr);
	}

	public String getConditionalParentField() {
		return getStringAttribute(CONDITIONALPARENTFIELD);
	}

	public void setConditionalParentField(String conditionalParentField) {
		String conditionalParentFieldStr = sRETSNAME.render(conditionalParentField);
		setAttribute(CONDITIONALPARENTFIELD, conditionalParentFieldStr);
	}

	public String getConditionalParentValue() {
		return getStringAttribute(CONDITIONALPARENTVALUE);
	}

	public void setConditionalParentValue(String conditionalParentValue) {
		String conditionalParentValueStr = sRETSNAME.render(conditionalParentValue);
		setAttribute(CONDITIONALPARENTVALUE, conditionalParentValueStr);
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
		return sNO_CHILDREN;
	}

	@Override
	protected String getIdAttr() {
		return FOREIGNKEYID;
	}

	@Override
	public final String getMetadataTypeName() {
		return METADATATYPENAME;
	}

	@Override
	public final MetadataType getMetadataType() {
		return MetadataType.FOREIGN_KEYS;
	}

	@Override
	public String getLevel() {
		return "";
	}
}
