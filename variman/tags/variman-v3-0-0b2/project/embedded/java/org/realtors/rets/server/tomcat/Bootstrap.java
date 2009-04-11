/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

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
        ClassLoader commonLoader = null;
        ClassLoader serverLoader = null;
        try
        {
//            ClassLoaderFactory.setDebug(1);
            File packed[] = new File[1];
            packed[0] = new File("common/lib");
            commonLoader =
                ClassLoaderFactory.createClassLoader(null, packed, null);
//            ((StandardClassLoader) commonLoader).setDebug(10) ;

            packed[0] = new File("server/lib");
            serverLoader = ClassLoaderFactory.createClassLoader(null, packed,
                                                                commonLoader);
//            ((StandardClassLoader) serverLoader).setDebug(10);
        }
        catch (Throwable t)
        {

            log("Class loader creation threw exception", t);
            System.exit(1);

        }


        Thread.currentThread().setContextClassLoader(serverLoader);

        try
        {
            loadClasses(serverLoader);
            Class paramTypes[];
            Object paramValues[];
            Class mainClass;
            Method method;

            log("Loading class");
            mainClass = serverLoader.loadClass(
                "org.realtors.rets.server.tomcat.EmbeddedTomcat");
//            mainClass = Class.forName(
//                "org.realtors.rets.server.tomcat.EmbeddedTomcat", true,
//                classLoader);
            log("Setting parent classloader");
            paramTypes = new Class[] { ClassLoader.class };
            paramValues = new Object[] { commonLoader };
            method = mainClass.getMethod("setParentClassLoader", paramTypes);
            method.invoke(null, paramValues);

            log("Launching main");
            paramTypes = new Class[] { args.getClass() };
            paramValues = new Object[] { args };
            method = mainClass.getMethod("main", paramTypes);
            method.invoke(null, paramValues);

        }
        catch (Exception e)
        {
            System.out.println("Exception during startup processing");
            e.printStackTrace(System.out);
            System.exit(2);
        }

    }

    private static void loadClasses(ClassLoader loader)
        throws Exception
    {
        log("Loading logger");
        loader.loadClass("org.apache.catalina.Logger");
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
