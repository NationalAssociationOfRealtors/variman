/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.protocol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class GetObjectParameters extends TransactionParameters
{
    public GetObjectParameters(Map parameterMap)
    {
        mResource = getParameter(parameterMap, "Resource");
        LOG.debug("Resource:" + mResource);
        mType = getParameter(parameterMap, "Type");
        LOG.debug("Type: " + mType);
        String id = getParameter(parameterMap, "ID");
        if (id == null)
        {
            id = getParameter(parameterMap, "Id");
        }
        initID(id);
    }

    public GetObjectParameters(String resource, String type, String id)
    {
        mResource = resource;
        mType = type;
        initID(id);
    }

    private void initID(String id)
    {
        LOG.debug("ID: " + id);    
        mResourceSets = new ArrayList();
        ResourceSet resourceSet = new ResourceSet();
        // Split resource-set into resource-entity and object-id-list
        String[] resourceSetParameter = StringUtils.split(id, ":", 2);
        resourceSet.setResourceEntity(resourceSetParameter[0]);
        // Split object-id-list into object-id
        String[] objectIds = StringUtils.split(resourceSetParameter[1], ":");
        resourceSet.addObjectIds(objectIds);
        mResourceSets.add(resourceSet);
    }

    public String getType()
    {
        return mType;
    }

    public String getResource()
    {
        return mResource;
    }

    public int numberOfResources()
    {
        return mResourceSets.size();
    }

    public String getResourceEntity(int resourceIndex)
    {
        ResourceSet resourceSet = (ResourceSet) mResourceSets.get(0);
        return resourceSet.getResourceEntity();
    }

    public List getObjectIdList(int resourceIndex)
    {
        ResourceSet resourceSet = (ResourceSet) mResourceSets.get(0);
        return resourceSet.getObjectIds();
    }

    private static class ResourceSet
    {
        public ResourceSet()
        {
            mObjectIds = new ArrayList();
        }

        public String getResourceEntity()
        {
            return mResourceEntity;
        }

        public void setResourceEntity(String resourceEntity)
        {
            mResourceEntity = resourceEntity;
        }

        public void addObjectId(String objectId)
        {
            mObjectIds.add(objectId);
        }

        public List getObjectIds()
        {
            return mObjectIds;
        }

        public void addObjectIds(String[] objectIds)
        {
            mObjectIds.addAll(Arrays.asList(objectIds));
        }

        private String mResourceEntity;
        private List mObjectIds;
    }

    private static final Logger LOG =
        Logger.getLogger(GetObjectParameters.class);
    private String mType;
    private String mResource;
    private List mResourceSets;
}
