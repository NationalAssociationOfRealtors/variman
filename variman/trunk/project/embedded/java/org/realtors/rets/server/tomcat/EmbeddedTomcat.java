package org.realtors.rets.server.tomcat;

import java.net.URL;

import org.apache.catalina.Connector;
import org.apache.catalina.Context;
import org.apache.catalina.Deployer;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.logger.SystemOutLogger;
import org.apache.catalina.realm.MemoryRealm;
import org.apache.catalina.startup.Embedded;

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
    }

    public void setPath(String path)
    {
        mPath = path;
    }

    public String getPath()
    {
        return mPath;
    }

    public void startTomcat()
        throws Exception
    {
        // Set the home directory
        System.setProperty("catalina.home", mPath);

        // Create an embedded server
        mEmbedded = new Embedded();
        // Print all log statments to standard error
        mEmbedded.setDebug(0);
        mEmbedded.setLogger(new SystemOutLogger());

        // Create an engine
        Engine engine = mEmbedded.createEngine();
        engine.setDefaultHost("localhost");

        // Create a default virtual host
        mHost = mEmbedded.createHost("localhost", mPath + "/webapps");
        engine.addChild(mHost);
        engine.setRealm(new MemoryRealm());

        // Install the assembed container hierarchy
        mEmbedded.addEngine(engine);

        // Assembled and install a default HTTP connector
        int port = 0;

        String myport = System.getProperty("retszilla.port");
        if (myport != null)
        {
            try
            {
                port = Integer.parseInt(myport);
            }
            catch (NumberFormatException e)
            {
                port = 6103;
            }
        }
        if (port == 0)
        {
            port = 6103;
        }
        Connector connector = mEmbedded.createConnector(null, port, false);
        mEmbedded.addConnector(connector);

        // Start the embedded server
        mEmbedded.start();
    }

    public void stopTomcat()
        throws Exception
    {
        mEmbedded.stop();
        synchronized(this)
        {
            notify();
        }
    }

    public void waitUntilStopped()
        throws InterruptedException
    {
        synchronized(this)
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

        Deployer deployer = (Deployer) mHost;
        Context context = deployer.findDeployedApp(contextPath);
        if (context != null)
        {
            throw new Exception("Context " + contextPath + " already exists");
        }
        deployer.install(contextPath, webapp);
    }

    public static void main(String args[])
    {
        try
        {
            EmbeddedTomcat tomcat = EmbeddedTomcat.getInstance();
//            String workingDir = System.getProperty("catalina.realhome",
//                System.getProperty("user.dir"));
            String workingDir = System.getProperty("user.dir");
            tomcat.setPath(workingDir);
            tomcat.startTomcat();

            URL url = new URL("file:" + workingDir + "/webapp");
            tomcat.registerWebapp("/", url);

            /*
            // Two minutes
            Thread.sleep(2*60*1000L);

            tomcat.stopTomcat();
            */
            tomcat.waitUntilStopped();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String mPath;
    private Embedded mEmbedded;
    private Host mHost;
}
