<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>
<html:html>
 <!-- html:form action="/confirmation" -->
 <table>
  <tr><td>
  <bean:message key="registration.field.agentId"/></td><td>
  <bean:write name="RegUser" property="agentId"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.company"/></td><td>
  <bean:write name="RegUserInfo" property="company"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.email"/></td><td>
  <bean:write name="RegUserInfo" property="email"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.firstName"/></td><td>
  <bean:write name="RegUser" property="firstName"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.lastName"/></td><td>
  <bean:write name="RegUser" property="lastName"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.productName"/></td><td>
  <bean:write name="RegUserInfo" property="productName"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.productVersion"/></td><td>
  <bean:write name="RegUserInfo" property="productVersion"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.userAgent"/></td><td>
  <bean:write name="RegUserInfo" property="userAgent"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.username"/></td><td>
  <bean:write name="RegUser" property="username"/></td></tr>
  <tr><td colspan=2 align="center">
  <bean:message key="registration.confirmation.instruction"/>
  <!-- html:submit /-->
 <!--/html:form -->
  </td></tr>
  </table>
</html:html>
