/*
 */
package org.realtors.rets.server.metadata.format;



public abstract class LookupTypeFormatter extends MetadataFormatter
{
    public void setLevels(String[] levels)
    {
        mResourceName = levels[0];
        mLookupName = levels[1];
    }

    protected String mLookupName;
    protected String mResourceName;
}
