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
 <table cellpadding="2" cellspacing="0" border="1" width="99%">
  <tr>
   <th>Test description</th><th>Date run</th><th>Status</th><th>Messages</th><th>Start/Stop</th>
  </tr>
  <logic:iterate id="displayBean" name="cctDisplayBeans"
                 type="org.realtors.rets.server.webapp.cct.TestDisplayBean"
                 indexId="counter">
    <tr>
     <cct:evenoddtd count="counter" width="40%">
      <bean:write name="displayBean" property="test.description" />
     </cct:evenoddtd>
     <cct:evenoddtd count="counter">
      <bean:write name="displayBean" property="result.formattedDate"/>
     </cct:evenoddtd>
     <cct:teststatustd name="displayBean" />
     <cct:evenoddtd count="counter" width="30%">
       <logic:iterate id="message" name="displayBean" type="String"
                      property="result.messages" indexId="message_count">
         <bean:write name="message_count"/>: <bean:write name="message"/><br/>
       </logic:iterate>
     </cct:evenoddtd>
     <cct:evenoddtd count="counter" width="10%">
      <logic:notPresent name="cctActiveTest">
       <html:form action="/cct/start_test.do" method="GET" >
        <html:submit>Start</html:submit>
        <html:hidden property="testNumber" value='<%=counter.toString()%>'/>
       </html:form>
      </logic:notPresent>
      <logic:present name="cctActiveTest">
       <logic:equal name="cctActiveTest" value="<%=counter.toString()%>" >
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
  <html:link forward="logout">Logout</html:link>
</html:html>
