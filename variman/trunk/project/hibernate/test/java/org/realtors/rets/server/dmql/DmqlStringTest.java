/*
 */
package org.realtors.rets.server.dmql;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class DmqlStringTest extends TestCase
{

    public void testConstantString()
    {
        DmqlString string = new DmqlString("foo");
        assertFalse(string.containsWildcards());

        List components = new ArrayList();
        components.add(new ConstantStringComponent("foo"));
        assertEquals(components, string.getComponents());
        assertEquals("'foo'", TestUtil.toSql(string));

        string = new DmqlString();
        string.add("bar");
        assertFalse(string.containsWildcards());

        components = new ArrayList();
        components.add(new ConstantStringComponent("bar"));
        assertEquals(components, string.getComponents());
        assertEquals("'bar'", TestUtil.toSql(string));
    }

    public void testStartsWith()
    {
        DmqlString string = new DmqlString();
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        string.add("foo");
        assertTrue(string.containsWildcards());
        assertEquals("'%foo'", TestUtil.toSql(string));
    }

    public void testContains()
    {
        DmqlString string = new DmqlString();
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        string.add("foo");
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        assertTrue(string.containsWildcards());
        assertEquals("'%foo%'", TestUtil.toSql(string));
    }

    public void testEndsWith()
    {
        DmqlString string = new DmqlString();
        string.add("foo");
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        assertTrue(string.containsWildcards());
        assertEquals("'foo%'", TestUtil.toSql(string));
    }

    public void testSingle()
    {
        DmqlString string = new DmqlString();
        string.add("f");
        string.add(DmqlString.MATCH_ZERO_OR_ONE);
        string.add("o");
        assertTrue(string.containsWildcards());
        assertEquals("'f_o'", TestUtil.toSql(string));
    }

    public void testComplexPattern()
    {
        DmqlString string = new DmqlString();
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        string.add("foo");
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        string.add("b");
        string.add(DmqlString.MATCH_ZERO_OR_ONE);
        string.add("r");
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        assertTrue(string.containsWildcards());
        assertEquals("'%foo%b_r%'", TestUtil.toSql(string));
    }
}
