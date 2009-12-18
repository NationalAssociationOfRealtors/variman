/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.realtors.rets.common.util.DataRowBuilder;
import org.realtors.rets.common.util.TagBuilder;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.types.MUpdateType;
import org.realtors.rets.common.metadata.types.MTable;

public class CompactUpdateTypeFormatter extends MetadataFormatter
{
    private Collection filterInaccessible(FormatterContext context,
                                                Collection<MetaObject> updateTypes,
                                                String[] levels)
    {
        String resource = levels[RESOURCE_LEVEL];
        String retsClass = levels[CLASS_LEVEL];
        List<MUpdateType> filteredUpdateTypes = new ArrayList<MUpdateType>();
        for (Iterator<?> iterator = updateTypes.iterator(); iterator.hasNext();)
        {
            MUpdateType updateType = (MUpdateType) iterator.next();
            MTable table = updateType.getMTable();
            if (context.isAccessibleTable(table, resource, retsClass))
            {
                filteredUpdateTypes.add(updateType);
            }
        }
        return filteredUpdateTypes;
    }

    public void format(FormatterContext context, Collection updateTypes,
                       String[] levels)
    {
        updateTypes = filterInaccessible(context, updateTypes, levels);
        if (updateTypes.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(context.getWriter(),
                                        "METADATA-UPDATE_TYPE")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Class", levels[CLASS_LEVEL])
            .appendAttribute("Update", levels[UPDATE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine()
            .appendColumns(COLUMNS);
        for (Iterator<?> iterator = updateTypes.iterator(); iterator.hasNext();)
        {
            MUpdateType updateType = (MUpdateType) iterator.next();
            apppendDataRow(context, updateType);
        }
        tag.close();
    }

    private void apppendDataRow(FormatterContext context, MUpdateType updateType)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(updateType.getMetadataEntryID());
        row.append(updateType.getSystemName());
        row.append(updateType.getSequence());
        row.append(updateType.getAttributes());
        row.append(updateType.getDefault());
        row.append(updateType.getValidationExpressionID());
        row.append(updateType.getUpdateHelpID());
        row.append(updateType.getValidationLookupName());
        row.append(updateType.getValidationExternalName());
        row.append(updateType.getMaxUpdate());
        row.end();
    }

    private static final String[] COLUMNS = new String[] {
        MUpdateType.METADATAENTRYID,
        MUpdateType.SYSTEMNAME,
        MUpdateType.SEQUENCE,
        MUpdateType.ATTRIBUTES,
        MUpdateType.DEFAULT,
        MUpdateType.VALIDATIONEXPRESSIONID,
        MUpdateType.UPDATEHELPID,
        MUpdateType.VALIDATIONLOOKUPNAME,
        MUpdateType.VALIDATIONEXTERNALNAME,
        MUpdateType.MAXUPDATE,
    };
}
