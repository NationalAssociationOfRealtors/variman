<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/cct.tld" prefix="cct" %>
<html:html>
<head>
<title>Client Compliance Testing Home</title>
<link rel="stylesheet" href="cct.css" />
</head>
 Welcome, <bean:write name="cctUser" property="firstName" />
 <bean:write name="cctUser" property="lastName" />
 <br>
 <table cellpadding="2" cellspacing="0" border="1">
  <tr>
   <td>&nbsp;</td>
   <th>Test name</th><th>Status</th><th>Show Log</th><th>Start/Stop</th>
  </tr>
  <logic:iterate id="test" name="cctSuite"
                 property="tests"
                 type="org.realtors.rets.server.webapp.cct.CertificationTest"
                 indexId="counter">
    <tr>
     <td><%= counter %></td>
     <cct:evenoddtd count="counter" >
      <bean:write name="test" property="description" />
     </cct:evenoddtd>
     <cct:teststatustd name="test" />
     <cct:evenoddtd count="counter" >
      <bean:write name="test" property="message" />
     </cct:evenoddtd>
     <cct:evenoddtd count="counter">
      <logic:notPresent name="cctActiveTest">
       <html:form action="/cct/start_test.do" method="GET" >
        <html:submit>Start</html:submit>
        <html:hidden property="testNumber" value='<%=counter.toString()%>'/>
       </html:form>
      </logic:notPresent>
      <logic:present name="cctActiveTest">
       <logic:equal name="cctActiveTest" value="<%= counter.toString() %>" >
        <html:form action="/cct/stop_test.do" method="GET" >
         <html:submit>Stop</html:submit>
         <html:hidden property="testNumber" value='<%=counter.toString()%>'/>
        </html:form>
       </logic:equal>
       <logic:notEqual name="cctActiveTest" value="<%= counter.toString() %>" >
        <html:form action="/cct/start_test.do" method="GET" >
         <html:submit disabled="true" >Start</html:submit>
         <html:hidden property="testNumber" value='<%=counter.toString()%>'/>
        </html:form>
       </logic:notEqual>
      </logic:present>
     </cct:evenoddtd>
    </tr>
  </logic:iterate>
  </table>
</html:html>
