/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;
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

    private void callMain(String[] args) throws Exception
    {
        initRexHome();
        List urlList = new ArrayList();
        addJars(urlList, "admin/lib");
        addJars(urlList, "webapp/WEB-INF/lib");
        urlList.add(new File(mRexHome, "admin/classes/").toURL());
        URL[] urls = (URL[]) urlList.toArray(new URL[urlList.size()]);
        URLClassLoader classLoader = new URLClassLoader(urls);
        Thread.currentThread().setContextClassLoader(classLoader);
        Class main = classLoader.loadClass(mMain);
        Class[] paramTypes = new Class[] { args.getClass() };
        Object[] paramValues = new Object[] { args };
        Method method = main.getMethod("main", paramTypes);
        method.invoke(null, paramValues);
    }

    private void initRexHome()
    {
        mRexHome = System.getProperty("rex.home");
        if (mRexHome == null)
        {
            mRexHome = System.getProperty("user.dir");
            System.setProperty("rex.home", mRexHome);
        }
    }

    private  void addJars(List urlList, String directory)
        throws MalformedURLException
    {
        File libDir = new File(mRexHome, directory);
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

        for (int i = 0; i < jars.length; i++)
        {
            File jar = jars[i].getAbsoluteFile();
            urlList.add(jar.toURL());
        }
    }

    public static void main(String[] args)
        throws Exception
    {
        Bootstrap bootstrap = new Bootstrap();
        String main = System.getProperty("rex.main");
        if (main != null)
        {
            bootstrap.setMain(main);
        }
        bootstrap.callMain(args);

    }

    private String mRexHome;
    private String mMain;
}
