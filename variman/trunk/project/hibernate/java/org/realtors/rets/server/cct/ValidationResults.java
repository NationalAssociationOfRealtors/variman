/*
 * Created on Sep 9, 2003
 *
 */
package org.realtors.rets.server.cct;

import java.util.Iterator;

/**
 * @author kgarner
 */
public interface ValidationResults
{
    public ValidationResult getResultByName(String name);
    public void addResult(ValidationResult result);
    public Iterator iterator();
}