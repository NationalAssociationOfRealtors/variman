<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>
<html:html>
 <html:form action="/registration">

  <bean:message key="registration.field.acceptLegalese"/>
  <html:checkbox property="acceptedLegalese" /><br>

  <bean:message key="registration.field.agentId"/>
  <html:text property="agentId"/><br>

  <bean:message key="registration.field.company"/>
  <html:text property="company"/><br>

  <bean:message key="registration.field.email"/>
  <html:text property="email"/><br>

  <bean:message key="registration.field.firstName"/>
  <html:text property="firstName"/><br>

  <bean:message key="registration.field.lastName"/>
  <html:text property="lastName"/><br>

  <bean:message key="registration.field.password"/>
  <html:password property="password" /><br>

  <bean:message key="registration.field.verifyPassword"/>
  <html:password property="verifyPassword" /><br>

  <bean:message key="registration.field.productName"/>
  <html:text property="productName"/><br>

  <bean:message key="registration.field.productVersion"/>
  <html:text property="productVersion"/><br>

  <bean:message key="registration.field.userAgent"/>
  <html:text property="userAgent"/><br>

  <bean:message key="registration.field.username"/>
  <html:text property="username"/><br>

  <html:submit /><html:reset />
 </html:form>
</html:html>
