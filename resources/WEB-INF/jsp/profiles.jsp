<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en" xmlns:c="https://jakarta.ee/xml/ns/jakartaee">
<head>
    <title>GetDate!</title>
    <style>
        table, td {border: 1px solid;border-collapse: collapse;}
        table, th {border: 1px solid;border-collapse: collapse;}
    </style>
</head>
<body>
<%@ include file="header.jsp" %>
<div>
    <table>
        <tr>
            <th>id</th>
            <th>${requestScope.wordBundle.getWord("name")}</th>
            <th>${requestScope.wordBundle.getWord("age")}</th>
            <th>${requestScope.wordBundle.getWord("email")}</th>
        </tr>
        <c:forEach var="profile" items="${requestScope.profiles}">
            <tr>
                <td style="text-align: center">${profile.id}</td>
                <td><a href="/profile?id=${profile.id}">${profile.name}</a></td>
                <td style="text-align: center">${profile.age}</td>
                <td>${profile.email}</td>

            </tr>
        </c:forEach>
    </table>
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
