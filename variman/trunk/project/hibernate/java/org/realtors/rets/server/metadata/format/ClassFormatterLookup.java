/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

/**
 * An implementation of FormatterLookup that uses the Class of the first
 * element of the collection.
 */
public class ClassFormatterLookup implements FormatterLookup
{
    public ClassFormatterLookup(int format)
    {
        mFormmatters = new HashMap();
        if (format == MetadataFormatter.COMPACT)
        {
            mFormmatters.put(MSystem.class, new CompactSystemFormatter());
            mFormmatters.put(Resource.class, new CompactResourceFormatter());
            mFormmatters.put(MClass.class, new CompactClassFormatter());
            mFormmatters.put(Table.class, new CompactTableFormatter());
            mFormmatters.put(Update.class, new CompactUpdateFormatter());
            mFormmatters.put(UpdateType.class,
                             new CompactUpdateTypeFormatter());
            mFormmatters.put(MObject.class, new CompactObjectFormatter());
            mFormmatters.put(SearchHelp.class,
                             new CompactSearchHelpFormatter());
            mFormmatters.put(EditMask.class, new CompactEditMaskFormatter());
            mFormmatters.put(Lookup.class, new CompactLookupFormatter());
            mFormmatters.put(UpdateHelp.class,
                             new CompactUpdateHelpFormatter());
            mFormmatters.put(LookupType.class,
                             new CompactLookupTypeFormatter());
            mFormmatters.put(ValidationLookup.class,
                             new CompactValidationLookupFormatter());
            mFormmatters.put(ValidationLookupType.class,
                             new CompactValidationLookupTypeFormatter());
            mFormmatters.put(ValidationExternal.class,
                             new CompactValidationExternalFormatter());
            mFormmatters.put(ValidationExternalType.class,
                             new CompactValidationExternalTypeFormatter());
            mFormmatters.put(ValidationExpression.class,
                             new CompactValidationExpressionFormatter());
            mFormmatters.put(ForeignKey.class,
                             new CompactForeignKeyFormatter());
        }
        else if (format == MetadataFormatter.STANDARD)
        {
            mFormmatters.put(MSystem.class, new StandardSystemFormatter());
            mFormmatters.put(Resource.class, new StandardResourceFormatter());
            mFormmatters.put(MClass.class, new StandardClassFormatter());
            mFormmatters.put(Table.class, new StandardTableFormatter());
            mFormmatters.put(Update.class, new StandardUpdateFormatter());
            mFormmatters.put(UpdateType.class,
                             new StandardUpdateTypeFormatter());
            mFormmatters.put(MObject.class, new StandardObjectFormatter());
            mFormmatters.put(SearchHelp.class,
                             new StandardSearchHelpFormatter());
            mFormmatters.put(EditMask.class, new StandardEditMaskFormatter());
            mFormmatters.put(Lookup.class, new StandardLookupFormatter());
            mFormmatters.put(UpdateHelp.class,
                             new StandardUpdateHelpFormatter());
            mFormmatters.put(LookupType.class,
                             new StandardLookupTypeFormatter());
            mFormmatters.put(ValidationLookup.class,
                             new StandardValidationLookupFormatter());
            mFormmatters.put(ValidationLookupType.class,
                             new StandardValidationLookupTypeFormatter());
            mFormmatters.put(ValidationExternal.class,
                             new StandardValidationExternalFormatter());
            mFormmatters.put(ValidationExternalType.class,
                             new StandardValidationExternalTypeFormatter());
            mFormmatters.put(ValidationExpression.class,
                             new StandardValidationExpressionFormatter());
//            mFormmatters.put(ForeignKey.class,
//                             new StandardForeignKeyFormatter());
        }
        else
        {
            throw new IllegalArgumentException("Unknown format: " + format);
        }
    }

    /**
     * Lookup a formatter from a class.  Package scope for testing
     *
     * @param clazz Class to lookup
     * @return formatter for the class, or <code>null</code>
     */
    MetadataFormatter lookupFormatter(Class clazz)
    {
        return (MetadataFormatter) mFormmatters.get(clazz);
    }

    public MetadataFormatter lookupFormatter(Collection metadataCollection)
    {
        Iterator i = metadataCollection.iterator();
        if (i.hasNext())
        {
            return lookupFormatter(i.next().getClass());
        }
        else
        {
            return NULL_FORMATTER;
        }
    }

    protected PrintWriter mOut;
    private Map mFormmatters;
    private static final MetadataFormatter NULL_FORMATTER =
        new NullMetadataFormatter();
}
