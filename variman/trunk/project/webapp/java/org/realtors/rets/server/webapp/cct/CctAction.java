package org.realtors.rets.server.webapp.cct;

import java.util.List;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;

import org.realtors.rets.server.webapp.cct.tests.NormalLogin;
import org.realtors.rets.server.User;

/**
 * Created by IntelliJ IDEA.
 * User: dbt
 * Date: Aug 26, 2003
 * Time: 2:05:42 PM
 * To change this template use Options | File Templates.
 */
public abstract class CctAction extends RetsAction
{
    void loadTestSuite(HttpSession session)
    {
        if (session.getAttribute("suite") == null)
        {
            User user = getUser(session);

            CertificationTestSuite suite = new CertificationTestSuite(
                user.getUsername());
            session.setAttribute("suite", suite);
        }
    }
}
