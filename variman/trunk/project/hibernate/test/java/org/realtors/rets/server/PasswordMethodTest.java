/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server;

import junit.framework.TestCase;

public class PasswordMethodTest extends TestCase
{
    public void testDigestEquals()
    {
        PasswordMethod method = PasswordMethod.getInstance("A1", "RETS Server");
        assertTrue(method instanceof DigestA1Method);
        PasswordMethod methodCopy = method.deepCopy();
        assertEquals(method, methodCopy);
    }

    public void testPlainTextEquals()
    {
        PasswordMethod method = PasswordMethod.getInstance("", "");
        assertTrue(method instanceof PlainTextMethod);
        PasswordMethod methodCopy = method.deepCopy();
        assertEquals(method, methodCopy);
    }
}