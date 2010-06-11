/*
 * Variman RETS Server
 *
 * Author: Mark Klein
 * Copyright (c) 2010, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.realtors.rets.server.metadata.StandardNameEntry;

import com.wutka.dtd.DTDElement;

/**
 * A <code>RetsDTDElement</code> is class that 
 * links the DTDelement to its children. It may contain references to system metadata.
 *
 */
public class RetsDTDElement
{
    private DTDElement mElement;
    private Map<String, RetsDTDElement> mChildren = new LinkedHashMap<String, RetsDTDElement>();
    private ArrayList<String> mStandardNames = new ArrayList<String>();
    private String mParentPath = null;
    
    public RetsDTDElement (DTDElement element)
    {
        mElement = element;
        mChildren.clear();
        mStandardNames.clear();
    }
    
    public RetsDTDElement(DTDElement element, RetsDTDElement child)
    {
        mElement = element;
        mChildren.put(child.getName(), child);
    }
    
    public void addChild (RetsDTDElement child)
    {
        if (!mChildren.containsKey(child.getName()))
            mChildren.put(child.getName(), child);
    }
    
    public void addPath(String path)
    {
        mParentPath = path;
    }
    
    public void addStandardName (String standardName)
    {
        mStandardNames.add(standardName);
    }
    
    /**
     * Create a copy of the current element. We want to preserve only
     * the DTDElement and the Parent Path.
     */
    public RetsDTDElement clone()
    {
        RetsDTDElement element = new RetsDTDElement (mElement);
        element.mParentPath = mParentPath;
        return element;
    }
    
    /**
     * Find the child with the given name.
     * @param name A String containing the element name.
     * @return The DTDElement
     */
    public RetsDTDElement findChild(String name)
    {
        return mChildren.get(name);
    }
    
    public Collection<RetsDTDElement> getChildren()
    {
        return mChildren.values();
    }
    
    public Collection<String> getStandardNames()
    {
        return mStandardNames;
    }
    
    public DTDElement getElement()
    {
        return mElement;
    }
    
    public String getName()
    {
        return mElement.name;
    }
    
    public String getPath()
    {
        return mParentPath;
    }
    
    public String getStandardName(int i)
    {
        if (i >= mStandardNames.size())
            return null;
        
        return mStandardNames.get(i);
    }
    
    public void setElement(DTDElement element)
    {
        mElement = element;
    }
    
    public boolean equals(Object obj)
    {
        if (!(obj instanceof RetsDTDElement))
        {
            return false;
        }
        RetsDTDElement rhs = (RetsDTDElement) obj;
        return new EqualsBuilder()
            .append(mElement, rhs.mElement)
            .append(mChildren, rhs.mChildren)
            .append(mStandardNames, rhs.mStandardNames)
            .append(mParentPath, rhs.mParentPath)
            .isEquals();
    }
    
    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("DTD element", mElement)
            .append("children", mChildren)
            .append("standard names", mStandardNames)
            .append("parent path", mParentPath)
            .toString();
    }
}
