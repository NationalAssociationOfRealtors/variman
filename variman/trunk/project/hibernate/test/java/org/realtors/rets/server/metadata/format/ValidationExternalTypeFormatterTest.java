/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import org.realtors.rets.server.metadata.ValidationExternalType;

public class ValidationExternalTypeFormatterTest extends FormatterTestCase
{
    protected void setUp()
    {
        mValidationExternalTypes = new ArrayList();
        ValidationExternalType validationExternalType =
            new ValidationExternalType();

        Set searchFields = new HashSet();
        searchFields.add("AgentID");
        searchFields.add("AgentCode");
        validationExternalType.setSearchField(searchFields);

        Set displayFields = new HashSet();
        displayFields.add("AgentName");
        displayFields.add("OfficeName");
        validationExternalType.setDisplayField(displayFields);

        Map resultFields = new HashMap();
        resultFields.put("SaleAgentID", "AgentID");
        resultFields.put("SaleAgentName", "AgentName");
        validationExternalType.setResultFields(resultFields);

        mValidationExternalTypes.add(validationExternalType);
    }

    private ValidationExternalTypeFormatter getFormatter(int format)
    {
        ValidationExternalTypeFormatter formatter =
            ValidationExternalTypeFormatter.getInstance(format);
        formatter.setVersion("1.00.001", getDate());
        formatter.setResourceName("Property");
        formatter.setValidationExternalName("VET1");
        return formatter;
    }

    public void testCompactFormatValidationExternalType()
    {
        ValidationExternalTypeFormatter formatter =
            getFormatter(MetadataFormatter.COMPACT);
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), mValidationExternalTypes);
        assertEquals(
            "<METADATA-VALIDATION_EXTERNAL_TYPE Resource=\"Property\" " +
            "ValidationExternal=\"VET1\" Version=\"" + VERSION + "\" " +
            "Date=\"" + DATE + "\">\n" +


            "<COLUMNS>\tSearchField\tDisplayField\tResultFields\t</COLUMNS>\n" +

            "<DATA>\tAgentCode,AgentID\tAgentName,OfficeName\t" +
            "SaleAgentID=AgentID,SaleAgentName=AgentName\t</DATA>\n" +

            "</METADATA-VALIDATION_EXTERNAL_TYPE>\n",

            formatted.toString());
    }

    public void testEmptyCompactFormat()
    {
        ValidationExternalTypeFormatter formatter =
            getFormatter(MetadataFormatter.COMPACT);
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), new ArrayList());
        assertEquals("", formatted.toString());
    }

    private List mValidationExternalTypes;
}
