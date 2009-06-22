/*
 * Variman RETS Server
 *
 * Author: Dave Dribin and modified by Danny Hurlburt
 * Copyright (c) 2007, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.protocol;

import java.util.List;

import org.realtors.rets.server.dmql.DmqlParserMetadata;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.RetsServerException;

/**
 * A search sql builder generates SQL statements for search transaction.
 */
public interface SearchSqlBuilder
{
    public String getResourceId();
    
    public void setResourceId(String resourceId);
    
    public String getClassName();
    
    public void setClassName(String className);
    
    public List/*String*/ getSelectList();
    
    public void setSelectList(List/*String*/ selectList);
    
    public DmqlQuery getDmqlQuery();
    
    public void setDmqlQuery(DmqlQuery dmqlQuery);
    
    public List/*DmqlQuery*/ getDmqlConstraints();
    
    public void setDmqlConstraints(List/*DmqlQuery*/ dmqlConstraints);
    
    public List/*String*/ getSqlConstraints();
    
    public void setSqlConstraints(List/*String*/ sqlConstraints);
    
    public Integer getLimit();
    
    public void setLimit(Integer limit);
    
    public MetadataManager getMetadataManager();
    
    public void setMetadataManager(MetadataManager metadataManager);
    
    public SqlStatements createSqlStatements() throws RetsServerException;
    
    public static interface SqlStatements
    {
        public Query getCountQuery();
        
        public SearchQuery getSearchQuery();
        
        public static interface Query
        {
            public String getSql();
        }
        
        public static interface SearchQuery extends Query
        {
            public List/*String*/ getSelectedColumnNames();
            
            public DmqlParserMetadata getDmqlParserMetadata();
        }
        
    }
    
}
