/*
 */
package org.realtors.rets.server;

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
        PasswordMethod copy = new PlainTextMethod();
        copy.mMethod = mMethod;
        copy.mOptions = mOptions;
        return copy;
    }
}
