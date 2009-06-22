/*
 * Variman RETS Server
 *
 * Author: Dave Dribin and Danny Hurlburt
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.webapp;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.realtors.rets.server.QueryCountTable;
import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.config.GroupRules;
import org.realtors.rets.server.config.RetsConfig;
import org.realtors.rets.server.config.RetsConfigLoader;
import org.realtors.rets.server.config.RetsConfigUtils;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.MetadataLoader;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.protocol.ConditionRuleSet;
import org.realtors.rets.server.protocol.TableGroupFilter;
import org.realtors.rets.server.webapp.auth.NonceReaper;
import org.realtors.rets.server.webapp.auth.NonceTable;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WebApp
{    
    private WebApp() {
        // Prevents instantiation.
    }
    
    public static void load(ServletContext servletContext) throws RetsServerException
    {
        synchronized (sLock) {
            if (sMustReload) {
                setServletContext(servletContext);
                try {
                    initReleaseProperties();
                    initRetsConfiguration();
                    initMetadata();
                    initGroupRulesFieldFilter();
                    initConditionRuleSet();
                } catch (IOException e) {
                    throw new RetsServerException(e);
                }
                sMustReload = false;
            }
        }
    }
    
    public static void invalidate()
    {
        synchronized (sLock) {
            sMustReload = true;
        }
    }
    
    private static void setServletContext(ServletContext servletContext)
    {
        if (servletContext == null && sServletContext == null) {
            // This ensures that once the WebApp is loaded, that servletContext
            // is never null.
            throw new IllegalArgumentException("servletContext must not be null.");
        }
        sServletContext = servletContext;
    }

    public static ServletContext getServletContext()
    {
        synchronized (sLock) {
            if (sServletContext == null) {
                throw new IllegalStateException("Must set the servlet context via the load method before calling.");
            }
            return sServletContext;
        }
    }

    public static MetadataManager getMetadataManager()
    {
        synchronized (sLock) {
            return sMetadataManager;
        }
    }

    public static String getGetObjectRoot()
    {
        synchronized (sLock) {
            String getObjectRoot = null;
            if (sRetsConfig == null) {
                return getObjectRoot;
            }
            getObjectRoot = sRetsConfig.getGetObjectRoot();
            return getObjectRoot;
        }
    }

    public static String getPhotoPattern()
    {
        synchronized (sLock) {
            String photoPattern = null;
            if (sRetsConfig == null) {
                return photoPattern;
            }
            photoPattern = sRetsConfig.getPhotoPattern();
            return photoPattern;
        }
    }

    public static String getObjectSetPattern()
    {
        synchronized (sLock) {
            String objectSetPattern = null;
            if (sRetsConfig == null) {
                return objectSetPattern;
            }
            objectSetPattern = sRetsConfig.getObjectSetPattern();
            return objectSetPattern;
        }
    }

    public static NonceTable getNonceTable()
    {
        synchronized (sLock) {
            return sNonceTable;
        }
    }

    public static NonceReaper getNonceReaper()
    {
        synchronized (sLock) {
            return sNonceReaper;
        }
    }

    public static String getVersion()
    {
        synchronized (sLock) {
            return sVersion;
        }
    }

    public static String getBuildDate()
    {
        synchronized (sLock) {
            return sBuildDate;
        }
    }

    public static void setHPMAMode(boolean mode)
    {
        sHPMAMode = mode;
    }
    
    public static boolean getHPMAMode()
    {
        return sHPMAMode;
    }
    
    private static void initReleaseProperties() throws IOException
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        properties.load(classLoader.getResourceAsStream(PROJECT_NAME + "-webapp.properties"));
        sVersion = properties.getProperty("version");
        sBuildDate = properties.getProperty("build-date");
    }

    private static void initRetsConfiguration() throws RetsServerException
    {
        RetsConfigLoader retsConfigLoader = RetsServer.getRetsConfigLoader();
        retsConfigLoader.invalidate();
        sRetsConfig = retsConfigLoader.load();
        assertRetsConfigurationValid(sRetsConfig);

        RetsServer.setSecurityConstraints(sRetsConfig.getSecurityConstraints());
        RetsServer.setQueryCountTable(new QueryCountTable());
        initNonceTable();
    }
    
    private static void assertRetsConfigurationValid(RetsConfig retsConfig)
    {
        checkGetObjectRoot(retsConfig);
    }

    private static void checkGetObjectRoot(RetsConfig retsConfig)
    {
        ServletContext context = getServletContext();
        String getObjectRoot = RetsConfigUtils.getGetObjectRoot(retsConfig, context.getRealPath("/"));
        File getObjectRootFile = new File(getObjectRoot);
        if (!getObjectRootFile.exists()) {
            LOGGER.warn("GetObject root does not exist: " + getObjectRoot);
        } else if (!getObjectRootFile.isDirectory()) {
            LOGGER.warn("GetObject root is not a directory: " + getObjectRoot);
        } else if (!getObjectRootFile.canRead()) {
            LOGGER.warn("GetObject root is not readable: " + getObjectRoot);
        }
    }

    private static void initMetadata() throws RetsServerException
    {
        LOGGER.debug("Initializing metadata");
        MSystem system = null;
        MetadataLoader metadataLoader = RetsServer.getMetadataLoader();
        metadataLoader.invalidate();
        system = metadataLoader.load();
        LOGGER.debug("Creating metadata manager");
        MetadataManager manager = new MetadataManager();
        manager.addRecursive(system);
        LOGGER.debug("Adding metadata to servlet context");
        sMetadataManager = manager;
    }

    private static void initNonceTable()
    {
        NonceTable nonceTable = new NonceTable();
        int initialTimeout = sRetsConfig.getNonceInitialTimeout();
        if (initialTimeout != -1) {
            nonceTable.setInitialTimeout(initialTimeout * DateUtils.MILLIS_PER_MINUTE);
            LOGGER.info("Set initial nonce timeout to " + initialTimeout + " minutes");
        }

        int successTimeout = sRetsConfig.getNonceSuccessTimeout();
        if (successTimeout != -1) {
            nonceTable.setSuccessTimeout(successTimeout * DateUtils.MILLIS_PER_MINUTE);
            LOGGER.info("Set success nonce timeout to " + successTimeout + " minutes");
        }
        sNonceTable = nonceTable;
        sNonceReaper = new NonceReaper(nonceTable);
    }
    
    /*
     * Must be called after initializing the metadata manager and the RETS
     * configuration.
     */
    private static void initGroupRulesFieldFilter()
    {
        LOGGER.debug("Initializing group filter");
        TableGroupFilter groupFilter = new TableGroupFilter();
        MSystem system = (MSystem)sMetadataManager.findUnique(MSystem.TABLE, "");
        Set resources = system.getResources();
        for (Iterator resIter = resources.iterator(); resIter.hasNext(); ) {
            Resource resource = (Resource)resIter.next();
            String resourceID = resource.getResourceID();
            Set classes = resource.getClasses();
            for (Iterator clsIter = classes.iterator(); clsIter.hasNext(); ) {
                MClass aClass = (MClass)clsIter.next();
                String className = aClass.getClassName();
                LOGGER.debug("Setting tables for " + resourceID + ":" + className);
                groupFilter.setTables(resourceID, className, aClass.getTables());
            }
        }
        List groupRulesSet = sRetsConfig.getSecurityConstraints().getAllGroupRules();
        for (Iterator iter = groupRulesSet.iterator(); iter.hasNext(); ) {
            GroupRules rules = (GroupRules)iter.next();
            LOGGER.debug("Adding rules for " + rules.getGroup().getName());
            groupFilter.addRules(rules);
        }

        RetsServer.setTableGroupFilter(groupFilter);
    }

    /*
     * Must be called after initializing the RETS configuration.
     */
    private static void initConditionRuleSet()
    {
        LOGGER.debug("Initializing condition rule set");
        ConditionRuleSet conditionRuleSet = new ConditionRuleSet();
        List groupRulesSet = sRetsConfig.getSecurityConstraints().getAllGroupRules();
        for (Iterator iter = groupRulesSet.iterator(); iter.hasNext(); ) {
            GroupRules rules = (GroupRules)iter.next();
            LOGGER.debug("Adding condition rules for " + rules.getGroup().getName());
            conditionRuleSet.addRules(rules);
        }
        RetsServer.setConditionRuleSet(conditionRuleSet);
    }

    public static final String PROJECT_NAME = "variman";
    public static final String SERVER_NAME = "Variman";

    private static ServletContext sServletContext;
    private static MetadataManager sMetadataManager;
    private static NonceTable sNonceTable;
    private static NonceReaper sNonceReaper;
    private static String sVersion;
    private static String sBuildDate;
    private static RetsConfig sRetsConfig;
    private static boolean sMustReload = true;
    private static boolean sHPMAMode;
    private static final WebApp sLock = new WebApp();
    private static final Log LOGGER = LogFactory.getLog(WebApp.class);
    
}
