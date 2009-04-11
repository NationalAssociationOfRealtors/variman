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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Date;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.MetadataSegment;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

public class HibernateMetadataFetcher implements MetadataFetcher
{
    static
    {
        initFinders();
    }

    public HibernateMetadataFetcher(SessionFactory sessions)
    {
        mSessions = sessions;
    }

    private static void initFinders()
    {
        sMetadataFinders = new HashMap();
        sMetadataFinders.put("SYSTEM",
                              new MetadataFinder("MSystem", 0));
        sMetadataFinders.put("RESOURCE",
                              new MetadataFinder("Resource", 0));
        sMetadataFinders.put("CLASS",
                              new MetadataFinder("MClass", 1));
        sMetadataFinders.put("TABLE",
                             new MetadataFinder("Table", 2));
        sMetadataFinders.put("UPDATE",
                             new MetadataFinder("Update", 2));
        sMetadataFinders.put("UPDATE_TYPE",
                             new MetadataFinder("UpdateType", 3));
        sMetadataFinders.put("OBJECT",
                             new MetadataFinder("MObject", 1));
        sMetadataFinders.put("SEARCH_HELP",
                             new MetadataFinder("SearchHelp", 1));
        sMetadataFinders.put("EDIT_MASK",
                             new MetadataFinder("EditMask", 1));
        sMetadataFinders.put("LOOKUP",
                             new MetadataFinder("Lookup", 1));
        sMetadataFinders.put("LOOKUP_TYPE",
                             new MetadataFinder("LookupType", 2));
        sMetadataFinders.put("VALIDATION_LOOKUP",
                             new MetadataFinder("ValidationLookup", 1));
        sMetadataFinders.put("VALIDATION_LOOKUP_TYPE",
                             new MetadataFinder("ValidatoinLookupType", 2));
        sMetadataFinders.put("VALIDATION_EXTERNAL",
                             new MetadataFinder("ValidationExternal", 1));
        sMetadataFinders.put("VALIDATION_EXTERNAL_TYPE",
                             new MetadataFinder("ValidationExternalType", 2));
        sMetadataFinders.put("VALIDATION_EXPRESSION",
                             new MetadataFinder("ValidationExpression", 1));
    }

    public MetadataSegment fetchMetadataOld(String type, String[] levels)
        throws RetsReplyException
    {
        // Always need system to get version and date
        MSystem system = findSystemFromHibernate();

        MetadataFinder finder = (MetadataFinder) sMetadataFinders.get(type);
        if (finder != null)
        {
            StopWatch stopWatch = new StopWatch();
            LOG.debug("Using finder for type: " + type);
            stopWatch.start();
            List metadata = finder.findMetadata(levels, mSessions);
            stopWatch.stop();
            LOG.debug("End finder: " + stopWatch.getTime());
            return new MetadataSegment(metadata, levels,
                                       system.getVersionString(),
                                       system.getDate());
        }
        else
        {
            LOG.warn("Recieved query for unknown metadataResults type: " +
                     type + ", level=" + StringUtils.join(levels, ":"));
            throw new RetsReplyException(ReplyCode.INVALID_TYPE, type);
        }
    }

    public List fetchMetadata(String type, String[] levels)
    {
        throw new UnsupportedOperationException("not implemented");
    }

    public String getSystemVersion() throws RetsServerException
    {
        throw new UnsupportedOperationException("not implemented");
    }

    public Date getSystemDate() throws RetsServerException
    {
        throw new UnsupportedOperationException("not implemented");
    }

    private MSystem findSystemFromHibernate()
    {
        SessionHelper helper = new SessionHelper(mSessions);
        MSystem system = null;
        try
        {
            Session session = helper.beginTransaction();
            List results = new ArrayList();
            Iterator iterator = session.iterate("from MSystem");
            while (iterator.hasNext())
            {
                results.add(iterator.next());
            }
            if (results.size() == 1)
            {
                system = (MSystem) results.get(0);
            }
        }
        catch (HibernateException e)
        {
            LOG.warn("Caught", e);
            helper.rollback(LOG);
        }
        finally
        {
            helper.close(LOG);
        }
        return system;
    }

    private static final Logger LOG = 
        Logger.getLogger(HibernateMetadataFetcher.class);
    private static Map sMetadataFinders;
    private SessionFactory mSessions;
}
