/*
 * Created on Aug 6, 2003
 *
 */
package org.realtors.rets.server.admin;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.jdom.JDOMException;

import junit.framework.TestCase;

/**
 * @author kgarner
 */
public class TestConfigSerializer extends TestCase
{
    public void testFromXML() throws JDOMException, IOException
    {
        StringReader read = new StringReader(sXMLString); 
        ConfigOptions opts = ConfigSerializer.fromXML(read);
        
        assertNotNull("Checking to see if opts was created", opts);
        assertEquals("Checking hostname", sHostname, opts.getHostname());
        assertEquals("Checking port", sPort, opts.getPort());
        assertEquals("Checking timeout", sTimeout, opts.getSessionTimeout());
    }

    public void testToXML() throws IOException
    {
        ConfigOptions opts = new ConfigOptions();
        opts.setHostname(sHostname);
        opts.setPort(sPort);
        opts.setSessionTimeout(sTimeout);
        
        StringWriter sw = new StringWriter();
        ConfigSerializer.toXML(opts, sw);
        
        assertEquals("Testing config serialization", sXMLString,
                     sw.toString());
    }
    
    private static final String sHostname = "test.host.test";
    private static final int sPort = 31337;
    private static final int sTimeout = 90;
    private static final String sXMLString =
        "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" +
        "<Retzilla-Config>\r\n" +
        "  <hostname>" + sHostname + "</hostname>\r\n" +
        "  <port>" + Integer.toString(sPort) + "</port>\r\n" +
        "  <timeout>" + Integer.toString(sTimeout) + "</timeout>\r\n" +
        "</Retzilla-Config>\r\n\r\n";

}
