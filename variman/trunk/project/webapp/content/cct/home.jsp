<%@ page contentType="text/html;charset=UTF-8" language="java"
    import="org.realtors.rets.server.webapp.cct.*" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>
<jsp:useBean id="suite" scope="session"
  class="CertificationTestSuite" />
<html:html>
 <table border="1">
  <tr>
   <th>Test name</th><th>Status</th><th>Show Log</th><th>Start/Stop</th>
  </tr>
  <logic:iterate id="test" name="suite"
                 property="tests"
                 type="CertificationTest"
                 indexId="counter">
    <tr>
      <th align="left"><bean:write name="test" property="description" /> </th>
      <td><bean:write name="test" property="status.name" /></td>
      <td><bean:write name="test" property="message" /></td>
      <td><bean:write name="counter"/>, <%= counter %>
        <html:form action="/cct/start_test.do" method="GET" >
          <html:submit>Start</html:submit>
          <html:hidden property="testNumber" value='<%=counter.toString()%>'/>
        </html:form>
        <br/>
        <html:form action="/cct/stop_test.do" method="GET" >
          <html:submit>Stop</html:submit>
          <html:hidden property="testNumber" value='<%=counter.toString()%>'/>
        </html:form>
      </td>
    </tr>
  </logic:iterate>
  </table>
</html:html>
