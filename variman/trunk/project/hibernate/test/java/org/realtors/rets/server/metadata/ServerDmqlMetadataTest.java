/*
 */
package org.realtors.rets.server.metadata;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import org.realtors.rets.server.dmql.DmqlFieldType;

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
        assertNull(metadata.fieldToColumn("HIDDEN"));
        assertNull(metadata.fieldToColumn("r_HIDDEN"));

        // Check ordering of all fields
        List expectedColumns = new ArrayList();
        expectedColumns.add("r_LP");
        expectedColumns.add("r_LD");
        expectedColumns.add("r_STATUS");
        expectedColumns.add("r_OWNER");
        expectedColumns.add("r_LT");
        expectedColumns.add("r_AR");
        assertEquals(expectedColumns, metadata.getAllColumns());

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
        assertFalse(metadata.isValidFieldName("HIDDEN"));
        assertFalse(metadata.isValidFieldName("r_HIDDEN"));

        // Check types
        assertEquals(DmqlFieldType.CHARACTER, metadata.getFieldType("OWNER"));
        assertEquals(DmqlFieldType.LOOKUP, metadata.getFieldType("AR"));
        assertEquals(DmqlFieldType.LOOKUP, metadata.getFieldType("STATUS"));
        assertEquals(DmqlFieldType.NUMERIC, metadata.getFieldType("LP"));
        assertEquals(DmqlFieldType.TEMPORAL, metadata.getFieldType("LD"));
        assertEquals(DmqlFieldType.TEMPORAL, metadata.getFieldType("LDT"));
        assertEquals(DmqlFieldType.TEMPORAL, metadata.getFieldType("LT"));
        assertNull(metadata.getFieldType("Owner"));

        // Check lookups
        assertTrue(metadata.isValidLookupValue("AR", "GENVA"));
        assertTrue(metadata.isValidLookupValue("AR", "BATV"));
        assertFalse(metadata.isValidLookupValue("AR", "STC"));
        assertNull(metadata.getFieldType("Area"));

        assertEquals("GENVA", metadata.getLookupDbValue("AR", "GENVA"));
        assertEquals("Geneva", metadata.getLookupShortValue("AR", "GENVA"));
        assertEquals("Geneva, Illinois, USA",
                     metadata.getLookupLongValue("AR", "GENVA"));
        assertEquals("BATV", metadata.getLookupDbValue("AR", "BATV"));
        assertEquals("Batavia", metadata.getLookupShortValue("AR", "BATV"));
        assertEquals("Batavia, Illinois, USA",
                     metadata.getLookupLongValue("AR", "BATV"));
        assertNull(metadata.getLookupDbValue("AR", "STC"));
        assertNull(metadata.getLookupShortValue("AR", "STC"));
        assertNull(metadata.getLookupLongValue("AR", "STC"));
        assertNull(metadata.getLookupDbValue("Area", "GENVA"));
        assertNull(metadata.getLookupShortValue("Area", "GENVA"));
        assertNull(metadata.getLookupLongValue("Area", "GENVA"));

        assertTrue(metadata.isValidLookupValue("STATUS", "S"));
        assertTrue(metadata.isValidLookupValue("STATUS", "A"));
        assertTrue(metadata.isValidLookupValue("STATUS", "P"));
        assertFalse(metadata.isValidLookupValue("STATUS", "Z"));
        assertNull(metadata.getFieldType("ListingStatus"));

        // Check tables
        assertEquals(mOwner, metadata.getTable("OWNER"));
        assertEquals(mArea, metadata.getTable("AR"));
        assertEquals(mStatus, metadata.getTable("STATUS"));
        assertEquals(mListingPrice, metadata.getTable("LP"));
        assertEquals(mListDate, metadata.getTable("LD"));
        assertEquals(mListTime, metadata.getTable("LT"));
        assertEquals(mListDateTime, metadata.getTable("LDT"));
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
        assertNull(metadata.fieldToColumn("HIDDEN"));
        assertNull(metadata.fieldToColumn("r_HIDDEN"));

        // Check ordering of all fields
        List expectedColumns = new ArrayList();
        expectedColumns.add("r_LP");
        expectedColumns.add("r_LD");
        expectedColumns.add("r_STATUS");
        expectedColumns.add("r_OWNER");
        expectedColumns.add("r_AR");
        assertEquals(expectedColumns, metadata.getAllColumns());

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
        assertFalse(metadata.isValidFieldName("HIDDEN"));
        assertFalse(metadata.isValidFieldName("r_HIDDEN"));

        // Check types
        assertEquals(DmqlFieldType.CHARACTER, metadata.getFieldType("Owner"));
        assertEquals(DmqlFieldType.LOOKUP, metadata.getFieldType("Area"));
        assertEquals(DmqlFieldType.LOOKUP,
                     metadata.getFieldType("ListingStatus"));
        assertEquals(DmqlFieldType.NUMERIC,
                     metadata.getFieldType("ListingPrice"));
        assertEquals(DmqlFieldType.TEMPORAL, metadata.getFieldType("ListDate"));
        assertNull(metadata.getFieldType("LDT"));
        assertNull(metadata.getFieldType("LT"));
        assertNull(metadata.getFieldType("OWNER"));

        // Check lookups
        assertTrue(metadata.isValidLookupValue("Area", "GENVA"));
        assertTrue(metadata.isValidLookupValue("Area", "BATV"));
        assertFalse(metadata.isValidLookupValue("Area", "STC"));
        assertNull(metadata.getFieldType("AR"));

        assertEquals("GENVA", metadata.getLookupDbValue("Area", "GENVA"));
        assertEquals("Geneva", metadata.getLookupShortValue("Area", "GENVA"));
        assertEquals("Geneva, Illinois, USA",
                     metadata.getLookupLongValue("Area", "GENVA"));
        assertEquals("BATV", metadata.getLookupDbValue("Area", "BATV"));
        assertEquals("Batavia", metadata.getLookupShortValue("Area", "BATV"));
        assertEquals("Batavia, Illinois, USA",
                     metadata.getLookupLongValue("Area", "BATV"));
        assertNull(metadata.getLookupDbValue("Area", "STC"));
        assertNull(metadata.getLookupShortValue("Area", "STC"));
        assertNull(metadata.getLookupLongValue("Area", "STC"));
        assertNull(metadata.getLookupDbValue("AR", "GENVA"));
        assertNull(metadata.getLookupShortValue("AR", "GENVA"));
        assertNull(metadata.getLookupLongValue("AR", "GENVA"));

        // Check listing status uses lookup values from DTD
        assertTrue(metadata.isValidLookupValue("ListingStatus", "Pending"));
        assertTrue(metadata.isValidLookupValue("ListingStatus", "Active"));
        assertFalse(metadata.isValidLookupValue("ListingStatus", "Z"));
        assertFalse(metadata.isValidLookupValue("ListingStatus", "A"));
        assertFalse(metadata.isValidLookupValue("ListingStatus", "P"));
        assertNull(metadata.getFieldType("STATUS"));

        // Check tables
        assertEquals(mOwner, metadata.getTable("Owner"));
        assertEquals(mArea, metadata.getTable("Area"));
        assertEquals(mStatus, metadata.getTable("ListingStatus"));
        assertEquals(mListDate, metadata.getTable("ListDate"));
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
        lookupType.setShortValue("Geneva");
        lookupType.setLongValue("Geneva, Illinois, USA");
        area.addLookupType(lookupType);

        lookupType = new LookupType(id++);
        lookupType.setValue("BATV");
        lookupType.setShortValue("Batavia");
        lookupType.setLongValue("Batavia, Illinois, USA");
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
        // Don't set default...
        mClazz.addTable(mArea);

        mStatus = new Table(id++);
        mStatus.setSystemName("STATUS");
        mStatus.setStandardName(new TableStandardName("ListingStatus"));
        mStatus.setLookup(status);
        mStatus.setDbName("r_STATUS");
        mStatus.setDataType(DataTypeEnum.CHARACTER);
        mStatus.setDefault(3);
        mClazz.addTable(mStatus);

        // Create a table w/o a lookup
        mOwner = new Table(id++);
        mOwner.setSystemName("OWNER");
        mOwner.setStandardName(new TableStandardName("Owner"));
        mOwner.setDbName("r_OWNER");
        mOwner.setDataType(DataTypeEnum.CHARACTER);
        mOwner.setDefault(4);
        mClazz.addTable(mOwner);

        mListingPrice = new Table(id++);
        mListingPrice.setSystemName("LP");
        mListingPrice.setStandardName(new TableStandardName("ListingPrice"));
        mListingPrice.setDbName("r_LP");
        mListingPrice.setDataType(DataTypeEnum.DECIMAL);
        mListingPrice.setDefault(1);
        mClazz.addTable(mListingPrice);

        mListDate = new Table(id++);
        mListDate.setSystemName("LD");
        mListDate.setStandardName(new TableStandardName("ListDate"));
        mListDate.setDbName("r_LD");
        mListDate.setDataType(DataTypeEnum.DATE);
        mListDate.setDefault(2);
        mClazz.addTable(mListDate);

        mListTime = new Table(id++);
        mListTime.setSystemName("LT");
        mListTime.setDbName("r_LT");
        mListTime.setDataType(DataTypeEnum.TIME);
        mListTime.setDefault(5);
        mClazz.addTable(mListTime);

        mListDateTime = new Table(id++);
        mListDateTime.setSystemName("LDT");
        mListDateTime.setDbName("r_LDT");
        mListDateTime.setDataType(DataTypeEnum.DATETIME);
        mListDateTime.setDefault(0);
        mClazz.addTable(mListDateTime);

        mHiddenTable = new Table(id++);
        mHiddenTable.setSystemName("HIDDEN");
        mHiddenTable.setDbName("r_HIDDEN");
        mHiddenTable.setDataType(DataTypeEnum.CHARACTER);
        mHiddenTable.setDefault(-1);
        mClazz.addTable(mHiddenTable);
    }

    private MClass mClazz = null;
    private Table mArea;
    private Table mStatus;
    private Table mOwner;
    private Table mListingPrice;
    private Table mListDate;
    private Table mListTime;
    private Table mListDateTime;
    private Table mHiddenTable;
}
