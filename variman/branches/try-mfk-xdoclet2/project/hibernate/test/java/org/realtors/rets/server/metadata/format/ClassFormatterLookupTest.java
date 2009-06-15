/*
 */
package org.realtors.rets.server.metadata.format;

import org.realtors.rets.server.metadata.EditMask;
import org.realtors.rets.server.metadata.ForeignKey;
import org.realtors.rets.server.metadata.Lookup;
import org.realtors.rets.server.metadata.LookupType;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.MObject;
import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.SearchHelp;
import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.Update;
import org.realtors.rets.server.metadata.UpdateHelp;
import org.realtors.rets.server.metadata.UpdateType;
import org.realtors.rets.server.metadata.ValidationExpression;
import org.realtors.rets.server.metadata.ValidationExternal;
import org.realtors.rets.server.metadata.ValidationExternalType;
import org.realtors.rets.server.metadata.ValidationLookup;
import org.realtors.rets.server.metadata.ValidationLookupType;

import junit.framework.TestCase;

public class ClassFormatterLookupTest extends TestCase
{
    public void testCompactFormatters()
    {
        ClassFormatterLookup lookup =
            new ClassFormatterLookup(MetadataFormatter.COMPACT);
        assertTrue(lookup.lookupFormatter(MSystem.class) instanceof
                   CompactSystemFormatter);
        assertTrue(lookup.lookupFormatter(Resource.class) instanceof
                   CompactResourceFormatter);
        assertTrue(lookup.lookupFormatter(MClass.class) instanceof
                   CompactClassFormatter);
        assertTrue(lookup.lookupFormatter(Table.class) instanceof
                   CompactTableFormatter);
        assertTrue(lookup.lookupFormatter(Update.class) instanceof
                   CompactUpdateFormatter);
        assertTrue(lookup.lookupFormatter(UpdateType.class) instanceof
                   CompactUpdateTypeFormatter);
        assertTrue(lookup.lookupFormatter(MObject.class) instanceof
                   CompactObjectFormatter);
        assertTrue(lookup.lookupFormatter(SearchHelp.class) instanceof
                   CompactSearchHelpFormatter);
        assertTrue(lookup.lookupFormatter(EditMask.class) instanceof
                   CompactEditMaskFormatter);
        assertTrue(lookup.lookupFormatter(Lookup.class) instanceof
                   CompactLookupFormatter);
        assertTrue(lookup.lookupFormatter(UpdateHelp.class) instanceof
                   CompactUpdateHelpFormatter);
        assertTrue(lookup.lookupFormatter(LookupType.class) instanceof
                   CompactLookupTypeFormatter);
        assertTrue(lookup.lookupFormatter(ValidationLookup.class) instanceof
                   CompactValidationLookupFormatter);
        assertTrue(lookup.lookupFormatter(ValidationLookupType.class) instanceof
                   CompactValidationLookupTypeFormatter);
        assertTrue(lookup.lookupFormatter(ValidationExternal.class) instanceof
                   CompactValidationExternalFormatter);
        assertTrue(lookup.lookupFormatter(ValidationExternalType.class)
                   instanceof CompactValidationExternalTypeFormatter);
        assertTrue(lookup.lookupFormatter(ValidationExpression.class) instanceof
                   CompactValidationExpressionFormatter);
        assertTrue(lookup.lookupFormatter(ForeignKey.class) instanceof
                   CompactForeignKeyFormatter);
    }

    public void testStandardFormatters()
    {
        ClassFormatterLookup lookup =
            new ClassFormatterLookup(MetadataFormatter.STANDARD);
        assertTrue(lookup.lookupFormatter(MSystem.class) instanceof
                   StandardSystemFormatter);
        assertTrue(lookup.lookupFormatter(Resource.class) instanceof
                   StandardResourceFormatter);
        assertTrue(lookup.lookupFormatter(MClass.class) instanceof
                   StandardClassFormatter);
        assertTrue(lookup.lookupFormatter(Table.class) instanceof
                   StandardTableFormatter);
        assertTrue(lookup.lookupFormatter(Update.class) instanceof
                   StandardUpdateFormatter);
        assertTrue(lookup.lookupFormatter(UpdateType.class) instanceof
                   StandardUpdateTypeFormatter);
        assertTrue(lookup.lookupFormatter(MObject.class) instanceof
                   StandardObjectFormatter);
        assertTrue(lookup.lookupFormatter(SearchHelp.class) instanceof
                   StandardSearchHelpFormatter);
        assertTrue(lookup.lookupFormatter(EditMask.class) instanceof
                   StandardEditMaskFormatter);
        assertTrue(lookup.lookupFormatter(Lookup.class) instanceof
                   StandardLookupFormatter);
        assertTrue(lookup.lookupFormatter(UpdateHelp.class) instanceof
                   StandardUpdateHelpFormatter);
        assertTrue(lookup.lookupFormatter(LookupType.class) instanceof
                   StandardLookupTypeFormatter);
        assertTrue(lookup.lookupFormatter(ValidationLookup.class) instanceof
                   StandardValidationLookupFormatter);
        assertTrue(lookup.lookupFormatter(ValidationLookupType.class) instanceof
                   StandardValidationLookupTypeFormatter);
        assertTrue(lookup.lookupFormatter(ValidationExternal.class) instanceof
                   StandardValidationExternalFormatter);
        assertTrue(lookup.lookupFormatter(ValidationExternalType.class)
                   instanceof StandardValidationExternalTypeFormatter);
        assertTrue(lookup.lookupFormatter(ValidationExpression.class) instanceof
                   StandardValidationExpressionFormatter);
//        assertTrue(lookup.lookupFormatter(ForeignKey.class) instanceof
//                   StandardForeignKeyFormatter);
    }
}
