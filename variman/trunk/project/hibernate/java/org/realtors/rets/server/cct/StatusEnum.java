/*
 * Created on Sep 3, 2003
 *
 */
package org.realtors.rets.server.cct;

import org.apache.commons.lang.enum.Enum;

import net.sf.hibernate.PersistentEnum;

/**
 * @author kgarner
 */
public class StatusEnum
    extends Enum
    implements PersistentEnum
{
    private StatusEnum(int i, String name)
    {
        super(name);
        mValue = i;
    }

    public int toInt()
    {
        return mValue;
    }
    
    public StatusEnum fromInt(int value)
    {
        switch (value)
        {
            case 0:
                return PASSED;
            case 1:
                return FAILED;
            case 2:
                return RUNNING;
        }
        
        return NOT_RUN;
    }
    
    private int mValue;
    
    public static final StatusEnum PASSED = new StatusEnum(0, "passed");
    public static final StatusEnum FAILED = new StatusEnum(1, "failed");
    public static final StatusEnum RUNNING = new StatusEnum(2, "running");
    public static final StatusEnum NOT_RUN = new StatusEnum(3, "not-run");
}