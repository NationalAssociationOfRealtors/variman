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
        assertFalse(parameters.isMultipartId());
        assertFalse(parameters.getUseLocation());
    }

    public void testComplexId()
    {
        Map parameterMap = parameterMap(new String[] {
            "Type", "Photo",
            "Resource", "Property",
            "ID", "04000638:*,0400181:1:2"
        });
        GetObjectParameters parameters = new GetObjectParameters(parameterMap);
        assertEquals("Photo", parameters.getType());
        assertEquals("Property", parameters.getResource());
        assertEquals(2, parameters.numberOfResources());
        assertEquals("04000638", parameters.getResourceEntity(0));
        List objectIds = new ArrayList();
        objectIds.add("*");
        assertEquals(objectIds, parameters.getObjectIdList(0));
        assertEquals("0400181", parameters.getResourceEntity(1));
        objectIds.clear();
        objectIds.add("1");
        objectIds.add("2");
        assertEquals(objectIds, parameters.getObjectIdList(1));
        assertTrue(parameters.isMultipartId());
        assertFalse(parameters.getUseLocation());
    }

    public void testMultipartIdForStar()
    {
        Map parameterMap = parameterMap(new String[] {
            "Type", "Photo",
            "Resource", "Property",
            "ID", "abc-123:*"
        });
        GetObjectParameters parameters = new GetObjectParameters(parameterMap);
        assertTrue(parameters.isMultipartId());
    }

    public void testMultipartIdList()
    {
        Map parameterMap = parameterMap(new String[] {
            "Type", "Photo",
            "Resource", "Property",
            "ID", "abc-123:1:2"
        });
        GetObjectParameters parameters = new GetObjectParameters(parameterMap);
        assertTrue(parameters.isMultipartId());
    }

    public void testLocationExplicitlyFalse()
    {
        Map parameterMap = parameterMap(new String[] {
            "Type", "Photo",
            "Resource", "Property",
            "ID", "abc-123:1:2",
            "Location", "0"
        });
        GetObjectParameters parameters = new GetObjectParameters(parameterMap);
        assertFalse(parameters.getUseLocation());
    }

    public void testLocationExplicitlyTrue()
    {
        Map parameterMap = parameterMap(new String[] {
            "Type", "Photo",
            "Resource", "Property",
            "ID", "abc-123:1:2",
            "Location", "1"
        });
        GetObjectParameters parameters = new GetObjectParameters(parameterMap);
        assertTrue(parameters.getUseLocation());
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
