/*
 */
package org.realtors.rets.server.metadata.format;



abstract public class ValidationLookupTypeFormatter extends MetadataFormatter
{
    public void setLevels(String[] levels)
    {
        mResourceName = levels[0];
        mValidationLookupName = levels[1];
    }

    protected String mResourceName;
    protected String mValidationLookupName;
}
