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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.ReplyCode;
import org.realtors.rets.server.RetsReplyException;
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
    }

    public void setRootDirectory(String rootDirectory)
    {
        mRootDirectory = rootDirectory;
    }

    public void setImagePattern(String imagePattern)
    {
        mImagePattern = imagePattern;
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
                executeSinglePart(response, objectDescriptor);
            }
            else
            {
                executeMultipart(response, allObjects);
            }
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
            LOG.info("GetObject (mulitpart) URL: " + objectDescriptor.getUrl());
            out.writeBytes(CRLF + "--" + boundary + CRLF);
            out.writeBytes("Content-ID: " + objectDescriptor.getObjectKey() +
                           CRLF);
            String description = objectDescriptor.getDescription();
            if (description != null)
            {
                out.writeBytes("Content-Description: " + description + CRLF);
            }
            out.writeBytes("Object-ID: " + objectDescriptor.getObjectId() +
                           CRLF);
            if (useLocation())
            {
                out.writeBytes("Content-Type: application/octet-stream" + CRLF);
                out.writeBytes("Location: " + getLocationUrl(objectDescriptor) +
                               CRLF);
                out.writeBytes(CRLF);
            }
            else
            {
                ObjectStream stream = objectDescriptor.openObjectStream();
                out.writeBytes("Content-Type: " + stream.getMimeType() + CRLF);
                out.writeBytes(CRLF);
                IOUtils.copyStream(stream.getInputStream(), out);
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
        location.append(objectDescriptor.getObjectKey()).append("/");
        location.append(objectDescriptor.getObjectId());
        return location.toString();
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
                    objects.addAll(objectSet.findAllObjects(mType));
                }
                else
                {
                    int objectId = NumberUtils.stringToInt(objectIdString);
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

    private ObjectSet getObjectSet(String resourceEntity)
        throws RetsServerException
    {
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
            return new XmlObjectSet(xmlObjectFile);
        }
        else
        {
            return new PatternObjectSet(mRootDirectory, mImagePattern,
                                        resourceEntity);
        }
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

    private static final Logger LOG =
        Logger.getLogger(GetObjectTransaction.class);
    private static final String CRLF = "\r\n";
    public static final MultipartBoundaryGenerator DEFAULT_BOUNDARY_GENERATOR =
        new BoundaryGenerator();
    private String mRootDirectory;
    private GetObjectParameters mParameters;
    private MultipartBoundaryGenerator mBoundaryGenerator;
    private String mBaseLocationUrl;
    private boolean mBlockLocation;
    private String mImagePattern;
    private String mObjectSetPattern;
    private String mResource;
    private String mType;
}
