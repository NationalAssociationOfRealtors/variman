/*
 */
package org.realtors.rets.server.webapp.cct;

import org.apache.commons.lang.enum.Enum;

public interface CertificationTest
{
    public void init(String testContext);

    public String getDescription();

    public String getProcedure();

    public String getMessage();

    public Status getStatus();

    public void start();

    public void stop();

    public void cancel();

    static class Status extends Enum
    {
        protected Status(String status)
        {
            super(status);
        }
    }

    public static final Status PASSED = new Status("passed");
    public static final Status FAILED = new Status("failed");
    public static final Status RUNNING = new Status("running");
    public static final Status NOTRUN = new Status("not-run");
}
