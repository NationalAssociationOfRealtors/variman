/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
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
import org.realtors.rets.server.metadata.ServerMetadata;
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
        mFormatters = new HashMap();
        if (format == MetadataFormatter.COMPACT)
        {
            mFormatters.put(MSystem.class, new CompactSystemFormatter());
            mFormatters.put(Resource.class, new CompactResourceFormatter());
            mFormatters.put(MClass.class, new CompactClassFormatter());
            mFormatters.put(Table.class, new CompactTableFormatter());
            mFormatters.put(Update.class, new CompactUpdateFormatter());
            mFormatters.put(UpdateType.class,
                             new CompactUpdateTypeFormatter());
            mFormatters.put(MObject.class, new CompactObjectFormatter());
            mFormatters.put(SearchHelp.class,
                             new CompactSearchHelpFormatter());
            mFormatters.put(EditMask.class, new CompactEditMaskFormatter());
            mFormatters.put(Lookup.class, new CompactLookupFormatter());
            mFormatters.put(UpdateHelp.class,
                             new CompactUpdateHelpFormatter());
            mFormatters.put(LookupType.class,
                             new CompactLookupTypeFormatter());
            mFormatters.put(ValidationLookup.class,
                             new CompactValidationLookupFormatter());
            mFormatters.put(ValidationLookupType.class,
                             new CompactValidationLookupTypeFormatter());
            mFormatters.put(ValidationExternal.class,
                             new CompactValidationExternalFormatter());
            mFormatters.put(ValidationExternalType.class,
                             new CompactValidationExternalTypeFormatter());
            mFormatters.put(ValidationExpression.class,
                             new CompactValidationExpressionFormatter());
            mFormatters.put(ForeignKey.class,
                             new CompactForeignKeyFormatter());
        }
        else if (format == MetadataFormatter.STANDARD)
        {
            mFormatters.put(MSystem.class, new StandardSystemFormatter());
            mFormatters.put(Resource.class, new StandardResourceFormatter());
            mFormatters.put(MClass.class, new StandardClassFormatter());
            mFormatters.put(Table.class, new StandardTableFormatter());
            mFormatters.put(Update.class, new StandardUpdateFormatter());
            mFormatters.put(UpdateType.class,
                             new StandardUpdateTypeFormatter());
            mFormatters.put(MObject.class, new StandardObjectFormatter());
            mFormatters.put(SearchHelp.class,
                             new StandardSearchHelpFormatter());
            mFormatters.put(EditMask.class, new StandardEditMaskFormatter());
            mFormatters.put(Lookup.class, new StandardLookupFormatter());
            mFormatters.put(UpdateHelp.class,
                             new StandardUpdateHelpFormatter());
            mFormatters.put(LookupType.class,
                             new StandardLookupTypeFormatter());
            mFormatters.put(ValidationLookup.class,
                             new StandardValidationLookupFormatter());
            mFormatters.put(ValidationLookupType.class,
                             new StandardValidationLookupTypeFormatter());
            mFormatters.put(ValidationExternal.class,
                             new StandardValidationExternalFormatter());
            mFormatters.put(ValidationExternalType.class,
                             new StandardValidationExternalTypeFormatter());
            mFormatters.put(ValidationExpression.class,
                             new StandardValidationExpressionFormatter());
//            mFormatters.put(ForeignKey.class,
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
        MetadataFormatter metadataFormatter = recursivelyLookupFormatter(clazz);
        return metadataFormatter;
    }

    public MetadataFormatter lookupFormatter(Collection metadataCollection)
    {
        Iterator i = metadataCollection.iterator();
        if (i.hasNext())
        {
            ServerMetadata metadata = (ServerMetadata)i.next();
            Class clazz = metadata.getClass();
            MetadataFormatter metadataFormatter = recursivelyLookupFormatter(clazz);
            return metadataFormatter;
        }
        else
        {
            return NULL_FORMATTER;
        }
    }

    /*
     * Searches for an appropriate formatter for the specified class. The
     * appropriate formatter is based off the specified class or one of its
     * super classes. This allows the appropriate formatter to be found when
     * metadata classes are subclassed.
     */
    private MetadataFormatter recursivelyLookupFormatter(Class clazz)
    {
        MetadataFormatter metadataFormatter = null;
        if (clazz != null) {
            metadataFormatter = (MetadataFormatter)mFormatters.get(clazz);
            if (metadataFormatter == null) {
                Class superClazz = clazz.getSuperclass();
                metadataFormatter = recursivelyLookupFormatter(superClazz);
            }
        }
        return metadataFormatter;
    }

    protected PrintWriter mOut;
    private Map mFormatters;
    private static final MetadataFormatter NULL_FORMATTER =
        new NullMetadataFormatter();
}
