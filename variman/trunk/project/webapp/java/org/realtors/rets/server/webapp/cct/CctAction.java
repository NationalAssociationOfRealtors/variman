package org.realtors.rets.server.webapp.cct;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.struts.action.Action;

/**
 * Created by IntelliJ IDEA.  Obliterated by Eclipse.
 * User: dbt
 * Date: Aug 26, 2003
 * Time: 2:05:42 PM
 * To change this template use Options | File Templates.
 */
public abstract class CctAction extends Action
{
    /**
     * 
     * @param session
     * @return
     */
    protected CertificationTestSuite getSuite(HttpSession session)
    {
        return (CertificationTestSuite) session.getAttribute(TESTSUITE_KEY);
    }
    
    private static final Log LOG = LogFactory.getLog(CctAction.class);
    public static final String TESTSUITE_KEY = "cctSuite";
    public static final String USER_KEY = "cctUser";
    public static final String USERINFO_KEY = "cctUserInfo";

    protected static final UserUtils UTILS = new UserUtils();
}
