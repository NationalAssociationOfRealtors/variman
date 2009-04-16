/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.SystemUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class JdomUtilsTest extends LinesEqualTestCase
{
    public void testMergeDocuments() throws JDOMException, IOException
    {
        SAXBuilder builder = new SAXBuilder();
        String xml1 = "<foo a='b'><aaa><bbb/></aaa></foo>";
        String xml2 = "<bar c='d'><xxx><yyy/></xxx></bar>";
        Document doc1 = builder.build(new StringReader(xml1));
        Document doc2 = builder.build(new StringReader(xml2));
        List docs = new ArrayList();
        docs.add(doc1);
        docs.add(doc2);
        Element root = new Element("root");
        Document merged = JdomUtils.mergeDocuments(docs, root);

        Format xmlFormat = Format.getRawFormat();
        xmlFormat.setIndent("  ");
//        xmlFormat.setNewlines(true);
        xmlFormat.setOmitDeclaration(true);
        xmlFormat.setLineSeparator(NL);
        
        XMLOutputter outputter = new XMLOutputter(xmlFormat);
        String mergedString = outputter.outputString(merged);
        assertLinesEqual(
            "<root>" + NL +
            "  <foo a=\"b\">" + NL +
            "    <aaa>" + NL +
            "      <bbb />" + NL +
            "    </aaa>" + NL +
            "  </foo>" + NL +
            "  <bar c=\"d\">" + NL +
            "    <xxx>" + NL +
            "      <yyy />" + NL +
            "    </xxx>" + NL +
            "  </bar>" + NL +
            "</root>" + NL + NL,
            mergedString);
    }
}
