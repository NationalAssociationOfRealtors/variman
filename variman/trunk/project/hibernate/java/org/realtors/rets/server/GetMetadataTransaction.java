/*
 */
package org.realtors.rets.server;

import java.io.PrintWriter;
import java.util.List;

import org.realtors.rets.server.metadata.MetadataSegment;
import org.realtors.rets.server.metadata.format.MetadataSegmentFormatter;

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
                                                     parameters.isRecursive());

        printOpenRetsSuccess(out);
        formatOutput(out, metadataObjects, parameters.getFormat());
        printCloseRets(out);
    }

    public void formatOutput(PrintWriter out, List metadataSegments,
                              int format)
    {
        LOG.debug("Formatting " + metadataSegments.size() + " segments");
        long start = System.currentTimeMillis();
        MetadataSegmentFormatter formatter =
            new MetadataSegmentFormatter(out, format);
        for (int i = 0; i < metadataSegments.size(); i++)
        {
            MetadataSegment metadataSegment =
                (MetadataSegment) metadataSegments.get(i);
            formatter.format(metadataSegment);
        }
        LOG.debug("Formatting done: " + (System.currentTimeMillis() - start));
    }

    private static final Logger LOG =
        Logger.getLogger(GetMetadataTransaction.class);
}
