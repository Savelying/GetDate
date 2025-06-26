<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
    <head>
        <title>Charm Profiles</title>
        <%@ include file="style.html" %>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div>
            <h4 style="color: red">${requestScope.wordBundle.getWord("email-warning")}</h4>
            <form method="post" action="/email?id=${profile.id}" enctype="multipart/form-data">
                <input type="hidden" name="_method" value="put"/>
                <input type="hidden" name="id" value="${profile.id}">
                <table>
                    <tr>
                        <td>${requestScope.wordBundle.getWord("email")}</td>
                        <td><input type="email" name="email" value="${profile.email}"></td>
                    </tr>
                    <tr>
                        <td>${wordBundle.getWord("password")}</td>
                        <td><input type="password" name="password"></td>
                    </tr>
                    <tr>
                        <td>${wordBundle.getWord("password-new")}</td>
                        <td><input type="password" name="newPassword"></td>
                    </tr>
                    <tr>
                        <td>${wordBundle.getWord("password-confirm")}</td>
                        <td><input type="password" name="confirmPassword"></td>
                    </tr>
                </table>
                <br>
                <button type="submit">${requestScope.wordBundle.getWord("save")}</button>
            </form>
        </div>
        <%@ include file="footer.jsp" %>
    </body>
</html>