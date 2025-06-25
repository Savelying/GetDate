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
    <table>
        <tr>
            <th>id</th>
            <th>${wordBundle.getWord("name")}</th>
            <th>${wordBundle.getWord("age")}</th>
            <th>${wordBundle.getWord("email")}</th>
            <th>${wordBundle.getWord("status")}</th>
        </tr>
        <c:forEach var="profile" items="${profiles}">
            <tr>
                <form action="/profile" method="post">
                    <td style="text-align: center">${profile.id}</td>
                    <td><a href="/profile?id=${profile.id}">${profile.name}</a></td>
                    <td style="text-align: center">${profile.age}</td>
                    <td>${profile.email}</td>
                    <td>
                        <input type="hidden" name="_method" value="put"/>
                        <input type="hidden" name="id" value="${profile.id}">
                        <select name="status">
                            <option value="${profile.status}" selected hidden>
                                ${wordBundle.getWord(profile.status)}
                            </option>
                            <c:forEach var="status" items="${applicationScope.statuses}">
                                <option value="${status}">${wordBundle.getWord(status)}</option>
                            </c:forEach>
                        </select>
                        <button type="submit">${wordBundle.getWord("save")}</button>
                    </td>
                </form>
            </tr>
        </c:forEach>
    </table>
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
