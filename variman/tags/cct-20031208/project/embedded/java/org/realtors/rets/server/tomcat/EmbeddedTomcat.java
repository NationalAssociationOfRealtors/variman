package org.realtors.rets.server.tomcat;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.StringTokenizer;

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
    public class JarFileFilter implements FileFilter
    {
        /* (non-Javadoc)
         * @see java.io.FileFilter#accept(java.io.File)
         */
        public boolean accept(File pathname)
        {
            if (pathname.isFile())
            {
                String name = pathname.getName();
                return name.endsWith(".jar");
            }
            return false;
        }

    }

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

        Deployer deployer = (Deployer) mHost;
        Context context = deployer.findDeployedApp(contextPath);
        if (context != null)
        {
            throw new Exception("Context " + contextPath + " already exists");
        }
        deployer.install(contextPath, webapp);
    }
    

    
    /**
     * Creates a classPath based on what's passed in.  When "java -jar" is
     * called, it sets java.class.path incorrectly.  This functions attempts
     * to fix that by building the classpath based on what is passed in.
     * "java -jar" always only has one element in it.  If there is more than
     * one element in the classPath, its just tossed back.
     * 
     * @param classPath passed in class path
     * @return String a "fixed" classpath
     */
    public String buildClassPath(String classPath)
    {
        // How many items are in the classpath?
        StringTokenizer tok =
            new StringTokenizer(classPath, File.pathSeparator);
        if (tok.countTokens() > 1)
        {
            return classPath; 
        }

        // Create a classpath based on the known entity.            
        File file = new File(classPath);
        File dir = file.getParentFile();
        File jarFiles[] = dir.listFiles(new JarFileFilter());
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < jarFiles.length; i++)
        {
            if (i > 0)
            {
                buffer.append(File.pathSeparatorChar);
            }
            buffer.append(jarFiles[i].getPath());
        }
        return buffer.toString();
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

            // fix the classpath
            System.setProperty("java.class.path",
                tomcat.buildClassPath(System.getProperty("java.class.path")));

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
