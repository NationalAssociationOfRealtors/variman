/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.UpdateHelp;
import org.realtors.rets.server.metadata.UpdateType;
import org.realtors.rets.server.metadata.UpdateTypeAttributeEnum;
import org.realtors.rets.server.metadata.ValidationExpression;
import org.realtors.rets.server.metadata.ValidationExternal;
import org.realtors.rets.server.metadata.ValidationLookup;

public class UpdateTypeFormatterTest extends FormatterTestCase
{
    protected void setUp()
    {
        mUpdateTypes = new ArrayList();
        Table table = new Table();
        table.setId(new Long(123));
        table.setSystemName("STATUS");
        
        UpdateType updateType = new UpdateType();
        updateType.setTable(table);
        updateType.setSequence(12);

        Set attributes = new HashSet();
        attributes.add(UpdateTypeAttributeEnum.INTERACTIVEVALIDATE);
        attributes.add(UpdateTypeAttributeEnum.REQUIRED);
        updateType.setAttributes(attributes);

        updateType.setDefault("0");

        Set validationExpressions = new HashSet();
        ValidationExpression ve = new ValidationExpression(1);
        ve.setValidationExpressionID("VE1");
        validationExpressions.add(ve);
        ve = new ValidationExpression(2);
        ve.setValidationExpressionID("VE2");
        validationExpressions.add(ve);
        updateType.setValidationExpressions(validationExpressions);

        UpdateHelp updateHelp = new UpdateHelp(1);
        updateHelp.setUpdateHelpID("UH1");
        updateType.setUpdateHelp(updateHelp);

        ValidationLookup validationLookup = new ValidationLookup(1);
        validationLookup.setValidationLookupName("VL_NAME");
        updateType.setValidationLookup(validationLookup);

        ValidationExternal validationExternal = new ValidationExternal(1);
        validationExternal.setValidationExternalName("VE_NAME");
        updateType.setValidationExternal(validationExternal);

        mUpdateTypes.add(updateType);
    }

    private UpdateTypeFormatter getFormatter(int format)
    {
        UpdateTypeFormatter formatter = UpdateTypeFormatter.getInstance(format);
        formatter.setVersion("1.00.001", getDate());
        formatter.setLevels(new String[] {"Property", "RES", "Change"});
        return formatter;
    }

    public void testCompactFormatUpdateType()
    {
        UpdateTypeFormatter formatter = getFormatter(MetadataFormatter.COMPACT);
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), mUpdateTypes);
        assertEquals(
            "<METADATA-UPDATE_TYPE Resource=\"Property\" Class=\"RES\" " +
            "Update=\"Change\" Version=\"" + VERSION + "\" Date=\"" + DATE +
            "\">\n" +

            "<COLUMNS>\tSystemName\tSequence\tAttributes\tDefault\t" +
            "ValidationExpressionID\tUpdateHelpID\tValidationLookupName\t" +
            "ValidationExternalName\t</COLUMNS>\n" +

            "<DATA>\tSTATUS\t12\t2,4\t0\tVE1,VE2\tUH1\tVL_NAME\tVE_NAME\t" +
            "</DATA>\n" +

            "</METADATA-UPDATE_TYPE>\n",

            formatted.toString());
    }

    public void testEmptyCompactFormatUpdateType()
    {
        UpdateTypeFormatter formatter = getFormatter(MetadataFormatter.COMPACT);
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), new ArrayList());
        assertEquals("", formatted.toString());
    }

    private List mUpdateTypes;
}
