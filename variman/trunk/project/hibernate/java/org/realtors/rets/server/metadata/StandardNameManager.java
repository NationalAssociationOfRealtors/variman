/*
 * Variman RETS Server
 *
 * Author: Mark Klein
 * Copyright (c) 2010, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.metadata;

import java.io.FileReader;
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
import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.metadata.StandardNameEntry;

public class StandardNameManager
{
    private static final Logger LOG = Logger.getLogger(StandardNameManager.class);

    private static String                           sBasePath;

    private static Map<String, StandardNameEntry>   sStandardNameMap = new LinkedHashMap<String, StandardNameEntry>();
    private static Map<String, ArrayList<String>>   sStandardNameToPathMap = new LinkedHashMap<String, ArrayList<String>>();
    
    /**
     * Add a path to the Standard Name to Path translation map.
     * @param standardName A String containing the Standard Name
     * @param path A String containing the DTD path.
     */
    public static void addPathToStandardName(String standardName, String path)
    {
        ArrayList<String> standardNameList = sStandardNameToPathMap.get(standardName);
        if (standardNameList == null)
        {
            standardNameList = new ArrayList<String>();
            sStandardNameToPathMap.put(standardName, standardNameList);
        }
        if (!standardNameList.contains(path))
            standardNameList.add(path);
    }
    
    /**
     * Create and add a Standard Name Entry if it doesn't already exist.
     * @param standardName A String containing the Standard Name.
     * @param elementName A String containing the DTD Element Name for this Standard Name.
     * @param rootPath A String containing the DTD path for this Standard Name.
     */
    public static void addToStandardNames(String standardName, String elementName, String rootPath)
    {
        if (!sStandardNameMap.containsKey(standardName))
            sStandardNameMap.put(standardName, new StandardNameEntry(standardName, elementName, rootPath));
    }
    
    /**
     * Dump the Standard Names map as an XML document.
     * @param out The PrintWriter upon which to write the document.
     * @throws Exception
     */
    public static void dumpStandardNames(PrintWriter out) throws Exception
    {
        if (out == null) 
            return;
        
        out.println("<standardnames>");
        
        for (StandardNameEntry entry : sStandardNameMap.values())
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
    }
    
    /**
     * Find the Standard Paths associated with a Standard Name.
     * @param standardName A String containing the Standard Name.
     * @return An <code>ArrayList</code> of Strings containing the paths, or <code>null</code> if not found
     */
    public static ArrayList<String> findStandardPaths(String standardName)
    {
        return sStandardNameToPathMap.get(standardName);
    }
    
    /**
     * Find the <code>StandardNameEntry</code> for the given Standard Name.
     * @param standardName A String containing the Standard Name.
     * @return A <code>StandardNameEntry</code> if it exists, <code>null</code> otherwise.
     */
    public static StandardNameEntry findStandardNameEntry(String standardName)
    {
        return sStandardNameMap.get(standardName);
    }
        
    /**
     * Load the standard names into the map.
     * @throws RetsReplyException
     */
    public static void loadStandardNames() throws RetsReplyException
    {
        try
        {
            StandardNameDao standardNameDao = RetsServer.getStandardNameDao();
            standardNameDao.setBasePath(sBasePath);

            sStandardNameMap = standardNameDao.getStandardNameMap();
            sStandardNameToPathMap = standardNameDao.getStandardNamePathMap();
        }
        catch (Exception e)
        {
            LOG.debug(e);
            throw new RetsReplyException(ReplyCode.DTD_UNAVAILABLE.getValue(), 
                                "Unable to load Standard Names");
        }
    }
    
    /**
     * Establish the base path relative to where the DTDs will be found.
     * @param basePath A string containing the path.
     */
    public static void setBasePath(String basePath)
    {
        sBasePath = basePath;
    }
}
