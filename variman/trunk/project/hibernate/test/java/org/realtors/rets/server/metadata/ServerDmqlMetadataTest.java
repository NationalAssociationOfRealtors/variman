/*
 */
package org.realtors.rets.server.metadata;

import java.util.HashSet;

import junit.framework.TestCase;

public class ServerDmqlMetadataTest extends TestCase
{
    public void testSystemNames()
    {
        ServerDmqlMetadata metadata =
            new ServerDmqlMetadata(mClazz, ServerDmqlMetadata.SYSTEM);

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
    }

    public void testStandardNames()
    {
        ServerDmqlMetadata metadata =
            new ServerDmqlMetadata(mClazz, ServerDmqlMetadata.STANDARD);

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
    }

    protected void setUp()
    {
        if (mClazz != null)
        {
            return;
        }

        int id = 1;
        Resource resource = ObjectMother.createResource();
        resource.setLookups(new HashSet());
        resource.setClasses(new HashSet());

        Lookup area = new Lookup(id++);
        area.setLookupName("AR");
        area.setLookupTypes(new HashSet());
        resource.addLookup(area);

        LookupType lookupType = new LookupType(id++);
        lookupType.setValue("GENVA");
        area.addLookupType(lookupType);

        lookupType = new LookupType(id++);
        lookupType.setValue("BATV");
        area.addLookupType(lookupType);

        Lookup status = new Lookup(id++);
        status.setLookupName("LISTING_STATUS");
        status.setLookupTypes(new HashSet());
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
        mClazz.setTables(new HashSet());
        resource.addClass(mClazz);

        Table table = new Table(id++);
        table.setSystemName("AR");
        table.setStandardName(new TableStandardName("Area"));
        table.setLookup(area);
        mClazz.addTable(table);

        table = new Table(id++);
        table.setSystemName("STATUS");
        table.setStandardName(new TableStandardName("ListingStatus"));
        table.setLookup(status);
        mClazz.addTable(table);

        // Create a table w/o a lookup
        table = new Table(id++);
        table.setSystemName("OWNER");
        table.setStandardName(new TableStandardName("Owner"));
        mClazz.addTable(table);
    }

    private MClass mClazz = null;
}
