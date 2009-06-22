/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2007, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.config;

import java.util.List;

import net.sf.hibernate.SessionFactory;

import org.realtors.rets.server.RetsServerException;
import org.springframework.orm.hibernate.HibernateTemplate;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

public class SpringHibernate2RetsConfigLoader
        extends HibernateDaoSupport implements RetsConfigLoader
{
    private RetsConfig mRetsConfig;
    private boolean mCacheInvalid;
    
    public SpringHibernate2RetsConfigLoader()
    {
        // Default constructor. Must set the session factory before use.
    }
    
    public SpringHibernate2RetsConfigLoader(SessionFactory sessionFactory)
    {
        if (sessionFactory == null) {
            throw new NullPointerException("sessionFactory is null.");
        }
        setSessionFactory(sessionFactory);
    }
    
    public SpringHibernate2RetsConfigLoader(HibernateTemplate hibernateTemplate)
    {
        if (hibernateTemplate == null) {
            throw new NullPointerException("hibernateTemplate is null.");
        }
        setHibernateTemplate(hibernateTemplate);
    }
    
    public RetsConfig load() throws RetsServerException
    {
        synchronized (this) {
            if (mRetsConfig == null || mCacheInvalid) {
                List retsCfgList = getHibernateTemplate().find("from HibernateRetsConfig");
                if (retsCfgList == null || retsCfgList.size() == 0) {
                    throw new RetsServerException("Unable to find the RETS configuration data.");
                }
                if (retsCfgList.size() != 1) {
                    throw new RetsServerException("Found more than one RETS configuration data.");
                }
                mRetsConfig = (RetsConfig)retsCfgList.get(0);
                setCacheInvalid(false);
            }
            return mRetsConfig;
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
