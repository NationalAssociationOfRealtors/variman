/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server;

import java.io.PrintWriter;
import java.util.List;

import org.realtors.rets.server.metadata.MetadataSegment;
import org.realtors.rets.server.metadata.format.ClassFormatterLookup;
import org.realtors.rets.server.metadata.format.FormatterContext;
import org.realtors.rets.server.metadata.format.FormatterLookup;
import org.realtors.rets.server.metadata.format.MetadataFormatter;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

public class GetMetadataTransaction
{
    public GetMetadataTransaction(PrintWriter out,
                                  GetMetadataParameters parameters,
                                  MetadataFetcher fetcher)
    {
        mOut = out;
        mParameters = parameters;
        mFetcher = fetcher;
    }

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
        throws RetsServerException
    {
        // Fetch metadata before starting to print, so any exceptions can be
        // handled and reported to the user better.
        List segments = fetcher.fetchMetadata(parameters.getType(),
                                              parameters.getIds());

        printHeaders();
        printMetadata(segments);
        printFooters();
    }

    public void execute()
        throws RetsServerException
    {
        // Fetch metadata before starting to print, so any exceptions can be
        // handled and reported to the user better.
        List segments = mFetcher.fetchMetadata(mParameters.getType(),
                                               mParameters.getIds());

        printHeaders();
        printMetadata(segments);
        printFooters();
    }

    private void printHeaders()
    {
        RetsUtils.printXmlHeader(mOut);
        RetsUtils.printOpenRetsSuccess(mOut);
        if (mParameters.getFormat() == MetadataFormatter.STANDARD)
        {
            mOut.println("<METADATA>");
        }
    }

    private void printMetadata(List segments)
    {
        FormatterLookup lookup =
            new ClassFormatterLookup(mParameters.getFormat());
        StopWatch stopWatch = new StopWatch();
        LOG.debug("Formatting started");
        stopWatch.start();
        for (int i = 0; i < segments.size(); i++)
        {
            MetadataSegment segment = (MetadataSegment) segments.get(i);
            FormatterContext context =
                new FormatterContext(segment.getVersion(), segment.getDate(),
                                     mParameters.isRecursive(), mOut, lookup);
            context.format(segment.getDataList(), segment.getLevels());
        }
        stopWatch.stop();
        LOG.debug("Formatting done: " + stopWatch.getTime());
    }

    private void printFooters()
    {
        if (mParameters.getFormat() == MetadataFormatter.STANDARD)
        {
            mOut.println("</METADATA>");
        }
        RetsUtils.printCloseRets(mOut);
    }

    private static final Logger LOG =
        Logger.getLogger(GetMetadataTransaction.class);
    private PrintWriter mOut;
    private GetMetadataParameters mParameters;
    private MetadataFetcher mFetcher;
}
