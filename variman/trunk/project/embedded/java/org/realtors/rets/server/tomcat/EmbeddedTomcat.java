/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.tomcat;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.InetAddress;
import java.util.Enumeration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.InstanceEvent;
import org.apache.catalina.InstanceListener;
import org.apache.catalina.Wrapper;
import org.apache.catalina.realm.MemoryRealm;
import org.apache.catalina.startup.Embedded;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class EmbeddedTomcat
{

    public static synchronized EmbeddedTomcat getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new EmbeddedTomcat();
        }
        return mInstance;
    }

    private static EmbeddedTomcat mInstance;

    private EmbeddedTomcat()
    {
        mInitFailed = true;
    }

    public void setHomeDirectory(String path)
    {
        mHomeDirectory = path;
        mCatalinaHome = mHomeDirectory + File.separator + "server";
    }

    public String getHomeDirectory()
    {
        return mCatalinaHome;
    }

    public void startTomcat()
        throws Exception
    {
        readRetsConfig();
        // Set the Tomcat home directory
        System.setProperty("catalina.home", mCatalinaHome);
        System.setProperty("catalina.base", mCatalinaHome);
        // Set Tomcat logging to a file instead of to the console
        System.setProperty("java.util.logging.config.file",
        		mCatalinaHome + File.separator +
        		"conf" + File.separator +
        		"tomcat.logging.properties");
        
        // Create an embedded server
        mEmbedded = new Embedded();

        // Create an engine
        Engine engine = mEmbedded.createEngine();
        engine.setDefaultHost("localhost");

        // Create a default virtual host
        mHost = mEmbedded.createHost("localhost", mCatalinaHome + "/webapps");
        engine.addChild(mHost);
        engine.setRealm(new MemoryRealm());

        // Install the assembled container hierarchy
        mEmbedded.addEngine(engine);

        // Assembled and install a default HTTP connector
       Connector connector = mEmbedded.createConnector(
           getAddress(), getPort(), false);
        mEmbedded.addConnector(connector);

        // Start the embedded server
        mEmbedded.start();

        Runtime.getRuntime().addShutdownHook(new Shutdown());
    }

    private void readRetsConfig()
        throws ParserConfigurationException, SAXException, IOException
    {
        // Use standard DOM rather than RetsConfig to parse the XML file.
        // Moving the Variman jar file into the Tomcat classpath messes up the
        // classloader, and then it cannot be used inside the webapp. I really
        // don't know what games Tomcat plays with classloaders, but it causes
        // all sorts of problems. In any case, we only need the port parameter,
        // so it's easy enough just to use DOM and grab that one XML tag.
        String configFile =
            mHomeDirectory + "/variman/WEB-INF/rets/rets-config.xml";
        DocumentBuilder builder =
            DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(new File(configFile));
        Element root = document.getDocumentElement();
        Node portTag = root.getElementsByTagName("port").item(0);
        mPort = Integer.parseInt(portTag.getFirstChild().getNodeValue());

        mAddress = null;
        Node addressTag = root.getElementsByTagName("address").item(0);
        if (addressTag != null)
        {
            Node firstChild = addressTag.getFirstChild();
            if (firstChild != null)
                mAddress = InetAddress.getByName(firstChild.getNodeValue());
        }
    }

    private InetAddress getAddress()
    {
        return mAddress;
    }

    private int getPort()
    {
        return mPort;
    }

    public void stopTomcat()
        throws Exception
    {
        mEmbedded.stop();
        synchronized (this)
        {
            notify();
        }
    }

    public void waitUntilStopped()
        throws InterruptedException
    {
        synchronized (this)
        {
            wait();
        }
    }

    public void registerWebapp(String contextPath, URL webapp)
        throws Exception
    {
        if (contextPath.equals("/"))
        {
            contextPath = "";
        }

        mContext = mEmbedded.createContext(contextPath, webapp.getFile());
        mContext.addInstanceListener(InitCompleteListener.class.getName());
        Enumeration propertyNames = System.getProperties().propertyNames();
        while (propertyNames.hasMoreElements())
        {
            String name = (String) propertyNames.nextElement();
            if (name.startsWith("context."))
            {
                String value = System.getProperty(name);
                String contextName = name.substring("context.".length());
                mContext.addParameter(contextName, value);
            }
        }
        mHost.addChild(mContext);
    }

    public static class InitCompleteListener implements InstanceListener
    {
        public void instanceEvent(InstanceEvent event)
        {
            Wrapper wrapper = event.getWrapper();
            String servletName = event.getWrapper().getName();
            // Ignore this event, and unregister for future events not coming
            // from the init servlet
            if (!servletName.equals("init-servlet"))
            {
                wrapper.removeInstanceListener(this);
                return;
            }

            // Wait for initialization of servlet to complete
            if (!event.getType().equals(InstanceEvent.AFTER_INIT_EVENT))
            {
                return;
            }

            // Init only succeeded if the init servlet completed without
            // throwing an exception.
            EmbeddedTomcat tomcat = EmbeddedTomcat.getInstance();
            if (event.getException() == null)
            {
                tomcat.setInitFailed(false);
            }
            // We got what we needed, so remove this listener
            wrapper.removeInstanceListener(this);
        }
    }

    public void setInitFailed(boolean initFailed)
    {
        mInitFailed = initFailed;
    }

    public boolean initFailed()
    {
        return mInitFailed;
    }

    public static void main(String args[])
    {
        try
        {
            String homeDirectory = System.getProperty("variman.home");
            if (homeDirectory == null)
            {
                homeDirectory = System.getProperty("user.dir");
                System.setProperty("variman.home", homeDirectory);
            }
            EmbeddedTomcat tomcat = EmbeddedTomcat.getInstance();
            tomcat.setHomeDirectory(homeDirectory);

            tomcat.startTomcat();

            URL url = new URL("file:" + homeDirectory + "/variman");
            tomcat.registerWebapp("/", url);
            if (tomcat.initFailed())
            {
                System.exit(1);
            }
            tomcat.waitUntilStopped();
            System.out.println("Stopped");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    class Shutdown extends Thread
    {
        public void run()
        {
            try
            {
                EmbeddedTomcat.this.stopTomcat();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private String mHomeDirectory;
    private String mCatalinaHome;
    private Embedded mEmbedded;
    private Host mHost;
    private InetAddress mAddress;
    private int mPort;
    private boolean mInitFailed;
    private Context mContext;
}
