package org.realtors.rets.server.admin;

public class StringJoiner
{
    public StringJoiner(String separator)
    {
        mSeparator = separator;
        mCurrentSeparator = "";
        mBuffer = new StringBuffer();
    }

    public void append(Object object)
    {
        mBuffer.append(mCurrentSeparator);
        mBuffer.append(object.toString());
        mCurrentSeparator = mSeparator;
    }

    public String toString()
    {
        return mBuffer.toString();
    }

    private String mSeparator;
    private String mCurrentSeparator;
    private StringBuffer mBuffer;
}
