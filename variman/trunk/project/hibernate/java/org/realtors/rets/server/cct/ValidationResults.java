/*
 * Created on Sep 9, 2003
 *
 */
package org.realtors.rets.server.cct;

/**
 * @author kgarner
 */
public interface ValidationResults
{
    public abstract ValidationResult getResultByName(String name);
}