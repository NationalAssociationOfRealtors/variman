/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2007, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server;

import java.util.Set;

/**
 * A user-agent is the product name and optional product version used to
 * identify itself. All client requests are required to include the
 * <code>User-Agent</code> request header.
 */
public class UserAgent
{
    public static final UserAgent UNKNOWN = new UserAgent("Unknown", "0.0");
    
    private Long m_id;
    private String m_productName;
    private String m_productVersion;
    private Set/*Group*/ m_groups;
    
    public UserAgent()
    {
        // For use by Hibernate and other code expecting a default-constructor.
    }
    
    /**
     * @param productName the product name representing this user-agent. Must
     *        not be <code>null</code> or empty.
     * @param productVersion the product's version. May be <code>null</code>.
     */
    public UserAgent(final String productName, final String productVersion)
    {
        if (productName == null) {
            throw new NullPointerException("productName is null.");
        }
        if (productName.trim().length() == 0) {
            throw new IllegalArgumentException("productName is empty.");
        }
        m_productName = productName;
        m_productVersion = productVersion;
    }
    
    /**
     * 
     * @param userAgentHeader the User-Agent request header value. Must not be
     *        <code>null</code>.
     */
    public UserAgent(final String userAgentHeader)
    {
        final String INVALID_PRODUCT_NAME_MSG = "userAgentHeader did not contain a valid product name. Must be of the form: product-name[/product-version].";
        if (userAgentHeader == null) {
            throw new NullPointerException("userAgentHeader is null.");
        }
        
        final String potentialProductName = UserAgentUtils.extractProductName(userAgentHeader);
        final String potentialProductVersion = UserAgentUtils.extractProductVersion(userAgentHeader);
        
        if (potentialProductName == null || potentialProductVersion == null) {
            throw new IllegalArgumentException(INVALID_PRODUCT_NAME_MSG);
        }
        
        m_productName = potentialProductName;
        m_productVersion = potentialProductVersion;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return m_id;
    }

    /**
     * @param id the id to set
     */
    public void setId(final Long id) {
        m_id = id;
    }

    /**
     * Returns the product name representing this user-agent. Never returns
     * <code>null</code> or an emtpy string.
     * 
     * @return the product name.
     */
    public String getProductName()
    {
        return m_productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(final String productName) {
        if (productName == null) {
            throw new NullPointerException("productName is null.");
        }
        if (productName.trim().length() == 0) {
            throw new IllegalArgumentException("productName is empty.");
        }
        m_productName = productName;
    }

    /**
     * Returns the product version of this user-agent. Never returns
     * <code>null</code>.
     * 
     * @return the product version of this user-agent.
     */
    public String getProductVersion() {
        if (m_productVersion == null) {
            m_productVersion = "";
        }
        return m_productVersion;
    }

    /**
     * @param productVersion the productVersion to set
     */
    public void setProductVersion(final String productVersion) {
        m_productVersion = productVersion;
    }
    
    protected Set/*Group*/ getGroups()
    {
        return m_groups;
    }

    protected void setGroups(Set/*Group*/ groups)
    {
        m_groups = groups;
    }

}
