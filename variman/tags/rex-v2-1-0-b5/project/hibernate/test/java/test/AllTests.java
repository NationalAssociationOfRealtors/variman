/*
 */
package test;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.lang.reflect.Modifier;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

public class AllTests
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        String[] paths = StringUtils.split(SystemUtils.JAVA_CLASS_PATH,
                                           SystemUtils.PATH_SEPARATOR);
        int numSuites = 0;
        for (int i = 0; i < paths.length; i++)
        {
            String path = paths[i];
            numSuites += addSuitesFromPath(suite, path);
        }
        System.out.println(numSuites + " test classes found");
        return suite;
    }

    private static int addSuitesFromPath(TestSuite suite, String path)
    {
        if (path.endsWith(".jar"))
        {
            return 0;
        }
        File file = new File(path);
        return addSuitesFromDirectory(suite, file, "");
    }

    private static int addSuitesFromDirectory(TestSuite suite, File directory,
                                              String pkg)
    {
        String[] classNames = listClasses(directory);
        int numSuites = 0;
        for (int i = 0; i < classNames.length; i++)
        {
            String className = classNames[i];
            // Rip off trailing ".class"
            int suffixIndex = className.length() - ".class".length();
            className = className.substring(0, suffixIndex);
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
                    numSuites++;
                }
            }
            catch (ClassNotFoundException e)
            {
                continue;
            }
        }

        File[] subdirectories = listDirectories(directory);
        for (int i = 0; i < subdirectories.length; i++)
        {
            File subdirectory = subdirectories[i];
            String newPkg = appendPackage(pkg, subdirectory.getName());
            numSuites +=
                addSuitesFromDirectory(suite, subdirectory, newPkg);
        }
        return numSuites;
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
        File[] directories = file.listFiles(new FileFilter()
        {
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
        String[] classNames = file.list(new FilenameFilter()
        {
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
