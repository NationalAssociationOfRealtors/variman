/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.Map;
import java.util.HashMap;

public class EscapeUtils
{
    public static String escapeXml(String unescaped)
    {
        int length = unescaped.length();
        StringBuffer escaped = new StringBuffer(length * 5/4);
        for (int i = 0; i < length; i++)
        {
            char c = unescaped.charAt(i);
            String entity = (String) sEntityLookup.get(new Character(c));
            if (entity != null)
            {
                escaped.append(entity);
            }
            else
            {
                escaped.append(c);
            }
        }

        return escaped.toString();
    }

    static Map sEntityLookup;

    static
    {
        sEntityLookup = new HashMap();
        sEntityLookup.put(new Character('&'), "&amp;");
        sEntityLookup.put(new Character('<'), "&lt;");
        sEntityLookup.put(new Character('>'), "&gt;");
        sEntityLookup.put(new Character('"'), "&quot;");
    }
}
