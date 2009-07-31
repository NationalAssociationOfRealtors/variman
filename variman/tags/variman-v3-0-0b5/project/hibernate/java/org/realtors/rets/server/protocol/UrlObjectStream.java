package org.realtors.rets.server.protocol;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class UrlObjectStream implements ObjectStream
{
    public UrlObjectStream(URL url) throws IOException
    {
        URLConnection urlConnection = url.openConnection();
        mInputStream = urlConnection.getInputStream();
        mMimeType = urlConnection.getContentType();
    }

    public UrlObjectStream(URL url, String mimeType) throws IOException
    {
        URLConnection urlConnection = url.openConnection();
        mInputStream = urlConnection.getInputStream();
        mMimeType = mimeType;
    }

    public InputStream getInputStream()
    {
        return mInputStream;
    }

    public String getMimeType()
    {
        return mMimeType;
    }

    private InputStream mInputStream;
    private String mMimeType;
}
