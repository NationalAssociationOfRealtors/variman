/*
 */
package org.realtors.rets.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.MetadataSegment;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class HibernateMetadataFetcher extends BaseMetadataFetcher
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

    public List fetchMetadata(String type, String[] levels, boolean recursive)
        throws RetsReplyException
    {
        // Always need system to get version and date
        MSystem system = findSystemFromHibernate();
        String version = system.getVersionString();
        Date date = system.getDate();
        List metadataResults = new ArrayList();

        MetadataFinder finder =
            (MetadataFinder) sMetadataFinders.get(type);
        if (finder != null)
        {
            LOG.debug("Using finder for type: " + type);
            long start = System.currentTimeMillis();
            List metadata = finder.findMetadata(levels, mSessions);
            LOG.debug("End finder: " + (System.currentTimeMillis() - start));
            metadataResults.add(new MetadataSegment(metadata, levels, version,
                                                    date));
            if (recursive)
            {
                recurseChildren(metadata,  metadataResults, version, date);
            }
        }
        else
        {
            LOG.warn("Recieved query for unknown metadataResults type: " +
                     type + ", level=" + StringUtils.join(levels, ":"));
            throw new RetsReplyException(20501, "Invalid Type");
        }
        return metadataResults;
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
