/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.webapp.auth;




public class DigestAuthenticateResponse
{
    public DigestAuthenticateResponse(String realm)
    {
        this(realm, "auth");
    }

    public DigestAuthenticateResponse(String realm, String qop)
    {
        mRealm = realm;
        mQop = qop;
        mStale = false;
        mNonce = null;
        mOpaque = null;
    }

    public String getRealm()
    {
        return mRealm;
    }

    public void setRealm(String realm)
    {
        mRealm = realm;
    }

    public String getQop()
    {
        return mQop;
    }

    public void setQop(String qop)
    {
        mQop = qop;
    }

    public String getNonce()
    {
        return mNonce;
    }

    public void setNonce(String nonce)
    {
        mNonce = nonce;
    }

    public String getOpaque()
    {
        return mOpaque;
    }

    public void setOpaque(String opaque)
    {
        mOpaque = opaque;
    }

    public void setStale(boolean stale)
    {
        mStale = stale;
    }

    /**
     * Returns a header suitable for a "WWW-Authenticate" HTTP header.
     *
     * @return a "WWW-Authenticate" header
     */
    public String getHeader()
    {
        StringBuffer header = new StringBuffer();
        header.append("Digest");
        String separator = " ";
        header.append(separator).append("realm=\"").append(mRealm).append("\"");
        separator = ", ";
        header.append(separator).append("qop=\"").append(mQop).append("\"");
        header.append(separator).append("nonce=\"").append(mNonce).append("\"");
        header.append(separator).append("opaque=\"").append(mOpaque)
            .append("\"");
        if (mStale)
        {
            header.append(separator).append("stale=true");
        }
        return header.toString();
    }

    private String mRealm;
    private String mQop;
    private String mNonce;
    private String mOpaque;
    private boolean mStale;
}
