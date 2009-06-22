/*
 */
package org.realtors.rets.server.protocol;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.server.RetsServerException;

import junit.framework.TestCase;

public class GetObjectParametersTest extends TestCase
{
    public void testRequiredArguments() throws RetsServerException
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
        assertFalse(parameters.doGetPreferredObject(0));
        assertEquals("abc-123", parameters.getResourceEntity(0));
        List objectIds = new ArrayList();
        objectIds.add("1");
        assertEquals(objectIds, parameters.getObjectIdList(0));
        assertFalse(parameters.isMultipartId());
        assertFalse(parameters.getUseLocation());
    }

    public void testGetPreferredObject() throws RetsServerException
    {
        Map parameterMap = parameterMap(new String[] {
            "Type", "Photo",
            "Resource", "Property",
            "ID", "abc-123" // no object-id-list specified
        });
        GetObjectParameters parameters = new GetObjectParameters(parameterMap);
        assertEquals("Photo", parameters.getType());
        assertEquals("Property", parameters.getResource());
        assertEquals(1, parameters.numberOfResources());
        assertTrue(parameters.doGetPreferredObject(0));
        assertEquals("abc-123", parameters.getResourceEntity(0));
        List objectIds = new ArrayList();
        objectIds.add("0");
        assertEquals(objectIds, parameters.getObjectIdList(0));
        assertFalse(parameters.isMultipartId());
        assertFalse(parameters.getUseLocation());
    }

    public void testGetPreferredObjectUsingZero() throws RetsServerException
    {
        Map parameterMap = parameterMap(new String[] {
            "Type", "Photo",
            "Resource", "Property",
            "ID", "abc-123:0"
        });
        GetObjectParameters parameters = new GetObjectParameters(parameterMap);
        assertEquals("Photo", parameters.getType());
        assertEquals("Property", parameters.getResource());
        assertEquals(1, parameters.numberOfResources());
        assertTrue(parameters.doGetPreferredObject(0));
        assertEquals("abc-123", parameters.getResourceEntity(0));
        List objectIds = new ArrayList();
        objectIds.add("0");
        assertEquals(objectIds, parameters.getObjectIdList(0));
        assertFalse(parameters.isMultipartId());
        assertFalse(parameters.getUseLocation());
    }

    public void testGetPreferredObjectUsingMixedObjectIds() throws RetsServerException
    {
        Map parameterMap = parameterMap(new String[] {
            "Type", "Photo",
            "Resource", "Property",
            "ID", "abc-123:0:1:2:3"
        });
        GetObjectParameters parameters = new GetObjectParameters(parameterMap);
        assertEquals("Photo", parameters.getType());
        assertEquals("Property", parameters.getResource());
        assertEquals(1, parameters.numberOfResources());
        assertTrue(parameters.doGetPreferredObject(0));
        assertEquals("abc-123", parameters.getResourceEntity(0));
        List objectIds = new ArrayList();
        objectIds.add("0");
        objectIds.add("1");
        objectIds.add("2");
        objectIds.add("3");
        assertEquals(objectIds, parameters.getObjectIdList(0));
        assertTrue(parameters.isMultipartId());
        assertFalse(parameters.getUseLocation());

        parameterMap = parameterMap(new String[] {
                "Type", "Photo",
                "Resource", "Property",
                "ID", "abc-123:4:1:0:3,0400181:1:2"
        });
        parameters = new GetObjectParameters(parameterMap);
        assertEquals("Photo", parameters.getType());
        assertEquals("Property", parameters.getResource());
        assertEquals(2, parameters.numberOfResources());
        assertTrue(parameters.doGetPreferredObject(0));
        assertEquals("abc-123", parameters.getResourceEntity(0));
        objectIds.clear();
        objectIds.add("4");
        objectIds.add("1");
        objectIds.add("0");
        objectIds.add("3");
        assertEquals(objectIds, parameters.getObjectIdList(0));
        
        assertFalse(parameters.doGetPreferredObject(1));
        assertEquals("0400181", parameters.getResourceEntity(1));
        objectIds.clear();
        objectIds.add("1");
        objectIds.add("2");
        assertEquals(objectIds, parameters.getObjectIdList(1));
        assertTrue(parameters.isMultipartId());
        assertFalse(parameters.getUseLocation());
    }

    public void testComplexId() throws RetsServerException
    {
        Map parameterMap = parameterMap(new String[] {
            "Type", "Photo",
            "Resource", "Property",
            "ID", "04000638:*,0400181:1:2,04000233:1:0,04000234:1"
        });
        GetObjectParameters parameters = new GetObjectParameters(parameterMap);
        assertEquals("Photo", parameters.getType());
        assertEquals("Property", parameters.getResource());
        assertEquals(4, parameters.numberOfResources());
        
        assertFalse(parameters.doGetPreferredObject(0));
        assertEquals("04000638", parameters.getResourceEntity(0));
        List objectIds = new ArrayList();
        objectIds.add("*");
        assertEquals(objectIds, parameters.getObjectIdList(0));
        
        assertFalse(parameters.doGetPreferredObject(1));
        assertEquals("0400181", parameters.getResourceEntity(1));
        objectIds.clear();
        objectIds.add("1");
        objectIds.add("2");
        assertEquals(objectIds, parameters.getObjectIdList(1));
        
        assertTrue(parameters.doGetPreferredObject(2));
        assertEquals("04000233", parameters.getResourceEntity(2));
        objectIds.clear();
        objectIds.add("1");
        objectIds.add("0");
        assertEquals(objectIds, parameters.getObjectIdList(2));
        
        assertFalse(parameters.doGetPreferredObject(3));
        assertEquals("04000234", parameters.getResourceEntity(3));
        objectIds.clear();
        objectIds.add("1");
        assertEquals(objectIds, parameters.getObjectIdList(3));
        
        assertTrue(parameters.isMultipartId());
        assertFalse(parameters.getUseLocation());
    }

    public void testMultipartIdForStar() throws RetsServerException
    {
        Map parameterMap = parameterMap(new String[] {
            "Type", "Photo",
            "Resource", "Property",
            "ID", "abc-123:*"
        });
        GetObjectParameters parameters = new GetObjectParameters(parameterMap);
        assertTrue(parameters.isMultipartId());
    }

    public void testMultipartIdList() throws RetsServerException
    {
        Map parameterMap = parameterMap(new String[] {
            "Type", "Photo",
            "Resource", "Property",
            "ID", "abc-123:1:2"
        });
        GetObjectParameters parameters = new GetObjectParameters(parameterMap);
        assertTrue(parameters.isMultipartId());
    }

    public void testLocationExplicitlyFalse() throws RetsServerException
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

    public void testLocationExplicitlyTrue() throws RetsServerException
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
