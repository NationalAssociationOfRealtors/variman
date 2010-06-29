/*
 * Variman RETS Server
 *
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.config;

import java.io.File;
import java.util.Date;

import org.realtors.rets.server.RetsServerException;

public class XmlRetsConfigDao implements RetsConfigDao {
    private String mAbsFilePath;

    public XmlRetsConfigDao() {
        // Must set the RETS configuration file path before use.
    }

    public XmlRetsConfigDao(String filePath) {
        setFilePath(filePath);
    }

    public String getFilePath() {
        return this.mAbsFilePath;
    }

    public void setFilePath(String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            throw new IllegalArgumentException("filePath must not represent a directory.");
        }
        String absFilePath = file.getAbsolutePath();
        this.mAbsFilePath = absFilePath;
    }

    /*- (non-Javadoc)
     * @see org.realtors.rets.server.config.RetsConfigDao#loadRetsConfig()
     */
    public RetsConfig loadRetsConfig() throws RetsServerException {
        String filePath = getFilePath();
        if (filePath == null) {
            String errMsg = "The RETS configuration XML file " + "path has not been set. Please " + "ensure this is set before " + "attempting to load the " + "configuration.";
            throw new IllegalStateException(errMsg);
        }

        RetsConfig retsConfig = XmlRetsConfigUtils.initFromXmlFile(filePath);
        return retsConfig;
    }

    /*- (non-Javadoc)
     * @see org.realtors.rets.server.config.RetsConfigDao#saveRetsConfig(org.realtors.rets.server.config.RetsConfig)
     */
    public void saveRetsConfig(RetsConfig retsConfig) throws RetsServerException {
        String filePath = getFilePath();
        if (filePath == null) {
            String errMsg = "The RETS configuration XML file " + "path has not been set. Please " + "ensure this is set before " + "attempting to load the " + "configuration.";
            throw new IllegalStateException(errMsg);
        }

        XmlRetsConfigUtils.toXml(retsConfig, filePath);
    }

    /*- (non-Javadoc)
     * @see org.realtors.rets.server.config.RetsConfigDao#isChanged()
     */
    public Date getConfigChangedDate() {
        Date configChanged = new Date(new File(getFilePath()).lastModified());
        return configChanged;
    }
}
