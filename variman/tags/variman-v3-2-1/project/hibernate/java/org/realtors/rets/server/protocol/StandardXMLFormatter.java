/*
 * Variman RETS Server
 *
 * Author: Mark Klein
 * Copyright (c) 2010, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.protocol;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.common.util.RetsDateTime;
import org.realtors.rets.common.util.TagBuilder;
import org.realtors.rets.server.ReplyCode;
import org.realtors.rets.server.RetsDTD;
import org.realtors.rets.server.RetsDTDElement;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.dmql.DmqlParserMetadata;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.metadata.MetadataUtils;
import org.realtors.rets.server.metadata.UnitEnum;

import com.wutka.dtd.DTDAttribute;
import com.wutka.dtd.DTDEnumeration;

public class StandardXMLFormatter implements SearchResultsFormatter
{
    private RetsDTD mRetsDTD = null;
    private MetadataManager mMetadataManager = null;
    
    public void formatResults(SearchFormatterContext context)
        throws RetsServerException
    {
        try
        {
            if (mRetsDTD == null)
                mRetsDTD = RetsDTD.getRetsDTD(context.getRetsVersion());
            if (mRetsDTD == null)
                throw new RetsReplyException(ReplyCode.DTD_UNAVAILABLE.getValue(), "Unable to load DTD");
                        
            if (mMetadataManager == null)
                mMetadataManager = RetsServer.getMetadataManager();
            
            if (mMetadataManager == null)
                throw new RetsReplyException(ReplyCode.MISC_SEARCH_ERROR.getValue(), "Unable to load MetadataManager");
            
            String pathName = "REData";
            PrintWriter out = context.getWriter();
            TagBuilder reData = new TagBuilder(out, pathName)
                .beginContentOnNewLine();
            String resourceName = context.getResourceStandardName();
            resourceName = RESOURCES_MAP.get(resourceName);
            if (resourceName == null || resourceName.length() == 0)
                resourceName = context.getResourceStandardName();
            
            pathName += ":" + resourceName;
            RetsDTDElement element = mRetsDTD.getDTDElement(pathName);
            TagBuilder reResource = null;
            if (element != null)
                reResource = new TagBuilder(out, element.getName())
                                            .beginContentOnNewLine();
            else
                reResource = reData;
            
            // Locate the element for the RETS class.
            String className = context.getClassStandardName();
            pathName += ":" + className;
            element = mRetsDTD.getDTDElement(pathName);
            while (context.hasNext())
            {
                formatRow(context, element, reResource);
            }
            if (reResource != reData)
                reResource.close();
            reData.close();
        }
        catch (SQLException e)
        {
            throw new RetsServerException(e);
        }
    }

    private boolean formatRow(SearchFormatterContext context, RetsDTDElement root, TagBuilder parentTag)
        throws SQLException
    {
        boolean result = false;
        
        DmqlParserMetadata metadata = context.getMetadata();
        String standardName = mMetadataManager.findStandardNameByPath(root.getPath());
        String systemName = mMetadataManager.findSystemdNameByPath(root.getPath());
        String elementName = root.getName();
        if (standardName == null)
            standardName = elementName;
        
        /*
         * If this is a container Element, recurse.
         */
        if (!root.getChildren().isEmpty())
        {
            /*
             * Temporarily stage the data in case it is empty after all.
             */
            StringWriter sw = new StringWriter();
            PrintWriter newOut = new PrintWriter (sw);
            
            TagBuilder levelTag = new TagBuilder (newOut, elementName)
                                        .beginContentOnNewLine();

            for (RetsDTDElement child : root.getChildren())
            {
                if (formatRow(context, child, levelTag) == true)
                    result = true;
            }
            levelTag.close();
            if (result)
                parentTag.getWriter().write(sw.toString());
        }
        else
        {
            /*
             * This is a data Element.
             */
            boolean addedAttribute = false;
            String datum;
            if (context.isStandardNames())
                datum = context.getResultString(standardName);
            else
                datum = context.getResultString(systemName);

            /*
             * Only add the tag if we have non-empty data.
             */
            if (datum != null && datum.length() > 0 && parentTag != null)
            {
                MTable table = metadata.getTable(standardName);

                if (table != null && table.getDataType().equals("DateTime"))
                {
                    /*
                     * Another uglyism. The data coming back is in SQL date. Convert it and render it
                     * as a RETS date.
                     */
                    try
                    {
                        Date date = RetsDateTime.parseSql(datum);
                        String buf = RetsDateTime.render(date, context.getRetsVersion());
                        datum = buf;
                    }
                    catch (Exception e)
                    {
                        /*
                         * Ignore the exception. We'll render the data as a SQL date.
                         */
                    }
                }
     
                if (root.getElement().attributes.size() > 0)
                {
                    /*
                     * Temporarily stage the data in case it is empty after all.
                     */
                    StringWriter sw = new StringWriter();
                    PrintWriter newOut = new PrintWriter (sw);
                    
                    TagBuilder levelTag = new TagBuilder (newOut, elementName);
                
                    /*
                     * Handle the "Type" attribute.
                     */
                    DTDAttribute attribute = root.getElement().getAttribute("Type");
                    if (attribute != null && attribute.getType() instanceof DTDEnumeration && table != null)
                    {
                        DTDEnumeration theEnum = (DTDEnumeration) attribute.getType();
                        boolean isFixed = theEnum.getItemsVec().contains("INTEGER");
                        boolean isFloat = theEnum.getItemsVec().contains("FLOAT");
                        
                        if (isFixed || isFloat)
                        {
                            if (table.getDataType().equals("Decimal"))
                                levelTag.appendAttribute("Type", "FLOAT");
                            else
                                levelTag.appendAttribute("Type", "INTEGER");
                            addedAttribute = true;
                        }
                    }
                    /*
                     * Handle the "Units" attribute.
                     */
                    attribute = root.getElement().getAttribute("Units");
                    if (attribute != null && attribute.getType() instanceof DTDEnumeration)
                    {
                        String units = getUnits(table);
 
                        if (units != null)
                        {
                            levelTag.appendAttribute("Units", units);
                            addedAttribute = true;
                        }
                    }
                    /*
                     * If we added an attribute, it will be in the temporary buffer. Now copy it
                     * to the real tag.
                     */
                    if (addedAttribute)
                    {
                        levelTag.beginContent().print(datum).close();
                        parentTag.getWriter().write(sw.toString());
                    }
                }
                /*
                 * We have data and we did not build the tag with an attribute. So, now add it
                 * and the data to the parent tag.
                 */
                if (!addedAttribute)
                    parentTag.simpleTag(elementName, datum);
                
                result = true;
            }
        }
        return result;
    }

    private String getUnits(MTable table)
    {
        if (table == null)
        {
            return null;
        }
        UnitEnum units = MetadataUtils.getUnitEnum(table);
        return UNITS_MAP.get(units);
    }

    private static final Map<UnitEnum, String> UNITS_MAP;
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
        
        UNITS_MAP = new HashMap<UnitEnum, String>();
        UNITS_MAP.put(UnitEnum.SQFT, "SqFeet");
        UNITS_MAP.put(UnitEnum.SQMETERS, "SqMeters");
        UNITS_MAP.put(UnitEnum.ACRES, "Acres");
        UNITS_MAP.put(UnitEnum.HECTARES, "Hectares");
    }

}
