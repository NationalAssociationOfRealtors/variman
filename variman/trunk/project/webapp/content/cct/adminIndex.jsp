<!-- <%@ page contentType="text/html;charset=UTF-8" language="java" %> -->
<!-- <%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %> -->
<!-- <%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %> -->
<!-- <%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %> -->
<!-- <%@ taglib uri="/WEB-INF/tld/cct.tld" prefix="cct" %> -->
<html:html>
  <head>
    <title>Admin view</title>
    <link rel="stylesheet" href="cct.css"/>
  </head>
  <body>
    <table cellpadding="2" cellspacing="0" border="1" width="99%">
      <tr>
        <th>User</th><th>Status</th>
      </tr>
      <logic:iterate id="info" name="cctAdminIndexInfos"
                     type="org.realtors.rets.server.webapp.cct.AdminIndexInfo">
        <tr>
          <td>
            <html:link action="/cct/adminUserView" paramId="username"
                       paramName="info" paramProperty="userInfo.user.username">
              <bean:write name="info" property="userInfo.user.name"/>
            </html:link>
          </td>
          <cct:teststatustd name="info" property="overallStatus"/>
        </tr>
      </logic:iterate>
    </table>
    <p>
      <html:link forward="logout">
        <bean:message key="home.logout.text"/>
      </html:link>
      <br/>
      <html:link action="/cct/index">
        <bean:message key="admin.mainpage.text"/>
      </html:link>
    </p>
  </body>
</html:html>
