/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.realtors.rets.common.metadata.types.MValidationExternalType;

public class ValidationExternalTypeFormatterTest extends FormatterTestCase
{
    protected List<MValidationExternalType> getData()
    {
        List<MValidationExternalType> validationExternalTypes = new ArrayList<MValidationExternalType>();
        MValidationExternalType validationExternalType =
            new MValidationExternalType();
        
        validationExternalType.setMetadataEntryID("AgentValidation");

        Set<String> searchFields = new LinkedHashSet<String>();
        searchFields.add("AgentCode");
        searchFields.add("AgentID");
        String searchField = MValidationExternalType.toSearchField(searchFields);
        validationExternalType.setSearchField(searchField);

        Set<String> displayFields = new LinkedHashSet<String>();
        displayFields.add("AgentName");
        displayFields.add("OfficeName");
        String displayField = MValidationExternalType.toDisplayField(displayFields);
        validationExternalType.setDisplayField(displayField);

        Map<String, String> resultFieldsMap = new LinkedHashMap<String, String>();
        resultFieldsMap.put("SaleAgentID", "AgentID");
        resultFieldsMap.put("SaleAgentName", "AgentName");
        String resultFields = MValidationExternalType.toResultFields(resultFieldsMap);
        validationExternalType.setResultFields(resultFields);

        validationExternalTypes.add(validationExternalType);
        return validationExternalTypes;
    }

    protected String[] getLevels()
    {
        return new String[] {"Property", "VET1"};
    }

    protected MetadataFormatter getCompactFormatter()
    {
        return new CompactValidationExternalTypeFormatter();
    }

    protected MetadataFormatter getStandardFormatter()
    {
        return new StandardValidationExternalTypeFormatter();
    }

    protected String getExpectedCompact()
    {
        return
            "<METADATA-VALIDATION_EXTERNAL_TYPE Resource=\"Property\" " +
            "ValidationExternalName=\"VET1\" Version=\"" + VERSION + "\" " +
            "Date=\"" + DATE + "\">\n" +


            "<COLUMNS>\tMetadataEntryID\tSearchField\tDisplayField\tResultFields\t</COLUMNS>\n" +

            "<DATA>\tAgentValidation\tAgentCode,AgentID\tAgentName,OfficeName\t" +
            "SaleAgentID=AgentID,SaleAgentName=AgentName\t</DATA>\n" +

            "</METADATA-VALIDATION_EXTERNAL_TYPE>\n";
    }

    protected String getExpectedCompactRecursive()
    {
        return getExpectedCompact();
    }

    protected String getExpectedStandard()
    {
        return
            "<METADATA-VALIDATION_EXTERNAL_TYPE Resource=\"Property\" " +
            "ValidationExternalName=\"VET1\" Version=\"" + VERSION + "\" " +
            "Date=\"" + DATE + "\">" + EOL +
            "<ValidationExternal>" + EOL +
            "<MetadataEntryID>AgentValidation</MetadataEntryID>" + EOL +
            "<SearchField>AgentCode</SearchField>" + EOL +
            "<SearchField>AgentID</SearchField>" + EOL +
            "<DisplayField>AgentName</DisplayField>" + EOL +
            "<DisplayField>OfficeName</DisplayField>" + EOL +
            "<ResultFields>" + EOL +
            "<Source>SaleAgentID</Source>" + EOL +
            "<Target>AgentID</Target>" + EOL +
            // Line 10
            "</ResultFields>" + EOL +
            "<ResultFields>" + EOL +
            "<Source>SaleAgentName</Source>" + EOL +
            "<Target>AgentName</Target>" + EOL +
            "</ResultFields>" + EOL +
            "</ValidationExternal>" + EOL +
            "</METADATA-VALIDATION_EXTERNAL_TYPE>" + EOL;
    }

    protected String getExpectedStandardRecursive()
    {
        return getExpectedStandard();
    }
}
