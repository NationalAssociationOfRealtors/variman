/*
 */
package org.realtors.rets.server.webapp;

import javax.servlet.ServletContext;

import org.realtors.rets.server.metadata.MetadataManager;

import org.apache.log4j.xml.DOMConfigurator;
import org.apache.log4j.PropertyConfigurator;

public class WebApp
{
    public static void setServletContext(ServletContext servletContext)
    {
        sServletContext = servletContext;
    }

    public static ServletContext getServletContext()
    {
        return sServletContext;
    }

    public static MetadataManager getMetadataManager()
    {
        return sMetadataManager;
    }

    public static void setMetadataManager(MetadataManager metadataManager)
    {
        sMetadataManager = metadataManager;
    }

    public static String getLog4jFile()
    {
        return sLog4jFile;
    }

    public static void setLog4jFile(String log4jFile)
    {
        sLog4jFile = log4jFile;
    }

    public static void loadLog4j()
    {
        if (sLog4jFile.endsWith(".xml"))
        {
            DOMConfigurator.configure(sLog4jFile);
        }
        else
        {
            PropertyConfigurator.configure(sLog4jFile);
        }
    }

    private static ServletContext sServletContext;
    private static MetadataManager sMetadataManager;
    private static String sLog4jFile;
}
