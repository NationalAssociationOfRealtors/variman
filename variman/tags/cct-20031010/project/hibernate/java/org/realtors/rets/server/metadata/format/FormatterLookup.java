/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.Collection;

public interface FormatterLookup
{
    public MetadataFormatter lookupFormatter(Collection metadataCollection);
}
