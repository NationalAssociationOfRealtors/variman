/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server.metadata.format;

import java.util.Collection;

import org.realtors.rets.common.metadata.MetaObject;

public interface FormatterLookup
{
    public MetadataFormatter lookupFormatter(Collection<MetaObject> metadataCollection);
}
