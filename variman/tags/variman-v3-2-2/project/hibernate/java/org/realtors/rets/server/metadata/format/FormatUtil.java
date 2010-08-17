/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public class FormatUtil
{

    /**
     * Takes toString() of each element in the collection and puts the values
     * into a list and then sorts the list.
     *
     * @param collection Collection to convert
     * @return sorted list of toString value foreach item in collection
     */
    public static List toSortedStringList(Collection collection)
    {
        List strings = new ArrayList(collection.size());
        for (Iterator iterator = collection.iterator(); iterator.hasNext();)
        {
            Object o = iterator.next();
            strings.add(o.toString());
        }
        Collections.sort(strings);
        return strings;
    }
}
