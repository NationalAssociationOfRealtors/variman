/*
 * Variman RETS Server
 *
 * Author: Mark Klein
 * Copyright (c) 2010 The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.metadata;

import java.util.ArrayList;
import java.util.Map;

import org.realtors.rets.common.metadata.Metadata;

/**
 * The data access interface for StandardNameManager.
 */
public interface StandardNameDao 
{

    /**
     * @return The StandardNameMap.
     */
    public Map<String, StandardNameEntry> getStandardNameMap() throws Exception;
    
    /**
     * @return The StandardNamePathMap.
     */
    public Map<String, ArrayList<String>> getStandardNamePathMap() throws Exception;

    /**
     * Save the Standard Names.
     */
    public void saveStandardNameMap(Map<String, StandardNameEntry> standardNameMap) throws Exception;
    
    /**
     * Set the base path.
     * @param basePath A string containing the base path to WEB-INF if needed.
     */
    public void setBasePath(String basePath);
}
