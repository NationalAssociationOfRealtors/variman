/*
 */
package org.realtors.rets.server.protocol;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class GetObjectParametersTest extends TestCase
{
    public void testRequiredArguments()
    {
        Map parameterMap = parameterMap(new String[] {
            "Type", "Photo",
            "Resource", "Property",
            "ID", "abc-123:1"
        });
        GetObjectParameters parameters = new GetObjectParameters(parameterMap);
        assertEquals("Photo", parameters.getType());
        assertEquals("Property", parameters.getResource());
        assertEquals(1, parameters.numberOfResources());
        assertEquals("abc-123", parameters.getResourceEntity(0));
        List objectIds = new ArrayList();
        objectIds.add("1");
        assertEquals(objectIds, parameters.getObjectIdList(0));
    }

    private Map parameterMap(String[] strings)
    {
        HashMap parameters = new HashMap();
        for (int i = 0; i < strings.length; i+=2)
        {
            String name = strings[i];
            String[] value = new String[] {strings[i+1]};
            parameters.put(name, value);
        }
        return parameters;
    }
}
