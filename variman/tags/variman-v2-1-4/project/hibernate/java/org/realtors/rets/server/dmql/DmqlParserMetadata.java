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
     * Returns <code>true</code> if the field name is valid
     *
     * @param fieldName The field name to validate
     * @return <code>true</code> if the field name is valid
     */
    public boolean isValidFieldName(String fieldName);

    /**
     * Returns <code>true</code> if the lookup name is a valid lookup name.
     *
     * @param fieldName The lookup name to validate
     * @return <code>true</code> if the lookup name is valid
     */
    public boolean isLookupField(String fieldName);

    /**
     * Returns <code>true</code> if the lookup value is valid for the lookups
     * in lookup name.  The lookup name must be valid.
     *
     * @param lookupName The lookup name to use for the specified lookup
     * @param lookupValue The value to validate
     * @return <code>true</code> if the lookup value is valid
     */
    public boolean isValidLookupValue(String lookupName, String lookupValue);

    /**
     * Returns <code>true</code> if the field name is a character type.
     *
     * @param fieldName A field name
     * @return <code>true</code> if the field name is a character type
     */
    public boolean isCharacterField(String fieldName);

    /**
     * Returns <code>true</code> if the field name is a numeric type (tiny,
     * small, int, long, or decimal).
     *
     * @param fieldName A field name
     * @return <code>true</code> if the field name is a numeric type
     */
    public boolean isNumericField(String fieldName);

    public String getLookupLongValue(String lookupName, String value);

    public String getLookupShortValue(String lookupName, String value);

    public String getLookupDbValue(String lookupName, String lookupValue);

    public String fieldToColumn(String fieldName);

    public String columnToField(String columnName);

    public List getAllColumns();

    public Table getTable(String fieldName);
}
