/*
 * Variman RETS Server
 *
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.config;

import java.util.Date;

import org.realtors.rets.server.RetsServerException;

/**
 * A <code>RetsConfigDao</code> loads the server's configuration data and
 * makes it available as a {@link RetsConfig} instance.
 */
public interface RetsConfigDao
{
    /**
     * Loads the server's configuration and return a {@link RetsConfig}
     * instance.
     *
     * @return an {@link RetsConfig} instance. Never returns <code>null</code>.
     * @throws RetsServerException if unable to load the configuration data.
     */
    public RetsConfig loadRetsConfig() throws RetsServerException;

    /**
     * Persists the server's configuration.
     * @param retsConfig A rets configuration object to persist.
     * @throws RetsServerException if unable to save the configuration data.
     */
    public void saveRetsConfig(RetsConfig retsConfig) throws RetsServerException;

    /**
     * Return the DateTime that the RETS config was last changed. Ideally this
     * is done with a non-intensive check such as a timestamp or MD5 hash.
     * 
     * @return Date containing the date and time the RETS config was changed
     * @throws RetsServerException if unable to load the configuration data
     */
    public Date getConfigChangedDate();
}
