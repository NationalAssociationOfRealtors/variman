/*
 * Created on Sep 9, 2003
 *
 */
package org.realtors.rets.server.cct;

/**
 * @hibernate.class table="rets_cct_validationresults"
 */
public class DBValidationResult extends ValidationResult
{
    /**
     * @hibernate.id generator-class="native"
     */
    public Long getId()
    {
        return mId;
    }

    /**
     * @hibernate.property 
     */
    public String getUsername()
    {
        return mUsername;
    }

//    /**
//     * @hibernate.component
//     *   class="org.realtors.rets.server.cct.ValidationResult" 
//     */
//    public ValidationResult getValidationResult()
//    {
//        return mValidationResult;
//    }

    /**
     * @param id The identifier
     */
    public void setId(Long id)
    {
        mId = id;
    }

    /**
     * @param username The username
     */
    public void setUsername(String username)
    {
        mUsername = username;
    }

//    public void setValidationResult(ValidationResult result)
//    {
//        mValidationResult = result;
//    }

    private Long mId;
    private String mUsername;
//    private ValidationResult mValidationResult;
}
