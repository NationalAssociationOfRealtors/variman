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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.ReplyCode;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.RetsServerException;

public class GetObjectTransaction
{
    public GetObjectTransaction(String resource, String type)
    {
        mParameters = null;
        mResource = resource;
        mType = type;
        init();
    }

    public GetObjectTransaction(GetObjectParameters parameters)
    {
        mParameters = parameters;
        mResource = mParameters.getResource();
        mType = mParameters.getType();
        init();
    }

    private void init()
    {
        mBoundaryGenerator = DEFAULT_BOUNDARY_GENERATOR;
        mBaseLocationUrl = "http://images.invalid/";
        mBlockLocation = false;
        mWildObject = false;
    }

    public void setRootDirectory(String rootDirectory)
    {
        mRootDirectory = rootDirectory;
    }

    public void setPhotoPattern(String photoPattern)
    {
        mPhotoPattern = photoPattern;
    }

    public void setBoundaryGenerator(
        MultipartBoundaryGenerator boundaryGenerator)
    {
        mBoundaryGenerator = boundaryGenerator;
    }

    public void execute(GetObjectResponse response)
        throws RetsServerException
    {
        try
        {
            List allObjects = findAllObjectDescriptors();
            if (allObjects.size() == 0)
            {
                throw new RetsReplyException(ReplyCode.NO_OBJECT_FOUND);
            }
            else if (!mParameters.isMultipartId())
            {
                ObjectDescriptor objectDescriptor =
                    (ObjectDescriptor) allObjects.get(0);
                /*
                 * Check the ReplyCode. If not SUCCESSFUL, throw an exception.
                 */
                if (objectDescriptor.getRetsReplyCode() != ReplyCode.SUCCESSFUL)
                	throw new RetsReplyException(objectDescriptor.getRetsReplyCode());
                
                executeSinglePart(response, objectDescriptor);
            }
            else
            {
                executeMultipart(response, allObjects);
            }
        }
        catch (FileNotFoundException e)
        {
            throw new RetsReplyException(ReplyCode.OBJECT_UNAVAILABLE);
        }
        catch (IOException e)
        {
            throw new RetsServerException(e);
        }
    }

    private void executeSinglePart(GetObjectResponse response,
                                   ObjectDescriptor objectDescriptor)
        throws IOException
    {
        LOG.info("GetObject URL: " + objectDescriptor.getUrl());
        response.setHeader("MIME-Version", "1.0");
        response.setHeader("Content-ID", objectDescriptor.getObjectKey());
        String description = objectDescriptor.getDescription();
        if (description != null)
        {
            response.setHeader("Content-Description", description);
        }
        response.setHeader("Object-ID", "" + objectDescriptor.getObjectId());
        if (useLocation())
        {
            response.setHeader("Location", getLocationUrl(objectDescriptor));
            response.setContentType("application/octet-stream");
        }
        else
        {
            ObjectStream stream = objectDescriptor.openObjectStream();
            response.setContentType(stream.getMimeType());
            IOUtils.copyStream(stream.getInputStream(),
                               response.getOutputStream());
        }
    }

    private void executeMultipart(GetObjectResponse response, List allObjects)
        throws IOException
    {
        String boundary = mBoundaryGenerator.generateBoundary();
        LOG.debug("Multipart boundary: " + boundary);
        response.setContentType("multipart/parallel; " +
                                "boundary=\"" + boundary +"\"");
        response.setHeader("MIME-Version", "1.0");
        DataOutputStream out = new DataOutputStream(response.getOutputStream());

        for (int i = 0; i < allObjects.size(); i++)
        {
            ObjectDescriptor objectDescriptor =
                (ObjectDescriptor) allObjects.get(i);
            LOG.info("GetObject (mulitpart) URL: " + objectDescriptor.getUrl() + 
                ", objectKey: " + objectDescriptor.getObjectKey());
            out.writeBytes(CRLF + "--" + boundary + CRLF);
            out.writeBytes("Content-ID: " + objectDescriptor.getObjectKey() +
                           CRLF);
            String description = objectDescriptor.getDescription();
            if (description != null)
            {
                out.writeBytes("Content-Description: " + description + CRLF);
            }
            if (objectDescriptor.getRetsReplyCode() != ReplyCode.SUCCESSFUL &&
                    allObjects.size() == 1 && mWildObject)
            {
                out.writeBytes("Object-ID: *" + CRLF);
            }
            else
            {
                out.writeBytes("Object-ID: " + objectDescriptor.getObjectId() +
                           CRLF);
            }
            if (useLocation())
            {
                out.writeBytes("Content-Type: text/xml" + CRLF);
            	if (objectDescriptor.getRetsReplyCode() == ReplyCode.SUCCESSFUL)
            	{
            		out.writeBytes("Location: " + getLocationUrl(objectDescriptor) +
                               CRLF + CRLF);
            		out.writeBytes("<RETS ReplyCode=\"0\" ReplyText=\"Operation Successful\"/>" +
            						CRLF);
            	}
            	else
            	{
            		out.writeBytes("Location: " + CRLF + CRLF);
            		out.writeBytes("<RETS ReplyCode=\"20403\" ReplyText=\"No Object Found\"/>" +
            							CRLF);
            	}
            }
            else
            {
                /*
                 * Fetch the object and output it
                 */
                try
                {
                    ObjectStream stream = objectDescriptor.openObjectStream();
                    out.writeBytes("Content-Type: " + stream.getMimeType() + CRLF);
                    out.writeBytes(CRLF);
                    IOUtils.copyStream(stream.getInputStream(), out);
                }
                catch (IOException e)
                {
                    /*
                     * Log the error and masquerade as No Object Found
                     */
                    LOG.error(e);
                    out.writeBytes("Content-Type: text/xml" + CRLF + CRLF);
                    out.writeBytes("<RETS ReplyCode=\"20403\" ReplyText=\"No Object Found\"/>" +
                                        CRLF);
                }
            }
        }
        out.writeBytes(CRLF + "--" + boundary + "--" + CRLF);
        out.close();
    }

    private boolean useLocation()
    {
        return (!mBlockLocation) && mParameters.getUseLocation();
    }

    private String getLocationUrl(ObjectDescriptor objectDescriptor)
    {
        StringBuffer location = new StringBuffer();
        location.append(mBaseLocationUrl);
        location.append(mResource).append("/");
        location.append(mType).append("/");
        return objectDescriptor.getLocationUrl(location.toString());
    }

    private List /* ObjectDescriptor */ findAllObjectDescriptors()
        throws RetsServerException
    {
        List objects = new ArrayList();
        int numberOfResources = mParameters.numberOfResources();
        for (int i = 0; i < numberOfResources; i++)
        {
            String resourceEntity = mParameters.getResourceEntity(i);
            List objectIdList = mParameters.getObjectIdList(i);
            ObjectSet objectSet = getObjectSet(resourceEntity);
            for (int j = 0; j < objectIdList.size(); j++)
            {
                String objectIdString = (String) objectIdList.get(j);
                if (objectIdString.equals("*"))
                {
                    mWildObject = true;
                    objects.addAll(objectSet.findAllObjects(mType));
                }
                else
                {
                    int objectId = NumberUtils.toInt(objectIdString);
                    ObjectDescriptor object =
                        objectSet.findObject(mType, objectId);
                    if (object != null)
                    {
                        objects.add(object);
                    }
                }
            }
        }
        return objects;
    }
    
    public ObjectDescriptor findObjectDescriptor(String resourceEntity,
                                                 int objectId)
        throws RetsServerException
    {
        ObjectSet objectSet = getObjectSet(resourceEntity);
        return objectSet.findObject(mType, objectId);
    }

    /**
     * Get the correct object set for this resource entity.  First, check
     * for an XML object set.  If that does not exist, try an image
     * pattern object set.  If no image pattern was set, then return
     * a null object set that always returns no object descriptors.
     *
     * @param resourceEntity
     * @return
     * @throws RetsServerException
     */
    private ObjectSet getObjectSet(String resourceEntity)
        throws RetsServerException
    {
        ObjectSet objectSet = RetsServer.createCustomObjectSet();
        if (objectSet == null)
        {
            objectSet = getXmlObjectSet(resourceEntity);
            if (objectSet == null)
            {
                objectSet = getPatternObjectSet(resourceEntity);
                if (objectSet == null)
                {
                    objectSet = NULL_OBJECT_SET;
                }
            }
        }
        
        objectSet.setResource(mResource);
        objectSet.setResourceEntity(resourceEntity);
        return objectSet;
    }

    /**
     * Return an XmlObjectSet if, and only if, the object set pattern is
     * not blank and the resulting XML file actually exists.
     *
     * @param resourceEntity
     * @return
     * @throws RetsServerException
     */
    private ObjectSet getXmlObjectSet(String resourceEntity)
        throws RetsServerException
    {
        if (StringUtils.isBlank(mObjectSetPattern) ||
            StringUtils.isBlank(mRootDirectory))
        {
            LOG.debug("No XML object set available");
            return null;
        }

        GetObjectPatternParser patternParser =
            new GetObjectPatternParser(mObjectSetPattern);
        GetObjectPatternFormatter formatter = patternParser.parse();
        GetObjectPatternContext patternContext =
            new GetObjectPatternContext(resourceEntity, -1);
        StringBuffer buffer = new StringBuffer(mRootDirectory);
        buffer.append(File.separator);
        formatter.format(buffer, patternContext);
        File xmlObjectFile = new File(buffer.toString());
        if (xmlObjectFile.exists())
        {
            LOG.debug("Using object set file: " + xmlObjectFile);
            return new XmlObjectSet(xmlObjectFile);
        }
        else
        {
            LOG.debug("Object set file does not exist: " + xmlObjectFile);
            return null;
        }
    }

    private ObjectSet getPatternObjectSet(String resourceEntity)
    {
        if (StringUtils.isBlank(mPhotoPattern) ||
            StringUtils.isBlank(mRootDirectory))
        {
            LOG.debug("No pattern object set available");
            return null;
        }

        return new PatternObjectSet(mRootDirectory, mPhotoPattern,
                                    resourceEntity);
    }

    public void setBaseLocationUrl(String baseLocationUrl)
    {
        mBaseLocationUrl = baseLocationUrl;
    }

    public void setBlockLocation(boolean blockLocation)
    {
        mBlockLocation = blockLocation;
    }

    public void setObjectSetPattern(String objectSetPattern)
    {
        mObjectSetPattern = objectSetPattern;
    }

    private static class BoundaryGenerator implements MultipartBoundaryGenerator
    {
        public String generateBoundary()
        {
            return DigestUtils.shaHex("" + System.currentTimeMillis());
        }
    }

    private static class NullObjectset implements ObjectSet
    {
        public void setResource(String resource)
        {
        }
        
        public void setResourceEntity(String resourceEntity)
        {
        }

        public List findAllObjects(String type) throws RetsServerException
        {
            return Collections.EMPTY_LIST;
        }

        public ObjectDescriptor findObject(String type, int objectId)
            throws RetsServerException
        {
            return null;
        }
    }

    private static final Logger LOG =
        Logger.getLogger(GetObjectTransaction.class);
    private static final String CRLF = "\r\n";
    public static final MultipartBoundaryGenerator DEFAULT_BOUNDARY_GENERATOR =
        new BoundaryGenerator();
    private static final ObjectSet NULL_OBJECT_SET = new NullObjectset();

    private String mRootDirectory;
    private GetObjectParameters mParameters;
    private MultipartBoundaryGenerator mBoundaryGenerator;
    private String mBaseLocationUrl;
    private boolean mBlockLocation;
    private String mPhotoPattern;
    private String mObjectSetPattern;
    private String mResource;
    private String mType;
    private boolean mWildObject;
}
