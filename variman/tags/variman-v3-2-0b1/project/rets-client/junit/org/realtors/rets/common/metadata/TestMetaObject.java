package org.realtors.rets.common.metadata;

import java.util.Map;

class TestMetaObject extends MetaObject {
	
	public static final String SYSTEM_NAME = "SystemName";
	public static final String STRING1 = "String1";
	private static int sAddAttributeCount;

	public TestMetaObject(boolean strictParsing) {
		super(strictParsing);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sNO_CHILDREN;
	}

	@Override
	protected String getIdAttr() {
		return SYSTEM_NAME;
	}

	@Override
	protected void addAttributesToMap(Map attributeMap) {
		attributeMap.put(SYSTEM_NAME, sAlphanum10);
		attributeMap.put(STRING1, sText);
		sAddAttributeCount++;
	}

	public String getSystemName() {
		return getStringAttribute(SYSTEM_NAME);
	}

	public String getString1() {
		return getStringAttribute(STRING1);
	}

	public static void resetAddAttributeCount() {
		sAddAttributeCount = 0;
	}

	public static int getAddAttributeCount() {
		return sAddAttributeCount;
	}

	public boolean isAttributeRequired(String name) {
		return false;
	}
	
	public final String getMetadataTypeName() {
		return "SYSTEM";
	}
	
	public final MetadataType getMetadataType() {
		return MetadataType.SYSTEM;
	}
}
