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

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.builder.EqualsBuilder;

public class DigestA1Method extends PasswordMethod
{
    public String hash(String username, String plainTextPassword)
    {
        String a1 = DigestUtils.md5Hex(username + ":" + mRealm + ":" +
                                       plainTextPassword);
        return a1;
    }

    public boolean verifyPassword(String expectedPassword,
                                  String passwordToVerify)
    {
        return expectedPassword.equals(passwordToVerify);
    }

    protected boolean parseOptions(String options)
    {
        mRealm = options;
        return true;
    }

    protected PasswordMethod deepCopy()
    {
        DigestA1Method copy = new DigestA1Method();
        copy.mMethod = mMethod;
        copy.mOptions = mOptions;
        copy.mRealm = mRealm;
        return copy;
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof DigestA1Method))
        {
            return false;
        }
        DigestA1Method rhs = (DigestA1Method) obj;
        return new EqualsBuilder()
            .append(mMethod, rhs.mMethod)
            .append(mOptions, rhs.mOptions)
            .append(mRealm, rhs.mRealm)
            .isEquals();
    }

    private String mRealm;
}
