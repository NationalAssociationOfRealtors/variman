<!-- <%@ page contentType="text/html;charset=UTF-8" language="java" %> -->
<!-- <%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %> -->
<!-- <%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %> -->
<!-- <%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %> -->
<!-- <%@ taglib uri="/WEB-INF/tld/cct.tld" prefix="cct" %> -->
<html:html>
  <head>
    <title>Client Compliance Tester Procedure</title>
    <link rel="stylesheet" href="cct.css"/>
  </head>
  <body>
    <table>
      <tr>
        <td>Description:</td>
        <td>
          <bean:write name="cctDisplayBean" property="test.description"/>
        </td>
      </tr>
    </table>
    <p>
      From the client, do the following:
    </p>
    <ol>
      <logic:iterate name="cctDisplayBean" property="test.procedure"
                     id="procedureLine" type="String">
        <li><bean:write name="procedureLine"/></li>
      </logic:iterate>
    </ol>
    <html:link forward="userPage">Return to main page</html:link>
  </body>
</html:html>
