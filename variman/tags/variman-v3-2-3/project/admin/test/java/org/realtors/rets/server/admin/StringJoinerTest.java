package org.realtors.rets.server.admin;

import junit.framework.TestCase;

public class StringJoinerTest extends TestCase
{
    public void testJoin()
    {
        StringJoiner joiner = new StringJoiner(":");
        joiner.append("newspaper");
        joiner.append("Property");
        joiner.append("RES");
        assertEquals("newspaper:Property:RES", joiner.toString());
    }
}
