/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.config;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Properties;

import org.realtors.rets.server.Util;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.IOUtils;

import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.strategy.HyphenatedNameMapper;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.xml.sax.SAXException;

public class RetsConfig
{
    public RetsConfig()
    {
        mNonceInitialTimeout = -1;
        mNonceSuccessTimeout = -1;
    }

    public String getGetObjectRoot()
    {
        return mGetObjectRoot;
    }

    public void setGetObjectRoot(String getObjectRoot)
    {
        mGetObjectRoot = getObjectRoot;
    }

    public String getGetObjectPattern()
    {
        return mGetObjectPattern;
    }

    public void setGetObjectPattern(String getObjectPattern)
    {
        mGetObjectPattern = getObjectPattern;
    }

    public int getNonceInitialTimeout()
    {
        return mNonceInitialTimeout;
    }

    public void setNonceInitialTimeout(int nonceInitialTimeout)
    {
        mNonceInitialTimeout = nonceInitialTimeout;
    }

    public int getNonceSuccessTimeout()
    {
        return mNonceSuccessTimeout;
    }

    public void setNonceSuccessTimeout(int nonceSuccessTimeout)
    {
        mNonceSuccessTimeout = nonceSuccessTimeout;
    }

    public DatabaseConfig getDatabase()
    {
        return mDatabase;
    }

    public void setDatabase(DatabaseConfig database)
    {
        mDatabase = database;
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("GetObject root", mGetObjectRoot)
            .append("GetObject pattern", mGetObjectPattern)
            .append("nonce initial timeout", mNonceInitialTimeout)
            .append("nonce success timeout", mNonceSuccessTimeout)
            .append(mDatabase)
            .toString();
    }

    public String toXml()
        throws RetsServerException
    {
        try
        {
            StringWriter xml = new StringWriter();

            // Betwixt just writes out the bean as a fragment So if we want
            // well-formed xml, we need to add the prolog
            xml.write("<?xml version='1.0' ?>");

            BeanWriter beanWriter = new BeanWriter(xml);
            beanWriter.setWriteIDs(false);
            beanWriter.enablePrettyPrint();
            XMLIntrospector introspector = beanWriter.getXMLIntrospector();
            introspector.setAttributesForPrimitives(false);
            introspector.setElementNameMapper(new HyphenatedNameMapper());

            beanWriter.write(this);
            return xml.toString();
        }
        catch (IOException e)
        {
            throw new RetsServerException(e);
        }
        catch (SAXException e)
        {
            throw new RetsServerException(e);
        }
        catch (IntrospectionException e)
        {
            throw new RetsServerException(e);
        }
    }

    public void toXml(String file) throws RetsServerException
    {
        try
        {
            IOUtils.writeString(file, toXml());
        }
        catch (IOException e)
        {
            throw new RetsServerException(e);
        }
    }

    public static RetsConfig initFromXml(String xml)
        throws RetsServerException
    {
        return initFromXml(new StringReader(xml));
    }

    public static RetsConfig initFromXmlFile(String file)
        throws RetsServerException
    {
        try
        {
            return initFromXml(new FileReader(file));
        }
        catch (FileNotFoundException e)
        {
            throw new RetsServerException(e);
        }
    }

    public static RetsConfig initFromXml(Reader xml)
        throws RetsServerException
    {
        try
        {
            return createIfNull(createBeanReader().parse(xml));
        }
        catch (IOException e)
        {
            throw new RetsServerException(e);
        }
        catch (SAXException e)
        {
            throw new RetsServerException(e);
        }
        catch (IntrospectionException e)
        {
            throw new RetsServerException(e);
        }
    }

    public static RetsConfig initFromXml(InputStream xml)
        throws RetsServerException
    {
        if (xml == null)
        {
            return new RetsConfig();
        }
        else
        {
            try
            {
                return createIfNull(createBeanReader().parse(xml));
            }
            catch (IOException e)
            {
                throw new RetsServerException(e);
            }
            catch (SAXException e)
            {
                throw new RetsServerException(e);
            }
            catch (IntrospectionException e)
            {
                throw new RetsServerException(e);
            }
        }
    }

    private static RetsConfig createIfNull(Object config)
    {
        if (config == null)
        {
            return new RetsConfig();
        }
        else
        {
            return (RetsConfig) config;
        }
    }

    private static BeanReader createBeanReader() throws IntrospectionException
    {
        BeanReader beanReader = new BeanReader();
        beanReader.setMatchIDs(false);
        XMLIntrospector introspector = beanReader.getXMLIntrospector();
        introspector.setAttributesForPrimitives(false);
        introspector.setElementNameMapper(new HyphenatedNameMapper());
        beanReader.registerBeanClass(RetsConfig.class);
        return beanReader;
    }

    public String getGetObjectPattern(String defaultValue)
    {
        if (mGetObjectPattern == null)
        {
            return defaultValue;
        }
        else
        {
            return mGetObjectPattern;
        }
    }

    public String  getGetObjectRoot(String defaultValue)
    {
        if (mGetObjectRoot == null)
        {
            return defaultValue;
        }
        else
        {
            return mGetObjectRoot;
        }
    }

    public int getNonceInitialTimeout(int defaultValue)
    {
        if (mNonceInitialTimeout == -1)
        {
            return defaultValue;
        }
        else
        {
            return mNonceInitialTimeout;
        }
    }

    public int getNonceSuccessTimeout(int defaultValue)
    {
        if (mNonceSuccessTimeout == -1)
        {
            return defaultValue;
        }
        else
        {
            return mNonceSuccessTimeout;
        }
    }

    public Properties createHibernateProperties()
    {
        return mDatabase.getHibernateProperties();
    }

    private String mGetObjectRoot;
    private String mGetObjectPattern;
    private int mNonceInitialTimeout;
    private int mNonceSuccessTimeout;
    private DatabaseConfig mDatabase;
}
