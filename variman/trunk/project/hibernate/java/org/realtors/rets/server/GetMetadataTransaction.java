/*
 */
package org.realtors.rets.server;

import java.io.PrintWriter;
import java.util.List;

import org.realtors.rets.server.metadata.MetadataSegment;
import org.realtors.rets.server.metadata.format.MetadataSegmentFormatter;
import org.realtors.rets.server.metadata.format.FormatterContext;

import org.apache.log4j.Logger;

public class GetMetadataTransaction extends RetsTransaction
{
    /**
     * Gets metadata and formats it. Only throws an exception if an error
     * occured while getting metadata. This guarantees the print writer is
     * clear from output if an error occurs.
     *
     * @param out Formats output to this writer.
     * @param parameters Get metadata parameters.
     * @param fetcher Used to get the metadata.
     * @throws RetsReplyException If an error occured while getting the
     * metadata.
     */
    public void execute(PrintWriter out, GetMetadataParameters parameters,
                        MetadataFetcher fetcher)
        throws RetsReplyException
    {
        List metadataObjects = fetcher.fetchMetadata(parameters.getType(),
                                                     parameters.getIds(),
                                                     false);

        printOpenRetsSuccess(out);
        MetadataSegmentFormatter formatter =
            new MetadataSegmentFormatter(parameters.getFormat());
        MetadataSegment segment = (MetadataSegment) metadataObjects.get(0);
        FormatterContext context =
            new FormatterContext(segment.getVersion(), segment.getDate(),
                                 parameters.isRecursive(), out, formatter);
        LOG.debug("Formatting started");
        context.format(segment.getDataList(), segment.getLevels());
        LOG.debug("Formatting done");
        printCloseRets(out);
    }

    private static final Logger LOG =
        Logger.getLogger(GetMetadataTransaction.class);
}
