/*
 */
package org.realtors.rets.server.metadata;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;

import junit.framework.TestCase;

public class CompactFormatterTest extends TestCase
{
    public void testFormatSystem()
    {
        MSystem system = new MSystem();
        system.setSystemID("CRT_RETS");
        system.setDescription("Center for REALTOR Technology");
        system.setComments("The reference implementation of a RETS Server");

        CompactFormatter formatter = new CompactFormatter();
        String formatted = formatter.format(system);
        assertEquals(
            "<SYSTEM SystemID=\"CRT_RETS\"" +
            " SystemDescription=\"Center for REALTOR Technology\"/>\n" +
            "<COMMENTS>The reference implementation of a RETS Server" +
            "</COMMENTS>\n",
            formatted);
    }

    public void testFormatForeignKey()
    {
        ForeignKey foreignKey = new ForeignKey();
        foreignKey.setForeignKeyID("Agent_Office");
    }

    public void testFormatResource()
    {
        Resource resource = new Resource();
        resource.setResourceID("PropertyID");
        resource.setStandardName(ResourceStandardNameEnum.PROPERTY);
        resource.setVisibleName("Prop");
        resource.setDescription("Property Database");
        resource.setKeyField("LN");

        HashSet classes = new HashSet();
        classes.add(new MClass(1));
        classes.add(new MClass(2));
        resource.setClasses(classes);

        CompactFormatter formatter = new CompactFormatter();
        String formatted = formatter.format(resource, "1.00.001", getDate());
        assertEquals(
            "<DATA>\tPropertyID\tProperty\tProp\tProperty Database\t" +
            "LN\t2" +
            VERSION_DATE + VERSION_DATE + VERSION_DATE + VERSION_DATE +
            VERSION_DATE + VERSION_DATE + VERSION_DATE + VERSION_DATE +
            VERSION_DATE + "\t</DATA>\n",
            formatted);
    }

    private static final String VERSION_DATE =
        "\t1.00.001\tWed Jan 01 00:01:00 CST 2003";

    private Date getDate()
    {
        return new GregorianCalendar(2003, 0, 01, 0, 1).getTime();
    }
}
