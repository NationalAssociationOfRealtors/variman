<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<html:html>

 <html:messages header="registration.error.header"
                footer="registration.error.footer" id="error">
  <li><bean:write name="error"/></li>
 </html:messages>

 <html:form action="/registration">
 <table>
  <tr><td>
  <bean:message key="registration.field.agentId"/></td><td>
  <html:text property="agentId"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.company"/></td><td>
  <html:text property="company"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.email"/></td><td>
  <html:text property="email"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.firstName"/></td><td>
  <html:text property="firstName"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.lastName"/></td><td>
  <html:text property="lastName"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.password"/></td><td>
  <html:password property="password" /></td></tr>

  <tr><td>
  <bean:message key="registration.field.verifyPassword"/></td><td>
  <html:password property="verifyPassword" /></td></tr>

  <tr><td>
  <bean:message key="registration.field.productName"/></td><td>
  <html:text property="productName"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.productVersion"/></td><td>
  <html:text property="productVersion"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.userAgent"/></td><td>
  <html:text property="userAgent"/></td></tr>

  <tr><td>
  <bean:message key="registration.field.username"/></td><td>
  <html:text property="username"/></td></tr>
  <tr><td colspan=2 align="center">
  <textarea ROWS=20 COLS=80 READONLY>
    <jsp:include page="eula.html" />
  </textarea>
  <br>
  <bean:message key="registration.field.acceptLegalese"/>:
  <html:checkbox property="acceptedLegalese" /><br>
  <html:submit /><html:reset />
  </td></tr>
  </table>
 </html:form>
</html:html>
