/*
 */
package org.realtors.rets.server;

import junit.framework.AssertionFailedError;

public class LinesEqualTestCaseTest extends LinesEqualTestCase
{
    public void testEmptyLines()
    {
        assertLinesEqual("", "");
    }

    public void testNoLine()
    {
        assertLinesEqual("foo", "foo");
        try
        {
            assertLinesEqual("foo", "bar");
            fail("Should have thrown exception");
        }
        catch (AssertionFailedError e)
        {
            assertTrue(e.getMessage().startsWith("Line 1"));
        }
    }

    public void testLine()
    {
        assertLinesEqual("foo\nbar",
                         "foo\nbar");
        try
        {
            assertLinesEqual("foo\nbar",
                             "foo\nblah");
            fail("Should have thrown exception");
        }
        catch (AssertionFailedError e)
        {
            assertTrue(e.getMessage().startsWith("Line 2"));
        }
    }

    public void testExpectedLonger()
    {
        try
        {
            assertLinesEqual("foo\nbar", "foo");
            fail("Should have thrown excpetion");
        }
        catch (AssertionFailedError e)
        {
            assertTrue(e.getMessage().startsWith("Line 2"));
        }
    }

    public void testActualLonger()
    {
        try
        {
            assertLinesEqual("foo", "foo\nbar");
            fail("Should have thrown exception");
        }
        catch (AssertionFailedError e)
        {
            String message = e.getMessage();
            assertTrue(message.startsWith("Line 2"));
        }
    }

    public void testCarriageReturn()
    {
        try
        {
            assertLinesEqual("foo\rbar", "foo\nbar");
            fail("Should have thrown exception");
        }
        catch (AssertionFailedError e)
        {
            assertTrue(e.getMessage().startsWith("Line separator"));
        }
    }

    public void testLineFeed()
    {
        try
        {
            assertLinesEqual("foo\nbar", "foo\rbar");
            fail("Should have thrown exception");
        }
        catch (AssertionFailedError e)
        {
            assertTrue(e.getMessage().startsWith("Line separator"));
        }
    }

    public void testCarriageReturnLineFeed()
    {
        try
        {
            assertLinesEqual("foo\r\nbar", "foo\nbar");
            fail("Should have thrown exception");
        }
        catch (AssertionFailedError e)
        {
            assertTrue(e.getMessage().startsWith("Line separator"));
        }
    }
}
