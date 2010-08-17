/*
 * Variman RETS Server
 *
 * Author: Mark Klein
 * Copyright (c) 2010, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Appender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.spi.LoggerRepository;
import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MSystem;
import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.metadata.StandardNameEntry;
import org.realtors.rets.server.metadata.StandardNameManager;

import com.wutka.dtd.DTD;
import com.wutka.dtd.DTDAttribute;
import com.wutka.dtd.DTDCardinal;
import com.wutka.dtd.DTDChoice;
import com.wutka.dtd.DTDElement;
import com.wutka.dtd.DTDItem;
import com.wutka.dtd.DTDMixed;
import com.wutka.dtd.DTDName;
import com.wutka.dtd.DTDPCData;
import com.wutka.dtd.DTDParser;
import com.wutka.dtd.DTDSequence;

/**
 * A <code>RetsDTD</code> loads the DTD and
 * makes it available as a Collection or Map.
 */
public class RetsDTD
{
    private static String                           sBastPath;
    private static Map<RetsVersion, RetsDTD>        sRetsDTDMap = new LinkedHashMap<RetsVersion, RetsDTD>();
    
    private static final Logger LOG = Logger.getLogger(RetsDTD.class);

    private Map<String, RetsDTDElement>     mElementMap;
    private Map<String, RetsDTDElement>     mElementMetadataMap;
    private int                             mLevel = 0;
    private MetadataManager                 mMetadataManager;
    private PrintWriter                     mOut = null;
    private RetsVersion                     mRetsVersion;
    private String                          mRootElementName;
    private MSystem                         mSystem;
    
    private static final Map<String, String> RESOURCES_MAP;

    static
    {
        RESOURCES_MAP = new HashMap<String, String>();
        RESOURCES_MAP.put("ActiveAgent", "REAgents");
        RESOURCES_MAP.put("Agent", "REAgents");
        RESOURCES_MAP.put("History", "REHistories");
        RESOURCES_MAP.put("Offices", "REOffices");
        RESOURCES_MAP.put("OpenHouse","REActivities");
        RESOURCES_MAP.put("Property", "REProperties");
        RESOURCES_MAP.put("Prospect", "REProspects");
        RESOURCES_MAP.put("Tax", "REPublicRecords");
        RESOURCES_MAP.put("Tour", "REActivities");
        RESOURCES_MAP.put("Office", "REOffice");
    }

    private RetsDTD()
    {
        
    }
    
    private RetsDTD(RetsVersion retsVersion) throws RetsServerException
    {
        mElementMap = new LinkedHashMap<String, RetsDTDElement>();
        mElementMetadataMap = new LinkedHashMap<String, RetsDTDElement>();
        mMetadataManager = RetsServer.getMetadataManager();
        mRetsVersion = retsVersion;
        
        if (mRetsVersion == RetsVersion.RETS_1_0)
            loadDTD("REData-20021015.dtd");
        else
        if (mRetsVersion == RetsVersion.RETS_1_5)
            loadDTD("REData-20041001.dtd");
        else
            loadDTD("REData-20080829.dtd");
    }
    /**
     * Add the DTD Element and path to to the standard names map. This is a recursive 
     * procedure. 
     * @param root The <code>RetsDTDElement</code> that is the root for the path.
     * @param rootPath The colon delimited path for this element.
     */
    private void addToStandardNames(RetsDTDElement root, String rootPath)
    {
        String standardName = root.getName();
        
        String path = "";
        if (rootPath != null)
            path = rootPath + ":";

        /*
         * The standardName may be a RETS standard name and not a DTD element name. We 
         * really want to convert RETS standard names to DTD element names.
         */
        StandardNameEntry entry = StandardNameManager.findStandardNameEntry(standardName);
        if (entry != null)
            standardName = entry.getElementName();

        path += standardName;
   
        /*
         * Add standard name and path.
         */
        StandardNameManager.addToStandardNames(standardName, standardName, rootPath);
        StandardNameManager.addPathToStandardName(standardName, path);

        /*
         * If this is a compound element, process all its children.
         */
        for (RetsDTDElement child : root.getChildren())
            addToStandardNames(child, path);
    }
    
    private void dumpElement(RetsDTDElement root) throws Exception
    {
        if (mOut == null || root == null) 
            return;
        
        boolean isContainer = root.getChildren().size() > 0;
        
        mLevel++;
        if (isContainer)
            printIndented ("<container name=\"" + root.getName() + "\">");
        else
        {
            printIndentedNoCR ("<element name=\"" + root.getName() + "\"");
            if (root.getElement().attributes.size() > 0)
            {
                String delim = " ";
                mLevel++;
                for (DTDAttribute attribute : (Collection<DTDAttribute>) root.getElement().attributes.values())
                {
                    mOut.print (delim + attribute.getName() + " = \"\"");
                    delim = ", ";
                }
                mLevel--;
            }
            mOut.println ("/>");
        }
        for (RetsDTDElement child : root.getChildren())
            dumpElement (child);
        if (isContainer) printIndented ("</container>");
        mLevel--;
    }
    
    private void dumpMaps(String dirName)
    {
        try
        {
            mOut = new PrintWriter(new FileWriter(dirName + "elementmetadatadump.txt"));
            dumpElement(mElementMetadataMap.get(mRootElementName));
            mOut.close();
            mOut = new PrintWriter(new FileWriter(dirName + "elementdump.txt"));
            dumpElement(mElementMap.get(mRootElementName));
            mOut.close();
            mOut = new PrintWriter(new FileWriter(dirName + "standardnames.txt"));
            StandardNameManager.dumpStandardNames(mOut);
            mOut.close();
            mOut = null;
        }
        catch (Exception e)
        {
            
        }
    }

    /**
     * Convert the metadata path into Standard Name path.
     * @param pathName A String containing the <code>resource:class:table</code> metadata path components
     * @return The metadata based Standard Name for the table.
     */
    private String findDTDPath(String pathName)
    {
        String className = null;
        String resourceName = null;
        String standardName = null;
        String [] pathNames = pathName.split(":");
       
        if (pathNames[0] != null)
            resourceName = RESOURCES_MAP.get(pathNames[0]);
        
        if (pathNames.length > 1 && pathNames[1] != null)
        {
            MClass clazz = (MClass) mMetadataManager.findByPath(MetadataType.CLASS.name(), 
                                                        pathNames[0] + ":" + pathNames[1]);
            if (clazz != null)
                className = clazz.getStandardName();
        }
        
        if (pathNames.length > 2 && pathNames[2] != null)
        {
            MTable table = (MTable) mMetadataManager.findByPath(MetadataType.TABLE.name(), pathName);
            if (table != null)
                standardName = table.getStandardName();
        }
        
        if (resourceName != null && className != null && standardName != null)
        {
            String path = resourceName + ":" + className;
            ArrayList<String> standardNameList = StandardNameManager.findStandardPaths(standardName);
            if (standardNameList != null)
            {
                /*
                 * This could be a standard name to element name translation, so check.
                 */
                if (standardNameList.size() == 1)
                {
                    /*
                     * See if the class name is already in the path.
                     */
                    if (standardNameList.get(0).startsWith(className))
                        return resourceName + ":" + standardNameList.get(0);
                    
                    return path + ":" + standardNameList.get(0);
                }
                
                /*
                 * Find shortest match.
                 */
                String shortestMatch = null;
                for (String name : standardNameList)
                {
                    if (name.startsWith(path) && name.endsWith(standardName))
                        if (shortestMatch == null || name.length() < shortestMatch.length())
                            shortestMatch = name;
                }
                return shortestMatch;
            }
        }
        return null;
    }
    
    /**
     * Find the DTD element(s) that match the standardNamePath. This is a recursive procedure. It is used to build up
     * the <code>mElementMetadataMap</code> such that it only contains those elements that appear in the metadata.
     * @param element The RetsDTDElement that corresponds to the first standard name in the <code>standardNamePath</code>.
     * @param metadataElement The RetsDTDElement from the <code>mElementMetadataMap</code>
     * @param standardNamePath The path from the DTD using standard names for the element.
     * @return The element matching the path, or null if none found.
     */
    private RetsDTDElement findElement(RetsDTDElement element, RetsDTDElement metadataElement, 
                                        String standardNamePath, String originalPath)
    {
        int index = standardNamePath.indexOf(":");
        RetsDTDElement result = null;
        String standardName = standardNamePath;
        if (index > 0)
            standardName = standardNamePath.substring(0, index);
        
        /*
         * If this is a container Element, recurse.
         */
        if (!element.getChildren().isEmpty())
        {
            String childPath = standardNamePath.substring(index + 1);
            index = childPath.indexOf(":");
            String childStandardName = childPath;
            if (index > 0)
                childStandardName = childPath.substring(0, index);
            
            if (metadataElement == null)
                metadataElement = element.clone();
            
            RetsDTDElement elementChild = element.findChild(childStandardName);
            if (elementChild != null)   
            {
                RetsDTDElement metadataChild = metadataElement.findChild(childStandardName);

                if (metadataChild == null)
                    metadataChild = elementChild.clone();
                
                result = findElement(elementChild, metadataChild, childPath, originalPath);
                if (result != null)
                {
                    /*
                     * We've found a match somewhere in the recursion. No
                     * need to continue. Make sure that the element is known
                     * to the element/metadata map
                     */
                    if (!metadataElement.getChildren().contains(result))
                        metadataElement.addChild(result);
                    return metadataElement;
                }
            }
        }
        else
        {
            /*
             * This is a data Element.
             */
            if (element.getName().equals(standardName))
            {
                if (!metadataElement.getStandardNames().contains(standardName))
                {
                    metadataElement.addStandardName(standardName);
                    metadataElement.addPath(originalPath);
                }
                return metadataElement;
            }
        }
        return null;
    }
    
    /**
     * Return the <code>RetsDTD</code> for the given <code>RetsVersion</code>.
     * @param retsVersion The <code>RetsVersion</code>.
     * @return The <code>RetsDTD</code>.
     * @throws RetsServerException
     */   
    public static synchronized RetsDTD getRetsDTD(RetsVersion retsVersion) throws RetsServerException
    {
        RetsDTD retsDTD = null;
        
        StandardNameManager.loadStandardNames();
        
        synchronized(sRetsDTDMap)
        {
            retsDTD = sRetsDTDMap.get(retsVersion);
            if (retsDTD == null)
            {
                retsDTD = new RetsDTD(retsVersion);
                sRetsDTDMap.put(retsVersion, retsDTD);
                retsDTD.syncDTDToMetadata();
            }
        }
        return retsDTD;
    }
    
    /**
     * Fetch the <code>RetsDTDElement</code> for the given Element Name. This is
     * from the intersection of the DTD and the metadata.
     * @param standardName A String containing the element name.
     * @return
     */
    public RetsDTDElement getDTDElement(String elementName)
    {
        return mElementMetadataMap.get(elementName);
    }
    
    /**
     * Link up the <code>mElementMap</code> in DTD order. The initial 
     * <code>DTDItem</code> should be the root of the DTD.
     * @param item The <code>DTDItem</code> that is the root for the current level.
     * @param entry The <code>RetsDTDElement</code> for the root of this level.
     */
    private void linkDTDItem(DTDItem item, RetsDTDElement entry)
    {
        linkDTDItem(mElementMap, item, entry);
    }
    
    /**
     * Link up the map in DTD order. This is recursive and the initial <code>DTDItem</code> should
     * be the root of the DTD.
     * @param map A Map containing the nodes to be linked together in DTD order.
     * @param item The <code>DTDItem</code> that is the root for the current level.
     * @param entry The <code>RetsDTDElement</code> for the root of this level.
     */
    private void linkDTDItem(Map<String, RetsDTDElement> map, DTDItem item, RetsDTDElement entry)
    {
        if (item == null) return;

        if (item instanceof DTDName)
        {
            String name = ((DTDName)item).getValue();
            RetsDTDElement child = map.get(name);

            if (child != null)
            {
                entry.addChild(child);

                // See if there are children to this child to link.
                DTDElement elem = child.getElement();
                DTDItem content = elem.getContent();
                linkDTDItem(map, content, child);
            }
        }
        else if (item instanceof DTDSequence)
        {
            DTDItem[] items = ((DTDSequence) item).getItems();

            for (int i=0; i < items.length; i++)
            {
                linkDTDItem(map, items[i], entry);
            }
        }
        else if (item instanceof DTDChoice)
        {
            DTDItem[] items = ((DTDChoice) item).getItems();

            for (int i=0; i < items.length; i++)
            {
                linkDTDItem(map, items[i], entry);
            }
        }
        else if (item instanceof DTDMixed)
        {
            DTDItem[] items = ((DTDMixed) item).getItems();

            for (int i=0; i < items.length; i++)
            {
                linkDTDItem(map, items[i], entry);
            }
        }
        else if (item instanceof DTDPCData)
        {
        }

        if (item.cardinal == DTDCardinal.OPTIONAL)
        {
        }
        else if (item.cardinal == DTDCardinal.ZEROMANY)
        {
        }
        else if (item.cardinal == DTDCardinal.ONEMANY)
        {
        }
    }
    
    /**
     * Load the DTD from the given file.
     * @param filename The filename of the DTD to be loaded.
     * @throws RetsReplyException
     */
    private void loadDTD(String filename) throws RetsReplyException
    {
        final String FS = File.separator;
        
        String configFile = "variman" + FS + "WEB-INF" + FS + "rets" + FS + filename;

        if (sBastPath != null)
            configFile = sBastPath + FS + "WEB-INF" + FS + "rets" + FS + filename;
        
        LOG.debug ("Loading DTD " + configFile);
        
        try
        {
            DTDElement root = null;
            DTDParser parser = new DTDParser(new File(configFile), false);
            
            DTD dtd = parser.parse(true);
            
            Enumeration<DTDElement> e = dtd.elements.elements();
    
            /*
             * Load of the elements into the <code>mElementMap</code>.
             */
            while (e.hasMoreElements())
            {
                DTDElement elem = e.nextElement();
    
                RetsDTDElement entry = new RetsDTDElement(elem);
                // FIXME: Process any attributes if we need to preserve them. Not sure if necessary at this point.
                if (elem.attributes.size() > 0)
                {
                }
                mElementMap.put(elem.name, entry);
                /*
                 * <code>REData</code> is the root of the DTD. Preserve the node when we find it.
                 */
                if (root == null && elem.name.equals("REData"))
                    root = elem;
            }
    
            /*
             * Try linking everything in DTD order, starting with the root element.
             */
            if (dtd.rootElement == null)
                dtd.rootElement = root;
            mRootElementName = dtd.rootElement.name;
            RetsDTDElement rootRetsDTDElement = mElementMap.get(mRootElementName);
    
            linkDTDItem(rootRetsDTDElement.getElement().getContent(), rootRetsDTDElement);
            
            /*
             * Everything in the DTD can be considered a "StandardName", so add them to the
             * StandardNames table. We can ignore the root element.
             */
            for (RetsDTDElement child : rootRetsDTDElement.getChildren())
                addToStandardNames(child, null);
        }
        catch (Exception e)
        {
            LOG.debug(e);
            throw new RetsReplyException(ReplyCode.DTD_UNAVAILABLE.getValue(), 
                                "Unable to load DTD " + filename);
        }
    }


    
    /**
     * Match the metadata to the corresponding DTD element. This routine is recursive.
     * @param metadata The root metadata entry.
     * @param parentPath The path to this metadata entry.
     */
    private void matchMetadataToDTD(MetaObject metadata, String parentPath)
    {
        if (metadata instanceof MTable)
        {
            String resourceName = null;
            String className = null;

            String standardNamePath = findDTDPath(parentPath);
            if (standardNamePath != null)
            {
                String [] pathNames = standardNamePath.split(":");
                RetsDTDElement resourceElement = null;
                RetsDTDElement classElement = null;
                RetsDTDElement tableElement = null;
                RetsDTDElement metadataResourceElement = null;
                RetsDTDElement metadataClassElement = null;
                
                if (pathNames.length >= 2)
                {
                    if (pathNames[0] != null)
                        resourceName = pathNames[0];
                    
                    if (pathNames[1] != null)
                        className = pathNames[1];
                }
                
                if (resourceName != null)
                    resourceElement = mElementMap.get(resourceName);
                
                if (resourceElement != null)
                {
                    /*
                     * Find the class entry.
                     */
                    classElement = resourceElement.findChild(className);
                }
                if (classElement != null)
                {
                    /*
                     * So far so good. Now find the associated metadata elements.
                     * If they don't exist, create them.
                     */
                    metadataResourceElement = mElementMetadataMap.get(resourceName);
                    if (metadataResourceElement == null)
                        metadataResourceElement = resourceElement.clone();

                    metadataClassElement = metadataResourceElement.findChild(className);

                    if (metadataClassElement == null)
                        metadataClassElement = classElement.clone();
                    /*
                     * We're starting with the class element, but the standardNamePath begins with the
                     * resource. Adjust accordingly.
                     */
                    String classNamePath = "";
                    String delim = "";
                    for (int i = 1; i < pathNames.length; i++)
                    {
                        classNamePath = classNamePath + delim + pathNames[i];
                        delim = ":";
                    }
                    
                    /*
                     * Iterate through the class element to see if there is a child path that matches the
                     * standardPath.
                     */
                    tableElement = findElement(classElement, metadataClassElement, classNamePath, parentPath);

                    if (tableElement != null)
                    {
                        if (!mElementMetadataMap.containsKey(resourceName))
                            mElementMetadataMap.put(resourceName, metadataResourceElement);
                        
                        if (!mElementMetadataMap.containsKey(className))
                            mElementMetadataMap.put(className, metadataClassElement);
                        
                        if (!metadataResourceElement.getChildren().contains(metadataClassElement))
                            metadataResourceElement.addChild(metadataClassElement);
                    }
                }
            }
        }
        
        MetadataType [] childMetadataTypes = metadata.getChildTypes();
        for (MetadataType childMetadataType : childMetadataTypes)
        {
            Collection<? extends MetaObject> children = metadata.getChildren(childMetadataType);
            for (MetaObject childMetadata : children)
            {
                String path = childMetadata.getPath();
                matchMetadataToDTD(childMetadata, path);
            }
        }
    }
        
    /**
     * The metadata has changed, so synchronize it to the DTD.
     */
    public static void metadataChanged()
    {
        synchronized (sRetsDTDMap)
        {
            for (RetsDTD retsDTD : sRetsDTDMap.values())
                retsDTD.syncDTDToMetadata();
        }
    }
    
    /**
     * Take a Variman path and convert each member into the corresponding standard name.
     * @param parentPath The path to be converted.
     * @return A string containing the same path expressed in standard names.
     */
    private String normalizePath(String parentPath)
    {
        String className = null;
        String resourceName = null;
        String tableName = null;
        String pathName = parentPath;
        String [] pathNames = parentPath.split(":");
        if (pathNames[0] != null)
            resourceName = RESOURCES_MAP.get(pathNames[0]);
        /*
         * Locate the class entry in the metadata and extract the standard name.
         */
        if (pathNames.length > 1 && pathNames[1] != null)
        {
            String classPathName = pathNames[0] + ":" + pathNames[1];
            MClass clazz = (MClass) mMetadataManager.findByPath(MetadataType.CLASS.name(), classPathName);
            if (clazz != null)
                className = clazz.getStandardName();
        }
        /*
         * Locate the table name in the metadata and extract the standard name.
         */
        if (pathNames.length > 2 && pathNames[2] != null)
        {
            MTable table = (MTable) mMetadataManager.findByPath(MetadataType.TABLE.name(), parentPath);
            if (table != null)
                tableName = table.getStandardName();
        }
        /*
         * If we have all three elements, build the standard name. If we don't, the original
         * path will be returned.
         */
        if (resourceName != null && className != null && tableName != null)
        {
            pathName = resourceName + ":" + className + ":" + tableName;
        }
        return pathName;
    }
    
    private void printIndented(String buf)
    {
        for (int i = 0; i < mLevel * 2; i++) mOut.print(" ");
        mOut.println(buf);
    }
    
    private void printIndentedNoCR(String buf)
    {
        for (int i = 0; i < mLevel * 2; i++) mOut.print(" ");
        mOut.print(buf);
    }
    /**
     * Reorder the child elements within the <code>metadataElement</code> to be in <code>element</code> order.
     * This is a recursive routine that will descend for each child element found in <code>element</code>.
     * @param element The root element from the DTD map for this level.
     * @param metadataElement The root element from the metadata element map for this level.
     * @return
     */
    private RetsDTDElement reorderLevel(RetsDTDElement element, RetsDTDElement metadataElement)
    {
        /*
         * Make sure these are corresponding elements. If not, return.
         */
        if (!element.getName().equals(metadataElement.getName()))
            return null;
        
        /*
         * If there are no children, this is an element instead of container.
         */
        if (element.getChildren().isEmpty())
        {
            return metadataElement.clone();
        }
        
        RetsDTDElement newElement = null;
        
        /*
         * In Element order, see if the corresponding children exist in the metadata. If so,
         * add them back in DTD order.
         */
        for (RetsDTDElement elementChild : element.getChildren())
        {
            String childName = elementChild.getName();
            /*
             * See if the metadata contains this child.
             */
            if (metadataElement.findChild(childName) != null)
            {
                RetsDTDElement grandChild = reorderLevel(elementChild, metadataElement.findChild(childName));
                if (grandChild != null)
                {
                    if (newElement == null)
                        newElement = metadataElement.clone();

                    newElement.addChild(grandChild);
                }
            }
        }
        return newElement;
    }
    
    /**
     * Recursively descend through the children to this element and add each to the given map.
     * @param map The map to which each element and path will be added.
     * @param root The root element for this level.
     * @param path The pathname to the parent of this level. This is <code>null</code> for the root level.
     */
    private void recursivelyAddToMapByPath(Map<String, RetsDTDElement> map, RetsDTDElement root, String path)
    {
        String pathName = "";
        if (path != null)
            pathName = path + ":";
        
        pathName += root.getName();
        
        /*
         * Add this element to the map. 
         */
        if (!map.containsKey(pathName))
            map.put(pathName, root);
        
        for (RetsDTDElement element : root.getChildren())
            recursivelyAddToMapByPath(map, element, pathName);
    }
    /**
     * The metadata is out of order as compared to the DTD. Reorder the map into DTD order.
     */
    private void reorderMetadata()
    {
        Map<String, RetsDTDElement> elementMetadataMap = new LinkedHashMap<String, RetsDTDElement>();
        
        RetsDTDElement rootMetadataElement = mElementMetadataMap.get(mRootElementName);
        RetsDTDElement child = reorderLevel(mElementMap.get(mRootElementName), rootMetadataElement);
        
        if (child != null)
            rootMetadataElement = child;
        
        recursivelyAddToMapByPath(elementMetadataMap, rootMetadataElement, null);

        mElementMetadataMap = elementMetadataMap;
        mElementMetadataMap.put(mRootElementName, rootMetadataElement);
    }
    
    /**
     * Establish the base path relative to where the DTDs will be found.
     * @param basePath A string containing the path.
     */
    public static void setBasePath(String basePath)
    {
        sBastPath = basePath;
        StandardNameManager.setBasePath(basePath);
    }
    
    /**
     * Syncrhonize the DTD element map for metadata to the actual metadata.
     */
    private void syncDTDToMetadata()
    {
        mSystem = mMetadataManager.getSystem();
        mElementMetadataMap.clear();
        RetsDTDElement rootRetsDTDElement = mElementMap.get(mRootElementName).clone();

        matchMetadataToDTD(mSystem, null);
        /*
         * The only thing in the mElementMetadataMap at this point ought to be the children
         * Elements of REData. Match them up and make sure the root node is properly connected.
         */
        for (RetsDTDElement element : mElementMap.get(mRootElementName).getChildren())
        {
            RetsDTDElement child = mElementMetadataMap.get(element.getName());
            if (child != null)
                if (rootRetsDTDElement.findChild(child.getName()) == null)
                    rootRetsDTDElement.addChild(child);
        }
        
        mElementMetadataMap.put(mRootElementName, rootRetsDTDElement);

        /*
         * Nearly done. The element metadata map now has only those DTD elements that appear in
         * metadata. However, the elements may be out of order as declared in the DTD. To be
         * compliant with the DTD, the elements in the map must now be reordered into DTD order.
         */
        reorderMetadata();
        
        if (LOG.isDebugEnabled())
        {
            String dirName = "";
            Enumeration appenders = LOG.getRootLogger().getAllAppenders();
            while (appenders.hasMoreElements() && dirName.length() == 0)
            {
                Appender appender = (Appender) appenders.nextElement();
                if (appender instanceof RollingFileAppender)
                {
                    dirName = ((RollingFileAppender) appender).getFile();
                    if (dirName != null && dirName.length() > 0)
                    {
                        int indx = dirName.lastIndexOf("/");
                        if (indx > 1)
                        {
                            dirName = dirName.substring(0, indx + 1);
                            break;
                        }
                    }
                    else
                        dirName = "";
                }
            }
            dumpMaps(dirName);
        }
    }
}
