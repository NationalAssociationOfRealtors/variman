/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MResource;
import org.realtors.rets.common.metadata.types.MValidationExternal;
import org.realtors.rets.common.metadata.types.MValidationExternalType;

public class ValidationExternalFormatterTest extends FormatterTestCase
{
    protected List<MValidationExternal> getData()
    {
        List<MValidationExternal> validationExternals = new ArrayList<MValidationExternal>();
        MValidationExternal validationExternal = new MValidationExternal();
        validationExternal.setMetadataEntryID("Vet1");
        validationExternal.setValidationExternalName("VET1");

        MClass res = new MClass();
        res.setUniqueId(Long.valueOf(1));
        res.setClassName("RES");
        MResource property = new MResource();
        property.setUniqueId(Long.valueOf(1));
        property.setResourceID("Property");
        res.setMResource(property);
        validationExternal.setMClass(res);

        MValidationExternalType[] validationExternalTypes = new MValidationExternalType[1];
        MValidationExternalType validationExternalType = new MValidationExternalType();
        validationExternalType.setUniqueId(Long.valueOf(1));
        validationExternalTypes[0] = validationExternalType;
        validationExternal.setMValidationExternalTypes(validationExternalTypes);

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

            MetadataType.VALIDATION_EXTERNAL_TYPE.name() + "\n";
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
            MetadataType.VALIDATION_EXTERNAL_TYPE.name() + EOL +
            "</ValidationExternalType>" + EOL +
            "</METADATA-VALIDATION_EXTERNAL>" + EOL;
    }
}
