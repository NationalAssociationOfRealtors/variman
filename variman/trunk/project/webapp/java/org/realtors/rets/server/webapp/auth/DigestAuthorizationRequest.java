/*
 */
package org.realtors.rets.server.webapp.auth;

import org.realtors.rets.server.HashUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class DigestAuthorizationRequest
{
    public DigestAuthorizationRequest()
    {
        mUsername = null;
        mUri = null;
        mNonce = null;
        mQop = "auth";
        mAlgorithm = "HashUtils";
        mNonceCount = "";
        mCnonce = "";
        mResponse = "";
        mOpaque = "";
    }

    /**
     * Initialize a request from an HTTP "Authorization" header line.
     *
     * @param header HTTP "Authorizatoin" header
     * @throws IllegalArgumentException if the header is unparseable
     */
    public DigestAuthorizationRequest(String header)
        throws IllegalArgumentException
    {
        if (!header.startsWith(PREFIX))
        {
            throw new IllegalArgumentException("Incorrect prefix [" + header +
                                               "]");
        }
        String authorization = header.substring(PREFIX.length());

        // Setup default values
        mUsername = null;
        mRealm = null;
        mNonce = null;
        mUri = null;
        mResponse = null;
        mAlgorithm = "HashUtils";
        mCnonce = "";
        mOpaque = "";
        mQop = "auth";
        mNonceCount = "";

        String[] tokens = StringUtils.split(authorization, ",");
        for (int i = 0; i < tokens.length; i++)
        {
            String token = tokens[i];
            String[] keyValue = StringUtils.split(token, "=", 2);
            if (keyValue.length != 2)
            {
                throw new IllegalArgumentException("Invalid key/value pair: " +
                                                   token + "[" + header + "]");
            }
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();
            if (key.equals("username"))
            {
                mUsername = removeQuotes(value);
            }
            else if (key.equals("realm"))
            {
                mRealm = removeQuotes(value);
            }
            else if (key.equals("nonce"))
            {
                mNonce = removeQuotes(value);
            }
            else if (key.equals("uri"))
            {
                mUri = removeQuotes(value);
            }
            else if (key.equals("qop"))
            {
                mQop = removeQuotes(value);
            }
            else if (key.equals("algorithm"))
            {
                mAlgorithm = removeQuotes(value);
            }
            else if (key.equals("nc"))
            {
                mNonceCount = value;
            }
            else if (key.equals("cnonce"))
            {
                mCnonce = removeQuotes(value);
            }
            else if (key.equals("response"))
            {
                mResponse = removeQuotes(value);
            }
            else if (key.equals("opaque"))
            {
                mOpaque = removeQuotes(value);
            }
        }

        if ((mUsername == null) || (mRealm == null) || (mNonce == null) ||
            (mUri == null) || (mResponse == null))
        {
            throw new IllegalArgumentException("Required fields not set");
        }
    }

    private String removeQuotes(String string)
    {
        int start = 0;
        if (string.startsWith("\""))
        {
            start++;
        }

        int end = string.length();
        if(string.endsWith("\""))
        {
            end--;
        }

        return string.substring(start, end);
    }

    public void setUsername(String username)
    {
        mUsername = username;
    }

    public String getUsername()
    {
        return mUsername;
    }

    public void setUri(String uri)
    {
        mUri = uri;
    }

    public String getUri()
    {
        return mUri;
    }

    public void setNonce(String nonce)
    {
        mNonce = nonce;
    }

    public String getNonce()
    {
        return mNonce;
    }

    public void setQop(String qop)
    {
        mQop = qop;
    }

    public String getQop()
    {
        return mQop;
    }

    public String getAlgorithm()
    {
        return mAlgorithm;
    }

    public void setNonceCount(String nonceCount)
    {
        mNonceCount = nonceCount;
    }

    public String getNonceCount()
    {
        return mNonceCount;
    }

    public String getCnonce()
    {
        return mCnonce;
    }

    public void setResponse(String response)
    {
        mResponse = response;
    }

    public String getResponse()
    {
        return mResponse;
    }

    public void setOpaque(String opaque)
    {
        mOpaque = opaque;
    }

    public String getOpaque()
    {
        return mOpaque;
    }

    public void setCnonce(String cnonce)
    {
        mCnonce = cnonce;
    }

    public void setRealm(String realm)
    {
        mRealm = realm;
    }

    public String getRealm()
    {
        return mRealm;
    }

    public void setMethod(String method)
    {
        mMethod = method;
    }

    public String getMethod()
    {
        return mMethod;
    }

    public boolean verifyResponse(String password)
    {
        return verifyResponse(password, false);
    }

    public boolean verifyResponse(String password, boolean passwordIsA1)
    {
        if (password == null)
        {
            LOG.debug("Null password always fails");
            return false;
        }
        String a1;
        if (passwordIsA1)
        {
            a1 = password;
        }
        else
        {
            a1 = HashUtils.md5(mUsername + ":" + mRealm + ":" + password);
        }
        String a2 = HashUtils.md5(mMethod + ":" + mUri);
        String expectedResponse =
            HashUtils.md5(a1 + ":" + mNonce + ":" + mNonceCount +
                          ":" + mCnonce + ":" + mQop + ":" + a2);
        LOG.debug("Expected response: " + expectedResponse);
        LOG.debug("Actual response:   " + mResponse);
        return (expectedResponse.equals(mResponse));
    }

    private static final String PREFIX = "Digest ";
    private String mUsername;
    private String mUri;
    private String mNonce;
    private String mQop;
    private String mAlgorithm;
    private String mNonceCount;
    private String mCnonce;
    private String mResponse;
    private String mOpaque;
    private String mRealm;
    private String mMethod;
    private static final Logger LOG =
        Logger.getLogger(DigestAuthorizationRequest.class);
}
