<!-- <%@ page contentType="text/html;charset=UTF-8" language="java" %> -->
<!-- <%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %> -->
<!-- <%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %> -->

<html:html>
  <head>
    <title>Login</title>
  </head>
  <body>
    <bean:parameter id="done" name="done" value="/" />

    <html:messages header="registration.error.header"
                   footer="registration.error.footer" id="error">
      <li><bean:write name="error"/></li>
    </html:messages>

    <html:form action="/login" focus="username">
      <html:hidden property="done" value="<%= done %>"/>
      <table border="0">
        <tr>
          <td align="right">
            <bean:message key="login.field.username"/>
          </td>
          <td align="left">
            <html:text property="username" size="30"/>
          </td>
        </tr>
        <tr>
          <td align="right">
            <bean:message key="login.field.password"/>
          </td>
          <td align="left">
            <html:password property="password" size="30"/>
          </td>
        </tr>
        <tr>
          <td><br/></td>
          <td>
            <html:submit styleClass="mbutton">
              <bean:message key="login.button.submit"/>
            </html:submit>
            <html:cancel styleClass="button">
              <bean:message key="login.button.cancel"/>
            </html:cancel>
          </td>
        </tr>
      </table>
    </html:form>
  </body>
</html:html>
