/*
 */
package org.realtors.rets.server.metadata;

import junit.framework.TestCase;

public class ServerDmqlMetadataTest extends TestCase
{
    public void testSystemNames()
    {
        ServerDmqlMetadata metadata =
            new ServerDmqlMetadata(mClazz, ServerDmqlMetadata.SYSTEM);

        // Check field names
        assertEquals("r_AR", metadata.fieldToColumn("AR"));
        assertEquals("r_STATUS", metadata.fieldToColumn("STATUS"));
        assertEquals("r_OWNER", metadata.fieldToColumn("OWNER"));
        assertEquals("AR", metadata.columnToField("r_AR"));
        assertEquals("STATUS", metadata.columnToField("r_STATUS"));
        assertEquals("OWNER", metadata.columnToField("r_OWNER"));
        assertNull(metadata.fieldToColumn("Area"));
        assertNull(metadata.fieldToColumn("ListingStatus"));
        assertNull(metadata.fieldToColumn("Owner"));
        assertNull(metadata.fieldToColumn("FOO"));

        // Check lookups
        assertEquals("GENVA", metadata.getLookupDbValue("AR", "GENVA"));
        assertEquals("BATV", metadata.getLookupDbValue("AR", "BATV"));
        assertNull(metadata.getLookupDbValue("AR", "STC"));
        assertNull(metadata.getLookupDbValue("Area", "GENVA"));
        assertTrue(metadata.isLookupField("AR"));
        assertTrue(metadata.isValidLookupValue("AR", "GENVA"));
        assertTrue(metadata.isValidLookupValue("AR", "BATV"));
        assertFalse(metadata.isValidLookupValue("AR", "STC"));
        assertFalse(metadata.isLookupField("Area"));

        // -----

        // Check field names
        assertTrue(metadata.isValidFieldName("AR"));
        assertTrue(metadata.isValidFieldName("STATUS"));
        assertTrue(metadata.isValidFieldName("OWNER"));
        assertTrue(metadata.isValidFieldName("LP"));
        assertFalse(metadata.isValidFieldName("Area"));
        assertFalse(metadata.isValidFieldName("ListingStatus"));
        assertFalse(metadata.isValidFieldName("Owner"));
        assertFalse(metadata.isValidFieldName("ListingPrice"));
        assertFalse(metadata.isValidFieldName("FOO"));

        // Check lookups
        assertTrue(metadata.isLookupField("AR"));
        assertTrue(metadata.isValidLookupValue("AR", "GENVA"));
        assertTrue(metadata.isValidLookupValue("AR", "BATV"));
        assertFalse(metadata.isValidLookupValue("AR", "STC"));
        assertFalse(metadata.isLookupField("Area"));

        assertTrue(metadata.isLookupField("STATUS"));
        assertTrue(metadata.isValidLookupValue("STATUS", "S"));
        assertTrue(metadata.isValidLookupValue("STATUS", "A"));
        assertTrue(metadata.isValidLookupValue("STATUS", "P"));
        assertFalse(metadata.isValidLookupValue("STATUS", "Z"));
        assertFalse(metadata.isLookupField("ListingStatus"));

        assertFalse(metadata.isLookupField("OWNER"));
        assertFalse(metadata.isLookupField("Owner"));
        assertFalse(metadata.isLookupField("FOO"));

        // Check strings
        assertTrue(metadata.isCharacterField("OWNER"));
        assertFalse(metadata.isCharacterField("AR"));
        assertFalse(metadata.isCharacterField("STATUS"));
        assertFalse(metadata.isCharacterField("LP"));
        assertFalse(metadata.isCharacterField("FOO"));
        assertFalse(metadata.isCharacterField("Owner"));

        // Check numerics
        assertFalse(metadata.isNumericField("OWNER"));
        assertFalse(metadata.isNumericField("AR"));
        assertFalse(metadata.isNumericField("STATUS"));
        assertTrue(metadata.isNumericField("LP"));
        assertFalse(metadata.isNumericField("FOO"));
        assertFalse(metadata.isCharacterField("ListingPrice"));

        // Check tables
        assertEquals(mOwner, metadata.getTable("OWNER"));
        assertEquals(mArea, metadata.getTable("AR"));
        assertEquals(mStatus, metadata.getTable("STATUS"));
    }

    public void testStandardNames()
    {
        ServerDmqlMetadata metadata =
            new ServerDmqlMetadata(mClazz, ServerDmqlMetadata.STANDARD);

        // Check field names
        assertEquals("r_AR", metadata.fieldToColumn("Area"));
        assertEquals("r_STATUS", metadata.fieldToColumn("ListingStatus"));
        assertEquals("r_OWNER", metadata.fieldToColumn("Owner"));
        assertNull(metadata.fieldToColumn("AR"));
        assertNull(metadata.fieldToColumn("STATUS"));
        assertNull(metadata.fieldToColumn("OWNER"));
        assertNull(metadata.fieldToColumn("FOO"));

        // Check lookups
        assertEquals("GENVA", metadata.getLookupDbValue("Area", "GENVA"));
        assertEquals("BATV", metadata.getLookupDbValue("Area", "BATV"));
        assertNull(metadata.getLookupDbValue("Area", "STC"));
        assertNull(metadata.getLookupDbValue("AR", "GENVA"));

        // Check listing status uses lookup values from DTD
        assertEquals("P", metadata.getLookupDbValue("ListingStatus",
                                                    "Pending"));
        assertEquals("A", metadata.getLookupDbValue("ListingStatus",
                                                    "Active"));
        assertNull(metadata.getLookupDbValue("ListingStatus", "Z"));
        assertNull(metadata.getLookupDbValue("ListingStatus", "A"));
        assertNull(metadata.getLookupDbValue("ListingStatus", "P"));
        assertNull(metadata.getLookupDbValue("STATUS", "Active"));

        // -----

        // Check field names
        assertTrue(metadata.isValidFieldName("Area"));
        assertTrue(metadata.isValidFieldName("ListingStatus"));
        assertTrue(metadata.isValidFieldName("Owner"));
        assertTrue(metadata.isValidFieldName("ListingPrice"));
        assertFalse(metadata.isValidFieldName("AR"));
        assertFalse(metadata.isValidFieldName("STATUS"));
        assertFalse(metadata.isValidFieldName("OWNER"));
        assertFalse(metadata.isValidFieldName("FOO"));

        // Check lookups
        assertTrue(metadata.isLookupField("Area"));
        assertTrue(metadata.isValidLookupValue("Area", "GENVA"));
        assertTrue(metadata.isValidLookupValue("Area", "BATV"));
        assertFalse(metadata.isValidLookupValue("Area", "STC"));
        assertFalse(metadata.isLookupField("AR"));

        // Check listing status uses lookup values from DTD
        assertTrue(metadata.isLookupField("ListingStatus"));
        assertTrue(metadata.isValidLookupValue("ListingStatus", "Pending"));
        assertTrue(metadata.isValidLookupValue("ListingStatus", "Active"));
        assertFalse(metadata.isValidLookupValue("ListingStatus", "Z"));
        assertFalse(metadata.isValidLookupValue("ListingStatus", "A"));
        assertFalse(metadata.isValidLookupValue("ListingStatus", "P"));
        assertFalse(metadata.isLookupField("STATUS"));

        assertFalse(metadata.isLookupField("OWNER"));
        assertFalse(metadata.isLookupField("Owner"));
        assertFalse(metadata.isLookupField("Foo"));

        // Check strings
        assertTrue(metadata.isCharacterField("Owner"));
        assertFalse(metadata.isCharacterField("Area"));
        assertFalse(metadata.isCharacterField("ListingStatus"));
        assertFalse(metadata.isCharacterField("FOO"));
        assertFalse(metadata.isCharacterField("OWNER"));

        // Check numerics
        assertFalse(metadata.isNumericField("Owner"));
        assertFalse(metadata.isNumericField("Area"));
        assertTrue(metadata.isNumericField("ListingPrice"));
        assertFalse(metadata.isNumericField("FOO"));
        assertFalse(metadata.isNumericField("LP"));

        // Check tables
        assertEquals(mOwner, metadata.getTable("Owner"));
        assertEquals(mArea, metadata.getTable("Area"));
        assertEquals(mStatus, metadata.getTable("ListingStatus"));
    }

    protected void setUp()
    {
        if (mClazz != null)
        {
            return;
        }

        int id = 1;
        Resource resource = ObjectMother.createResource();

        Lookup area = new Lookup(id++);
        area.setLookupName("AR");
        resource.addLookup(area);

        LookupType lookupType = new LookupType(id++);
        lookupType.setValue("GENVA");
        area.addLookupType(lookupType);

        lookupType = new LookupType(id++);
        lookupType.setValue("BATV");
        area.addLookupType(lookupType);

        Lookup status = new Lookup(id++);
        status.setLookupName("LISTING_STATUS");
        resource.addLookup(status);

        lookupType = new LookupType(id++);
        lookupType.setValue("S");
        status.addLookupType(lookupType);

        lookupType = new LookupType(id++);
        lookupType.setValue("A");
        status.addLookupType(lookupType);

        lookupType = new LookupType(id++);
        lookupType.setValue("P");
        status.addLookupType(lookupType);

        mClazz = new MClass(id++);
        mClazz.setClassName("RES");
        mClazz.setDbTable("rets_Property_RES");
        resource.addClass(mClazz);

        mArea = new Table(id++);
        mArea.setSystemName("AR");
        mArea.setStandardName(new TableStandardName("Area"));
        mArea.setLookup(area);
        mArea.setDbName("r_AR");
        mArea.setDataType(DataTypeEnum.CHARACTER);
        mClazz.addTable(mArea);

        mStatus = new Table(id++);
        mStatus.setSystemName("STATUS");
        mStatus.setStandardName(new TableStandardName("ListingStatus"));
        mStatus.setLookup(status);
        mStatus.setDbName("r_STATUS");
        mStatus.setDataType(DataTypeEnum.CHARACTER);
        mClazz.addTable(mStatus);

        // Create a table w/o a lookup
        mOwner = new Table(id++);
        mOwner.setSystemName("OWNER");
        mOwner.setStandardName(new TableStandardName("Owner"));
        mOwner.setDbName("r_OWNER");
        mOwner.setDataType(DataTypeEnum.CHARACTER);
        mClazz.addTable(mOwner);

        mListingPrice = new Table(id++);
        mListingPrice.setSystemName("LP");
        mListingPrice.setStandardName(new TableStandardName("ListingPrice"));
        mListingPrice.setDbName("r_LP");
        mListingPrice.setDataType(DataTypeEnum.DECIMAL);
        mClazz.addTable(mListingPrice);
    }

    private MClass mClazz = null;
    private Table mArea;
    private Table mStatus;
    private Table mOwner;
    private Table mListingPrice;
}
