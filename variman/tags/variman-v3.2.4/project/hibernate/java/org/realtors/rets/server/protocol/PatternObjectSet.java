package org.realtors.rets.server.protocol;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import org.realtors.rets.server.ReplyCode;
import org.realtors.rets.server.RetsServerException;

public class PatternObjectSet implements ObjectSet
{
    public PatternObjectSet(String rootDirectory, String pattern,
                            String resourceKey)
    {
        mRootDirectory = rootDirectory;
        GetObjectPatternParser parser = new GetObjectPatternParser(pattern);
        mPatternFormatter = parser.parse();
        mPatternContext = new GetObjectPatternContext();
        mResourceKey = resourceKey;
    }
    
    public void setResource(String resource)
    {
    }
    
    public void setResourceEntity(String resourceEntity)
    {
    }

    public List findAllObjects(String type) throws RetsServerException
    {
        List objects = new ArrayList();
        // Keep track of URLs to detect infinite loops
        Set urls = new HashSet();
        int objectId = 1;
        while (addIfNotNull(findObject(type, objectId), objects, urls))
        {
            objectId++;
        }
        return objects;
    }

    private boolean addIfNotNull(ObjectDescriptor object, List objects,
                                 Set urls)
    {
        if (object != null)
        {
        	/*
        	 * This is only used by findAllObjects. If the ObjectID is not
        	 * equal to "1" and an error happened, do not add the object and
        	 * assume that there are no more objects to find.
        	 */
        	if (object.getObjectId() != 1 && object.getRetsReplyCode() != ReplyCode.SUCCESSFUL)
        		return false;
        	
            URL url = object.getUrl();
            if (!urls.contains(url))
            {
                objects.add(object);
                urls.add(url);
                return true;
            }
            else
            {
                // If this URL was already used, assume pattern results in an
                // infinite loop and stop
                LOG.warn("Infinite loop detected for URL: " + url +
                         ", URLs: " + urls);
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public ObjectDescriptor findObject(String type, int objectId)
        throws RetsServerException
    {
        try
        {
            if (!type.equals("Photo"))
            {
                return null;
            }
            if (objectId == DEFAULT_OBJECT_ID)
            {
                objectId = 1;
            }
            mPatternContext.setKey(mResourceKey);
            mPatternContext.setObjectId(objectId);
            StringBuffer fileBuffer = new StringBuffer(mRootDirectory);
            fileBuffer.append(File.separator);
            mPatternFormatter.format(fileBuffer, mPatternContext);
            String filePath = fileBuffer.toString();
            File file = new File(filePath);
            if (file.exists() && file.isFile())
            {
                LOG.debug("File " + filePath + " exists");
                return new ObjectDescriptor(mResourceKey, objectId,
                                            file.toURI().toURL());
            }
            else if (filePath.startsWith("http:"))
            {
                URL imageURL = new URL(filePath);
                HttpURLConnection connection =  (HttpURLConnection)  imageURL.openConnection(); 
                connection.setRequestMethod("GET"); 
                connection.setDoOutput(true);
                connection.connect(); 
                int code = connection.getResponseCode();
                connection.disconnect();
                if (code == 200)
                {
                    LOG.debug("File " + filePath + " exists");
                    ObjectDescriptor objectDescriptor = new ObjectDescriptor(mResourceKey, objectId,
                                                                            imageURL);
                    objectDescriptor.setRemoteLocationAllowable(true);
                    return objectDescriptor;
                }
            }

            LOG.debug("File " + filePath + " does not exist");
            /*
             * In case this is a multipart response, we need to create an object of text/xml
             * that contains the RETS ReplyCode message. 
             */
            try
            {
            	StringBuffer notFoundName = new StringBuffer(mRootDirectory);
            	notFoundName.append(File.separator + "notfound.xml");

            	/*
            	 * Not the best way, but this is the exceptional case, 
            	 * so brute force should work.
            	 */
            	File notFound = new File(notFoundName.toString());

            	synchronized (this)
            	{
                	if (notFound.length() == 0)
                	{
                		BufferedWriter out = new BufferedWriter(new FileWriter(notFound));
                		out.write("<RETS ReplyCode=\"20403\" ReplyText=\"No Object Found\"/>\r\n");
                		out.close();
                		notFound.deleteOnExit();
                	}
            	}
            	ObjectDescriptor objectDescriptor = new ObjectDescriptor(mResourceKey, objectId,
            													notFound.toURI().toURL(), null);
            	objectDescriptor.setRetsReplyCode(ReplyCode.NO_OBJECT_FOUND);
            	return objectDescriptor;
            }
            catch (Exception e)
            {
            }
            return null;
        }
        catch (Exception e)
        {
            throw new RetsServerException(e);
        }
    }

    private static final Logger LOG =
        Logger.getLogger(PatternObjectSet.class);
    private String mRootDirectory;
    private GetObjectPatternFormatter mPatternFormatter;
    private GetObjectPatternContext mPatternContext;
    private String mResourceKey;
}
