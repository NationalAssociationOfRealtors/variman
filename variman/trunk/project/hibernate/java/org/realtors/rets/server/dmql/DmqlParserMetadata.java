/*
 */
package org.realtors.rets.server.dmql;

import java.util.Collection;

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
     * @param lookupName The lookup name to validate
     * @return <code>true</code> if the lookup name is valid
     */
    public boolean isValidLookupName(String lookupName);

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
     * Returns <code>true</code> if the field name is a valid string name.
     *
     * @param fieldName The field name to validate
     * @return <code>true</code> if the field name is a valid string name
     */
    public boolean isValidStringName(String fieldName);

    String getLookupDbValue(String lookupName, String lookupValue);

    String fieldToColumn(String fieldName);

    String columnToField(String columnName);

    Collection getAllColumns();
}
