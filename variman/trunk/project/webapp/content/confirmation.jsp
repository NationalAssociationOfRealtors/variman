<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<html:html>
 <html:form action="/confirmation">
 <table>
  <tr><td>
  <bean:message key="registration.field.company"/></td><td>
  <bean:write name="cctRegistrationForm" property="company"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.email"/></td><td>
  <bean:write name="cctRegistrationForm" property="email"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.firstName"/></td><td>
  <bean:write name="cctRegistrationForm" property="firstName"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.lastName"/></td><td>
  <bean:write name="cctRegistrationForm" property="lastName"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.productName"/></td><td>
  <bean:write name="cctRegistrationForm" property="productName"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.productVersion"/></td><td>
  <bean:write name="cctRegistrationForm" property="productVersion"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.userAgent"/></td><td>
  <bean:write name="cctRegistrationForm" property="userAgent"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.username"/></td><td>
  <bean:write name="cctRegistrationForm" property="username"/></td></tr>
  <tr><td colspan=2 align="center">
  <bean:message key="registration.confirmation.instruction"/>
  <html:submit><bean:message key="registration.confirmation.ok"/></html:submit>
  <html:cancel><bean:message key="registration.confirmation.fix"/></html:cancel>
 </html:form>
  </td></tr>
  </table>
</html:html>
