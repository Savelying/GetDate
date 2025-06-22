<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
    <head>
        <title>Charm Registration</title>
        <%@ include file="style.html" %>
    </head>
    <body>
      <%@ include file="header.jsp" %>
      <div>
          <form method="post" action="/registration">
              <table>
                  <tr>
                      <td>${requestScope.wordBundle.getWord("email")}</td>
                      <td><input type="email" name="email" placeholder="user@mail.ru"></td>
                  </tr>
                  <tr>
                      <td>${requestScope.wordBundle.getWord("password")}</td>
                      <td><input type="password" name="password"></td>
                  </tr>
              </table>
              <br>
              <button type="submit">${requestScope.wordBundle.getWord("save")}</button>
          </form>
      </div>
      <%@ include file="footer.jsp" %>
    </body>
</html>