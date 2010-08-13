/*
 * Variman RETS Server
 *
 * Author: Danny
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server.metadata;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import org.realtors.rets.server.config.RetsConfig;

/**
 * An implementation of the {@link MetadataDao} interface which reads and writes
 * the metadata information to an XML file in RETS 1.7.2 compact format.
 * 
 * @author Danny
 */
public class XmlMetadataDao implements MetadataDao {

    private static final Logger LOG = Logger.getLogger(XmlMetadataDao.class);

    /*- (non-Javadoc)
     * @see org.realtors.rets.common.metadata.MetadataDao#getMetadata()
     */
    public Metadata getMetadata() {
        Metadata metadata = null;
        final String FS = File.separator;
        RetsConfig retsConfig = RetsConfig.getInstance();
        String metadataDir = retsConfig.getMetadataDir();
        boolean strictParsing = retsConfig.getStrictParsing();
        File rootDir = new File(metadataDir);
        
        if (rootDir.isDirectory())
        {
            List<File> files = new ArrayList<File>();
            /*
             * See if metadata.xml exists. If so, open it and process it.
             */
            try
            {
                File metadataFile = new File(rootDir + FS + "metadata.xml");
                if (metadataFile.isFile() && metadataFile.canRead())
                {
                    if (LOG.isInfoEnabled()) {
                        String msg = "Found metadata.xml at: " + metadataFile.getCanonicalPath();
                        LOG.info(msg);
                    }
                    files.add(metadataFile);
                }
            }
            catch (Exception e)
            {
                metadata = new Metadata(new MSystem());
                LOG.warn("Unable to locate metadata.xml: " + e);
                return metadata;
            }
            /*
             * If metadata.xml doesn't exist, assume old metadata format and recurse the
             * directory, locating all .xml files.
             */
            if (files.isEmpty())
            {
                try
                {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Looking for metadata in old format.");
                    }
                    files = IOUtils.listFilesRecursive(
                            new File(metadataDir),
                            new MetadataFileFilter()
                    );
                }
                catch (Exception e)
                {
                    metadata = new Metadata(new MSystem());
                    LOG.warn("Unable to locate metadata: " + e);
                    return metadata;
                }
            }
            try
            {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Merging metadata into a single XML document.");
                }
                List<Document> documents = parseAllFiles(files);
                Document merged = documents.get(0);
                if (documents.size() > 1) {
                    merged = JdomUtils.mergeDocuments(
                            documents,
                            new Element("RETS")
                    );
                }
                JDomCompactBuilder builder = new JDomCompactBuilder();
                builder.setStrict(strictParsing);
                metadata = builder.build(merged);
            }
            catch (Exception e)
            {
                metadata = new Metadata(new MSystem());
                LOG.error("Unable to merge metadata: " + e);
                return metadata;
            }
        }
        else
        {
            metadata = new Metadata(new MSystem());
        }
        return metadata;
    }

    /*- (non-Javadoc)
     * @see org.realtors.rets.common.metadata.MetadataDao#saveMetadata(org.realtors.rets.common.metadata.Metadata)
     */
    public void saveMetadata(Metadata metadata) {
        _saveMetadata(metadata);
    }

    private static void _saveMetadata(Metadata metadata) {
        if (metadata == null) {
            // Nothing to save.
            return;
        }
        
        final String FS = File.separator;
        String metadataDir = RetsConfig.getInstance().getMetadataDir();
        File rootDir = new File(metadataDir);
        
        if (rootDir.isDirectory())
        {
            /*
             * See if metadata.xml exists. If so, open it and process it.
             */
            try
            {
                File backup = new File(rootDir + FS + "metadata.xml-");
                File metadataFile = new File(rootDir + FS + "metadata.xml");
                /*
                 * If there is an old backup file, rmeove it.
                 */
                if (backup.isFile() && backup.canWrite())
                {
                    if (LOG.isDebugEnabled()) {
                        String backupFileName = backup.getCanonicalPath();
                        String msg = "Deleting " + backupFileName;
                        LOG.debug(msg);
                    }
                    backup.delete();
                }
                /*
                 * Rename the current file to the backup.
                 */
                if (metadataFile.isFile() && metadataFile.canWrite())
                {
                    if (LOG.isDebugEnabled()) {
                        String backupFileName = backup.getCanonicalPath();
                        String metadataFileName = metadataFile.getCanonicalPath();
                        String msg = "Renaming " + metadataFileName + " to " + backupFileName;
                        LOG.debug(msg);
                    }
                    metadataFile.renameTo(backup);
                }
                if (!metadataFile.isFile() && !metadataFile.isDirectory())
                {
                    metadataFile.createNewFile();
                }
                if (metadataFile.isFile() && metadataFile.canWrite())
                {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Formatting metadata into COMPACT format.");
                    }
                    PrintWriter out = null;
                    try {
                        out = new PrintWriter(metadataFile);
                        MetadataCompactFormatter formatter = new MetadataCompactFormatter(
                                metadata,
                                out,
                                RetsVersion.RETS_1_7_2
                        );
                        formatter.output();
                    } finally {
                        if (out != null) {
                            out.close();
                        }
                    }
                }
            }
            catch (Exception e)
            {
                LOG.error("Unable to save metadata: " + e);
            }
        }
    }

    /**
     * Parses all files, returning a list of JDOM Document objects.
     *
     * @param files list of File objects
     * @return a list of Document objects
     * @throws JDOMException if a JDOM error occurs
     * @throws IOException if an I/O error occurs
     */
    private static List<Document> parseAllFiles(List<File> files)
            throws JDOMException, IOException
    {
        List<Document> documents = new ArrayList<Document>();
        SAXBuilder builder = new SAXBuilder();
        for (File file : files) {
            Document document = builder.build(file);
            documents.add(document);
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
        public MetadataFileFilter() {
            // Nothing to initialize.
        }
        
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

    /*- (non-Javadoc)
     * @see org.realtors.rets.server.metadata.MetadataDao#getChangedDate()
     */
    public Date getChangedDate() {
        RetsConfig retsConfig = RetsConfig.getInstance();
        String metadataDir = retsConfig.getMetadataDir();
        File metadataFile = new File(metadataDir);
        Date changedDate = new Date(metadataFile.lastModified());
        return changedDate;
    }

}
