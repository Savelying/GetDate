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
    <form method="post" action="/profile?id=${profile.id}" enctype="multipart/form-data">
            <input type="hidden" name="_method" value="PUT">
        <table>
            <tr>
                <td>${wordBundle.getWord("profile")}(id):</td>
                <td><input type="text" name="id" hidden value="${profile.id}">${profile.id}
                </td>
            </tr>
            <tr>
                <td>${wordBundle.getWord("email")}:</td>
                <td><a href="/email?id=${profile.id}">${profile.email}</a></td>
            </tr>
            <tr>
                <td>${wordBundle.getWord("name")}:</td>
                <td><input type="text" name="name" value="${profile.name}"></td>
            </tr>
<!--            <tr>-->
<!--                <td>${wordBundle.getWord("birth-date")}:</td>-->
<!--                <td><input type="date" name="birthDate" value="${profile.birthDate}"></td>-->
<!--            </tr>-->
            <tr>
                <td>${wordBundle.getWord("age")}:</td>
                <td>${profile.age}</td>
            </tr>
            <tr>
                <td>${wordBundle.getWord("info")}:</td>
                <td><input type="text" name="info" value="${profile.info}"></td>
            </tr>
            <tr>
                <td>${wordBundle.getWord("gender")}:</td>
                <td>
                    <select name="gender">
                        <option value="${profile.gender}" selected hidden>
                            ${requestScope.profile.gender}
                        </option>
                        <c:forEach var="gender" items="${applicationScope.genders}">
                            <option value="${gender}">
                                ${wordBundle.getWord(gender)}
                            </option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td>${wordBundle.getWord("photo")}:</td>
                <td>
                    <c:if test="${profile.photoFileName != null}">
                        <img src="/content/${profile.photoFileName}" height="300"><br>
                        <br>
                    </c:if>
                    <input type="button" value="${wordBundle.getWord('update')}" onclick="document.getElementById('file').click();" />
                    <input type="file" name="photo" id="file" style="display: none"></td>
            </tr>
        </table>
        <br>
        <button type="submit">${wordBundle.getWord("save")}</button>
    </form>
    <c:if test="${profile.id != null}">
        <form method="post" action="/profile?id=${profile.id}">
            <input type = "hidden" name = "_method" value = "delete"/>
            <input type = "hidden" name="id" value="${profile.id}">
            <button type="submit">${wordBundle.getWord("delete")}</button>
        </form>
    </c:if>
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
