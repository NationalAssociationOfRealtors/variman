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

import org.apache.commons.lang.builder.EqualsBuilder;

public class PlainTextMethod extends PasswordMethod
{
    public String hash(String username, String plainTextPassword)
    {
        return plainTextPassword;
    }

    public boolean verifyPassword(String expectedPassword,
                                  String passwordToVerify)
    {
        return expectedPassword.equals(passwordToVerify);
    }

    protected boolean parseOptions(String options)
    {
        return true;
    }

    protected PasswordMethod deepCopy()
    {
        PlainTextMethod copy = new PlainTextMethod();
        copy.mMethod = mMethod;
        copy.mOptions = mOptions;
        return copy;
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof PlainTextMethod))
        {
            return false;
        }
        PlainTextMethod rhs = (PlainTextMethod) obj;
        return new EqualsBuilder()
            .append(mMethod, rhs.mMethod)
            .append(mOptions, rhs.mOptions)
            .isEquals();
    }
}
