<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en" xmlns:c="https://jakarta.ee/xml/ns/jakartaee">
<head>
    <title>GetDate!</title>
    <%@ include file="style.html" %>
</head>
<body>
<%@ include file="header.jsp" %>
<div>
    <form method="post" action="/profile">
        <c:if test="${requestScope.profile.id != null}">
            <input type="hidden" name="_method" value="PUT">
        </c:if>
        <table>
            <tr>
                <td>${requestScope.wordBundle.getWord("profile")}(id):</td>
                <td><input type="text" name="id" hidden value="${requestScope.profile.id}">${requestScope.profile.id}
                </td>
            </tr>
            <tr>
                <td>${requestScope.wordBundle.getWord("email")}:</td>
                <td><a href="/email?id=${requestScope.profile.id}">${requestScope.profile.email}</a></td>
            </tr>
            <tr>
                <td>${requestScope.wordBundle.getWord("name")}:</td>
                <td><input type="text" name="name" value="${requestScope.profile.name}"></td>
            </tr>
            <tr>
                <td>${requestScope.wordBundle.getWord("birth-date")}:</td>
                <td><input type="date" name="birthDate" value="${requestScope.profile.birthDate}"></td>
            </tr>
            <tr>
                <td>${requestScope.wordBundle.getWord("age")}:</td>
                <td>${requestScope.profile.age}</td>
            </tr>
            <tr>
                <td>${requestScope.wordBundle.getWord("info")}:</td>
                <td><input type="text" name="info" value="${requestScope.profile.info}"></td>
            </tr>
            <tr>
                <td>${requestScope.wordBundle.getWord("gender")}:</td>
                <td>
                    <select name="gender">
                        <option value="${requestScope.profile.gender}" selected hidden>
                            ${requestScope.profile.gender}
                        </option>
                        <c:forEach var="gender" items="${applicationScope.genders}">
                            <option value="${gender}">
                                ${requestScope.wordBundle.getWord(gender)}
                            </option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
        </table>
        <br>
        <button type="submit">${requestScope.wordBundle.getWord("save")}</button>
    </form>
    <c:if test="${requestScope.profile.id != null}">
        <form method="post" action="/profile">
            <input type = "hidden" name = "_method" value = "DELETE"/>
            <input type = "hidden" name="id" value="${requestScope.profile.id}">
            <button type="submit">${requestScope.wordBundle.getWord("delete")}</button>
        </form>
    </c:if>
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
