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

public class MValidationExpression extends MetaObject {
	private static final String METADATATYPENAME = "ValidationExpression";

	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String VALIDATIONEXPRESSIONID = "ValidationExpressionID";
	public static final String VALIDATIONEXPRESSIONTYPE = "ValidationExpressionType";
	public static final String VALUE = "Value";

    private static final List<MetadataElement> sAttributes =
    	new ArrayList<MetadataElement>()
        {{
    		add(new MetadataElement(METADATAENTRYID, sRETSID, sREQUIRED));
    		add(new MetadataElement(VALIDATIONEXPRESSIONID, sRETSNAME, sREQUIRED));
    		add(new MetadataElement(VALIDATIONEXPRESSIONTYPE, sExpressionType, sREQUIRED));
    		add(new MetadataElement(VALUE, sText512, sREQUIRED));
        }};
        
	public static void addAttributes(String name, AttrType type)
	{
		sAttributes.add(new MetadataElement(name, type));
	}
	
	public MValidationExpression() {
		this(DEFAULT_PARSING);
	}

	public MValidationExpression(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getValidationExpressionID() {
		return getStringAttribute(VALIDATIONEXPRESSIONID);
	}

	public String getValidationExpressionType() {
		return getStringAttribute(VALIDATIONEXPRESSIONTYPE);
	}

	public String getValue() {
		return getStringAttribute(VALUE);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sNoChildren;
	}

	@Override
	protected String getIdAttr() {
		return VALIDATIONEXPRESSIONID;
	}

	@Override
	public final String getMetadataTypeName() {
		return METADATATYPENAME;
	}
	
	@Override
	public final MetadataType getMetadataType() {
		return MetadataType.VALIDATION_EXPRESSION;
	}
	
	@Override
	protected void addAttributesToMap(Map attributeMap) {
		for (MetadataElement element : sAttributes)
		{
			attributeMap.put(element.getName(), element.getType());
		}
	}

}
