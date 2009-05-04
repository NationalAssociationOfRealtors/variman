package org.realtors.rets.server.protocol;

import java.net.URL;
import java.net.MalformedURLException;

import junit.framework.TestCase;

public class ObjectDescriptorTest extends TestCase
{
    public void testLocalLocationUrl() throws MalformedURLException
    {
        ObjectDescriptor descriptor = new ObjectDescriptor(
            "abc123", 1, new URL("file:/tmp/abc123.jpg"));
        descriptor.setRemoteLocationAllowable(true);
        String  actual = descriptor.getLocationUrl(BASE_URL);
        assertEquals("http://example.com/objects/abc123/1",
                     actual);

        descriptor.setRemoteLocationAllowable(false);
        actual = descriptor.getLocationUrl(BASE_URL);
        assertEquals("http://example.com/objects/abc123/1",
                     actual);
    }

    public void testRemoveLocationUrl() throws MalformedURLException
    {
        ObjectDescriptor descriptor = new ObjectDescriptor(
            "abc123", 1, new URL("http://images.example/property/abc123.jpg"));
        descriptor.setRemoteLocationAllowable(true);
        String  actual = descriptor.getLocationUrl(BASE_URL);
        assertEquals("http://images.example/property/abc123.jpg",
                     actual);

        descriptor.setRemoteLocationAllowable(false);
        actual = descriptor.getLocationUrl(BASE_URL);
        assertEquals("http://example.com/objects/abc123/1",
                     actual);
    }

    private static final String BASE_URL = "http://example.com/objects/";
}
