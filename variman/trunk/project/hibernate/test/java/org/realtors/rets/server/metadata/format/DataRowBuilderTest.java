/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

public class DataRowBuilderTest extends TestCase
{
    public void testAppending()
    {
        StringWriter formatted = new StringWriter();
        DataRowBuilder row = new DataRowBuilder(new PrintWriter(formatted));
        // Append a string
        row.append("a&b<c>d\"e");
        row.append((String) null);

        // Append an object
        row.append(new StringBuffer("f&g<h>i\"j"));
        row.append((Object) null);

        // Append an int
        row.append(5);

        // Append a collection
        List list = new ArrayList();
        list.add("n\"");
        list.add("o");
        list.add("l<");
        list.add("k&");
        list.add("m>");
        row.append(list);
        row.append((Collection) null);

        assertEquals(
            "a&amp;b&lt;c&gt;d&quot;e\t\t" +
            "f&amp;g&lt;h&gt;i&quot;j\t\t" +
            "5\t" +
            "k&amp;,l&lt;,m&gt;,n&quot;,o\t\t",
            formatted.toString());
    }
}
