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

        List components = new ArrayList();
        components.add(new ConstantStringComponent("foo"));
        assertEquals(components, string.getComponents());
        assertEquals(" = 'foo'", TestUtil.toSql(string));

        string = new DmqlString();
        string.add("bar");

        components = new ArrayList();
        components.add(new ConstantStringComponent("bar"));
        assertEquals(components, string.getComponents());
        assertEquals(" = 'bar'", TestUtil.toSql(string));

        string.add("100");
        components.add(new ConstantStringComponent("100"));
        assertEquals(components, string.getComponents());
        assertEquals(" = 'bar100'", TestUtil.toSql(string));
    }

    public void testMutlipleConstantStrings()
    {
        DmqlString string = new DmqlString();
        string.add("foo");
        string.add("100");
        string.add("bar");

        List components = new ArrayList();
        components = new ArrayList();
        components.add(new ConstantStringComponent("foo"));
        components.add(new ConstantStringComponent("100"));
        components.add(new ConstantStringComponent("bar"));
        assertEquals(components, string.getComponents());
        assertEquals(" = 'foo100bar'", TestUtil.toSql(string));

    }

    public void testStartsWith()
    {
        DmqlString string = new DmqlString();
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        string.add("foo");
        assertEquals(" LIKE '%foo'", TestUtil.toSql(string));
    }

    public void testContains()
    {
        DmqlString string = new DmqlString();
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        string.add("foo");
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        assertEquals(" LIKE '%foo%'", TestUtil.toSql(string));
    }

    public void testEndsWith()
    {
        DmqlString string = new DmqlString();
        string.add("foo");
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        assertEquals(" LIKE 'foo%'", TestUtil.toSql(string));
    }

    public void testSingle()
    {
        DmqlString string = new DmqlString();
        string.add("f");
        string.add(DmqlString.MATCH_ZERO_OR_ONE);
        string.add("o");
        assertEquals(" LIKE 'f_o'", TestUtil.toSql(string));
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
        assertEquals(" LIKE '%foo%b_r%'", TestUtil.toSql(string));
    }

    public void testEquals()
    {
        DmqlString string1 = new DmqlString();
        string1.add(DmqlString.MATCH_ZERO_OR_MORE);
        string1.add("foo");
        string1.add(DmqlString.MATCH_ZERO_OR_MORE);
        string1.add("b");
        string1.add(DmqlString.MATCH_ZERO_OR_ONE);
        string1.add("r");
        string1.add(DmqlString.MATCH_ZERO_OR_MORE);

        DmqlString string2 = new DmqlString();
        string2.add(DmqlString.MATCH_ZERO_OR_MORE);
        string2.add("foo");
        string2.add(DmqlString.MATCH_ZERO_OR_MORE);
        string2.add("b");
        string2.add(DmqlString.MATCH_ZERO_OR_ONE);
        string2.add("r");
        string2.add(DmqlString.MATCH_ZERO_OR_MORE);

        assertEquals(string1, string2);
    }
}
