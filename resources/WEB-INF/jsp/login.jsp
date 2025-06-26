<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en" xmlns:c="https://jakarta.ee/xml/ns/jakartaee">
    <head>
        <title>Charm Registration</title>
        <%@ include file="style.html" %>
    </head>
    <body>
      <%@ include file="header.jsp" %>
      <div>
          <h4 >${wordBundle.getWord("login")}</h4>
          <form method="post" action="/login" enctype="multipart/form-data">
              <table>
                  <tr>
                      <td>${wordBundle.getWord("email")}</td>
                      <td><input type="email" name="email" value="${profile.email}" placeholder="user@email.com"></td>
                  </tr>
                  <tr>
                      <td>${wordBundle.getWord("password")}</td>
                      <td><input type="password" name="password"></td>
                  </tr>
              </table>
              <br>
              <button type="submit">${wordBundle.getWord("login")}</button>
          </form>
          <div style="color: red">
              <c:forEach var="error" items="${errors}">
                  <span>${wordBundle.getWord(error)}</span><br>
              </c:forEach>
          </div>
      </div>
      <%@ include file="footer.jsp" %>
    </body>
</html>