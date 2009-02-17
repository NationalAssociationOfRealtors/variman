package org.realtors.rets.server.protocol;

import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;

public class FileObjectStream implements ObjectStream
{
    public FileObjectStream(String path) throws FileNotFoundException
    {
        this(new File(path));
    }

    public FileObjectStream(File file) throws FileNotFoundException
    {
        mMimeType = getContentType(file.getPath());
        mInputStream = new FileInputStream(file);
    }

    public InputStream getInputStream()
    {
        return mInputStream;
    }

    public String getMimeType()
    {
        return mMimeType;
    }
    
    public String getContentType(String file)
    {
        if (file.endsWith(".gif"))
        {
            return "image/gif";
        }
        else if (file.endsWith(".jpg"))
        {
            return "image/jpeg";
        }
        else if (file.endsWith(".xml"))
        {
        	return "text/xml";
        }
        else
        {
            LOG.warn("Unknown file type: " + file);
            return "application/octet-stream";
        }
    }

    private static final Logger LOG =
        Logger.getLogger(FileObjectStream.class);

    private String mMimeType;
    private FileInputStream mInputStream;
}
