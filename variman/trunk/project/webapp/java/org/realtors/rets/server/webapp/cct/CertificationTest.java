/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.webapp.cct;

// import org.realtors.rets.server.cct.StatusEnum;
import org.realtors.rets.server.cct.ValidationResult;


public interface CertificationTest
{
    public void init(String testContext);

    public String getDescription();

    public String[] getProcedure();

    public void validate(ValidationResult results);

    public void start();

    public void stop();

    public void cancel();

    public String getName();
}
