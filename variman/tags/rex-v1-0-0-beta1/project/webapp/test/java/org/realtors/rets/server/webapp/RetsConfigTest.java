/*
 */
package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.beans.IntrospectionException;

import org.realtors.rets.server.LinesEqualTestCase;

import org.xml.sax.SAXException;

public class RetsConfigTest extends LinesEqualTestCase
{
    public void testToXml()
        throws IOException, SAXException, IntrospectionException
    {
        RetsConfig retsConfig = new RetsConfig();
        retsConfig.setGetObjectRoot("/tmp/pictures");
        retsConfig.setGetObjectPattern("%k-%i.jpg");
        retsConfig.setNonceInitialTimeout(5);
        retsConfig.setNonceSuccessTimeout(10);
        String xml = retsConfig.toXml();
        assertLinesEqual(
            "<?xml version='1.0' ?>\n" +
            "<rets-config>\n" +
            "  <get-object-pattern>%k-%i.jpg</get-object-pattern>\n" +
            "  <get-object-root>/tmp/pictures</get-object-root>\n" +
            "  <nonce-initial-timeout>5</nonce-initial-timeout>\n" +
            "  <nonce-success-timeout>10</nonce-success-timeout>\n" +
            "</rets-config>",
            xml
        );
    }

    public void testFromXml()
        throws SAXException, IOException, IntrospectionException
    {
        String xml =
            "<?xml version='1.0' ?>\n" +
            "<rets-config>\n" +
            "  <get-object-pattern>%k-%i.jpg</get-object-pattern>\n" +
            "  <get-object-root>/tmp/pictures</get-object-root>\n" +
            "  <nonce-initial-timeout>5</nonce-initial-timeout>\n" +
            "  <nonce-success-timeout>10</nonce-success-timeout>\n" +
            "</rets-config>";
        RetsConfig retsConfig = RetsConfig.initFromXml(xml);
        assertEquals("%k-%i.jpg", retsConfig.getGetObjectPattern());
        assertEquals("/tmp/pictures", retsConfig.getGetObjectRoot());
        assertEquals(5, retsConfig.getNonceInitialTimeout());
        assertEquals(10, retsConfig.getNonceSuccessTimeout());
    }

    public void testFromXmlDefaults()
        throws SAXException, IOException, IntrospectionException
    {
        String xml =
            "<?xml version='1.0' ?>\n" +
            "<rets-config>\n" +
            "</rets-config>";
        RetsConfig retsConfig = RetsConfig.initFromXml(xml);
        assertNull(retsConfig.getGetObjectPattern());
        assertNull(retsConfig.getGetObjectRoot());
        assertEquals(-1, retsConfig.getNonceInitialTimeout());
        assertEquals(-1, retsConfig.getNonceSuccessTimeout());
    }

    public void testGetDefaults()
    {
        RetsConfig retsConfig = new RetsConfig();
        assertEquals("/", retsConfig.getGetObjectRoot("/"));
        assertEquals("%i.jpg", retsConfig.getGetObjectPattern("%i.jpg"));
        assertEquals(2, retsConfig.getNonceInitialTimeout(2));
        assertEquals(2, retsConfig.getNonceSuccessTimeout(2));

        retsConfig.setGetObjectRoot("/tmp/pictures");
        retsConfig.setGetObjectPattern("%k-%i.jpg");
        retsConfig.setNonceInitialTimeout(5);
        retsConfig.setNonceSuccessTimeout(10);

        assertEquals("/tmp/pictures", retsConfig.getGetObjectRoot("/"));
        assertEquals("%k-%i.jpg", retsConfig.getGetObjectPattern("%i.jpg"));
        assertEquals(5, retsConfig.getNonceInitialTimeout(2));
        assertEquals(10, retsConfig.getNonceSuccessTimeout(2));
    }
}
