/*
 * Variman RETS Server
 *
 * Author: Mark Klein
 * Copyright (c) 2010, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server.protocol;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.realtors.rets.server.Media;
import org.realtors.rets.server.MediaUtils;
import org.realtors.rets.server.ReplyCode;
import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.RetsServerException;

/**
 * Create ObjectSets when the content can be found in a database table.
 * @author mklein
 *
 */
public class DatabaseObjectSet implements ObjectSet
{

    private static final Logger LOG =
        Logger.getLogger(DatabaseObjectSet.class);
    
    private String mRootDirectory;
    private String mResourceEntity;
    private String mResource;
    
    private static File sNotFound = null;
    
    public DatabaseObjectSet()
    {
    }
    
    /**
     * Get the RETS resource for this ObjectSet.
     * @return A string containing the RETS Resource.
     */
    public String getResource()
    {
        return mResource;
    }
  
    /**
     * Set the RETS resource for this ObjectSet.
     */
    public void setResource(String resource)
    {
        mResource = resource;
    }
    
    /**
     * Get the Object Resource Entity (e.g. the Listing ID or MLSNum.
     * @return A string containing the Resource Entity key.
     */
    public String getResourceEntity()
    {
        return mResourceEntity;
    }
    
    /**
     * Set the Resource Entity Key.
     * @param resourceEntity A string containing the Resource Entity Key.
     */
    public void setResourceEntity(String resourceEntity)
    {
        mResourceEntity = resourceEntity;
    }
    
    /**
     * Build the ObjectDescriptor for this <code>objectID</code>
     * @param file A Java <code>FILE</code> referencing the object.
     * @param objectId An <code>int</code> containing the Object ID.
     * @return An <code>ObjectDescriptor</code>
     * @throws RetsServerException
     */
    private ObjectDescriptor buildObjectDescriptor(File file, int objectId) 
        throws RetsServerException
    {
        ObjectDescriptor objectDescriptor = null;
        
        try
        {
            if (file.exists() && file.isFile())
            {
                LOG.debug("File " + file.getCanonicalPath() + " exists for Listing " + 
                        mResourceEntity + ", objectId: " + objectId);
                return new ObjectDescriptor(mResourceEntity, objectId,
                                            file.toURI().toURL());
            }
            else
            {
                LOG.debug("File " + file.getCanonicalPath() + " does not exist");
                /*
                 * In case this is a multipart response, we need to create an object of text/xml
                 * that contains the RETS ReplyCode message. 
                 */
                try
                {
                    if (sNotFound == null)
                    {
                        StringBuffer notFoundName = new StringBuffer();
                        if (mRootDirectory == null)
                            mRootDirectory = RetsServer.getRetsConfiguration().getGetObjectRoot();
                        
                        if (mRootDirectory != null && mRootDirectory.length() > 0)
                        {
                            notFoundName.append(mRootDirectory);
                            notFoundName.append(File.separator);
                        }
                        notFoundName.append("notfound.xml");
                        
                        /*
                         * Not the best way, but this is the exceptional case, 
                         * so brute force should work.
                         */
                        sNotFound = new File(notFoundName.toString());
    
                        synchronized (this)
                        {
                            if (sNotFound.length() == 0)
                            {
                                BufferedWriter out = new BufferedWriter(new FileWriter(sNotFound));
                                out.write("<RETS ReplyCode=\"20403\" ReplyText=\"No Object Found\"/>\r\n");
                                out.close();
                                sNotFound.deleteOnExit();
                            }
                        }
                    }
                    objectDescriptor = new ObjectDescriptor(mResourceEntity, objectId,
                                                                    sNotFound.toURI().toURL(), null);
                    objectDescriptor.setRetsReplyCode(ReplyCode.NO_OBJECT_FOUND);
                    return objectDescriptor;
                }
                catch (Exception e)
                {
                }
                return null;
            }
        }
        catch (Exception e)
        {
            LOG.debug(e);
            throw new RetsServerException(e);
        }
    }

    /**
     * Find all objects for this Resource Key of type. Only implemented for "Photo".
     * @param type A String containing the object type.
     * @return A List of ObjectDescriptors.
     */
    public List<ObjectDescriptor> findAllObjects(String mediaType) throws RetsServerException
    {
        List<ObjectDescriptor> objects = new ArrayList<ObjectDescriptor>();
        List<Media> photos;
        
        try
        {
            LOG.debug("FindAllObjects by Resource Key: " + mResourceEntity);
            photos = MediaUtils.findByResourceKey(mediaType, mResourceEntity);
        }
        catch (Exception e)
        {
            LOG.debug(e);
            throw new RetsServerException(e);
        }
        for (Media photo : photos)
        {
            StringBuffer fileBuffer = new StringBuffer();
            if (mRootDirectory == null)
                mRootDirectory = RetsServer.getRetsConfiguration().getGetObjectRoot();
            
            if (mRootDirectory != null && mRootDirectory.length() > 0)
            {
                fileBuffer.append(mRootDirectory);
                fileBuffer.append(File.separator);
            }
            
            fileBuffer.append(photo.getResourceFilePath());

            String filePath = fileBuffer.toString();
            File file = new File(filePath);
            LOG.debug("buildObjectDescriptor for photo.getObjectID(): " + photo.getObjectID() + 
                                ", Resource Key: " + mResourceEntity + ", photo ResourceKey: " + 
                                photo.getMediaKey().getResourceKey());
            ObjectDescriptor objectDescriptor = buildObjectDescriptor(file, photo.getObjectID());
            if (objectDescriptor != null)
                objects.add(objectDescriptor);
        }

        return objects;
    }

    /**
     * Find an object for this Resource Key identified by <code>objectId</code>. Only implemented for "Photo".
     * @param mediaType A String containing the object type.
     * @param objectId An integer containing the <code>objectId</code>.
     * @return The ObjectDescriptor.
     */
    public ObjectDescriptor findObject(String mediaType, int objectId)
        throws RetsServerException
    {
        if (objectId == DEFAULT_OBJECT_ID)
        {
            objectId = 1;
        }
        
        Media media = MediaUtils.FindByObjectID(mediaType, mResourceEntity, objectId);
        if (media == null)
            return null;
        
        StringBuffer fileBuffer = new StringBuffer();
        if (mRootDirectory == null)
            mRootDirectory = RetsServer.getRetsConfiguration().getGetObjectRoot();
        
        if (mRootDirectory != null && mRootDirectory.length() > 0)
        {
            fileBuffer.append(mRootDirectory);
            fileBuffer.append(File.separator);
        }
        
        fileBuffer.append(media.getResourceFilePath());

        String filePath = fileBuffer.toString();
        File file = new File(filePath);
        return buildObjectDescriptor(file, objectId);
    }
}
