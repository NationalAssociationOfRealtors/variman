package org.realtors.rets.common.metadata.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	// 1.7.2
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
    	
    		// 1.7.2
    		add(new MetadataElement(CONDITIONALPARENTFIELD, sRETSNAME, RetsVersion.RETS_1_7_2));
    		add(new MetadataElement(CONDITIONALPARENTVALUE, sRETSNAME, RetsVersion.RETS_1_7_2));
        }};
    
	public static void addAttributes(String name, AttrType type)
	{
		sAttributes.add(new MetadataElement(name, type));
	}
    	
	public MForeignKey() {
		this(DEFAULT_PARSING);
	}

	public MForeignKey(boolean strictParsing) {
		super(strictParsing);
	}

	public String getForeignKeyID() {
		return getStringAttribute(FOREIGNKEYID);
	}

	public String getParentResourceID() {
		return getStringAttribute(PARENTRESOURCEID);
	}

	public String getParentClassID() {
		return getStringAttribute(PARENTCLASSID);
	}

	public String getParentSystemName() {
		return getStringAttribute(PARENTSYSTEMNAME);
	}

	public String getChildResourceID() {
		return getStringAttribute(CHILDRESOURCEID);
	}

	public String getChildClassID() {
		return getStringAttribute(CHILDCLASSID);
	}

	public String getChildSystemName() {
		return getStringAttribute(CHILDSYSTEMNAME);
	}
	
	public String getConditionalParentField() {
		return getStringAttribute(CONDITIONALPARENTFIELD);
	}
	
	public String getConditionalParentValue() {
		return getStringAttribute(CONDITIONALPARENTVALUE);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sNoChildren;
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
		return MetadataType.FOREIGNKEYS;
	}

	@Override
	protected void addAttributesToMap(Map attributeMap) {
		for (MetadataElement element : sAttributes)
		{
			attributeMap.put(element.getName(), element.getType());
		}
	}

}
