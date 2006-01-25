/*
 */
package org.realtors.rets.server.dmql;

import junit.framework.TestCase;

public class DmqlStringListTest extends TestCase
{
    public void testStringList()
    {
        DmqlStringList stringList =
            new DmqlStringList("TaxOwnersName", new DmqlString("Mickey Mouse"));
        stringList.setSqlColumn("owner");

        DmqlString string = new DmqlString();
        string.add("Minnie " );
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        stringList.add(string);

        string = new DmqlString();
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        string.add(" Duck");
        stringList.add(string);

        assertEquals("owner = 'Mickey Mouse' " +
                     "OR owner LIKE 'Minnie %' " +
                     "OR owner LIKE '% Duck'",
                     TestUtil.toSql(stringList));
    }

    public void testEquals()
    {
        DmqlStringList list1 = new DmqlStringList("field");
        list1.add(new DmqlString("foo"));

        DmqlStringList list2 = new DmqlStringList("field");
        list2.add(new DmqlString("foo"));

        assertEquals(list1, list2);

        list1.setSqlColumn("f");
        assertFalse(list1.equals(list2));

        list1.setSqlColumn("field");
        list1.add(new DmqlString("bar"));
        assertFalse(list1.equals(list2));
    }
}
