package org.realtors.rets.server.protocol;

import java.util.List;

import org.realtors.rets.server.RetsServerException;

/**
 * Created by IntelliJ IDEA.
 * User: dave
 * Date: Mar 7, 2005
 * Time: 4:00:19 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ObjectSet
{
    List findAllObjects(String type) throws RetsServerException;

    ObjectDescriptor findObject(String type, int objectId)
        throws RetsServerException;

    public static final int DEFAULT_OBJECT_ID = 0;
}
