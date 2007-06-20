package org.realtors.rets.server.protocol;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

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
            else
            {
                LOG.debug("File " + filePath + " does not exist");
                return null;
            }
        }
        catch (MalformedURLException e)
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
