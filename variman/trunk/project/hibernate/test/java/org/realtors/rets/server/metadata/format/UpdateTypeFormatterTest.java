/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.Set;
import java.util.HashSet;

import org.realtors.rets.server.metadata.UpdateType;
import org.realtors.rets.server.metadata.UpdateTypeAttributeEnum;
import org.realtors.rets.server.metadata.ValidationExpression;
import org.realtors.rets.server.metadata.UpdateHelp;
import org.realtors.rets.server.metadata.ValidationLookup;
import org.realtors.rets.server.metadata.ValidationExternal;

public class UpdateTypeFormatterTest extends FormatterTestCase
{
    protected void setUp()
    {
        UpdateType updateType = new UpdateType();
        updateType.setSystemName("STATUS");
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

        mUpdateTypes = new UpdateType[] {updateType};
    }

    private UpdateTypeFormatter getFormatter(int format)
    {
        UpdateTypeFormatter formatter = UpdateTypeFormatter.getInstance(format);
        formatter.setVersion("1.00.001", getDate());
        formatter.setResourceName("Property");
        formatter.setClassName("RES");
        formatter.setUpdateName("Change");
        return formatter;
    }

    public void testCompactFormatUpdateType()
    {
        UpdateTypeFormatter formatter = getFormatter(MetadataFormatter.COMPACT);
        String formatted = formatter.format(mUpdateTypes);
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

            formatted);
    }

    private UpdateType[] mUpdateTypes;
}
