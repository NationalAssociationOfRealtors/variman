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
    <p>
      <html:link forward="adminPage">Return to the main admin page</html:link>
    </p>
    <p>
      Displaying data for
      <bean:write name="cctAdminUserInfo" property="user.name"/> from
      <bean:write name="cctAdminUserInfo" property="company"/>
      using <bean:write name="cctAdminUserInfo" property="productName"/>
      <bean:write name="cctAdminUserInfo" property="productVersion"/>.
    </p>
    <table cellpadding="2" cellspacing="0" border="1" width="99%">
      <tr>
        <th>Test description</th><th>Status</th>
      </tr>
      <logic:iterate id="displayBean" name="cctDisplayBeans"
                     type="org.realtors.rets.server.webapp.cct.TestDisplayBean"
                     indexId="counter">
        <bean:define id="pageTestName" type="String"
                     name="displayBean" property="test.name"/>
        <tr>
          <cct:evenoddtd count="counter" width="40%">
            <bean:write name="displayBean" property="test.description" />
          </cct:evenoddtd>
          <cct:teststatustd name="displayBean" property="result.status"/>
        </tr>
      </logic:iterate>
    </table>
  </body>
</html:html>
