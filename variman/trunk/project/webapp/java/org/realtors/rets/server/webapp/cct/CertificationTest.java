/*
 */
package org.realtors.rets.server.webapp.cct;

import org.realtors.rets.server.cct.StatusEnum;
import org.realtors.rets.server.cct.ValidationResult;


public interface CertificationTest
{
    public void init(String testContext);

    public String getDescription();

    public String getProcedure();

    public String getMessage();

    public StatusEnum getStatus();
    
    //public ValidationResult validate(ValidationResult results);
    public ValidationResult validate();

    public void start();

    public void stop();

    public void cancel();

    public String getName();
}
