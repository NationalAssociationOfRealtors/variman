/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.io.StringWriter;

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
import org.realtors.rets.server.metadata.UpdateType;
import org.realtors.rets.server.metadata.ValidationExpression;
import org.realtors.rets.server.metadata.ValidationExternal;
import org.realtors.rets.server.metadata.ValidationExternalType;
import org.realtors.rets.server.metadata.ValidationLookup;
import org.realtors.rets.server.metadata.ValidationLookupType;
import org.realtors.rets.server.metadata.UpdateHelp;

import junit.framework.TestCase;

public class MetadataSegmentFormatterTest extends TestCase
{
    public void testCompactFormatters()
    {
        StringWriter formatted = new StringWriter();
        MetadataSegmentFormatter visitor =
            new MetadataSegmentFormatter(new PrintWriter(formatted),
                                  MetadataFormatter.COMPACT);
        assertTrue(visitor.getFormatter(MSystem.class) instanceof
                   CompactSystemFormatter);
        assertTrue(visitor.getFormatter(Resource.class) instanceof
                   CompactResourceFormatter);
        assertTrue(visitor.getFormatter(MClass.class) instanceof
                   CompactClassFormatter);
        assertTrue(visitor.getFormatter(Table.class) instanceof
                   CompactTableFormatter);
        assertTrue(visitor.getFormatter(Update.class) instanceof
                   CompactUpdateFormatter);
        assertTrue(visitor.getFormatter(UpdateType.class) instanceof
                   CompactUpdateTypeFormatter);
        assertTrue(visitor.getFormatter(MObject.class) instanceof
                   CompactObjectFormatter);
        assertTrue(visitor.getFormatter(SearchHelp.class) instanceof
                   CompactSearchHelpFormatter);
        assertTrue(visitor.getFormatter(EditMask.class) instanceof
                   CompactEditMaskFormatter);
        assertTrue(visitor.getFormatter(Lookup.class) instanceof
                   CompactLookupFormatter);
        assertTrue(visitor.getFormatter(UpdateHelp.class) instanceof
                   CompactUpdateHelpFormatter);
        assertTrue(visitor.getFormatter(LookupType.class) instanceof
                   CompactLookupTypeFormatter);
        assertTrue(visitor.getFormatter(ValidationLookup.class) instanceof
                   CompactValidationLookupFormatter);
        assertTrue(visitor.getFormatter(ValidationLookupType.class) instanceof
                   CompactValidationLookupTypeFormatter);
        assertTrue(visitor.getFormatter(ValidationExternal.class) instanceof
                   CompactValidationExternalFormatter);
        assertTrue(visitor.getFormatter(ValidationExternalType.class)
                   instanceof CompactValidationExternalTypeFormatter);
        assertTrue(visitor.getFormatter(ValidationExpression.class) instanceof
                   CompactValidationExpressionFormatter);
        assertTrue(visitor.getFormatter(ForeignKey.class) instanceof
                   CompactForeignKeyFormatter);
    }
}
