/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server;

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
        PasswordMethod copy = new DigestA1Method();
        copy.mMethod = mMethod;
        copy.mOptions = mOptions;
        return copy;
    }

    private String mRealm;
}
