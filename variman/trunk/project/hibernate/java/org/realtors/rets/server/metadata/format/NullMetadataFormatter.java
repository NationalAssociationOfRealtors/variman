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

import java.util.Collection;

class NullMetadataFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection data,
                       String[] levels)
    {
    }
}
