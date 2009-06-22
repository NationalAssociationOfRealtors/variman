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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.Table;

public class HibernateFilterRule implements FilterRule
{
    public HibernateFilterRule()
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

    public Type getType()
    {
        String typeName = getFilterRuleType();
        Type type = findType(typeName);
        return type;
    }

    public void setType(Type type)
    {
        if (type == null) {
            throw new NullPointerException("type is null.");
        }
        setFilterRuleType(type.getName());
    }

    public String getFilterRuleType()
    {
        return m_typeName;
    }

    public void setFilterRuleType(String type)
    {
        m_typeName = type;
    }

    public List getSystemNames()
    {
        if (m_retsMetaTablesChanged) {
            updateSystemNamesList();
        }
        return Collections.unmodifiableList(m_systemNamesList);
    }
    
    private void updateSystemNamesList()
    {
        Set/*Table*/ retsMetaTables = getRetsMetaTables();
        m_systemNamesList = new ArrayList/*String*/(retsMetaTables.size());
        for (Iterator iter = retsMetaTables.iterator(); iter.hasNext(); ) {
            Table retsMetaTable = (Table)iter.next();
            String systemName = retsMetaTable.getSystemName();
            m_systemNamesList.add(systemName);
        }
        m_retsMetaTablesChanged = false;
    }


    public void setSystemNames(List systemNames)
    {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    public void addSystemName(String systemName)
    {
        throw new UnsupportedOperationException("Not yet implemented.");
    }
    
    public Set/*Table*/ getRetsMetaTables()
    {
        return m_retsMetaTables;
    }
    
    public void setRetsMetaTables(Set/*Table*/ retsMetaTables)
    {
        if (retsMetaTables == null) {
            throw new NullPointerException("retsMetaTables is null.");
        }
        m_retsMetaTables = retsMetaTables;
        m_retsMetaTablesChanged = true;
    }

    protected Type findType(String typeName)
    {
        List/*FilterRule.Type*/ types = Type.getEnumList();
        
        for (Iterator iter = types.iterator(); iter.hasNext(); ) {
            FilterRule.Type type = (FilterRule.Type)iter.next();
            if (type.getName().equals(typeName)) {
                return type;
            }
        }
        return null; // not found
    }

    private Integer m_id;
    private GroupRules m_groupRule;
    private MClass m_metaClass;
    private String m_typeName;
    private Set/*Table*/ m_retsMetaTables = Collections.EMPTY_SET;
    private boolean m_retsMetaTablesChanged = true;
    private List/*String*/ m_systemNamesList = Collections.EMPTY_LIST;
    private String m_note;
}
