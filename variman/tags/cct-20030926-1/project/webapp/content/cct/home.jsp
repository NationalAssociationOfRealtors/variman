<!-- <%@ page contentType="text/html;charset=UTF-8" language="java" %> -->
<!-- <%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %> -->
<!-- <%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %> -->
<!-- <%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %> -->
<!-- <%@ taglib uri="/WEB-INF/tld/cct.tld" prefix="cct" %> -->
<html:html>
<head>
<title>Client Compliance Testing Home</title>
<link rel="stylesheet" href="cct.css" />
</head>
 <p>Welcome, <bean:write name="cctUser" property="firstName" />
 <bean:write name="cctUser" property="lastName" />
 <br>
 The login URL is <bean:write name="cctLoginUrl"/></p>
 <table cellpadding="2" cellspacing="0" border="1" width="99%">
  <tr>
   <th>Test description</th><th>Status</th><th>Start/Stop</th>
  </tr>
  <logic:iterate id="displayBean" name="cctDisplayBeans"
                 type="org.realtors.rets.server.webapp.cct.TestDisplayBean"
                 indexId="counter">
    <bean:define id="pageTestName" type="String"
                 name="displayBean" property="test.name"/>
    <tr>
     <cct:evenoddtd count="counter" width="40%">
      <html:link action="/cct/showTestProcedure" paramId="testName"
                 paramName="displayBean" paramProperty="test.name">
       <bean:write name="displayBean" property="test.description" />
      </html:link>
     </cct:evenoddtd>
     <cct:teststatustd name="displayBean" />
     <cct:evenoddtd count="counter" width="10%">
      <logic:notPresent name="cctActiveTest">
       <html:form action="/cct/start_test" method="GET" >
        <html:submit>Start</html:submit>
        <html:hidden property="testName" value='<%= pageTestName %>'/>
        <html:hidden property="done" value="home"/>
       </html:form>
      </logic:notPresent>
      <logic:present name="cctActiveTest">
       <logic:equal name="cctActiveTest" value="<%= pageTestName %>">
        <html:form action="/cct/stop_test" method="GET" >
         <html:submit>Stop</html:submit>
         <html:hidden property="testName" value="<%= pageTestName %>" />
         <html:hidden property="done" value="home"/>
        </html:form>
       </logic:equal>
       <logic:notEqual name="cctActiveTest" value="<%= pageTestName %>" >
        <html:form action="/cct/start_test" method="GET" >
         <html:submit disabled="true" >Start</html:submit>
         <html:hidden property="testName" value="<%= pageTestName %>" />
         <html:hidden property="done" value="home"/>
        </html:form>
       </logic:notEqual>
      </logic:present>
     </cct:evenoddtd>
    </tr>
  </logic:iterate>
  </table>
  <p>
    <bean:message key="home.link.instruction"/>
  </p>
  <p>
    <html:link action="/cct/resetAllTests">
      <bean:message key="home.reset.tests"/>
    </html:link>
  </p>
  <p>
    <html:link forward="logout">
      <bean:message key="home.logout.text"/>
    </html:link>
  </p>
</html:html>
