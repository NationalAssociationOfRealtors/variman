/*
 */
package org.realtors.rets.server.metadata.format;

import junit.framework.TestCase;

public class EscapeUtilsTest extends TestCase
{
    public void testEscapeXml()
    {
        String unescaped = "a&b<c>d\"e";
        String escaped = EscapeUtils.escapeXml(unescaped);
        assertEquals("a&amp;b&lt;c&gt;d&quot;e", escaped);
    }
}
