/*
 * Created on Aug 25, 2003
 *
 */
package org.realtors.rets.server.importer;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import org.realtors.rets.client.GetMetadataResponse;
import org.realtors.rets.client.Metadata;
import org.realtors.rets.client.MetadataSegment;
import org.realtors.rets.client.MetadataTable;
import org.realtors.rets.client.MetadataTableBuilder;
import org.realtors.rets.client.MetadataTables;
import org.realtors.rets.client.RetsException;
import org.realtors.rets.client.RetsSession;

import org.realtors.rets.server.metadata.TableStandardName;

/**
 * @author kgarner
 */
public abstract class MetadataHelpers
{
    public MetadataHelpers()
        throws RetsException
    {
        mResources = new HashMap();
        mClasses = new HashMap();
        mEditMasks = new HashMap();
        mTables = new HashMap();
        mLookups = new HashMap();
        mSearchHelps = new HashMap();
        mValidationExternals = new HashMap();
        mValidationExpressions = new HashMap();
        mValidationLookups = new HashMap();
        mUpdates = new HashMap();
        mUpdateHelps = new HashMap();
        mTableStandardNames = new HashMap();
        mMetadataTables =
           MetadataTableBuilder.buildFromXml(
               RetsSession.getResource(RetsSession.METADATA_TABLES));
   }
    
    protected void loadMetadataTables(InputStream in)
    {
        GetMetadataResponse response;
        try
        {
            response = new GetMetadataResponse(in);
        }
        catch (RetsException e)
        {
            LOG.error(e);
            throw new RuntimeException(e);
        }
        MetadataSegment[] segments = response.getMetadataSegments();
        for (int i = 0; i < segments.length; i++)
        {
            MetadataSegment segment = segments[i];
            String name = segment.getName();
            MetadataTable table = mMetadataTables.getTable(name);
            try
            {
                RetsSession.assertTableNotNull(name, table);
            }
            catch (RetsException e1)
            {
                LOG.error(e1);
                throw new RuntimeException(e1);
            }
            String path =
                RetsSession.pathFromAttributes(segment,
                                               table.getPathAttributes());
    
            List columns = segment.getColumns();
            List dataRows = segment.getData();
            if (dataRows == null)
            {
                continue;
            }
            for (Iterator j = dataRows.iterator(); j.hasNext();)
            {
                List dataRow = (List) j.next();
                Map dataMap = RetsSession.mapFromLists(columns, dataRow);
                if (dataMap == null)
                {
                    LOG.warn("Null return: name=" + name + ", path=" + path);
                }
                Metadata metadata =
                    new Metadata(dataMap, table.getIdColumn(), path);
                table.addMetadata(metadata);
            }
        }
    }

    protected TableStandardName lookupTableStandardName(String standardName)
    {
        return (TableStandardName) mTableStandardNames.get(standardName);
    }

    private static final Logger LOG = Logger.getLogger(MetadataLoader.class);

    protected MetadataTables mMetadataTables;

    protected Map mClasses;

    protected Map mEditMasks;

    protected Map mLookups;

    protected Map mResources;

    protected Map mSearchHelps;

    protected Map mTables;

    protected Map mUpdateHelps;

    protected Map mUpdates;

    protected Map mValidationExpressions;

    protected Map mValidationExternals;

    protected Map mValidationLookups;
    
    protected Map mTableStandardNames;
}
