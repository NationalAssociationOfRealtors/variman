/*
 */
package org.realtors.rets.server.metadata.format;



public abstract class UpdateFormatter extends MetadataFormatter
{

    public void setLevels(String[] levels)
    {
        mResourceName = levels[0];
        mClassName = levels[1];
    }

    protected String mResourceName;
    protected String mClassName;
}
