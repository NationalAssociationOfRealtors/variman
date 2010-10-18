/*
 * Variman RETS Server
 *
 * Author: Mark Klein
 * Copyright (c) 2010, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.apache.log4j.Logger;

/**
 * Hibernate helper class to load Media Objects from the database.
 * @author mklein
 *
 */
public class MediaUtils
{
    /**
     * Find all Media in the database.
     * @return A List of <code>Media</code>s.
     * @throws HibernateException
     */
    public static List<Media> findAll() throws HibernateException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            return findAll(helper);
        }
        finally
        {
            helper.close(LOG);
        }
    }
    
    /**
     * Find all Media in the database by MediaType.
     * @param mediaType A String containing the well known name of the media type.
     * @return A List of <code>Media</code>s.
     * @throws HibernateException
     */
    public static List<Media> findAll(String mediaType) throws HibernateException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            return findAll(mediaType, helper);
        }
        finally
        {
            helper.close(LOG);
        }
    }


    /**
     * Find all Media Objects in the database using a given <code>SessionHelper</code>.
     * @param helper The <code>SessionHelper</code>.
     * @return A List of <code>Media</code>s.
     * @throws HibernateException
     */
    @SuppressWarnings("unchecked")
    public static List<Media> findAll(SessionHelper helper) throws HibernateException
    {
        Query query = helper.createQuery(
            "  FROM Media aMedia " +
            "ORDER BY aMedia.mediaKey.resourceKey, aMedia.mediaKey.objectID");
        return query.list();
    }
    
    /**
     * Find all Media Objects by Media Type.
     * @param helper The <code>SessionHelper</code>.
     * @return A List of <code>Media</code>s.
     * @throws HibernateException
     */
    @SuppressWarnings("unchecked")
    public static List<Media> findAll(String mediaType, SessionHelper helper) throws HibernateException
    {
        Query query = helper.createQuery(
            "  FROM Media aMedia " +
            "WHERE aMedia.mediaKey.mediaType = :mediaType " +
            "ORDER BY aMedia.mediaKey.resourceKey, aMedia.mediaKey.objectID");
        query.setString("mediaType", mediaType);
        return query.list();
    }
    /**
     * Find a <code>Media</code> by Resource Key and Object ID.
     * @param resourceKey A String containing the Resource Key.
     * @param objectId An integer containing the object ID.
     * @return A <code>Media</code>.
     * @throws HibernateException
     */
    public static Media FindByObjectID(String resourceKey, int objectId) throws HibernateException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            return FindByObjectID("Photo", resourceKey, objectId, helper);
        }
        finally
        {
            helper.close(LOG);
        }
    }

    /**
     * Find <code>Media</code> by Resource Key and Object ID.
     * @param mediaType A String containing the media type well known name.
     * @param resourceKey A String containing the Resource Key.
     * @param objectId An integer containing the object ID.
     * @return A <code>Media</code>.
     * @throws HibernateException
     */
    public static Media FindByObjectID(String mediaType, String resourceKey, int objectId) throws HibernateException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            return FindByObjectID(mediaType, resourceKey, objectId, helper);
        }
        finally
        {
            helper.close(LOG);
        }
    }
    /**
     * Find a <code>Photo</code> by Resource Key and Object ID.
     * @param mediaType A string containing the media type well known name.
     * @param resourceKey A String containing the Resource Key.
     * @param objectId An integer containing the object ID.
     * @param helper The <code>SessionHelper</code> for this database session.
     * @return The <code>Media</code>.
     * @throws HibernateException
     */
    public static Media FindByObjectID(String mediaType, String resourceKey, int objectID, SessionHelper helper)
        throws HibernateException
    {
        Query query = helper.createQuery(
            " From Media aMedia " +
            "WHERE aMedia.mediaKey.resourceKey = :resourceKey " +
            "and aMedia.mediaKey.objectID = :objectID " +
            "and aMedia.mediaKey.mediaType = :mediaType");
        query.setString("resourceKey", resourceKey);
        query.setInteger("objectID", objectID);
        query.setString("mediaType", mediaType);
        return (Media) query.uniqueResult();
    }

    /**
     * Find a Media by Resource Key. Media type defaults to "Photo".
     * @param resourceKey A String containing the Resource Key.
     * @return A List of <code>Media</code>s.
     * @throws HibernateException
     */
    public static List<Media> findByResourceKey(String resourceKey) throws HibernateException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            return findByResourceKey("Photo", resourceKey, helper);
        }
        finally
        {
            helper.close(LOG);
        }
    }
    
    /**
     * Find a Media by Resource Key and Media type.
     * @param resourceKey A String containing the Resource Key.
     * @return A List of <code>Media</code>s.
     * @throws HibernateException
     */
    public static List<Media> findByResourceKey(String mediaType, String resourceKey) throws HibernateException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            return findByResourceKey(mediaType, resourceKey, helper);
        }
        finally
        {
            helper.close(LOG);
        }
    }


    /**
     * Find a Media by Resource Key.
     * @param resourceKey A String containing the Resource Key.
     * @param mediaType A String containing the media type well known name.
     * @param helper The <code>SessionHelper</code> for this database session.
     * @return A List of <code>Media</code>s.
     * @throws HibernateException
     */
    @SuppressWarnings("unchecked")
    public static List<Media> findByResourceKey(String mediaType, String resourceKey, SessionHelper helper)
        throws HibernateException
    {
        Query query = helper.createQuery(
            " From Media aMedia " +
            "WHERE aMedia.mediaKey.resourceKey = :resourceKey " +
            "and aMedia.mediaKey.mediaType = :mediaType " +
            "order by aMedia.mediaKey.objectID");
        query.setString("resourceKey", resourceKey);
        query.setString("mediaType", mediaType);
        return query.list();
    }

    private static final Logger LOG =
        Logger.getLogger(MediaUtils.class);
}
