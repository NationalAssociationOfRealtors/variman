<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<html:html>
  <head>
    <title>RETS Server</title>
  </head>
  <body bgcolor="#ffffff">
    <h1 align="center">RETS Server</h1>
    <p>Welcome to CRT's RETS Server</p>
    <!-- with jsp we can a) use the forward mechanism and b) disable this
         on servers without registration -->
    <html:link forward="registration">Register for verification</html:link>
    <br>
    <html:link forward="userPage">Click here to log in</html:link>
  </body>
</html:html>