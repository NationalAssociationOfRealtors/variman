/*
 */
package org.realtors.rets.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;


public abstract class PasswordMethod
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

    public static synchronized PasswordMethod getInstance(String method,
                                                          String options)
    {
        String id = makeId(method, options);
        PasswordMethod passwordMethod =
            (PasswordMethod) mCachedMethods.get(id);
        if (passwordMethod == null)
        {
            try
            {
                Class clazz = (Class) mRegisteredMethods.get(method);
                LOG.debug("Instantiating " + clazz.getName());
                passwordMethod = (PasswordMethod) clazz.newInstance();
                passwordMethod.mMethod = method;
                passwordMethod.mOptions = options;
                if (passwordMethod.parseOptions(options))
                {
                    LOG.debug("Adding id [" + id + "] to cache");
                    mCachedMethods.put(id, passwordMethod);
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
        mRegisteredMethods.put(method, clazz);
    }

    public static synchronized void initMethods()
    {
        // Do nothing... all work is done once in static block. This method
        // just insures the static block gets loaded.
    }

    static
    {
        mCachedMethods = new HashMap();
        mRegisteredMethods = new HashMap();
        registerMethod("", PlainTextMethod.class);
        registerMethod("A1", DigestA1Method.class);
    }

    private static final Logger LOG =
        Logger.getLogger(PasswordMethod.class);
    private static Map mCachedMethods;
    private static Map mRegisteredMethods;
    protected String mMethod;
    protected String mOptions;
}
