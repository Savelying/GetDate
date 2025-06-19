<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <title>GetDate!</title>
</head>
<body>
<%@ include file="header.html" %>
<div>
    <h4>Profile id=${requestScope.profile.id}</h4>
    <table>
        <tr>
            <td>Email:</td>
            <td>${requestScope.profile.email}</td>
        </tr>
        <tr>
            <td>Name:</td>
            <td>${requestScope.profile.name}</td>
        </tr>
        <tr>
            <td>Info:</td>
            <td>${requestScope.profile.info}</td>
        </tr>
    </table>
</div>
<%@ include file="footer.html" %>
</body>
</html>
