/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.realtors.rets.server.cct.*;

public interface ServletHandler
{
    public String getName();

    public void reset();

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException;

    public void validate(ValidationResults results);

    public ValidationResults validate();

    public void setGetInvokeCount(InvokeCount callCount);
}
