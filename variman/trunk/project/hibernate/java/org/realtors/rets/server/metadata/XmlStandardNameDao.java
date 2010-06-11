/*
 * Variman RETS Server
 *
 * Author: Mark Klein
 * Copyright (c) 2010 The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.metadata;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.realtors.rets.server.ReplyCode;
import org.realtors.rets.server.RetsReplyException;

/**
 * The data access interface for StandardNameManager.
 */
public class XmlStandardNameDao implements StandardNameDao
{
    private static final Logger LOG = Logger.getLogger(XmlStandardNameDao.class);

    private static String                           sBasePath;

    private static boolean                          sInitialized = false;
    private static Map<String, StandardNameEntry>   sStandardNameMap = new LinkedHashMap<String, StandardNameEntry>();
    private static Map<String, ArrayList<String>>   sStandardNameToPathMap = new LinkedHashMap<String, ArrayList<String>>();

    /**
     * @return The StandardNameMap.
     */
    public Map<String, StandardNameEntry> getStandardNameMap() throws RetsReplyException
    {
        if (!sInitialized)
            loadStandardNames();
        
        return sStandardNameMap;
    }
    
    /**
     * @return The StandardNamePathMap.
     */
    public Map<String, ArrayList<String>> getStandardNamePathMap() throws RetsReplyException
    {
        if (!sInitialized)
            loadStandardNames();
        
        return sStandardNameToPathMap;
    }

    /**
     * Load the standard names into the map.
     * @throws RetsReplyException
     */
    private void loadStandardNames() throws RetsReplyException
    {
        final String FS = File.separator;
        String configFile = "variman" + FS + "WEB-INF" + FS + "rets" + FS + "standardnames.xml";

        if (sBasePath != null)
            configFile = sBasePath + FS + "WEB-INF" + FS + "rets" + FS + "standardnames.xml";
        
        LOG.debug ("Loading StandardNames " + configFile);
        
        try
        {
            SAXBuilder saxBuilder = new SAXBuilder();

            FileReader reader = new FileReader(configFile);
            Document document= saxBuilder.build(reader);
            Element rootElement = document.getRootElement();
            
            Iterator<Element> children = rootElement.getChildren().iterator();
            while (children.hasNext())
            {
                Element element = children.next();

                String standardName = element.getTextTrim();
                String elementName  = element.getAttributeValue("element");
                String parentName   = element.getAttributeValue("container");
                
                if (elementName == null)
                    elementName = standardName;
                
                LOG.debug("standardName: " + standardName + ", elementName: " + elementName + ", parentName: " + parentName);
                StandardNameEntry entry = new StandardNameEntry(standardName, elementName, parentName);
                
                sStandardNameMap.put(standardName, entry);
                ArrayList<String> standardNameList = sStandardNameToPathMap.get(standardName);
                if (standardNameList == null)
                {
                    standardNameList = new ArrayList<String>();
                    sStandardNameToPathMap.put(standardName, standardNameList);
                }
                String pathName = "";
                if (parentName != null && parentName.length() > 0)
                    pathName = parentName + ":";
                
                pathName += elementName;

                if (!standardNameList.contains(pathName))
                    standardNameList.add(pathName);
            }
            sInitialized = true;
        }
        catch (Exception e)
        {
            LOG.debug(e);
            throw new RetsReplyException(ReplyCode.DTD_UNAVAILABLE.getValue(), 
                                "Unable to load Standard Names");
        }
    }
    
    /**
     * Save the Standard Names.
     */
    public void saveStandardNameMap(Map<String, StandardNameEntry> standardNameMap) throws Exception
    {
        String configFile = "WEB-INF/rets/standardnames.xml";

        if (sBasePath != null)
            configFile = sBasePath + "/" + configFile;
        
        LOG.debug ("Saving StandardNames to " + configFile);
        
        PrintWriter out = new PrintWriter(new FileWriter(configFile));
        
        out.println("<standardnames>");
        
        for (StandardNameEntry entry : standardNameMap.values())
        {
            String elementName  = entry.getElementName();
            String parent       = entry.getParent();
            String standardName = entry.getStandardName();
            
            out.print("    <standardname");
            
            if (!standardName.equals(elementName))
                out.print(" element=\"" + elementName + "\"");
            
            if (parent != null && parent.length() > 0)
                out.print(" container=\"" + parent + "\"");
            
            out.println(">" + entry.getStandardName() + "</standardname>");
        }
        out.println("</standardnames>");
 
        out.close();
    }

    public void setBasePath(String basePath)
    {
        sBasePath = basePath;
    }
}
