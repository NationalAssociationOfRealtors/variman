/*
 */
package org.realtors.rets.server.webapp;

import javax.servlet.ServletContext;

import org.realtors.rets.server.metadata.MetadataManager;

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

    private static ServletContext sServletContext;
    private static MetadataManager sMetadataManager;
}
