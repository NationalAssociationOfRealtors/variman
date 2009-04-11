/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.realtors.rets.common.util.TagBuilder;
import org.realtors.rets.server.LinesEqualTestCase;

public class TagBuilderTest extends LinesEqualTestCase
{
    public void testPrinting()
    {
        StringWriter formatted = new StringWriter();
        TagBuilder tag = new TagBuilder(new PrintWriter(formatted), "test");
        tag.beginContentOnNewLine();

        // Print a string
        tag.print("a&b<c>d\"e");
        tag.print((String) null);
        tag.print("\n");

        // Print an object
        tag.print(new StringBuffer("f&g<h>i\"j"));
        tag.print((Object) null);
        tag.print("\n");

        // Print an int
        tag.print(5);
        tag.print("\n");

        // Print a collection
        List list = new ArrayList();
        list.add("n\"");
        list.add("o");
        list.add("l<");
        list.add("k&");
        list.add("m>");
        tag.print(list);
        tag.print((Collection) null);
        tag.print("\n");
        tag.close();

        assertLinesEqual(
            "<test>\n" +
            "a&amp;b&lt;c&gt;d&quot;e\n" +
            "f&amp;g&lt;h&gt;i&quot;j\n" +
            "5\n" +
            "k&amp;,l&lt;,m&gt;,n&quot;,o\n" +
            "</test>\n",
            formatted.toString());
    }
}
