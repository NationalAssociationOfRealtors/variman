/*
 */
package org.realtors.rets.server.metadata.format;



public abstract class SearchHelpFormatter extends MetadataFormatter
{
    public void setLevels(String[] levels)
    {
        mResourceName = levels[0];
    }

    protected String mResourceName;
}
