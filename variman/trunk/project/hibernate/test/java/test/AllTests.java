/*
 */
package test;

import java.io.File;
import java.io.FilenameFilter;
import java.io.FileFilter;
import java.lang.reflect.Modifier;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.StringUtils;

public class AllTests
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        String[] paths = StringUtils.split(SystemUtils.JAVA_CLASS_PATH,
                                           SystemUtils.PATH_SEPARATOR);
        for (int i = 0; i < paths.length; i++)
        {
            String path = paths[i];
            addSuitesFromPath(suite, path);
        }
        return suite;
    }

    private static void addSuitesFromPath(TestSuite suite, String path)
    {
        if (path.endsWith(".jar"))
        {
            return;
        }
        File file = new File(path);
        addSuitesFromFile(suite, file, "");
    }

    private static void addSuitesFromFile(TestSuite suite, File file,
                                          String pkg)
    {
        String[] classNames = listClasses(file);
        for (int i = 0; i < classNames.length; i++)
        {
            String className = classNames[i];
            // Rip off trailing ".class"
            className = className.substring(
                0, className.length() - ".class".length());
            if (!className.endsWith("Test"))
            {
                continue;
            }
            className = appendPackage(pkg, className);
            try
            {
                Class clazz = Class.forName(className);
                if (isTestCase(clazz))
                {
                    suite.addTestSuite(clazz);
//                    System.out.println("Adding: " + className);
                }
            }
            catch (ClassNotFoundException e)
            {
                continue;
            }
        }

        File[] directories = listDirectories(file);
        for (int i = 0; i < directories.length; i++)
        {
            File directory = directories[i];
            String newPkg = appendPackage(pkg, directory.getName());
            addSuitesFromFile(suite, directory, newPkg);
        }
    }

    private static String appendPackage(String pkg, String element)
    {
        if (pkg.equals(""))
        {
            return element;
        }
        else
        {
            return pkg + "." + element;
        }
    }

    private static File[] listDirectories(File file)
    {
        File[] directories = file.listFiles(new FileFilter() {
            public boolean accept(File pathname)
            {
                if (pathname.isDirectory())
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        });
        return directories;
    }

    private static String[] listClasses(File file)
    {
        String[] classNames = file.list(new FilenameFilter() {
            public boolean accept(File dir, String name)
            {
                if (name.endsWith(".class"))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        });
        return classNames;
    }

    private static boolean isTestCase(Class clazz)
    {
        if (isJunitTestCase(clazz))
        {
            int modifiers = clazz.getModifiers();
            if (!Modifier.isAbstract(modifiers))
            {
                return true;
            }
        }
        return false;
    }

    private static boolean isJunitTestCase(Class clazz)
    {
        if (clazz.getName().equals("junit.framework.TestCase"))
        {
            return true;
        }
        else
        {
            Class superclass = clazz.getSuperclass();
            if (superclass != null)
            {
                return isJunitTestCase(superclass);
            }
            else
            {
                return false;
            }
        }
    }
}
