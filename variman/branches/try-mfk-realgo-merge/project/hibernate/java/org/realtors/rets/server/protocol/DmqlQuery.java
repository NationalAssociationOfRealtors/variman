/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2007, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.protocol;

import org.apache.commons.lang.enums.Enum;

/**
 * A DMQL query contains the DMQL statement, the version the statement is to be
 * interpreted as, and a flag determining whether field names in the statement
 * should be resolved as standard names or systemnames. 
 */
public class DmqlQuery
{
    private String mDmql;
    private Version mVersion;
    private boolean mIsStandardNames;
    
    public DmqlQuery(final String dmql)
    {
        this(dmql, Version.DMQL2, false);
    }
    
    public DmqlQuery(final String dmql, final Version version, final boolean isStandardNames) {
        String potentialDmql = dmql;
        if (potentialDmql == null || (potentialDmql = potentialDmql.trim()).length() == 0) {
            throw new NullPointerException("dmql must not be null or empty.");
        }
        if (version == null) {
            throw new NullPointerException("version must not be null.");
        }
        if (version != Version.DMQL && version != Version.DMQL2) {
            throw new IllegalArgumentException("version must be one of DmqlQuery.Version.DMQL or DmqlQuery.Version.DMQL2.");
        }
        mDmql = potentialDmql;
        mVersion = version;
        mIsStandardNames = isStandardNames;
    }
    
    public String getDmql() {
        return mDmql;
    }
    
    public Version getVersion() {
        return mVersion;
    }
    
    public boolean isStandardNames() {
        return mIsStandardNames;
    }
    
    public static class Version extends Enum
    {
        public static final Version DMQL = new Version("DMQL");
        
        public static final Version DMQL2 = new Version("DMQL2");
        
        /*
         * Save reference to original version string in case the super class'
         * constructor does something to the version string.
         */
        private String mVersionString;
        
        private Version(String versionString)
        {
            super(versionString);
            mVersionString = versionString;
        }
        
        public String getVersionString() {
            return mVersionString;
        }

        public static Version getEnum(String versionString)
        {
            return (Version) getEnum(Version.class, versionString);
        }
        
    }
    
}
