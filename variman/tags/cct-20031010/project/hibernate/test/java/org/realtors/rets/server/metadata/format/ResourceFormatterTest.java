/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.server.metadata.EditMask;
import org.realtors.rets.server.metadata.Lookup;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.MObject;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.ResourceStandardNameEnum;
import org.realtors.rets.server.metadata.SearchHelp;
import org.realtors.rets.server.metadata.ValidationExpression;
import org.realtors.rets.server.metadata.ValidationExternal;
import org.realtors.rets.server.metadata.ValidationLookup;
import org.realtors.rets.server.metadata.UpdateHelp;

public class ResourceFormatterTest extends FormatterTestCase
{
    protected List getData()
    {
        ArrayList resources = new ArrayList();
        Resource resource = new Resource();
        resource.setResourceID("PropertyID");
        resource.setStandardName(ResourceStandardNameEnum.PROPERTY);
        resource.setVisibleName("Prop");
        resource.setDescription("Property Database");
        resource.setKeyField("LN");

        resource.addClass(new MClass(1));
        resource.addClass(new MClass(2));
        resource.addObject(new MObject(1));
        resource.addSearchHelp(new SearchHelp(1));
        resource.addEditMask(new EditMask(1));
        resource.addLookup(new Lookup(1));
        resource.addUpdateHelp(new UpdateHelp(1));
        resource.addValidationLookup(new ValidationLookup(1));
        resource.addValidationExternal(new ValidationExternal(1));
        resource.addValidationExpression(new ValidationExpression(1));

        resources.add(resource);
        return resources;
    }

    protected String[] getLevels()
    {
        return new String[0];
    }

    protected MetadataFormatter getCompactFormatter()
    {
        return new CompactResourceFormatter();
    }

    protected String getExpectedCompact()
    {
        return
            "<METADATA-RESOURCE Version=\"" + VERSION + "\" " +
            "Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tResourceID\tStandardName\tVisibleName\tDescription\t" +
            "KeyField\tClassCount\tClassVersion\tClassDate\tObjectVersion\t" +
            "ObjectDate\tSearchHelpVersion\tSearchHelpDate\tEditMaskVersion\t" +
            "EditMaskDate\tLookupVersion\tLookupDate\tUpdateHelpVersion\t" +
            "UpdateHelpDate\tValidationExpressionVersion\t" +
            "ValidationExpressionDate\tValidationLookupVersion\t" +
            "ValidationLookupDate\tValidationExternalVersion\t" +
            "ValidationExternalDate\t</COLUMNS>\n" +

            "<DATA>\tPropertyID\tProperty\tProp\tProperty Database\t" +
            "LN\t2" +
            VERSION_DATE + VERSION_DATE + VERSION_DATE + VERSION_DATE +
            VERSION_DATE + VERSION_DATE + VERSION_DATE + VERSION_DATE +
            VERSION_DATE + "\t</DATA>\n" +

            "</METADATA-RESOURCE>\n";
    }

    protected MetadataFormatter getStandardFormatter()
    {
        return new StandardResourceFormatter();
    }

    protected String getExpectedCompactRecursive()
    {
        return
            "<METADATA-RESOURCE Version=\"" + VERSION + "\" " +
            "Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tResourceID\tStandardName\tVisibleName\tDescription\t" +
            "KeyField\tClassCount\tClassVersion\tClassDate\tObjectVersion\t" +
            "ObjectDate\tSearchHelpVersion\tSearchHelpDate\tEditMaskVersion\t" +
            "EditMaskDate\tLookupVersion\tLookupDate\tUpdateHelpVersion\t" +
            "UpdateHelpDate\tValidationExpressionVersion\t" +
            "ValidationExpressionDate\tValidationLookupVersion\t" +
            "ValidationLookupDate\tValidationExternalVersion\t" +
            "ValidationExternalDate\t</COLUMNS>\n" +

            "<DATA>\tPropertyID\tProperty\tProp\tProperty Database\t" +
            "LN\t2" +
            VERSION_DATE + VERSION_DATE + VERSION_DATE + VERSION_DATE +
            VERSION_DATE + VERSION_DATE + VERSION_DATE + VERSION_DATE +
            VERSION_DATE + "\t</DATA>\n" +

            "</METADATA-RESOURCE>\n" +
            MClass.TABLE + "\n" +
            MClass.TABLE + "\n" +
            MObject.TABLE + "\n" +
            SearchHelp.TABLE + "\n" +
            EditMask.TABLE + "\n" +
            Lookup.TABLE + "\n" +
            UpdateHelp.TABLE + "\n" +
            ValidationLookup.TABLE + "\n" +
            ValidationExternal.TABLE + "\n" +
            ValidationExpression.TABLE + "\n";
    }

    protected String getExpectedStandard()
    {
        return
            "<METADATA-RESOURCE Version=\"" + VERSION + "\" " +
            "Date=\"" + DATE + "\">" + EOL +
            "<Resource>" + EOL +
            "<ResourceID>PropertyID</ResourceID>" + EOL +
            "<StandardName>Property</StandardName>" + EOL +
            "<VisibleName>Prop</VisibleName>" + EOL +
            "<Description>Property Database</Description>" + EOL +
            "<KeyField>LN</KeyField>" + EOL +
            "<ClassCount>2</ClassCount>" + EOL +
            "<ClassVersion>" + VERSION + "</ClassVersion>" + EOL +
            "<ClassDate>" + DATE + "</ClassDate>" + EOL +

            "<ObjectVersion>" + VERSION + "</ObjectVersion>" + EOL +
            "<ObjectDate>" + DATE + "</ObjectDate>" + EOL +
            "<SearchHelpVersion>" + VERSION + "</SearchHelpVersion>" + EOL +
            "<SearchHelpDate>" + DATE + "</SearchHelpDate>" + EOL +
            "<EditMaskVersion>" + VERSION + "</EditMaskVersion>" + EOL +
            "<EditMaskDate>" + DATE + "</EditMaskDate>" + EOL +
            "<LookupVersion>" + VERSION + "</LookupVersion>" + EOL +
            "<LookupDate>" + DATE + "</LookupDate>" + EOL +
            "<UpdateHelpVersion>" + VERSION + "</UpdateHelpVersion>" + EOL +
            "<UpdateHelpDate>" + DATE + "</UpdateHelpDate>" + EOL +

            "<ValidationExpressionVersion>" + VERSION +
            "</ValidationExpressionVersion>" + EOL +
            "<ValidationExpressionDate>" + DATE +
            "</ValidationExpressionDate>" + EOL +
            "<ValidationLookupVersion>" + VERSION +
            "</ValidationLookupVersion>" + EOL +
            "<ValidationLookupDate>" + DATE + "</ValidationLookupDate>" + EOL +
            "<ValidationExternalVersion>" + VERSION +
            "</ValidationExternalVersion>" + EOL +
            "<ValidationExternalDate>" + DATE +
            "</ValidationExternalDate>" + EOL +
            "</Resource>" + EOL +
            "</METADATA-RESOURCE>" + EOL;
    }

    protected String getExpectedStandardRecursive()
    {
        return
            "<METADATA-RESOURCE Version=\"" + VERSION + "\" " +
            "Date=\"" + DATE + "\">" + EOL +
            "<Resource>" + EOL +
            "<ResourceID>PropertyID</ResourceID>" + EOL +
            "<StandardName>Property</StandardName>" + EOL +
            "<VisibleName>Prop</VisibleName>" + EOL +
            "<Description>Property Database</Description>" + EOL +
            "<KeyField>LN</KeyField>" + EOL +
            "<ClassCount>2</ClassCount>" + EOL +
            "<ClassVersion>" + VERSION + "</ClassVersion>" + EOL +
            "<ClassDate>" + DATE + "</ClassDate>" + EOL +

            "<ObjectVersion>" + VERSION + "</ObjectVersion>" + EOL +
            "<ObjectDate>" + DATE + "</ObjectDate>" + EOL +
            "<SearchHelpVersion>" + VERSION + "</SearchHelpVersion>" + EOL +
            "<SearchHelpDate>" + DATE + "</SearchHelpDate>" + EOL +
            "<EditMaskVersion>" + VERSION + "</EditMaskVersion>" + EOL +
            "<EditMaskDate>" + DATE + "</EditMaskDate>" + EOL +
            "<LookupVersion>" + VERSION + "</LookupVersion>" + EOL +
            "<LookupDate>" + DATE + "</LookupDate>" + EOL +
            "<UpdateHelpVersion>" + VERSION + "</UpdateHelpVersion>" + EOL +
            "<UpdateHelpDate>" + DATE + "</UpdateHelpDate>" + EOL +

            "<ValidationExpressionVersion>" + VERSION +
            "</ValidationExpressionVersion>" + EOL +
            "<ValidationExpressionDate>" + DATE +
            "</ValidationExpressionDate>" + EOL +
            "<ValidationLookupVersion>" + VERSION +
            "</ValidationLookupVersion>" + EOL +
            "<ValidationLookupDate>" + DATE + "</ValidationLookupDate>" + EOL +
            "<ValidationExternalVersion>" + VERSION +
            "</ValidationExternalVersion>" + EOL +
            "<ValidationExternalDate>" + DATE +
            "</ValidationExternalDate>" + EOL +
            MClass.TABLE + EOL +
            MClass.TABLE + EOL +
            MObject.TABLE + EOL +
            SearchHelp.TABLE + EOL +

            EditMask.TABLE + EOL +
            Lookup.TABLE + EOL +
            UpdateHelp.TABLE + EOL +
            ValidationLookup.TABLE + EOL +
            ValidationExpression.TABLE + EOL +
            ValidationExternal.TABLE + EOL +
            "</Resource>" + EOL +
            "</METADATA-RESOURCE>" + EOL;
    }
}
