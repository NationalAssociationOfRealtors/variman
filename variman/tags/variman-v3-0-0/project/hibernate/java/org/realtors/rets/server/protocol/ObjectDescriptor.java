package org.realtors.rets.server.protocol;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import org.realtors.rets.server.Util;
import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.ReplyCode;

public class ObjectDescriptor
{
    public ObjectDescriptor(String objectKey, int objectId, URL url)
    {
        this(objectKey, objectId, url, null);
    }

    public ObjectDescriptor(String objectKey, int objectId, URL url,
                            String description)
    {
        mObjectKey = objectKey;
        mObjectId = objectId;
        mUrl = url;
        if (url.getProtocol().equals("file"))
        {
            mIsLocal = true;
        }
        else
        {
            mIsLocal = false;
        }
        mDescription = description;
        mRemoteLocationAllowable = false;
        mRetsReplyCode = ReplyCode.SUCCESSFUL;
    }
    
    public String getObjectKey()
    {
        return mObjectKey;
    }

    public void setObjectKey(String objectKey)
    {
        mObjectKey = objectKey;
    }

    public int getObjectId()
    {
        return mObjectId;
    }

    public void setObjectId(int objectId)
    {
        mObjectId = objectId;
    }

    public URL getUrl()
    {
        return mUrl;
    }

    public boolean isLocal()
    {
        return mIsLocal;
    }

    public String getDescription()
    {
        return mDescription;
    }

    public void setDescription(String description)
    {
        mDescription = description;
    }

    public void setRemoteLocationAllowable(boolean remoteLocationAllowable)
    {
        mRemoteLocationAllowable = remoteLocationAllowable;
    }

    public boolean isRemoteLocationAllowable()
    {
        return mRemoteLocationAllowable;
    }

    public String getLocationUrl(String  baseUrl)
    {
        if (mRemoteLocationAllowable && !mIsLocal)
        {
            return mUrl.toString();
        }
        else
        {
            StringBuffer buffer = new StringBuffer(baseUrl);
            buffer.append(mObjectKey).append("/").append(mObjectId);
            return buffer.toString();
        }
    }
    
    public ReplyCode getRetsReplyCode()
    {
    	return mRetsReplyCode;
    }
    
    public void setRetsReplyCode(ReplyCode retsReplyCode)
    {
    	mRetsReplyCode = retsReplyCode;
    }

    public ObjectStream openObjectStream() throws IOException
    {
        if (mIsLocal)
        {
            return new FileObjectStream(IOUtils.urlToFile(mUrl));
        }
        else
        {
            return new UrlObjectStream(mUrl);
        }
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append(mObjectKey)
            .append(mObjectId)
            .append(mUrl)
            .append(mIsLocal)
            .append(mDescription)
            .append(mRemoteLocationAllowable)
            .append(mRetsReplyCode)
            .toString();
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof ObjectDescriptor))
        {
            return false;
        }
        ObjectDescriptor rhs = (ObjectDescriptor) obj;
        return new EqualsBuilder()
            .append(mObjectKey,  rhs.mObjectKey)
            .append(mObjectId, rhs.mObjectId)
            .append(mUrl, rhs.mUrl)
            .append(mIsLocal, rhs.mIsLocal)
            .append(mDescription, rhs.mDescription)
            .append(mRemoteLocationAllowable, rhs.mRemoteLocationAllowable)
            .append(mRetsReplyCode, rhs.mRetsReplyCode)
            .isEquals();
    }

    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(mObjectKey)
            .append(mObjectId)
            .append(mUrl)
            .append(mIsLocal)
            .append(mDescription)
            .append(mRemoteLocationAllowable)
            .append(mRetsReplyCode)
            .toHashCode();
    }

    private String mObjectKey;
    private int mObjectId;
    private URL mUrl;
    private boolean mIsLocal;
    private String mDescription;
    private boolean mRemoteLocationAllowable;
    private ReplyCode mRetsReplyCode;
}
