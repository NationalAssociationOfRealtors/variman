/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.realtors.rets.server.metadata.ValidationExternalType;

public class CompactValidationExternalTypeFormatter
    extends ValidationExternalTypeFormatter
{
    public void format(PrintWriter out, List validationExternalTypes)
    {
        if (validationExternalTypes.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(out);
        tag.begin("METADATA-VALIDATION_EXTERNAL_TYPE");
        tag.appendAttribute("Resource", mResourceName);
        tag.appendAttribute("ValidationExternal", mValidationExternalName);
        tag.appendAttribute("Version", mVersion);
        tag.appendAttribute("Date", mDate);
        tag.endAttributes();
        tag.appendColumns(COLUMNS);
        for (int i = 0; i < validationExternalTypes.size(); i++)
        {
            ValidationExternalType validationExternalType =
                (ValidationExternalType) validationExternalTypes.get(i);
            appendDataRow(out, validationExternalType);
        }
        tag.end();
    }

    private void appendDataRow(PrintWriter out,
                               ValidationExternalType validationExternalType)
    {
        DataRowBuilder row = new DataRowBuilder(out);
        row.begin();
        row.append(validationExternalType.getSearchField());
        row.append(validationExternalType.getDisplayField());
        List formattedResultFields =
            mapToKeyValues(validationExternalType.getResultFields());
        row.append(formattedResultFields);
        row.end();
    }

    private List mapToKeyValues(Map resultFields)
    {
        Set keys = resultFields.keySet();
        List keyValues = new ArrayList();
        for (Iterator iterator = keys.iterator(); iterator.hasNext();)
        {
            String key = (String) iterator.next();
            String value = (String) resultFields.get(key);
            keyValues.add(key + "=" + value);
        }
        return keyValues;
    }

    private static final String[] COLUMNS = new String[] {
        "SearchField", "DisplayField", "ResultFields",
    };
}
