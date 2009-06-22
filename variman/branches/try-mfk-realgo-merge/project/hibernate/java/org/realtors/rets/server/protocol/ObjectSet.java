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
    public List findAllObjects(String type) throws RetsServerException;

    public ObjectDescriptor findObject(String type, int objectId)
        throws RetsServerException;
    
    public void setResource(String resource);
    
    public void setResourceEntity(String resourceEntity);

    public static final int DEFAULT_OBJECT_ID = 0;
}
