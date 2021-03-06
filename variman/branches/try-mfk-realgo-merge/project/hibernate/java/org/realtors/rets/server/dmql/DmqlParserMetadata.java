/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.dmql;

import java.util.List;

import org.realtors.rets.server.metadata.Table;

/**
 * The DMQL parser's interface to the RETS metadata.
 */
public interface DmqlParserMetadata
{
    /**
     * Returns <code>true</code> if the field name is valid.
     *
     * @param fieldName The system name or standard name to validate. Must not
     *        be <code>null</code>.
     * @return <code>true</code> if the field name is valid, otherwise
     *         <code>false</code>.
     */
    public boolean isValidFieldName(String fieldName);

    /**
     * Returns the type of the field, or <code>null</code> if the field
     * does not exist.
     *
     * @param fieldName The system name or standard name.
     * @return The type for the field 
     */
    public DmqlFieldType getFieldType(String fieldName);

    /**
     * Returns <code>true</code> if the lookup value is valid for the lookups
     * in lookup name.  The lookup name must be valid.
     *
     * @param lookupName The lookup name to use for the specified lookup
     * @param lookupValue The value to validate
     * @return <code>true</code> if the lookup value is valid
     */
    public boolean isValidLookupValue(String lookupName, String lookupValue);

    public String getLookupLongValue(String lookupName, String lookupValue);

    public String getLookupShortValue(String lookupName, String lookupValue);

    public String getLookupDbValue(String lookupName, String lookupValue);
    
    /**
     * Returns the database column name where the specified field name's value
     * is stored.
     * 
     * @param fieldName The system name or standard name.
     * @return The database column name where the specified field name's value
     *         is stored.
     */
    public String fieldToColumn(String fieldName);
    
    /**
     * Returns the system name or standard name corresponding to the specified
     * column name.
     * 
     * @param columnName The database column name.
     * @return The system name or standard name corresponding to the specified
     *         column name.
     */
    public String columnToField(String columnName);
    
    /**
     * Returns all database column names known by this DmqlParserMetadata.
     * 
     * @return all database column names known by this DmqlParserMetadata.
     */
    public List/*String*/ getAllColumns();
    
    /**
     * Returns the RETS metadata table object identified by the specified
     * field name.
     * 
     * @param fieldName The system name or standard name.
     * @return The RETS metadata table object identified by the specified
     *         field name.
     */
    public Table getTable(String fieldName);
    
}
