/*
 * Variman RETS Server
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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.realtors.rets.server.RetsServerException;

public class GetObjectParameters extends TransactionParameters
{
    public GetObjectParameters(Map parameterMap) throws RetsServerException
    {
        String resource = getParameter(parameterMap, "Resource");
        setResource(resource);
        
        String type = getParameter(parameterMap, "Type");
        setType(type);
        
        String id = getParameter(parameterMap, "ID");
        if (id == null)
        {
            id = getParameter(parameterMap, "Id");
        }
        initID(id);
        initLocation(getParameter(parameterMap, "Location"));
    }

    public GetObjectParameters(String resource, String type, String id)
            throws RetsServerException
    {
        this(resource, type, id, false);
    }

    public GetObjectParameters(String resource, String type, String id,
                               boolean useLocation)
            throws RetsServerException
    {
        setResource(resource);
        setType(type);
        initID(id);
        mUseLocation = useLocation;
    }

    private void initLocation(String locationString)
    {
        mUseLocation = (locationString != null) && locationString.equals("1");
    }

    private void initID(String id) throws RetsServerException
    {
        if (StringUtils.isBlank(id)) {
            throw new RetsServerException("Missing required ID parameter.");
        }
        LOG.debug("ID: " + id);
        mResourceSets = new ArrayList();
        String[] resourceSets = StringUtils.split(id, ",");
        if (resourceSets.length > 1)
        {
            mMultipartId = true;
        }
        else
        {
            mMultipartId = false;
        }
        for (int i = 0; i < resourceSets.length; i++)
        {
            String stringResourceSet = resourceSets[i];
            ResourceSet resourceSet = new ResourceSet();
            // Split resource-set into resource-entity and object-id-list
            String[] resourceSetParameter =
                StringUtils.split(stringResourceSet, ":", 2);
            resourceSet.setResourceEntity(resourceSetParameter[0]);
            
            if (resourceSetParameter.length == 1) {
                resourceSet.addObjectId("0");
                resourceSet.setDoGetPreferredObject(true);
            } else {
                // Split object-id-list into object-id
                String[] objectIds =
                    StringUtils.split(resourceSetParameter[1], ":");
                if ((objectIds.length > 1) || ArrayUtils.contains(objectIds, "*"))
                {
                    mMultipartId = true;
                }
                if (ArrayUtils.contains(objectIds, "0")) {
                    resourceSet.setDoGetPreferredObject(true);
                }
                resourceSet.addObjectIds(objectIds);
            }
            mResourceSets.add(resourceSet);
        }
    }

    public String getType()
    {
        return mType;
    }

    private void setType(String type) throws RetsServerException
    {
        if (StringUtils.isBlank(type)) {
            throw new RetsServerException("Missing required Type parameter.");
        }
        mType = type;
        LOG.debug("Type: " + mType);
    }

    public String getResource()
    {
        return mResource;
    }
    
    private void setResource(String resource) throws RetsServerException
    {
        if (StringUtils.isBlank(resource)) {
            throw new RetsServerException("Missing required Resource parameter.");
        }
        mResource = resource;
        LOG.debug("Resource:" + mResource);
    }

    public int numberOfResources()
    {
        return mResourceSets.size();
    }

    public boolean doGetPreferredObject(int resourceIndex)
    {
        ResourceSet resourceSet =
            (ResourceSet) mResourceSets.get(resourceIndex);
        return resourceSet.doGetPreferredObject();
    }

    public String getResourceEntity(int resourceIndex)
    {
        ResourceSet resourceSet =
            (ResourceSet) mResourceSets.get(resourceIndex);
        return resourceSet.getResourceEntity();
    }

    public List getObjectIdList(int resourceIndex)
    {
        ResourceSet resourceSet =
            (ResourceSet) mResourceSets.get(resourceIndex);
        return resourceSet.getObjectIds();
    }

    public boolean isMultipartId()
    {
        return mMultipartId;
    }

    public boolean getUseLocation()
    {
        return mUseLocation;
    }

    public void setUseLocation(boolean useLocation)
    {
        mUseLocation = useLocation;
    }

    private static class ResourceSet
    {
        public ResourceSet()
        {
            mObjectIds = new ArrayList/*String*/();
        }
        
        public boolean doGetPreferredObject()
        {
            return mDoGetPreferred;
        }

        public void setDoGetPreferredObject(boolean doGetPreferred)
        {
            mDoGetPreferred = doGetPreferred;
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

        public List/*String*/ getObjectIds()
        {
            return mObjectIds;
        }

        public void addObjectIds(String[] objectIds)
        {
            mObjectIds.addAll(Arrays.asList(objectIds));
        }

        private boolean mDoGetPreferred;
        private String mResourceEntity;
        private List/*String*/ mObjectIds;
    }

    private static final Logger LOG = 
        Logger.getLogger(GetObjectParameters.class);
    private String mType;
    private String mResource;
    private List mResourceSets;
    private boolean mMultipartId;
    private boolean mUseLocation;
}
