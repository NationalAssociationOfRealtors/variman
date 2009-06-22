/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2007, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.config;

import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.Resource;

public class HibernateConditionRule implements ConditionRule
{
    public HibernateConditionRule()
    {
        super();
    }
    
    public Integer getId()
    {
        return m_id;
    }

    public void setId(Integer id)
    {
        m_id = id;
    }

    public GroupRules getGroupRule()
    {
        return m_groupRule;
    }

    public void setGroupRule(GroupRules groupRule)
    {
        m_groupRule = groupRule;
    }

    public MClass getMetaClass()
    {
        return m_metaClass;
    }

    public void setMetaClass(MClass metaClass)
    {
        m_metaClass = metaClass;
    }

    public String getNote()
    {
        return m_note;
    }

    public void setNote(String note)
    {
        m_note = note;
    }

    public String getResourceID()
    {
        String resourceID = null;
        MClass metaClass = getMetaClass();
        if (metaClass != null) {
            Resource metaResource = metaClass.getResource();
            if (metaResource != null) {
                resourceID = metaResource.getResourceID();
            }
        }
        return resourceID;
    }

    public void setResourceID(String resourceID)
    {
        MClass metaClass = getMetaClass();
        if (metaClass != null) {
            Resource metaResource = metaClass.getResource();
            if (metaResource != null) {
                metaResource.setResourceID(resourceID);
            }
        }
    }

    public String getRetsClassName()
    {
        String retsClassName = null;
        MClass metaClass = getMetaClass();
        if (metaClass != null) {
            retsClassName = metaClass.getClassName();
        }
        return retsClassName;
    }

    public void setRetsClassName(String retsClassName)
    {
        MClass metaClass = getMetaClass();
        if (metaClass != null) {
            metaClass.setClassName(retsClassName);
        }
    }

    public String getSqlConstraint()
    {
        return m_sqlConstraint;
    }

    public void setSqlConstraint(String sqlConstraint)
    {
        m_sqlConstraint = sqlConstraint;
    }

    public String getDmqlConstraint()
    {
        return m_dmqlConstraint;
    }

    public void setDmqlConstraint(String dmqlConstraint)
    {
        m_dmqlConstraint = dmqlConstraint;
    }

    private Integer m_id;
    private GroupRules m_groupRule;
    private MClass m_metaClass;
    private String m_sqlConstraint;
    private String m_dmqlConstraint;
    private String m_note;
    
}
