/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server;

import java.io.File;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

/**
 * Creates a manifest file suitable for running with "java -jar". It allows
 * the classpath to be created from a list of filesets in build.xml
 */
public class JarManifestTask extends Task
{
    public JarManifestTask()
    {
        mClassPathFileSets = new ArrayList();
    }

    public void execute() throws BuildException
    {
        try
        {
            assertDestFileIsValid();
            PrintStream out = new PrintStream(new FileOutputStream(mDestFile));
            out.println("Main-Class: " + mMain);

            // Each element of the classpath is put on it's own line. But the
            // line must begin with white space to show it's a continuation of
            // the header.
            out.print("Class-Path: ");
            List classPath = createClasspath();
            String separator = "";
            for (int i = 0; i < classPath.size(); i++)
            {
                out.print(separator);
                out.print(classPath.get(i));
                separator = "\n  ";
            }
        }
        catch (FileNotFoundException e)
        {
            throw new BuildException(e);
        }
    }

    /**
     * Ensures that the destination file is usable, otherwise throw a
     * <code>BuildException</code>.
     *
     * @throws BuildException if the destination file is not usable
     */
    private void assertDestFileIsValid()
        throws BuildException
    {
        if (mDestFile == null)
        {
            throw new BuildException("destfile attribute must be set",
                                     location);
        }

        if (mDestFile.exists() && mDestFile.isDirectory())
        {
            throw new BuildException("destfile is a directory!",
                                     location);
        }

        if (mDestFile.exists() && !mDestFile.canWrite())
        {
            throw new BuildException(
                "Can not write to the specified destfile", location);
        }
    }

    /**
     * Creates a list of elements to be included in the "Class-Path" manifest
     * header.
     *
     * @return a list of strings
     */
    private List createClasspath()
    {
        List classPath = new ArrayList();
        for (int i = 0; i < mClassPathFileSets.size(); i++)
        {
            FileSet fileSet = (FileSet) mClassPathFileSets.get(i);
            DirectoryScanner scanner =
                fileSet.getDirectoryScanner(getProject());
            // Add all files, as is
            classPath.addAll(Arrays.asList(scanner.getIncludedFiles()));

            // Add all directories, making sure they end in a "/"
            String[] directories = scanner.getIncludedDirectories();
            for (int j = 0; j < directories.length; j++)
            {
                String directory = directories[j];
                classPath.add(directory + "/");
            }
        }
        return classPath;
    }

    /**
     * Sets the main class name
     *
     * @param main the main class name
     */
    public void setMain(String main)
    {
        mMain = main;
    }

    /**
     * Sets the destination manifest file to create.
     *
     * @param destFile the destination manifest file.
     */
    public void setDestFile(File destFile)
    {
        mDestFile = destFile;
    }

    /**
     * Adds a file set to the classpath.
     *
     * @param fileSet file set to add
     */
    public void addClassPath(FileSet fileSet)
    {
        mClassPathFileSets.add(fileSet);
    }

    private String mMain;
    private File mDestFile;
    private List mClassPathFileSets;
}
