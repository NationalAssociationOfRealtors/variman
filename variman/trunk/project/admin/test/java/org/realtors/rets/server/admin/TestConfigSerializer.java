/*
 * Created on Aug 6, 2003
 *
 */
package org.realtors.rets.server.admin;

import java.io.IOException;
import java.io.StringWriter;

import junit.framework.TestCase;

/**
 * @author kgarner
 */
public class TestConfigSerializer extends TestCase
{
    public void testToXML() throws IOException
    {
        String hostname = "test.host.test";
        int port = 31337;
        int sessionTimeout = 90;
        
        String expectedResult =
            "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
            "<Retzilla-Config>\n" +
            "  <hostname>" + hostname + "</hostname>\n" +
            "  <port>" + Integer.toString(port) + "</port>\n" +
            "  <timeout>" + Integer.toString(sessionTimeout) + "</timeout>\n" +
            "</Retzilla-Config>\n";
        
        ConfigOptions opts = new ConfigOptions();
        opts.setHostname(hostname);
        opts.setPort(port);
        opts.setSessionTimeout(sessionTimeout);
        
        StringWriter sw = new StringWriter();
        ConfigSerializer.toXML(opts, sw);
        
        assertEquals("Testing config serialization", expectedResult,
                     sw.toString());
    }
}
