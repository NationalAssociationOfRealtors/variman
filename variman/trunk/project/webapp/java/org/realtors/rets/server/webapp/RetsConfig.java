/*
 */
package org.realtors.rets.server.webapp;

import java.io.StringWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.InputStream;
import java.beans.IntrospectionException;

import org.realtors.rets.server.Util;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.strategy.HyphenatedNameMapper;
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

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("GetObject root", mGetObjectRoot)
            .append("GetObject pattern", mGetObjectPattern)
            .append("nonce initial timeout", mNonceInitialTimeout)
            .append("nonce success timeout", mNonceSuccessTimeout)
            .toString();
    }

    public String toXml()
        throws IOException, SAXException, IntrospectionException
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

    public static RetsConfig initFromXml(String xml)
        throws SAXException, IOException, IntrospectionException
    {
        return initFromXml(new StringReader(xml));
    }

    public static RetsConfig initFromXml(Reader xml)
        throws SAXException, IOException, IntrospectionException
    {
        return createIfNull(createBeanReader().parse(xml));
    }

    public static RetsConfig initFromXml(InputStream xml)
        throws IntrospectionException, SAXException, IOException
    {
        if (xml == null)
        {
            return new RetsConfig();
        }
        else
        {
            return createIfNull(createBeanReader().parse(xml));
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

    private String mGetObjectRoot;
    private String mGetObjectPattern;
    private int mNonceInitialTimeout;
    private int mNonceSuccessTimeout;
}
