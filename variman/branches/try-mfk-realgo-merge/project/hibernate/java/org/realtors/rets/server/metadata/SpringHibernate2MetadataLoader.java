/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2006, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.metadata;

import java.util.List;

import net.sf.hibernate.SessionFactory;

import org.apache.log4j.Logger;
import org.realtors.rets.server.RetsServerException;
import org.springframework.orm.hibernate.HibernateTemplate;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

/**
 * A Spring/Hibernate 2.x dependent version of a {@link MetadataLoader}.
 *
 * @author Danny Hurlburt
 */
public class SpringHibernate2MetadataLoader
        extends HibernateDaoSupport
        implements MetadataLoader
{
    static final Logger LOG = Logger.getLogger(SpringHibernate2MetadataLoader.class);
    
    private MSystem mSystem;
    private boolean mCacheInvalid;

    public SpringHibernate2MetadataLoader()
    {
        // Default constructor. Must set the session factory before use.
        invalidate();
    }
    
    public SpringHibernate2MetadataLoader(SessionFactory sessionFactory)
    {
        if (sessionFactory == null) {
            throw new NullPointerException("sessionFactory is null.");
        }
        setSessionFactory(sessionFactory);
        invalidate();
    }
    
    public SpringHibernate2MetadataLoader(HibernateTemplate hibernateTemplate)
    {
        if (hibernateTemplate == null) {
            throw new NullPointerException("hibernateTemplate is null.");
        }
        setHibernateTemplate(hibernateTemplate);
    }
    
    public MSystem load() throws RetsServerException
    {
        synchronized (this) {
            if (mSystem == null || mCacheInvalid) {
                List systemMetadata = getHibernateTemplate().find("from MSystem");
                if (systemMetadata == null || systemMetadata.size() == 0) {
                    throw new RetsServerException("Unable to find the system metadata.");
                }
                if (systemMetadata.size() != 1) {
                    throw new RetsServerException("Found more than one system metadata.");
                }
                mSystem = (MSystem)systemMetadata.get(0);
                setCacheInvalid(false);
            }
            return mSystem;
        }
    }
    
    private void setCacheInvalid(boolean isInvalid)
    {
        synchronized (this) {
            mCacheInvalid = isInvalid;
        }
    }
    
    public void invalidate()
    {
        setCacheInvalid(true);
    }
    
}
