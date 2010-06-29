/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MEditMask;
import org.realtors.rets.common.metadata.types.MLookup;
import org.realtors.rets.common.metadata.types.MObject;
import org.realtors.rets.common.metadata.types.MResource;
import org.realtors.rets.common.metadata.types.MSearchHelp;
import org.realtors.rets.common.metadata.types.MUpdateHelp;
import org.realtors.rets.common.metadata.types.MValidationExpression;
import org.realtors.rets.common.metadata.types.MValidationExternal;
import org.realtors.rets.common.metadata.types.MValidationLookup;
import org.realtors.rets.server.metadata.ResourceStandardNameEnum;

public class ResourceFormatterTest extends FormatterTestCase
{
    protected List<MResource> getData()
    {
        List<MResource> resources = new ArrayList<MResource>();
        MResource resource = new MResource();
        resource.setResourceID("PropertyID");
        resource.setStandardName(ResourceStandardNameEnum.PROPERTY.toString());
        resource.setVisibleName("Prop");
        resource.setDescription("Property Database");
        resource.setKeyField("LN");
        
        MClass clazz = new MClass();
        clazz.setUniqueId(Long.valueOf(1));
        clazz.setClassName("RES");
        resource.addChild(MetadataType.CLASS, clazz);
        clazz = new MClass();
        clazz.setClassName("ATD");
        clazz.setUniqueId(Long.valueOf(2));
        resource.addChild(MetadataType.CLASS, clazz);
        
        MObject object = new MObject();
        object.setUniqueId(Long.valueOf(1));
        object.setObjectType("Photo");
        resource.addChild(MetadataType.OBJECT, object);
        
        MSearchHelp searchHelp = new MSearchHelp();
        searchHelp.setUniqueId(Long.valueOf(1));
        searchHelp.setSearchHelpID("PropertyMLSNumber");
        resource.addChild(MetadataType.SEARCH_HELP, searchHelp);
        
        MEditMask editMask = new MEditMask();
        editMask.setUniqueId(Long.valueOf(1));
        editMask.setEditMaskID("PropertyMLSNumber");
        resource.addChild(MetadataType.EDITMASK, editMask);
        
        MLookup lookup = new MLookup();
        lookup.setUniqueId(Long.valueOf(1));
        lookup.setLookupName("PropertyListingStatusId");
        resource.addChild(MetadataType.LOOKUP, lookup);
        
        MUpdateHelp updateHelp = new MUpdateHelp();
        updateHelp.setUniqueId(Long.valueOf(1));
        updateHelp.setUpdateHelpID("PropertyListingStatusId");
        resource.addChild(MetadataType.UPDATE_HELP, updateHelp);
        
        MValidationLookup validationLookup = new MValidationLookup();
        validationLookup.setUniqueId(Long.valueOf(1));
        validationLookup.setValidationLookupName("PropertyListingStatusId");
        resource.addChild(MetadataType.VALIDATION_LOOKUP, validationLookup);
        
        MValidationExternal validationExternal = new MValidationExternal();
        validationExternal.setUniqueId(Long.valueOf(1));
        validationExternal.setValidationExternalName("PropertyListingStatusId");
        resource.addChild(MetadataType.VALIDATION_EXTERNAL, validationExternal);
        
        MValidationExpression validationExpression = new MValidationExpression();
        validationExpression.setUniqueId(Long.valueOf(1));
        validationExpression.setValidationExpressionID("PropertyListingStatusId");
        resource.addChild(MetadataType.VALIDATION_EXPRESSION, validationExpression);
        
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
            MetadataType.CLASS.name() + "\n" +
            MetadataType.CLASS.name() + "\n" +
            MetadataType.OBJECT.name() + "\n" +
            MetadataType.SEARCH_HELP.name() + "\n" +
            MetadataType.EDITMASK.name() + "\n" +
            MetadataType.LOOKUP.name() + "\n" +
            MetadataType.UPDATE_HELP.name() + "\n" +
            MetadataType.VALIDATION_LOOKUP.name() + "\n" +
            MetadataType.VALIDATION_EXTERNAL.name() + "\n" +
            MetadataType.VALIDATION_EXPRESSION.name() + "\n";
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
            MetadataType.CLASS.name() + EOL +
            MetadataType.CLASS.name() + EOL +
            MetadataType.OBJECT.name() + EOL +
            MetadataType.SEARCH_HELP.name() + EOL +

            MetadataType.EDITMASK.name() + EOL +
            MetadataType.LOOKUP.name() + EOL +
            MetadataType.UPDATE_HELP.name() + EOL +
            MetadataType.VALIDATION_LOOKUP.name() + EOL +
            MetadataType.VALIDATION_EXPRESSION.name() + EOL +
            MetadataType.VALIDATION_EXTERNAL.name() + EOL +
            "</Resource>" + EOL +
            "</METADATA-RESOURCE>" + EOL;
    }
}
