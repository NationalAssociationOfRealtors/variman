package org.realtors.rets.server.protocol;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.realtors.rets.server.RetsServerException;

public class XmlObjectSet implements ObjectSet
{
    public XmlObjectSet(File file) throws RetsServerException
    {
        try
        {
            init(new FileReader(file), file.getParent());
        }
        catch (FileNotFoundException e)
        {
            throw new RetsServerException(e);
        }

    }

    public XmlObjectSet(Reader reader, String rootPath)
        throws RetsServerException
    {
        init(reader, rootPath);
    }

    private void init(Reader reader, String rootPath)
        throws RetsServerException
    {
        try
        {
            mObjectDescriptorsByType = new HashMap();
            mDefaultObjectIdsByType = new HashMap();
            mAreRemoteLocationsAllowable = false;
            mRootUrl = new File(rootPath).toURI().toURL();
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(reader);
            Element rootElement = document.getRootElement();
            parseObjectSet(rootElement);
        }
        catch (IOException e)
        {
            throw new RetsServerException(e);
        }
        catch (JDOMException e)
        {
            throw new RetsServerException(e);
        }
    }

    private boolean getAttributeBoolean(Element element, String attributeName)
    {
        String value = element.getAttributeValue(attributeName);
        if ((value != null) && (value.equals("true")))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public void setResource(String resource)
    {
    }
    
    public void setResourceEntity(String resourceEntity)
    {
    }

    private void parseObjectSet(Element objectSet) throws IOException
    {
        mResourceKey = objectSet.getAttributeValue("resource-key");
        mAreRemoteLocationsAllowable =
            getAttributeBoolean(objectSet, "remote-locations");
        String baseUrl = objectSet.getAttributeValue("base");
        if (baseUrl == null)
        {
            mBaseUrl = mRootUrl;
        }
        else
        {
            mBaseUrl = new URL(mRootUrl, baseUrl);
        }
        Namespace namespace = objectSet.getNamespace();
        Iterator objectGroups =
            objectSet.getChildren("object-group", namespace).iterator();
        while (objectGroups.hasNext())
        {
            Element objectGroup = (Element) objectGroups.next();
            parseObjectGroup(objectGroup);
        }
    }

    private void parseObjectGroup(Element objectGroup)
        throws IOException
    {
        String type = objectGroup.getAttributeValue("type");
        URL baseUrl = mBaseUrl;
        String groupBaseUrl = objectGroup.getAttributeValue("base");
        if (groupBaseUrl != null)
        {
            baseUrl = new URL(baseUrl, groupBaseUrl);
        }
        List objectDescriptors = new ArrayList();
        Namespace namespace = objectGroup.getNamespace();
        Iterator objects =
            objectGroup.getChildren("object",  namespace).iterator();
        int objectId = 1;
        int defaultObjectId = 1;
        while (objects.hasNext())
        {
            Element object = (Element) objects.next();
            String url = object.getAttributeValue("src");
            if (StringUtils.isBlank(url))
            {
                LOG.warn("Blank url for objectId [" + objectId + "], type [" +
                         type + "], resource key [" + mResourceKey + "]");
                continue;
            }
            if(getAttributeBoolean(object, "default"))
            {
                defaultObjectId = objectId;
            }
            URL objectUrl = new URL(baseUrl, url);
            ObjectDescriptor objectDescriptor =
                new ObjectDescriptor(mResourceKey, objectId, objectUrl);
            objectDescriptor.setRemoteLocationAllowable(
                mAreRemoteLocationsAllowable);
            String description = object.getAttributeValue("description");
            if (StringUtils.isNotBlank(description))
            {
                objectDescriptor.setDescription(description);
            }
            objectDescriptors.add(objectDescriptor);
            objectId++;
        }
        mObjectDescriptorsByType.put(type, objectDescriptors);
        mDefaultObjectIdsByType.put(type, new Integer(defaultObjectId));
    }

    public ObjectDescriptor findObject(String type, int objectId)
        throws RetsServerException
    {
        List allObjectDescriptors = (List) mObjectDescriptorsByType.get(type);
        if (allObjectDescriptors == null)
        {
            return null;
        }

        int objectIndex;
        if (objectId == DEFAULT_OBJECT_ID)
        {
            Integer defaultObjectId =
                (Integer) mDefaultObjectIdsByType.get(type);
            objectIndex = defaultObjectId.intValue() - 1;
        }
        else
        {
            objectIndex = objectId - 1;
        }

        if (objectIndex < allObjectDescriptors.size())
        {
            return (ObjectDescriptor) allObjectDescriptors.get(objectIndex);
        }
        else
        {
            LOG.warn("In resource key " + mResourceKey + ", type " + type +
                     ", skipping object ID " + objectId);
            return null;
        }
    }

    public List findAllObjects(String type)
    {
        List allObjectDescriptors = (List) mObjectDescriptorsByType.get(type);
        if (allObjectDescriptors == null)
        {
            return Collections.EMPTY_LIST;
        }
        else
        {
            return allObjectDescriptors;
        }
    }

    public boolean areRemoteLocationsAllowable()
    {
        return mAreRemoteLocationsAllowable;
    }

    private static final Logger LOG =
        Logger.getLogger(XmlObjectSet.class);
    private String mResourceKey;
    private URL mRootUrl;
    private URL mBaseUrl;
    private Map mObjectDescriptorsByType;
    private Map mDefaultObjectIdsByType;
    private boolean mAreRemoteLocationsAllowable;
}
