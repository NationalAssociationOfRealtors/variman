/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.config;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.CloneNotSupportedException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.JDomCompactBuilder;
import org.realtors.rets.common.metadata.Metadata;
import org.realtors.rets.common.metadata.MetadataCompactFormatter;
import org.realtors.rets.common.metadata.types.MSystem;
import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.JdomUtils;
import org.realtors.rets.server.Util;

public class RetsConfig
{
    public RetsConfig()
    {
        mPort = 6103;
        mNonceInitialTimeout = -1;
        mNonceSuccessTimeout = -1;
        mSecurityConstraints = new SecurityConstraints();
        mBlockLocation = false;
        mStrictParsing = true;
    }
    
    public Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException();
    }

    public String getAddress()
    {
        return mAddress;
    }

    public void setAddress(String address)
    {
        if (StringUtils.isBlank(address))
            mAddress = null;
        else
            mAddress = address;
    }

    public int getPort()
    {
        return mPort;
    }

    public void setPort(int port)
    {
        mPort = port;
    }

    public String getGetObjectRoot()
    {
        return mGetObjectRoot;
    }

    public void setGetObjectRoot(String getObjectRoot)
    {
        mGetObjectRoot = getObjectRoot;
    }

    public String getPhotoPattern()
    {
        return mPhotoPattern;
    }

    public void setPhotoPattern(String photoPattern)
    {
        mPhotoPattern = photoPattern;
    }

    public String getObjectSetPattern()
    {
        return mObjectSetPattern;
    }

    public void setObjectSetPattern(String objectSetPattern)
    {
        mObjectSetPattern = objectSetPattern;
    }

    public int getNonceInitialTimeout()
    {
        return mNonceInitialTimeout;
    }

    public void setNonceInitialTimeout(int nonceInitialTimeout)
    {
        mNonceInitialTimeout = nonceInitialTimeout;
    }

    public int getNonceSuccessTimeout()
    {
        return mNonceSuccessTimeout;
    }

    public void setNonceSuccessTimeout(int nonceSuccessTimeout)
    {
        mNonceSuccessTimeout = nonceSuccessTimeout;
    }

    public DatabaseConfig getDatabase()
    {
        return mDatabase;
    }

    public void setDatabase(DatabaseConfig database)
    {
        mDatabase = database;
    }
    
    public boolean getStrictParsing()
    {
        return mStrictParsing;
    }
    
    public void setStrictParsing(boolean strict)
    {
        mStrictParsing = strict;
    }
    
    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("GetObject root", mGetObjectRoot)
            .append("GetObject photo pattern", mPhotoPattern)
            .append("nonce initial timeout", mNonceInitialTimeout)
            .append("nonce success timeout", mNonceSuccessTimeout)
            .append("strict parsing", mStrictParsing)
            .append(mDatabase)
            .toString();
    }

    public String getPhotoPattern(String defaultValue)
    {
        return getDefault(mPhotoPattern, defaultValue);
    }

    public String  getGetObjectRoot(String defaultValue)
    {
        return getDefault(mGetObjectRoot, defaultValue);
    }

    public String getObjectSetPattern(String defaultValue)
    {
        return getDefault(mObjectSetPattern, defaultValue);
    }

    public int getNonceInitialTimeout(int defaultValue)
    {
        return getDefault(mNonceInitialTimeout, defaultValue);
    }

    public int getNonceSuccessTimeout(int defaultValue)
    {
        return getDefault(mNonceSuccessTimeout, defaultValue);
    }

    private String getDefault(String string, String defaultValue)
    {
        if (string == null)
        {
            return defaultValue;
        }
        else
        {
            return string;
        }
    }

    private int getDefault(int number, int defaultValue)
    {
        if (number == -1)
        {
            return defaultValue;
        }
        else
        {
            return number;
        }
    }

    public Properties createHibernateProperties()
    {
        return mDatabase.createHibernateProperties();
    }

    public void setMetadataDir(String metadataDir)
    {
        /*
         * See if the metadata directory changed and we need to force a reload.
         */
        if (mMetadataDir != null && !mMetadataDir.equals(metadataDir))
        {
            sMetadata = null;
        }
        mMetadataDir = metadataDir;
    }

    public String getMetadataDir()
    {
        return mMetadataDir;
    }

    public String getMetadataDir(String defaultValue)
    {
        return getDefault(mMetadataDir, defaultValue);
    }
 
    public List getAllGroupRules()
    {
        return mSecurityConstraints.getAllGroupRules();
    }

    public void setAllGroupRules(List allGroupRules) {
        mSecurityConstraints.setAllConstraints(allGroupRules);
    }

    public SecurityConstraints getSecurityConstraints()
    {
        return mSecurityConstraints;
    }

    public void setSecurityConstraints(List securityConstraints)
    {
        mSecurityConstraints.setAllConstraints(securityConstraints);
    }

    public boolean getBlockLocation()
    {
        return mBlockLocation;
    }

    public void setBlockLocation(boolean blockLocation)
    {
        mBlockLocation = blockLocation;
    }
    
    public Integer getId()
    {
        return this.mId;
    }

    public void setId(Integer id)
    {
        this.mId = id;
    }

    public Map getExtendableProperties()
    {
        if (mExtendableProperties == null)
        {
            mExtendableProperties = new LinkedHashMap();
        }
        return mExtendableProperties;
    }

    public void setExtendableProperties(Map extendableProperties)
    {
        mExtendableProperties = extendableProperties;
    }

    public Object getExtendableProperty(String name)
    {
        return getExtendableProperties().get(name);
    }

    public void setExtendableProperty(String name, Object value)
    {
        getExtendableProperties().put(name, value);
    }

    /**
     * Locate and load the metadata.
     * @return Metadata.
     */
    public Metadata getMetadata()
    {
        if (sMetadata == null)
        {
            File systemRoot = new File(getMetadataDir());
            
            if (systemRoot.isDirectory())
            {
                List files = new ArrayList();
                /*
                 * See if metadata.xml exists. If so, open it and process it.
                 */
                try
                {
                    File metadata = new File(systemRoot + File.separator + "metadata.xml");
                    if (metadata.isFile() && metadata.canRead())
                    {
                        files.add(metadata);
                        LOG.info("Found metadata.xml at: " + metadata.getCanonicalPath());
                    }
                }
                catch (Exception e)
                {
                    sMetadata = new Metadata(new MSystem());
                    LOG.warn("Unable to locate metadata.xml: " + e);
                    return sMetadata;
                }
                /*
                 * If metadata.xml doesn't exist, assume old metadata format and recurse the
                 * directory, locating all .xml files.
                 */
                if (files.isEmpty())
                {
                    try
                    {
                        files = IOUtils.listFilesRecursive(
                                            new File(getMetadataDir()), 
                                            new MetadataFileFilter());
                        LOG.info("Looking for metadata in old format.");
                    }
                    catch (Exception e)
                    {
                        sMetadata = new Metadata(new MSystem());
                        LOG.warn("Unable to locate metadata: " + e);
                        return sMetadata;
                    }
                }
                try
                {
                    LOG.debug("Merging metadata into a single XML document.");
                    List documents = parseAllFiles(files);
                    Document merged = (Document)documents.get(0);
                    if (documents.size() > 1)
                        merged = JdomUtils.mergeDocuments(documents, 
                                                            new Element("RETS"));
                    JDomCompactBuilder builder = new JDomCompactBuilder();
                    builder.setStrict(getStrictParsing());
                    sMetadata = builder.build(merged);
                }
                catch (Exception e)
                {
                    sMetadata = new Metadata(new MSystem());
                    LOG.error("Unable to merge metadata: " + e);
                    return sMetadata;
                }
            }
            else
            {
                sMetadata = new Metadata(new MSystem());
            }
        }
        return sMetadata;
    }
    
    public Metadata reloadMetadata()
    {
        sMetadata = null;
        return getMetadata();
    }
    
    public boolean saveMetadata()
    {
        if (sMetadata == null)
        {
            /*
             * Nothing to save.
             */
            return true;
        }
        
        File systemRoot = new File(getMetadataDir());
        
        if (systemRoot.isDirectory())
        {
            /*
             * See if metadata.xml exists. If so, open it and process it.
             */
            try
            {
                File backup = new File(systemRoot + File.separator + "metadata.xml-");
                File metadata = new File(systemRoot + File.separator + "metadata.xml");
                /*
                 * If there is an old backup file, rmeove it.
                 */
                if (backup.isFile() && backup.canWrite())
                {
                    LOG.debug("Deleteing " + backup.getCanonicalFile());
                    backup.delete();
                }
                /*
                 * Rename the current file to the backup.
                 */
                if (metadata.isFile() && metadata.canWrite())
                {
                       LOG.debug("Renaming " + metadata.getCanonicalPath() + 
                                   " to " + backup.getCanonicalFile());
                    metadata.renameTo(backup);
                }
                if (!metadata.isFile() && !metadata.isDirectory())
                {
                    metadata.createNewFile();
                }
                if (metadata.isFile() && metadata.canWrite())
                {
                    LOG.debug("Formatting metadata into COMPACT format.");
                    PrintWriter out = new PrintWriter(metadata);
                    MetadataCompactFormatter formatter = new MetadataCompactFormatter(sMetadata, 
                                                                        out, RetsVersion.RETS_1_7_2);
                    formatter.output();
                    out.close();
                    return true;
                }
            }
            catch (Exception e)
            {
                LOG.error("Unable to save metadata: " + e);
            }
        }
        return false;
    }
        
        
    /**
     * Parses all files, returning a list of JDOM Document objects.
     *
     * @param files list of File objects
     * @return a list of Document objects
     * @throws org.jdom.JDOMException if a JDOM error occurs
     * @throws java.io.IOException if an I/O error occurs
     */
    private List parseAllFiles(List files)
        throws JDOMException, IOException
    {
        List documents = new ArrayList();
        SAXBuilder builder = new SAXBuilder();
        for (int i = 0; i < files.size(); i++)
        {
            File file = (File) files.get(i);
            documents.add(builder.build(file));
        }
        return documents;
    }
    
    /**
     * Filters out directories and files that are not metadata, in particular
     * files used by the 1.0 version of the RETS server. Metadata files must
     * have a ".xml" extension. Certain directories, like Notices, Roles, and
     * Template do not contain any metadata, so they are skipped completely.
     */
    private static final class MetadataFileFilter implements FileFilter
    {
        public boolean accept(File file)
        {
            if (file.isDirectory())
            {
                return false;
            }

            // These directories do not contain metadata
            String parent = file.getParent();
            if (StringUtils.contains(parent, "Notices") ||
                StringUtils.contains(parent, "Roles") ||
                StringUtils.contains(parent, "Template") ||
                StringUtils.contains(parent, ".svn"))
            {
                return false;
            }

            if (file.getName().endsWith(".xml"))
            {
                return true;
            }
            // Everything else is not considered metadata
            return false;
        }
    }

    private static final Logger LOG =
        Logger.getLogger(RetsConfig.class);

    private String mAddress;
    private int mPort;
    private String mGetObjectRoot;
    private String mPhotoPattern;
    private String mObjectSetPattern;
    private int mNonceInitialTimeout;
    private int mNonceSuccessTimeout;
    private DatabaseConfig mDatabase;
    private String mMetadataDir;
    private SecurityConstraints mSecurityConstraints;
    private boolean mBlockLocation;
    private boolean mStrictParsing;
    private Integer mId;
    private Map mExtendableProperties;
    
    private static Metadata      sMetadata = null;
}
