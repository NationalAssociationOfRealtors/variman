/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Facade methods for common I/O tasks.
 */
public class IOUtils
{
    public static File urlToFile(URL url)
    {
        URI uri = URI.create(url.toString());
        return new File(uri);
    }

    public static String urlToFilename(URL url)
    {
        return urlToFile(url).getPath();
    }

    /**
     * Reads the input stream, blocking until the byte buffer is full
     * or the end of the stream is reached.ch
     *
     * @param input input stream to read from
     * @param buffer buffer to fill
     * @return the number of bytes read into the buffer
     * @throws IOException
     */
    public static int fillByteBuffer(InputStream input, byte[] buffer)
        throws IOException
    {
        return fillByteBuffer(input, buffer, buffer.length);
    }

    /**
     * Reads the input stream, blocking until the specified number of bytes is
     * read or the end of the stream is reached.
     *
     * @param input input stream to read from
     * @param buffer buffer to fill
     * @param length the number of bytes to read into the buffer
     * @return the number of bytes read into the buffer
     * @throws IOException
     */
    public static int fillByteBuffer(InputStream input, byte[] buffer,
                                     int length)
        throws IOException
    {
        int offset = 0;
        int bytesLeft = length;
        while (bytesLeft > 0)
        {
            int bytesRead = input.read(buffer, offset, bytesLeft);
            if (bytesRead == -1)
            {
                break;
            }
            offset += bytesRead;
            bytesLeft -= bytesRead;
        }
        return offset;
    }

    public static byte[] readBytes(URL file)
        throws IOException
    {
        return readBytes(new FileInputStream(urlToFile(file)));
    }

    public static byte[] readBytes(String fileName) throws IOException
    {
        return readBytes(new FileInputStream(fileName));
    }

    /**
     * Reads the contents of an input stream into an array of bytes.
     *
     * @param input stream to read from
     * @return the contents of the input stream
     * @throws IOException if an error occurs
     * @see #DEFAULT_BUFFER_SIZE
     */
    public static byte[] readBytes(InputStream input)
        throws IOException
    {
        return readBytes(input, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Read the contents of an input stream into an array of bytes.
     *
     * @param input stream to read from
     * @param bufferSize buffer size used when reading
     * @return the contents of the input stream
     * @throws IOException if an error occurs
     */
    public static byte[] readBytes(InputStream input, int bufferSize)
        throws IOException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[bufferSize];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1)
        {
            bytes.write(buffer, 0, bytesRead);
        }
        return bytes.toByteArray();
    }

    /**
     * Read the contents of a file into a string.
     *
     * @param file URL of the file to read
     * @return the contents of the file
     * @throws IOException if an error occurse
     */
    public static String readString(URL file) throws IOException
    {
        return readString(new FileReader(urlToFile(file)));
    }

    /**
     * Read the contents of a file into a string.
     *
     * @param fileName name of file to read
     * @return the contents of the file
     * @throws IOException if an error occurs
     */
    public static String readString(String fileName) throws IOException
    {
        return readString(new FileReader(fileName));
    }


    /**
     * Read the contents of a file into a string.
     *
     * @param file file to read
     * @return the contents of the file
     * @throws IOException if an error occurs
     */
    public static String readString(File file) throws IOException
    {
        return readString(new FileReader(file));
    }

    /**
     * Read the contents of a stream into a string.
     *
     * @param input stream to read
     * @return the contents of the stream
     * @throws IOException if an error occurs
     */
    public static String readString(InputStream input) throws IOException
    {
        return readString(input, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Read the contents of a stream, using a specified buffer size.
     *
     * @param input stream to read
     * @param bufferSize size of read buffer
     * @return the contents of the stream
     * @throws IOException if an error occurs
     */
    public static String readString(InputStream input, int bufferSize)
        throws IOException
    {
        return readString(new InputStreamReader(input), bufferSize);
    }

    /**
     * Read the contents of a reader into a string.
     *
     * @param input reader to read
     * @return the contents of the reader
     * @throws IOException if an error occurs
     */
    public static String readString(Reader input) throws IOException
    {
        return readString(input, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Read the contents of a read into a string, using a specified buffer
     * size.
     *
     * @param input reader to read
     * @param bufferSize size of the read buffer
     * @return the contents of the stream
     * @throws IOException if an error occurs
     */
    public static String readString(Reader input, int bufferSize)
        throws IOException
    {
        StringBuffer string = new StringBuffer();
        char[] buffer = new char[bufferSize];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1)
        {
            string.append(buffer,  0, bytesRead);
        }
        return string.toString();
    }

    /**
     * Read the contents of a file into a list of lines.
     *
     * @param file URL of the file to read
     * @return a list of strings, where each element is a line
     * @throws IOException if an error occurs
     */
    public static List readLines(URL file) throws IOException
    {
        return readLines(new FileReader(urlToFile(file)));
    }

    /**
     * Read the contents of a file into a list of lines.
     *
     * @param fileName name of file to read
     * @return a list of strings, where each element is a line
     * @throws IOException if an error occurs
     */
    public static List readLines(String fileName) throws IOException
    {
        return readLines(new FileReader(fileName));
    }

    /**
     * Read the contents of a stream into a list of lines.
     *
     * @param input stream to read from
     * @return a list of strings, where each element is a line
     * @throws IOException if an error occurs
     */
    public static List readLines(InputStream input) throws IOException
    {
        return readLines(new InputStreamReader(input));
    }

    /**
     * Read the contents of a reader into a list of lines.
     *
     * @param input reader to read from
     * @return a list of strings, where each element is a line
     * @throws IOException if an error occurs
     */
    public static List readLines(Reader input) throws IOException
    {
        return readLines(new BufferedReader(input));
    }

    /**
     * Read the contents of a buffered reader into a list of lines.
     *
     * @param input buffered reader to read from
     * @return a list of strings, where each element is a line
     * @throws IOException if an error occurs
     */
    public static List readLines(BufferedReader input) throws IOException
    {
        List lines = new ArrayList();
        String line;
        while ((line = input.readLine()) != null)
        {
            lines.add(line);
        }
        return lines;
    }

    public static void copyFile(File source, File destination)
        throws IOException
    {
        copyFile(source, destination, DEFAULT_BUFFER_SIZE);
    }

    public static void copyFile(File source, File destination, int bufferSize)
        throws IOException
    {
        if (destination.isDirectory())
        {
            destination = new File(destination, source.getName());
        }
        copyStream(new FileInputStream(source),
                   new FileOutputStream(destination), bufferSize);
    }

    /**
     * Copies the contents of an input stream to an output stream.
     *
     * @param source stream to copy from
     * @param destination stream to copy to
     * @throws IOException if an error occurs
     * @see #DEFAULT_BUFFER_SIZE
     * @see #copyStream(java.io.InputStream, java.io.OutputStream, int)
     */
    public static void copyStream(InputStream source, OutputStream destination)
        throws IOException
    {
        copyStream(source, destination, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Copies the contents of an input stream to an output stream. The input
     * stream is read until the end of the stream is reach. The output stream is
     * not closed or flushed upon completion.
     *
     * @param source stream to copy from
     * @param destination stream to copy to
     * @param bufferSize buffer size used when reading
     * @throws IOException if an error occurs
     */
    public static void copyStream(InputStream source, OutputStream destination,
                                  int bufferSize)
        throws IOException
    {
        byte[] buffer = new byte[bufferSize];
        int bytesRead;
        while ((bytesRead = source.read(buffer)) != -1)
        {
            destination.write(buffer, 0, bytesRead);
        }
    }

    public static void writeBytes(byte[] bytes, String fileName)
        throws IOException
    {
        FileOutputStream outputStream = new FileOutputStream(fileName);
        outputStream.write(bytes);
        outputStream.close();
    }

    /**
     * Writes a string to a file, overwriting the file if it exists.
     *
     * @param string string to write
     * @param fileName name of file to write to
     * @throws IOException if an error occurs
     */
    public static void writeString(String string, String fileName)
        throws IOException
    {
        writeString(string, new FileWriter(fileName));
    }

    /**
     * Writes a string to a file, overwriting the file if it exists.
     *
     * @param string string to write
     * @param url URL of file to write to
     * @throws IOException if an error occurs
     */
    public static void writeString(String string, URL url)
        throws IOException
    {
        writeString(string, new FileWriter(url.getFile()));
    }

    /**
     * Writes a string to a file, overwriting the file if it exists.
     *
     * @param string string to write
     * @param file file to write to
     * @throws IOException if an error occurs
     */
    public static void writeString(String string, File file)
        throws IOException
    {
        writeString(string, new FileWriter(file));
    }

    /**
     * Writes a string to a writer.
     *
     * @param string string to write
     * @param output writer of file to write to
     * @throws IOException if an error occurs
     */
    public static void writeString(String string, Writer output)
        throws IOException
    {
        output.write(string, 0, string.length());
        output.flush();
    }

    /**
     * Recursively list all files and directories of a directory.
     *
     * @param directory directory to list
     * @return a list of File objects
     * @throws IOException if an error occurs
     */
    public static List listFilesRecursive(File directory)
        throws IOException
    {
        return listFilesRecursive(directory,  null, null);
    }

    /**
     * Recursively list all files and directories of a directory, filter by
     * file name.
     *
     * @param directory directory to list
     * @param filter used to filter out files by name
     * @return a list of File objects
     * @throws IOException if an error occurs
     */
    public static List listFilesRecursive(File directory, FilenameFilter filter)
        throws IOException
    {
        return listFilesRecursive(directory,  filter, null);
    }

    /**
     * Recursively list all files and directories of a directory, filter by
     * file attributes.
     *
     * @param directory directory to list
     * @param filter used to filter out files by file attributes
     * @return a list of File objects
     * @throws IOException if an error occurs
     */
    public static List listFilesRecursive(File directory, FileFilter filter)
        throws IOException
    {
        return listFilesRecursive(directory,  null, filter);
    }

    /**
     * Recursively list all files and directories of a directory, filter by
     * file name.
     *
     * @param directory directory to list
     * @param filenameFilter used to filter out files by name, if not null
     * @param fileFilter used to filter out by file attributes, if not null
     * @return a list of File objects
     * @throws IOException if an error occurs
     */
    private static List listFilesRecursive(File directory,
                                           FilenameFilter filenameFilter,
                                           FileFilter fileFilter)
        throws IOException
    {
        List allFiles = new ArrayList();
        File[] files = directory.listFiles();
        if (files == null)
        {
            throw new FileNotFoundException(directory.getPath());
        }
        for (int i = 0; i < files.length; i++)
        {
            File file = files[i];
            if ((filenameFilter == null) && (fileFilter == null))
            {
                allFiles.add(file);
            }
            else if ((filenameFilter != null) &&
                (filenameFilter.accept(file.getParentFile(), file.getName())))
            {
                allFiles.add(file);
            }
            else if ((fileFilter != null) && (fileFilter.accept(file)))
            {
                allFiles.add(file);
            }

            if (file.isDirectory())
            {
                allFiles.addAll(listFilesRecursive(file, filenameFilter,
                                                   fileFilter));
            }
        }
        return allFiles;
    }

    public static File relativize(File base, File file)
    {
        URI baseUri = base.toURI();
        URI fileUri = file.toURI();
        URI relativeUri = baseUri.relativize(fileUri);
        if (!relativeUri.isAbsolute())
            return new File(relativeUri.getPath());
        else
            return new File(relativeUri);
    }

    public static String relativize(String base, String file)
    {
        /*
         * We create the URI this way to address
         * the drive letter that can appear in Windows
         * file and pathnames. File.toURI will properly
         * normalize the paths on Windows this way.
         */
        URI baseUri = new File(base).toURI();
        URI fileUri = new File(file).toURI();
        return baseUri.relativize(fileUri).getPath();
    }

    public static File resolve(File base, File file)
    {
        URI baseUri = base.toURI();
        URI fileUri = file.toURI();
        if (!file.isAbsolute())
        {
            file = new File(base, file.getPath());
            fileUri = file.toURI();
        }
        URI resolvedUri = baseUri.resolve(fileUri);
        return new File(resolvedUri);
    }

    public static String resolve(String base, String file)
    {
        File baseFile = new File(base);
        File fileFile = new File(file);
        return resolve(baseFile, fileFile).getPath();
    }

    /**
     * A filter that matches by file extension.
     */
    static public class ExtensionFilter implements FilenameFilter
    {
        /**
         * Create a new extension filter
         *
         * @param extension extension to filter on
         */
        public ExtensionFilter(String extension)
        {
            mExtension = extension;
        }

        /**
         * Returns true if the file name matches the extension.
         *
         * @param dir directory of file to test
         * @param name name of file to test
         * @return true if the name matches the extension.
         */
        public boolean accept(File dir, String name)
        {
            return name.endsWith(mExtension);
        }

        private String mExtension;
    }

    /**
     * A filter that only matches directories.
     */
    static public class DirectoryFilter implements FileFilter
    {
        /**
         * Returns true if the file is a directory.
         *
         * @param file file to test
         * @return true if the file is a directory.
         */
        public boolean accept(File file)
        {
            return file.isDirectory();
        }
    }

    /**
     * A filter that matches all but directories.
     */
    static public class NotDirectoryFilter implements FileFilter
    {
        /**
         * Return true if the file is not a directory.
         *
         * @param file file to test
         * @return true if the file is not a directory.
         */
        public boolean accept(File file)
        {
            return !file.isDirectory();
        }
    }

    /** The default buffer size is 8k. */
    public static final int DEFAULT_BUFFER_SIZE = 8 * 1024;
}
