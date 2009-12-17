/*
 */
package org.realtors.rets.server.metadata;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MLookup;
import org.realtors.rets.common.metadata.types.MLookupType;
import org.realtors.rets.common.metadata.types.MResource;
import org.realtors.rets.common.metadata.types.MTable;
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
        assertEquals("r_IF", metadata.fieldToColumn("IF"));
        assertEquals("r_HIDDEN", metadata.fieldToColumn("HIDDEN"));
        assertEquals("AR", metadata.columnToField("r_AR"));
        assertEquals("STATUS", metadata.columnToField("r_STATUS"));
        assertEquals("OWNER", metadata.columnToField("r_OWNER"));
        assertEquals("IF", metadata.columnToField("r_IF"));
        assertEquals("HIDDEN", metadata.columnToField("r_HIDDEN"));
        assertNull(metadata.fieldToColumn("Area"));
        assertNull(metadata.fieldToColumn("ListingStatus"));
        assertNull(metadata.fieldToColumn("Owner"));
        assertNull(metadata.fieldToColumn("FOO"));

        // Check default ordering of fields is alphabetical
        List expectedColumns = new ArrayList();
        expectedColumns.add("r_AR");
        expectedColumns.add("r_HIDDEN");
        expectedColumns.add("r_IF");
        expectedColumns.add("r_LD");
        expectedColumns.add("r_LDT");
        expectedColumns.add("r_LP");
        expectedColumns.add("r_LT");
        expectedColumns.add("r_OWNER");
        expectedColumns.add("r_STATUS");
        assertEquals(expectedColumns, metadata.getAllColumns());

        // -----

        // Check field names
        assertTrue(metadata.isValidFieldName("AR"));
        assertTrue(metadata.isValidFieldName("STATUS"));
        assertTrue(metadata.isValidFieldName("OWNER"));
        assertTrue(metadata.isValidFieldName("LP"));
        assertTrue(metadata.isValidFieldName("IF"));
        assertTrue(metadata.isValidFieldName("HIDDEN"));
        assertFalse(metadata.isValidFieldName("Area"));
        assertFalse(metadata.isValidFieldName("ListingStatus"));
        assertFalse(metadata.isValidFieldName("Owner"));
        assertFalse(metadata.isValidFieldName("ListingPrice"));
        assertFalse(metadata.isValidFieldName("FOO"));
        assertFalse(metadata.isValidFieldName("r_HIDDEN"));

        // Check types
        assertEquals(DmqlFieldType.CHARACTER, metadata.getFieldType("OWNER"));
        assertEquals(DmqlFieldType.LOOKUP, metadata.getFieldType("AR"));
        assertEquals(DmqlFieldType.LOOKUP, metadata.getFieldType("STATUS"));
        assertEquals(DmqlFieldType.LOOKUP_MULTI, metadata.getFieldType("IF"));
        assertEquals(DmqlFieldType.NUMERIC, metadata.getFieldType("LP"));
        assertEquals(DmqlFieldType.TEMPORAL, metadata.getFieldType("LD"));
        assertEquals(DmqlFieldType.TEMPORAL, metadata.getFieldType("LDT"));
        assertEquals(DmqlFieldType.TEMPORAL, metadata.getFieldType("LT"));
        assertEquals(DmqlFieldType.CHARACTER, metadata.getFieldType("HIDDEN"));
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
        assertTrue(metadata.isValidLookupValue("STATUS", ".ANY."));
        assertFalse(metadata.isValidLookupValue("STATUS", "Z"));
        assertNull(metadata.getFieldType("ListingStatus"));

        assertTrue(metadata.isValidLookupValue("IF", "FP"));
        assertTrue(metadata.isValidLookupValue("IF", "HEAT"));
        assertTrue(metadata.isValidLookupValue("IF", "COOL"));

        // Check tables
        assertEquals(mOwner, metadata.getTable("OWNER"));
        assertEquals(mArea, metadata.getTable("AR"));
        assertEquals(mStatus, metadata.getTable("STATUS"));
        assertEquals(mListingPrice, metadata.getTable("LP"));
        assertEquals(mListDate, metadata.getTable("LD"));
        assertEquals(mListTime, metadata.getTable("LT"));
        assertEquals(mListDateTime, metadata.getTable("LDT"));
        assertEquals(mHiddenTable, metadata.getTable("HIDDEN"));
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
        expectedColumns.add("r_AR");
        expectedColumns.add("r_LD");
        expectedColumns.add("r_LP");
        expectedColumns.add("r_OWNER");
        expectedColumns.add("r_STATUS");
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
        assertFalse(metadata.isValidFieldName("IF"));

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
        assertTrue(metadata.isValidLookupValue("Area", ".ANY."));
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
        assertNull( metadata.getLookupLongValue("Area", "STC"));
        assertNull(metadata.getLookupDbValue("AR", "GENVA"));
        assertNull(metadata.getLookupShortValue("AR", "GENVA"));
        assertNull(metadata.getLookupLongValue("AR", "GENVA"));

        // Check listing status uses lookup values from DTD
        assertTrue(metadata.isValidLookupValue("ListingStatus", "Pending"));
        assertTrue(metadata.isValidLookupValue("ListingStatus", "Active"));
        assertTrue(metadata.isValidLookupValue("ListingStatus", ".ANY."));
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
        MResource resource = ObjectMother.createResource();

        MLookup area = new MLookup();
        area.setUniqueId(Long.valueOf(id++));
        area.setLookupName("AR");
        resource.addChild(MetadataType.LOOKUP, area);

        MLookupType lookupType = new MLookupType();
        lookupType.setUniqueId(Long.valueOf(id++));
        lookupType.setValue("GENVA");
        lookupType.setShortValue("Geneva");
        lookupType.setLongValue("Geneva, Illinois, USA");
        area.addChild(MetadataType.LOOKUP_TYPE, lookupType);

        lookupType = new MLookupType();
        lookupType.setUniqueId(Long.valueOf(id++));
        lookupType.setValue("BATV");
        lookupType.setShortValue("Batavia");
        lookupType.setLongValue("Batavia, Illinois, USA");
        area.addChild(MetadataType.LOOKUP_TYPE, lookupType);

        MLookup status = new MLookup();
        status.setUniqueId(Long.valueOf(id++));
        status.setLookupName("LISTING_STATUS");
        resource.addChild(MetadataType.LOOKUP, status);

        lookupType = new MLookupType();
        lookupType.setUniqueId(Long.valueOf(id++));
        lookupType.setValue("S");
        status.addChild(MetadataType.LOOKUP_TYPE, lookupType);

        lookupType = new MLookupType();
        lookupType.setUniqueId(Long.valueOf(id++));
        lookupType.setValue("A");
        status.addChild(MetadataType.LOOKUP_TYPE, lookupType);

        lookupType = new MLookupType();
        lookupType.setUniqueId(Long.valueOf(id++));
        lookupType.setValue("P");
        status.addChild(MetadataType.LOOKUP_TYPE, lookupType);

        MLookup interiorFeatures = new MLookup();
        interiorFeatures.setUniqueId(Long.valueOf(id++));
        interiorFeatures.setLookupName("INT_FEATURES");
        resource.addChild(MetadataType.LOOKUP, interiorFeatures);

        lookupType = new MLookupType();
        lookupType.setUniqueId(Long.valueOf(id++));
        lookupType.setValue("FP");
        interiorFeatures.addChild(MetadataType.LOOKUP_TYPE, lookupType);

        lookupType = new MLookupType();
        lookupType.setUniqueId(Long.valueOf(id++));
        lookupType.setValue("HEAT");
        interiorFeatures.addChild(MetadataType.LOOKUP_TYPE, lookupType);

        lookupType = new MLookupType();
        lookupType.setUniqueId(Long.valueOf(id++));
        lookupType.setValue("COOL");
        interiorFeatures.addChild(MetadataType.LOOKUP_TYPE, lookupType);

        mClazz = new MClass();
        mClazz.setUniqueId(Long.valueOf(id++));
        mClazz.setClassName("RES");
        mClazz.setXDBName("rets_Property_RES");
        resource.addChild(MetadataType.CLASS, mClazz);

        mArea = new MTable();
        mArea.setUniqueId(Long.valueOf(id++));
        mArea.setSystemName("AR");
        mArea.setStandardName("Area");
        mArea.setMLookup(area);
        mArea.setDBName("r_AR");
        mArea.setDataType(DataTypeEnum.CHARACTER.toString());
        mArea.setInterpretation(InterpretationEnum.LOOKUP.toString());
        // Don't set default...
        mClazz.addChild(MetadataType.TABLE, mArea);

        mStatus = new MTable();
        mStatus.setUniqueId(Long.valueOf(id++));
        mStatus.setSystemName("STATUS");
        mStatus.setStandardName("ListingStatus");
        mStatus.setMLookup(status);
        mStatus.setDBName("r_STATUS");
        mStatus.setDataType(DataTypeEnum.CHARACTER.toString());
        mStatus.setInterpretation(InterpretationEnum.LOOKUP.toString());
        mStatus.setDefault(3);
        mClazz.addChild(MetadataType.TABLE, mStatus);

        mInteriorFeatures = new MTable();
        mInteriorFeatures.setUniqueId(Long.valueOf(id++));
        mInteriorFeatures.setSystemName("IF");
        mInteriorFeatures.setDBName("r_IF");
        mInteriorFeatures.setMLookup(interiorFeatures);
        mInteriorFeatures.setDataType(DataTypeEnum.CHARACTER.toString());
        mInteriorFeatures.setInterpretation(InterpretationEnum.LOOKUPMULTI.toString());
        mClazz.addChild(MetadataType.TABLE, mInteriorFeatures);

        // Create a table w/o a lookup
        mOwner = new MTable();
        mOwner.setUniqueId(Long.valueOf(id++));
        mOwner.setSystemName("OWNER");
        mOwner.setStandardName("Owner");
        mOwner.setDBName("r_OWNER");
        mOwner.setDataType(DataTypeEnum.CHARACTER.toString());
        mOwner.setDefault(4);
        mClazz.addChild(MetadataType.TABLE, mOwner);

        mListingPrice = new MTable();
        mListingPrice.setUniqueId(Long.valueOf(id++));
        mListingPrice.setSystemName("LP");
        mListingPrice.setStandardName("ListingPrice");
        mListingPrice.setDBName("r_LP");
        mListingPrice.setDataType(DataTypeEnum.DECIMAL.toString());
        mListingPrice.setDefault(1);
        mClazz.addChild(MetadataType.TABLE, mListingPrice);

        mListDate = new MTable();
        mListDate.setUniqueId(Long.valueOf(id++));
        mListDate.setSystemName("LD");
        mListDate.setStandardName("ListDate");
        mListDate.setDBName("r_LD");
        mListDate.setDataType(DataTypeEnum.DATE.toString());
        mListDate.setDefault(2);
        mClazz.addChild(MetadataType.TABLE, mListDate);

        mListTime = new MTable();
        mListTime.setUniqueId(Long.valueOf(id++));
        mListTime.setSystemName("LT");
        mListTime.setDBName("r_LT");
        mListTime.setDataType(DataTypeEnum.TIME.toString());
        mListTime.setDefault(5);
        mClazz.addChild(MetadataType.TABLE, mListTime);

        mListDateTime = new MTable();
        mListDateTime.setUniqueId(Long.valueOf(id++));
        mListDateTime.setSystemName("LDT");
        mListDateTime.setDBName("r_LDT");
        mListDateTime.setDataType(DataTypeEnum.DATETIME.toString());
        mListDateTime.setDefault(0);
        mClazz.addChild(MetadataType.TABLE, mListDateTime);

        mHiddenTable = new MTable();
        mHiddenTable.setUniqueId(Long.valueOf(id++));
        mHiddenTable.setSystemName("HIDDEN");
        mHiddenTable.setDBName("r_HIDDEN");
        mHiddenTable.setDataType(DataTypeEnum.CHARACTER.toString());
        mHiddenTable.setDefault(-1);
        mClazz.addChild(MetadataType.TABLE, mHiddenTable);

        mNullDbNameTable = new MTable();
        mNullDbNameTable.setUniqueId(Long.valueOf(id++));
        mNullDbNameTable.setSystemName("NULL_DBNAME");
        mNullDbNameTable.setDataType(DataTypeEnum.CHARACTER.toString());
        mClazz.addChild(MetadataType.TABLE, mNullDbNameTable);
    }

    private MClass mClazz = null;
    private MTable mArea;
    private MTable mStatus;
    private MTable mOwner;
    private MTable mListingPrice;
    private MTable mListDate;
    private MTable mListTime;
    private MTable mListDateTime;
    private MTable mHiddenTable;
    private MTable mInteriorFeatures;
    private MTable mNullDbNameTable;
}
