package org.realtors.rets.server.tomcat;


import java.io.File;
import java.lang.reflect.Method;

import org.apache.catalina.startup.ClassLoaderFactory;


/**
 * Boostrap loader for RETS server. This is based off of Tomcat's bootstrap.
 */

public final class Bootstrap
{
    /**
     * The main program for the bootstrap.
     *
     * @param args Command line arguments to be processed
     */
    public static void main(String args[])
    {
        ClassLoader classLoader = null;
        try
        {

            File packed[] = new File[1];
            packed[0] = new File("lib");
            classLoader =
                ClassLoaderFactory.createClassLoader(null, packed, null);
        }
        catch (Throwable t)
        {

            log("Class loader creation threw exception", t);
            System.exit(1);

        }


        Thread.currentThread().setContextClassLoader(classLoader);

        try
        {
            Class paramTypes[] = new Class[] { args.getClass() };
            Object paramValues[] = new Object[] { args };
            Method method =
                EmbeddedTomcat.class.getMethod("main", paramTypes);
            method.invoke(null, paramValues);

        }
        catch (Exception e)
        {
            System.out.println("Exception during startup processing");
            e.printStackTrace(System.out);
            System.exit(2);
        }

    }

    /**
     * Log a debugging detail message.
     *
     * @param message The message to be logged
     */
    private static void log(String message)
    {

        System.out.print("Bootstrap: ");
        System.out.println(message);

    }


    /**
     * Log a debugging detail message with an exception.
     *
     * @param message The message to be logged
     * @param exception The exception to be logged
     */
    private static void log(String message, Throwable exception)
    {

        log(message);
        exception.printStackTrace(System.out);

    }


}
