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
    public static void main(String[] args)
        throws Exception
    {
        List urlList = new ArrayList();
        addJars(urlList, "admin/lib");
        addJars(urlList, "webapp/WEB-INF/lib");
        urlList.add(new File("admin/classes/").toURL());
        URL[] urls = (URL[]) urlList.toArray(new URL[urlList.size()]);
        URLClassLoader classLoader = new URLClassLoader(urls);
        Thread.currentThread().setContextClassLoader(classLoader);
        Class main =
            classLoader.loadClass("org.realtors.rets.server.admin.Main");
        Class[] paramTypes = new Class[] { args.getClass() };
        Object[] paramValues = new Object[] { args };
        Method method = main.getMethod("main", paramTypes);
        method.invoke(null, paramValues);
    }

    private static void addJars(List urlList, String directory)
        throws MalformedURLException
    {
        File libDir = new File(directory);
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
}
