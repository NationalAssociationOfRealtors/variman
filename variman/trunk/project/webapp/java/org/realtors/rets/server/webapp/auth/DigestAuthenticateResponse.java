/*
 */
package org.realtors.rets.server.webapp.auth;

import org.apache.commons.lang.RandomStringUtils;


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
        generateNonce();
        generateOpaque();
    }

    private void generateNonce()
    {
        String nonce = "" + System.currentTimeMillis() +
            RandomStringUtils.randomAlphanumeric(5);
        mNonce = Util.md5(nonce);
    }

    private void generateOpaque()
    {
        mOpaque = mNonce;
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

    /**
     * Returns a header suitable for a "WWW-Authenticate" HTTP header.
     *
     * @return a "WWW-Authenticate" header
     */
    public String getHeader()
    {
        StringBuffer header = new StringBuffer();
        header.append("Digest ");
        header.append("realm=\"").append(mRealm).append("\", ");
        header.append("qop=\"").append(mQop).append("\", ");
        header.append("nonce=\"").append(mNonce).append("\", ");
        header.append("opaque=\"").append(mOpaque).append("\"");
        return header.toString();
    }

    private String mRealm;
    private String mQop;
    private String mNonce;
    private String mOpaque;
}
