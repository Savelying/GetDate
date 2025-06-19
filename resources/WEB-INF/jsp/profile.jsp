<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en" xmlns:c="https://jakarta.ee/xml/ns/jakartaee">
<head>
    <title>GetDate!</title>
</head>
<body>
<%@ include file="header.html" %>
<div>
    <form method="post" action="/profile">
        <c:if test="${requestScope.profile.id != null}">
            <input type="hidden" name="_method" value="PUT">
        </c:if>
        <table>
            <tr>
                <td>Profile id:</td>
                <td><input type="text" name="id" hidden value="${requestScope.profile.id}">${requestScope.profile.id}
                </td>
            </tr>
            <tr>
                <td>Email:</td>
                <td><input type="email" name="email" value="${requestScope.profile.email}"></td>
            </tr>
            <tr>
                <td>Name:</td>
                <td><input type="text" name="name" value="${requestScope.profile.name}"></td>
            </tr>
            <tr>
                <td>Info:</td>
                <td><input type="text" name="info" value="${requestScope.profile.info}"></td>
            </tr>
            <tr>
                <td>Gender:</td>
                <td>
                    <select name="gender">
                        <option value="${requestScope.profile.gender}" selected hidden>${requestScope.profile.gender}
                        </option>
                        <c:forEach var="gender" items="${applicationScope.genders}">
                            <option value="${gender}">${gender}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
        </table>
        <br>
        <button type="submit">Save</button>
    </form>
    <c:if test="${requestScope.profile.id != null}">
        <form method="post" action="/profile">
            <input type = "hidden" name = "_method" value = "DELETE"/>
            <input type = "hidden" name="id" value="${requestScope.profile.id}">
            <button type="submit">Delete</button>
        </form>
    </c:if>
</div>
<%@ include file="footer.html" %>
</body>
</html>
