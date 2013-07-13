/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class Bootstrap
{
    public Bootstrap()
    {
        setMain("Main");
    }

    private void setMain(String main)
    {
        mMain = "org.realtors.rets.server.admin." + main;
    }

    private void callMain(String[] args)
        throws Exception
    {
        initHomeDirectory();
        List urlList = new ArrayList();
        urlList.add(new File(mHomeDirectory, "admin/classes/").toURL());
        addJars(urlList, "admin/lib");
        addJars(urlList, "variman/WEB-INF/lib");
        URL[] urls = (URL[]) urlList.toArray(new URL[urlList.size()]);
        URLClassLoader classLoader = new URLClassLoader(urls);
        Thread.currentThread().setContextClassLoader(classLoader);
        Class main = classLoader.loadClass(mMain);
        Class[] paramTypes = new Class[]{args.getClass()};
        Object[] paramValues = new Object[]{args};
        Method method = main.getMethod("main", paramTypes);
        method.invoke(null, paramValues);
    }

    private void initHomeDirectory()
    {
        mHomeDirectory = System.getProperty("variman.home");
        if (mHomeDirectory == null)
        {
            mHomeDirectory = System.getProperty("user.dir");
            System.setProperty("variman.home", mHomeDirectory);
        }
    }

    private void addJars(List urlList, String directory)
        throws MalformedURLException
    {
        File libDir = new File(mHomeDirectory, directory);
        File[] jars = libDir.listFiles(new FilenameFilter()
        {
            public boolean accept(File dir, String name)
            {
                if (name.endsWith(".jar"))
                {
                    return true;
                }
                return false;
            }
        });

        if (jars != null)
        {
	        for (int i = 0; i < jars.length; i++)
	        {
	            File jar = jars[i].getAbsoluteFile();
	            urlList.add(jar.toURL());
	        }
        }
    }

    public static void main(String[] args)
        throws Exception
    {
        Bootstrap bootstrap = new Bootstrap();
        String main = System.getProperty("app.main");
        if (main != null)
        {
            bootstrap.setMain(main);
        }
        bootstrap.callMain(args);

    }

    private String mHomeDirectory;
    private String mMain;
}
