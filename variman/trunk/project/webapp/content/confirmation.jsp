<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>
<html:html>
 <html:form action="/confirmation">
 <table>
  <tr><td>
  <bean:message key="registration.field.agentId"/></td><td>
  <bean:write name="registrationForm" property="agentId"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.company"/></td><td>
  <bean:write name="registrationForm" property="company"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.email"/></td><td>
  <bean:write name="registrationForm" property="email"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.firstName"/></td><td>
  <bean:write name="registrationForm" property="firstName"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.lastName"/></td><td>
  <bean:write name="registrationForm" property="lastName"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.productName"/></td><td>
  <bean:write name="registrationForm" property="productName"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.productVersion"/></td><td>
  <bean:write name="registrationForm" property="productVersion"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.userAgent"/></td><td>
  <bean:write name="registrationForm" property="userAgent"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.username"/></td><td>
  <bean:write name="registrationForm" property="username"/></td></tr>
  <tr><td colspan=2 align="center">
  <bean:message key="registration.confirmation.instruction"/>
  <html:submit><bean:message key="registration.confirmation.ok"/></html:submit>
  <html:cancel><bean:message key="registration.confirmation.fix"/></html:cancel>
 </html:form>
  </td></tr>
  </table>
</html:html>
