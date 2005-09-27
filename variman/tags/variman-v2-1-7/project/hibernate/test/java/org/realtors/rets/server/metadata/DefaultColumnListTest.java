package org.realtors.rets.server.metadata;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class DefaultColumnListTest extends TestCase
{
    public void testOrdering()
    {
        DefaultColumnList columnList = new DefaultColumnList();
        columnList.add(2, "zulu");
        columnList.add(5, "alpha");
        columnList.add(2, "bravo");
        columnList.add(3, "charlie");
        columnList.add(1, "delta");
        columnList.add(4, "echo");

        List expected = new ArrayList();
        expected.add("delta");
        expected.add("bravo");
        expected.add("zulu");
        expected.add("charlie");
        expected.add("echo");
        expected.add("alpha");
        assertEquals(expected, columnList.getColumnNames());
    }
}
