/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2007, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.config;

import java.io.File;
import java.io.IOException;

import org.realtors.rets.server.RetsServerException;
import org.springframework.core.io.Resource;

public class SpringXmlRetsConfigLoader implements RetsConfigLoader
{
    private RetsConfig mRetsConfig;
    private boolean mCacheInvalid;
    private String mAbsFilePath;
    
    public SpringXmlRetsConfigLoader()
    {
        // Default constructor. Must set the RETS configuration file path before use.
        invalidate();
    }
    
    public SpringXmlRetsConfigLoader(final String filePath)
    {
        setFilePath(filePath);
        invalidate();
    }
    
    public SpringXmlRetsConfigLoader(final Resource resource) throws IOException
    {
        final File file = resource.getFile();
        if (file.isDirectory()) {
            throw new IllegalArgumentException("resource must not represent a directory.");
        }
        final String filePath = file.getAbsolutePath();
        setFilePath(filePath);
        invalidate();
    }
    
    public String getFilePath()
    {
        return mAbsFilePath;
    }
    
    public void setFilePath(final String filePath)
    {
        synchronized (this) {
            final File file = new File(filePath);
            if (file.isDirectory()) {
                throw new IllegalArgumentException("filePath must not represent a directory.");
            }
            final String absFilePath = file.getAbsolutePath();
            mAbsFilePath = absFilePath;
        }
        invalidate(); // Ensure the new configuration information gets loaded.
    }
    
    public RetsConfig load() throws RetsServerException
    {
        synchronized (this) {
            if (mRetsConfig == null || mCacheInvalid) {
                final String filePath = getFilePath();
                if (filePath == null) {
                    final String errMsg = "The RETS configuration XML file " +
                                          "path has not been set. Please " +
                                          "ensure this is set before " +
                                          "attempting to load the " +
                                          "configuration.";
                    throw new IllegalStateException(errMsg);
                }
                final RetsConfig retsConfig = XmlRetsConfigUtils.initFromXmlFile(filePath);
                mRetsConfig = retsConfig;
                setCacheInvalid(false);
            }
            return mRetsConfig;
        }
    }
    
    private void setCacheInvalid(final boolean isInvalid)
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
