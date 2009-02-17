/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.ValidationExternal;
import org.realtors.rets.server.metadata.ValidationExternalType;

public class ValidationExternalFormatterTest extends FormatterTestCase
{
    protected List getData()
    {
        List validationExternals = new ArrayList();
        ValidationExternal validationExternal = new ValidationExternal();
        validationExternal.setMetadataEntryID("Vet1");
        validationExternal.setValidationExternalName("VET1");

        MClass res = new MClass(1);
        res.setClassName("RES");
        Resource property = new Resource(1);
        property.setResourceID("Property");
        res.setResource(property);
        validationExternal.setSearchClass(res);

        validationExternal.addValidationExternalType(
            new ValidationExternalType(1));
        validationExternals.add(validationExternal);
        return validationExternals;
    }

    protected String[] getLevels()
    {
        return new String[] {"Property"};
    }

    protected MetadataFormatter getCompactFormatter()
    {
        return new CompactValidationExternalFormatter();
    }

    protected MetadataFormatter getStandardFormatter()
    {
        return new StandardValidationExternalFormatter();
    }

    protected String getExpectedCompact()
    {
        return
            "<METADATA-VALIDATION_EXTERNAL Resource=\"Property\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tMetadataEntryID\tValidationExternalName\tSearchResource\tSearchClass\t" +
            "Version\tDate\t</COLUMNS>\n" +

            "<DATA>\tVet1\tVET1\tProperty\tRES" + VERSION_DATE + "\t</DATA>\n" +

            "</METADATA-VALIDATION_EXTERNAL>\n";
    }

    protected String getExpectedCompactRecursive()
    {
        return
            "<METADATA-VALIDATION_EXTERNAL Resource=\"Property\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tMetadataEntryID\tValidationExternalName\tSearchResource\tSearchClass\t" +
            "Version\tDate\t</COLUMNS>\n" +

            "<DATA>\tVet1\tVET1\tProperty\tRES" + VERSION_DATE + "\t</DATA>\n" +

            "</METADATA-VALIDATION_EXTERNAL>\n" +

            ValidationExternalType.TABLE + "\n";
    }

    protected String getExpectedStandard()
    {
        return
            "<METADATA-VALIDATION_EXTERNAL Resource=\"Property\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">" + EOL +
            "<ValidationExternalType>" + EOL +
            "<MetadataEntryID>Vet1</MetadataEntryID>" + EOL +
            "<ValidationExternalName>VET1</ValidationExternalName>" + EOL +
            "<SearchResource>Property</SearchResource>" + EOL +
            "<SearchClass>RES</SearchClass>" + EOL +
            "<ValidationExternalTypeVersion>" + VERSION +
            "</ValidationExternalTypeVersion>" + EOL +
            "<ValidationExternalTypeDate>" + DATE +
            "</ValidationExternalTypeDate>" + EOL +
            "</ValidationExternalType>" + EOL +
            "</METADATA-VALIDATION_EXTERNAL>" + EOL;
    }

    protected String getExpectedStandardRecursive()
    {
        return
            "<METADATA-VALIDATION_EXTERNAL Resource=\"Property\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">" + EOL +
            "<ValidationExternalType>" + EOL +
            "<MetadataEntryID>Vet1</MetadataEntryID>" + EOL +
            "<ValidationExternalName>VET1</ValidationExternalName>" + EOL +
            "<SearchResource>Property</SearchResource>" + EOL +
            "<SearchClass>RES</SearchClass>" + EOL +
            "<ValidationExternalTypeVersion>" + VERSION +
            "</ValidationExternalTypeVersion>" + EOL +
            "<ValidationExternalTypeDate>" + DATE +
            "</ValidationExternalTypeDate>" + EOL +
            ValidationExternalType.TABLE + EOL +
            "</ValidationExternalType>" + EOL +
            "</METADATA-VALIDATION_EXTERNAL>" + EOL;
    }
}
