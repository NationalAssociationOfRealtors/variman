/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2004,2009 The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.metadata;

import org.realtors.rets.common.metadata.types.MTable;

/**
 * A set of metadata related utility methods.
 */
public class MetadataUtils {

    /**
     * Extracts a {@link DataTypeEnum} from the specified metadata table.
     * 
     * @param table The metadata table from which to extract the data type enum.
     *         May be {@code null}.
     * @return The data type enum if found, otherwise {@code null}.
     */
    public static DataTypeEnum getDataTypeEnum(MTable table) {
        if (table == null) {
            return null;
        }
        String dataType = table.getDataType();
        DataTypeEnum dataTypeEnum = DataTypeEnum.fromString(dataType);
        return dataTypeEnum;
    }

    /**
     * Extracts a {@link InterpretationEnum} from the specified metadata table.
     * 
     * @param table The metadata table from which to extract the interpretation
     *         enum. May be {@code null}.
     * @return The interpretation enum if found, otherwise {@code null}.
     */
    public static InterpretationEnum getInterpretationEnum(MTable table) {
        if (table == null) {
            return null;
        }
        String interpretation = table.getInterpretation();
        InterpretationEnum interpretationEnum = null;
        if (interpretation != null) {
            interpretationEnum = InterpretationEnum.fromString(interpretation);
        }
        return interpretationEnum;
    }

    /**
     * Extracts a {@link UnitEnum} from the specified metadata table.
     * 
     * @param table The metadata table from which to extract the unit enum.
     *         May be {@code null}.
     * @return The unit enum if found, otherwise {@code null}.
     */
    public static UnitEnum getUnitEnum(MTable table) {
        if (table == null) {
            return null;
        }
        String units = table.getUnits();
        if (units == null)
            return null;
        UnitEnum unitEnum = UnitEnum.fromString(units);
        return unitEnum;
    }

}
