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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.realtors.rets.server.cct.ValidationResult;

public interface ServletHandler
{
    public String getName();

    public void reset();

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException;

    public void validate(ValidationResult result);

    public ValidationResult validate();

    public void setGetInvokeCount(InvokeCount callCount);
}
