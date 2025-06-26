<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en" xmlns:c="https://jakarta.ee/xml/ns/jakartaee">
<div>
    <a href="/"><h1>GetDate!</h1></a>
    <form action="/language" method="post">
        <button type="submit" name="lang" value="ru">ru</button>
        <button type="submit" name="lang" value="en">en</button>
    </form>
    <c:if test="${sessionScope.user != null}">
        <form method="post" action="/logout">
            <a href="/profile?id=${sessionScope.user.id}">(${sessionScope.user.name})</a><br>
            <button type="submit">${wordBundle.getWord("logout")}</button>
        </form>
    </c:if><c:if test="${sessionScope.user == null}">
    <form method="get" action="/login">
        <button type="submit">${wordBundle.getWord("to-login")}</button>
    </form>
</c:if>
    <hr>
</div>
</html>