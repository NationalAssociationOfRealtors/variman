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
        assertTrue(metadata.isValidLookupName("AR"));
        assertTrue(metadata.isValidLookupValue("AR", "GENVA"));
        assertTrue(metadata.isValidLookupValue("AR", "BATV"));
        assertFalse(metadata.isValidLookupValue("AR", "STC"));
        assertFalse(metadata.isValidLookupName("Area"));

        // -----

        // Check field names
        assertTrue(metadata.isValidFieldName("AR"));
        assertTrue(metadata.isValidFieldName("STATUS"));
        assertTrue(metadata.isValidFieldName("OWNER"));
        assertFalse(metadata.isValidFieldName("Area"));
        assertFalse(metadata.isValidFieldName("ListingStatus"));
        assertFalse(metadata.isValidFieldName("Owner"));
        assertFalse(metadata.isValidFieldName("FOO"));

        // Check lookups
        assertTrue(metadata.isValidLookupName("AR"));
        assertTrue(metadata.isValidLookupValue("AR", "GENVA"));
        assertTrue(metadata.isValidLookupValue("AR", "BATV"));
        assertFalse(metadata.isValidLookupValue("AR", "STC"));
        assertFalse(metadata.isValidLookupName("Area"));

        assertTrue(metadata.isValidLookupName("STATUS"));
        assertTrue(metadata.isValidLookupValue("STATUS", "S"));
        assertTrue(metadata.isValidLookupValue("STATUS", "A"));
        assertTrue(metadata.isValidLookupValue("STATUS", "P"));
        assertFalse(metadata.isValidLookupValue("STATUS", "Z"));
        assertFalse(metadata.isValidLookupName("ListingStatus"));

        assertFalse(metadata.isValidLookupName("OWNER"));
        assertFalse(metadata.isValidLookupName("Owner"));
        assertFalse(metadata.isValidLookupName("FOO"));

        // Check strings
        assertTrue(metadata.isValidStringName("OWNER"));
        assertFalse(metadata.isValidStringName("Owner"));
        assertFalse(metadata.isValidStringName("AR"));
        assertFalse(metadata.isValidStringName("STATUS"));
        assertFalse(metadata.isValidStringName("FOO"));

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
        assertFalse(metadata.isValidFieldName("AR"));
        assertFalse(metadata.isValidFieldName("STATUS"));
        assertFalse(metadata.isValidFieldName("OWNER"));
        assertFalse(metadata.isValidFieldName("FOO"));

        // Check lookups
        assertTrue(metadata.isValidLookupName("Area"));
        assertTrue(metadata.isValidLookupValue("Area", "GENVA"));
        assertTrue(metadata.isValidLookupValue("Area", "BATV"));
        assertFalse(metadata.isValidLookupValue("Area", "STC"));
        assertFalse(metadata.isValidLookupName("AR"));

        // Check listing status uses lookup values from DTD
        assertTrue(metadata.isValidLookupName("ListingStatus"));
        assertTrue(metadata.isValidLookupValue("ListingStatus", "Pending"));
        assertTrue(metadata.isValidLookupValue("ListingStatus", "Active"));
        assertFalse(metadata.isValidLookupValue("ListingStatus", "Z"));
        assertFalse(metadata.isValidLookupValue("ListingStatus", "A"));
        assertFalse(metadata.isValidLookupValue("ListingStatus", "P"));
        assertFalse(metadata.isValidLookupName("STATUS"));

        assertFalse(metadata.isValidLookupName("OWNER"));
        assertFalse(metadata.isValidLookupName("Owner"));
        assertFalse(metadata.isValidLookupName("Foo"));

        // Check strings
        assertTrue(metadata.isValidStringName("Owner"));
        assertFalse(metadata.isValidStringName("OWNER"));
        assertFalse(metadata.isValidStringName("AR"));
        assertFalse(metadata.isValidStringName("STATUS"));
        assertFalse(metadata.isValidStringName("FOO"));

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
        mClazz.addTable(mArea);

        mStatus = new Table(id++);
        mStatus.setSystemName("STATUS");
        mStatus.setStandardName(new TableStandardName("ListingStatus"));
        mStatus.setLookup(status);
        mStatus.setDbName("r_STATUS");
        mClazz.addTable(mStatus);

        // Create a table w/o a lookup
        mOwner = new Table(id++);
        mOwner.setSystemName("OWNER");
        mOwner.setStandardName(new TableStandardName("Owner"));
        mOwner.setDbName("r_OWNER");
        mClazz.addTable(mOwner);
    }

    private MClass mClazz = null;
    private Table mArea;
    private Table mStatus;
    private Table mOwner;
}
