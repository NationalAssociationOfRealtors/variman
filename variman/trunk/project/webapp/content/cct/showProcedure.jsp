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
    <p>
      <html:link forward="userPage">Return to main page</html:link>
    </p>
    <table>
      <tr>
        <td>Description:</td>
        <td>
          <bean:write name="cctDisplayBean" property="test.description"/>
        </td>
      </tr>
      <tr>
        <td>Status:</td>
        <cct:teststatustd name="cctDisplayBean" property="result.status"/>
      </tr>

      <tr>
        <td valign="top">Procedure:</td>
        <td valign="top">
          <p>
            From the client, do the following:
          </p>
          <ol>
            <logic:iterate name="cctDisplayBean" property="test.procedure"
                           id="procedureLine" type="String">
              <li><bean:write name="procedureLine"/></li>
            </logic:iterate>
          </ol>
        </td>
      </tr>
      <logic:notEmpty name="cctDisplayBean" property="result.date">
        <tr>
          <td>Last run:</td>
          <td>
            <bean:write name="cctDisplayBean" property="result.date"
                        formatKey="pattern.date"/>
          </td>
        </tr>
      </logic:notEmpty>
    </table>
    <bean:define id="pageTestName" type="String"
                 name="cctDisplayBean" property="test.name"/>
    <logic:present name="cctActiveTest">
      <html:form action="/cct/stop_test" method="GET">
        <html:submit>Stop</html:submit>
        <html:hidden property="testName" value="<%= pageTestName %>" />
        <html:hidden property="done" value="showProcedure"/>
      </html:form>
    </logic:present>
    <logic:present name="cctNoRunningTest">
      <html:form action="/cct/start_test" method="GET">
        <html:submit>Start</html:submit>
        <html:hidden property="testName" value="<%= pageTestName %>" />
        <html:hidden property="done" value="showProcedure"/>
      </html:form>
    </logic:present>

    <logic:notEmpty name="cctDisplayBean" property="result.messages">
      <p>
        Messages from last run:
        <ol>
          <logic:iterate name="cctDisplayBean" property="result.messages"
                         id="messageLine" type="String">
            <li><bean:write name="messageLine"/></li>
          </logic:iterate>
        </ol>
      </p>
    </logic:notEmpty>
  </body>
</html:html>
