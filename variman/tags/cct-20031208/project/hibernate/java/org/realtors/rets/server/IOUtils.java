/*
 */
package org.realtors.rets.server;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;

/**
 * Facade methods for common I/O tasks.
 */
public class IOUtils
{
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

    public static String readString(URL file) throws IOException
    {
        return readString(new FileReader(file.getFile()));
    }

    public static String readString(String fileName) throws IOException
    {
        return readString(new FileReader(fileName));
    }

    public static String readString(InputStream input) throws IOException
    {
        return readString(input, DEFAULT_BUFFER_SIZE);
    }

    public static String readString(InputStream input, int bufferSize)
        throws IOException
    {
        return readString(new InputStreamReader(input), bufferSize);
    }

    public static String readString(Reader input) throws IOException
    {
        return readString(input, DEFAULT_BUFFER_SIZE);
    }

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

    public static List readLines(URL file) throws IOException
    {
        return readLines(new FileReader(file.getFile()));
    }

    public static List readLines(String fileName) throws IOException
    {
        return readLines(new FileReader(fileName));
    }

    public static List readLines(InputStream input) throws IOException
    {
        return readLines(new InputStreamReader(input));
    }

    public static List readLines(Reader input) throws IOException
    {
        return readLines(new BufferedReader(input));
    }

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

    /** The default buffer size is 8k. */
    public static final int DEFAULT_BUFFER_SIZE = 8 * 1024;
}
