/*
 * Created on Oct 2, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import org.realtors.rets.server.cct.StatusEnum;
import org.realtors.rets.server.cct.UserInfo;

/**
 * @author kgarner
 */
public class AdminIndexInfo
{
    /**
     * 
     * @return
     */
    public StatusEnum getOverallStatus()
    {
        return mOverallStatus;
    }

    /**
     * 
     * @return
     */
    public UserInfo getUserInfo()
    {
        return mUserInfo;
    }

    /**
     * 
     * @param enum
     */
    public void setOverallStatus(StatusEnum enum)
    {
        mOverallStatus = enum;
    }

    /**
     * 
     * @param info
     */
    public void setUserInfo(UserInfo info)
    {
        mUserInfo = info;
    }
    
    private StatusEnum mOverallStatus;
    private UserInfo mUserInfo;

}
