/*
 */
package org.realtors.rets.server.metadata.format;



public abstract class UpdateTypeFormatter extends MetadataFormatter
{
    public void setLevels(String[] levels)
    {
        mResourceName = levels[0];
        mClassName = levels[1];
        mUpdateName = levels[2];
    }

    protected String mResourceName;
    protected String mClassName;
    protected String mUpdateName;
}
