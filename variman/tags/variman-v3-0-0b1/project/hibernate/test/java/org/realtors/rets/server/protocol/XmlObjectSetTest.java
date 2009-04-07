package org.realtors.rets.server.protocol;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.IOUtils;

public class XmlObjectSetTest extends TestCase
{
    public void testFindOjbect() throws RetsServerException
    {
        ObjectDescriptor expected = new ObjectDescriptor(
            "abc123", 1, localUrl("abc123-1.jpg"),
            "Beautiful frontal view of home.");
        XmlObjectSet objectSet =
            new XmlObjectSet(getResourceFile(LOCAL_OBJECT_SET));
        assertFalse(objectSet.areRemoteLocationsAllowable());
        ObjectDescriptor actual = objectSet.findObject("Photo", 1);
        assertEquals(expected, actual);
    }

    public void testFindObjectWithoutDescription() throws RetsServerException
    {
        ObjectDescriptor expected = new ObjectDescriptor(
            "abc123", 3, localUrl("abc123-1.gif"));
        XmlObjectSet objectSet =
            new XmlObjectSet(getResourceFile(LOCAL_OBJECT_SET));
        assertFalse(objectSet.areRemoteLocationsAllowable());
        ObjectDescriptor actual = objectSet.findObject("Photo", 3);
        assertEquals(expected, actual);
    }

    public void testFindNonexistantObjectId() throws RetsServerException
    {
        XmlObjectSet objectSet =
            new XmlObjectSet(getResourceFile(LOCAL_OBJECT_SET));
        assertFalse(objectSet.areRemoteLocationsAllowable());
        assertNull(objectSet.findObject("Photo", 4));
    }

    public void testFindDefaultObjects() throws RetsServerException
    {
        ObjectDescriptor expected = new ObjectDescriptor(
            "abc123", 2, localUrl("abc123-2.jpg"));
        XmlObjectSet objectSet =
            new XmlObjectSet(getResourceFile(LOCAL_OBJECT_SET));
        assertFalse(objectSet.areRemoteLocationsAllowable());
        ObjectDescriptor actual = objectSet.findObject("Photo", 0);
        assertEquals(expected, actual);
    }

    public void testFindAllObjects() throws RetsServerException
    {
        List expected = new ArrayList();
        expected.add(new ObjectDescriptor(
            "abc123", 1, localUrl("abc123-1.jpg"),
            "Beautiful frontal view of home."));
        expected.add(new ObjectDescriptor(
            "abc123", 2, localUrl("abc123-2.jpg")));
        expected.add(new ObjectDescriptor(
            "abc123", 3, localUrl("abc123-1.gif")));

        XmlObjectSet objectSet =
            new XmlObjectSet(getResourceFile(LOCAL_OBJECT_SET));
        assertFalse(objectSet.areRemoteLocationsAllowable());
        List actual = objectSet.findAllObjects("Photo");
        assertEquals(expected, actual);
    }

    public void testRelativeGroupPath() throws RetsServerException
    {
        ObjectDescriptor expected = new ObjectDescriptor(
            "abc123", 3, localUrl("../dirTest/a/bar.html"),
            "Disclosure statement from current owners.");

        XmlObjectSet objectSet =
            new XmlObjectSet(getResourceFile(LOCAL_OBJECT_SET));
        assertFalse(objectSet.areRemoteLocationsAllowable());
        ObjectDescriptor actual = objectSet.findObject("Documents", 0);
        assertEquals(expected, actual);
    }

    public void testRemoteGroupPath()
        throws RetsServerException, MalformedURLException
    {
        ObjectDescriptor expected = new ObjectDescriptor(
            "abc123", 1, new URL("http://example.com/video/abc123-tour.mov"),
            "Take a virtual tour of this house.");

        XmlObjectSet objectSet =
            new XmlObjectSet(getResourceFile(LOCAL_OBJECT_SET));
        assertFalse(objectSet.areRemoteLocationsAllowable());
        ObjectDescriptor actual = objectSet.findObject("Video", 0);
        assertEquals(expected, actual);
    }

    public void testNonExistantType() throws RetsServerException
    {
        XmlObjectSet objectSet =
            new XmlObjectSet(getResourceFile(LOCAL_OBJECT_SET));
        assertFalse(objectSet.areRemoteLocationsAllowable());

        assertNull(objectSet.findObject("Unknown", 0));

        List actual = objectSet.findAllObjects("Unknown");
        assertEquals(Collections.EMPTY_LIST, actual);
    }

    public void testRemoteBaseUrl()
        throws RetsServerException, MalformedURLException
    {
        ObjectDescriptor expected = new ObjectDescriptor(
            "abc123", 1, new URL("http://example.com/objects/abc123-1.jpg"),
            "Beautiful frontal view of home.");
        expected.setRemoteLocationAllowable(true);

        XmlObjectSet objectSet =
            new XmlObjectSet(getResourceFile(REMOTE_OBJECT_SET));
        assertTrue(objectSet.areRemoteLocationsAllowable());
        ObjectDescriptor actual = objectSet.findObject("Photo", 1);
        assertEquals(expected, actual);
    }

    private URL localUrl(String resourceName)
    {
        return getClass().getResource(resourceName);
    }

    private File getResourceFile(String resourceName)
    {
        return IOUtils.urlToFile(getClass().getResource(resourceName));
    }

    private static final String LOCAL_OBJECT_SET = "abc123-local.xml";
    private static final String REMOTE_OBJECT_SET = "abc123-remote.xml";
}
