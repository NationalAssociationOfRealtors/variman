/*
 */
package org.realtors.rets.server.metadata.format;



public abstract class ValidationExternalTypeFormatter extends MetadataFormatter
{
    public void setLevels(String[] levels)
    {
        mResourceName = levels[0];
        mValidationExternalName = levels[1];
    }

    protected String mResourceName;
    protected String mValidationExternalName;
}
