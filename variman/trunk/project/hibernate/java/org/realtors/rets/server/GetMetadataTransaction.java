/*
 */
package org.realtors.rets.server;

import java.io.PrintWriter;

import org.realtors.rets.server.metadata.MetadataSegment;
import org.realtors.rets.server.metadata.format.ClassFormatterLookup;
import org.realtors.rets.server.metadata.format.FormatterContext;
import org.realtors.rets.server.metadata.format.FormatterLookup;
import org.realtors.rets.server.metadata.format.MetadataFormatter;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

public class GetMetadataTransaction
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
        MetadataSegment segment = fetcher.fetchMetadata(parameters.getType(),
                                                        parameters.getIds());

        RetsUtils.printOpenRetsSuccess(out);
        if (parameters.getFormat() == MetadataFormatter.STANDARD)
        {
            out.println("<METADATA>");
        }
        FormatterLookup lookup =
            new ClassFormatterLookup(parameters.getFormat());
        FormatterContext context =
            new FormatterContext(segment.getVersion(), segment.getDate(),
                                 parameters.isRecursive(), out, lookup);
        StopWatch stopWatch = new StopWatch();
        LOG.debug("Formatting started");
        stopWatch.start();
        context.format(segment.getDataList(), segment.getLevels());
        stopWatch.stop();
        LOG.debug("Formatting done: " + stopWatch.getTime());
        if (parameters.getFormat() == MetadataFormatter.STANDARD)
        {
            out.println("</METADATA>");
        }
        RetsUtils.printCloseRets(out);
    }

    private static final Logger LOG =
        Logger.getLogger(GetMetadataTransaction.class);
}
