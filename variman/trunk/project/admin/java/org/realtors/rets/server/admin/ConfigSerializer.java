/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 * Created on Aug 6, 2003
 *
 */
package org.realtors.rets.server.admin;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * @author kgarner
 */
public class ConfigSerializer
{
    public static void toXML(ConfigOptions opts, Writer writer)
        throws IOException
    {
        Element rootElement = new Element("Retzilla-Config");
        Document doc = new Document(rootElement, null);

        List list = new ArrayList();

        Element element = new Element("hostname");
        element.setText(opts.getHostname());
        list.add(element);

        element = new Element("port");
        element.setText(Integer.toString(opts.getPort()));
        list.add(element);

        element = new Element("timeout");
        element.setText(Integer.toString(opts.getSessionTimeout()));
        list.add(element);

        rootElement.setContent(list);
        
        Format xmlFormat = Format.getPrettyFormat();
        xmlFormat.setEncoding("ISO-8859-1");

        XMLOutputter out = new XMLOutputter(xmlFormat);
        out.output(doc, writer);
    }

    public static ConfigOptions fromXML(Reader reader)
        throws JDOMException, IOException
    {
        ConfigOptions opts = new ConfigOptions();
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(reader);
        Element root = doc.getRootElement();

        Element child = root.getChild("hostname");
        opts.setHostname(child.getText());

        child = root.getChild("port");
        opts.setPort(Integer.parseInt(child.getText()));

        child = root.getChild("timeout");
        opts.setSessionTimeout(Integer.parseInt(child.getText()));

        return opts;
    }
}
