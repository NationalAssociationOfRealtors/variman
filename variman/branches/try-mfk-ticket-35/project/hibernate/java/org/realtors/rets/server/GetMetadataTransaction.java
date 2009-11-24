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
import java.util.SortedSet;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

import org.realtors.rets.client.RetsVersion;

import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.metadata.MetadataSegment;
import org.realtors.rets.server.metadata.format.ClassFormatterLookup;
import org.realtors.rets.server.metadata.format.FormatterLookup;
import org.realtors.rets.server.metadata.format.MetadataFormatter;
import org.realtors.rets.server.metadata.format.MutableFormatterContext;
import org.hibernate.HibernateException;

public class GetMetadataTransaction
{
    public GetMetadataTransaction(PrintWriter out,
                                  GetMetadataParameters parameters,
                                  MetadataManager metadataManager,
                                  RetsVersion retsVersion)
    {
        mOut = out;
        mParameters = parameters;
        mMetadataManager = metadataManager;
        mRetsVersion = retsVersion;
    }

    public void execute()
        throws RetsServerException
    {
        // Fetch metadata before starting to print, so any exceptions can be
        // handled and reported to the user better.
        String type = mParameters.getType();
        String[] ids = mParameters.getIds();
        List<MetadataSegment> segments = mMetadataManager.fetchMetadata(type, ids);

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

    private void printMetadata(List<MetadataSegment> segments) throws RetsServerException
    {
        try
        {
            FormatterLookup lookup =
                new ClassFormatterLookup(mParameters.getFormat());
            StopWatch stopWatch = new StopWatch();
            LOG.debug("Formatting started");
            stopWatch.start();
            MutableFormatterContext context = new MutableFormatterContext();
            context.setRecursive(mParameters.isRecursive());
            context.setWriter(mOut);
            context.setLookup(lookup);
            User user = mParameters.getUser();
            SortedSet groups = UserUtils.getGroups(user);
            context.setTableFilter(RetsServer.getTableGroupFilter(),
                                   groups);
            for (int i = 0; i < segments.size(); i++)
            {
                MetadataSegment segment = segments.get(i);
                context.setVersion(segment.getVersion());
                context.setRetsVersion(mRetsVersion);
                context.setDate(segment.getDate());
                context.format(segment.getDataList(), segment.getLevels());
            }
            stopWatch.stop();
            LOG.debug("Formatting done: " + stopWatch.getTime());
        }
        catch (HibernateException e)
        {
            throw new RetsServerException(e);
        }
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
    private MetadataManager mMetadataManager;
    private RetsVersion mRetsVersion;
}
