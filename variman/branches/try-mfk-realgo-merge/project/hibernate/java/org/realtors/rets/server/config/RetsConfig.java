/*
 * Variman RETS Server
 *
 * Author: Dave Dribin, Danny Hurlburt
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.config;

public interface RetsConfig {

    /**
     * @deprecated Does not appear to be used for anything. To be removed.
     */
    public String getAddress();

    /**
     * @deprecated Does not appear to be used for anything. To be removed.
     */
    public void setAddress(String address);

    /**
     * @deprecated Does not appear to be used for anything. To be removed.
     */
    public int getPort();

    /**
     * @deprecated Does not appear to be used for anything. To be removed.
     */
    public void setPort(int port);

    public String getGetObjectRoot();

    public void setGetObjectRoot(String getObjectRoot);

    public String getPhotoPattern();

    public void setPhotoPattern(String photoPattern);

    public String getObjectSetPattern();

    public void setObjectSetPattern(String objectSetPattern);

    public int getNonceInitialTimeout();

    public void setNonceInitialTimeout(int nonceInitialTimeout);

    public int getNonceSuccessTimeout();

    public void setNonceSuccessTimeout(int nonceSuccessTimeout);

    /**
     * @deprecated Implementation detail for the metadata loader
     */
    public String getMetadataDir();

    /**
     * @deprecated Implementation detail for the metadata loader
     */
    public void setMetadataDir(String metadataDir);

    /**
     * @deprecated Implementation detail not related to the RETS server itself.
     */
    public DatabaseConfig getDatabase();

    /**
     * @deprecated Implementation detail not related to the RETS server itself.
     */
    public void setDatabase(DatabaseConfig database);

    public SecurityConstraints getSecurityConstraints();

    public void setSecurityConstraints(SecurityConstraints securityConstraints);

}
