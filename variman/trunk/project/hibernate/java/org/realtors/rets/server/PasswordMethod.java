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

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

import org.apache.log4j.Logger;


public abstract class PasswordMethod implements Serializable
{
    public abstract String hash(String username, String plainTextPassword);

    public abstract boolean verifyPassword(String expectedPassword,
                                           String passwordToVerify);

    protected abstract boolean parseOptions(String options);

    protected abstract PasswordMethod deepCopy();

    public String getMethod()
    {
        return mMethod;
    }

    public String getOptions()
    {
        return mOptions;
    }

    public String getId()
    {
        return makeId(mMethod, mOptions);
    }

    public String toString()
    {
        return getId();
    }

    public static String makeId(String method, String options)
    {
        StringBuffer buffer = new StringBuffer(method);
        if (!options.equals(""))
        {
            buffer.append(":").append(options);
        }
        return buffer.toString();
    }

    public static PasswordMethod getInstance(String method)
    {
        return getInstance(method, "");
    }

    public static synchronized PasswordMethod getInstance(String method,
                                                          String options)
    {
        String id = makeId(method, options);
        PasswordMethod passwordMethod =
            (PasswordMethod) sCachedMethods.get(id);
        if (passwordMethod == null)
        {
            try
            {
                Class clazz = (Class) sRegisteredMethods.get(method);
                LOG.debug("Instantiating " + clazz.getName());
                passwordMethod = (PasswordMethod) clazz.newInstance();
                passwordMethod.mMethod = method;
                passwordMethod.mOptions = options;
                if (passwordMethod.parseOptions(options))
                {
                    LOG.debug("Adding id [" + id + "] to cache");
                    sCachedMethods.put(id, passwordMethod);
                }
                else
                {
                    LOG.debug("Parsing options [" + options + "] failed for " +
                              "method: " + method);
                    passwordMethod = null;
                }
            }
            catch (InstantiationException e)
            {
                LOG.warn("Could not instantiate", e);
            }
            catch (IllegalAccessException e)
            {
                LOG.warn("Could not instantiate", e);
            }
        }

        return passwordMethod;
    }

    public static synchronized void registerMethod(String method, Class clazz)
    {
        sRegisteredMethods.put(method, clazz);
    }

    public static void setDefaultMethod(String method)
    {
        setDefaultMethod(method, "");
    }

    public static synchronized void setDefaultMethod(String method,
                                                     String options)
    {
        sDefaultMethod = method;
        sDefaultOptions = options;
        LOG.debug("Set default method to <" + method + ">, options = <" + 
                  options + ">");
    }

    public static PasswordMethod getDefaultMethod()
    {
        return getInstance(sDefaultMethod, sDefaultOptions);
    }

    public static synchronized void initMethods()
    {
        // Do nothing... all work is done once in static block. This method
        // just insures the static block gets loaded.
    }

    public static final String PLAIN_TEXT = "";

    /**
     * Store the password as A1 from HTTP Digest Authentication. Use the
     * option to set the realm to use in the hash.
      */
    public static final String DIGEST_A1 = "A1";

    /**
     * The realm to use for A1 hashed password generation: "RETS Server".
     */
    public static final String RETS_REALM = "RETS Server";

    private static final Logger LOG =
        Logger.getLogger(PasswordMethod.class);
    private static Map sCachedMethods;
    private static Map sRegisteredMethods;
    private static String sDefaultMethod;
    private static String sDefaultOptions;
    protected String mMethod;
    protected String mOptions;

    static
    {
        sCachedMethods = new HashMap();
        sRegisteredMethods = new HashMap();
        sDefaultMethod = PLAIN_TEXT;
        sDefaultOptions = "";
        registerMethod("", PlainTextMethod.class);
        registerMethod("A1", DigestA1Method.class);
    }
}
