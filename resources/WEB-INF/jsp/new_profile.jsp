<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <title>GetDate!</title>
</head>
<body>
<%@ include file="header.html" %>
<div>
<!--    <h4>Create new profile:</h4>-->
    <form method="post" action="/profile">
        <table>
            <tr>
                <td>Profile id:</td>
                <td><input type="text" name="id" hidden value="${requestScope.profile.id}">${requestScope.profile.id}</td>
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
        </table>
        <br>
        <button type="submit">Save</button>
    </form>
</div>
<%@ include file="footer.html" %>
</body>
</html>
