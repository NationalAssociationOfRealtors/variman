/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.webapp.cct;

import org.realtors.rets.server.cct.ValidationResult;

/**
 * Subclasses must implement getDescription(), start(), and validate().
 */
public abstract class BaseCertificationTest implements CertificationTest
{
    public void validate(ValidationResult result)
    {
        RetsHandlers handlers = getRetsHandlers();
        handlers.validateAll(result);
    }

    public void init(String testContext)
    {
        mTestContext = testContext;
    }

    protected RetsHandlers getRetsHandlers()
    {
        RetsHandlers handlers =
            HandlerManager.getInstance().getHandlers(mTestContext);
        return handlers;
    }

    protected String mTestContext;
}
