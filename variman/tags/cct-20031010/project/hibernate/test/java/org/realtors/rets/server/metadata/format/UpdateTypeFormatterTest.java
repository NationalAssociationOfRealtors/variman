/*
 */
package org.realtors.rets.server.metadata.format;

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
    protected List getData()
    {
        List updateTypes = new ArrayList();
        Table table = new Table(123);
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

        updateTypes.add(updateType);
        return updateTypes;
    }

    protected String[] getLevels()
    {
        return new String[] {"Property", "RES", "Change"};
    }

    protected MetadataFormatter getCompactFormatter()
    {
        return new CompactUpdateTypeFormatter();
    }

    protected MetadataFormatter getStandardFormatter()
    {
        return new StandardUpdateTypeFormatter();
    }

    protected String getExpectedCompact()
    {
        return
            "<METADATA-UPDATE_TYPE Resource=\"Property\" Class=\"RES\" " +
            "Update=\"Change\" Version=\"" + VERSION + "\" Date=\"" + DATE +
            "\">\n" +

            "<COLUMNS>\tSystemName\tSequence\tAttributes\tDefault\t" +
            "ValidationExpressionID\tUpdateHelpID\tValidationLookupName\t" +
            "ValidationExternalName\t</COLUMNS>\n" +

            "<DATA>\tSTATUS\t12\t2,4\t0\tVE1,VE2\tUH1\tVL_NAME\tVE_NAME\t" +
            "</DATA>\n" +

            "</METADATA-UPDATE_TYPE>\n";
    }

    protected String getExpectedCompactRecursive()
    {
        return getExpectedCompact();
    }

    protected String getExpectedStandard()
    {
        return
            "<METADATA-UPDATE_TYPE Resource=\"Property\" Class=\"RES\" " +
            "Update=\"Change\" Version=\"" + VERSION + "\" Date=\"" + DATE +
            "\">" + EOL +
            "<UpdateField>" + EOL +
            "<SystemName>STATUS</SystemName>" + EOL +
            "<Sequence>12</Sequence>" + EOL +
            "<Attributes>2,4</Attributes>" + EOL +
            "<Default>0</Default>" + EOL +
            "<ValidationExpressionID>VE1,VE2</ValidationExpressionID>" + EOL +
            "<UpdateHelpID>UH1</UpdateHelpID>" + EOL +
            "<ValidationLookupName>VL_NAME</ValidationLookupName>" + EOL +
            "<ValidationExternalName>VE_NAME</ValidationExternalName>" + EOL +
            "</UpdateField>" + EOL +
            "</METADATA-UPDATE_TYPE>" + EOL;
    }

    protected String getExpectedStandardRecursive()
    {
        return getExpectedStandard();
    }
}
