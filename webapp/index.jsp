<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en" xmlns:c="https://jakarta.ee/xml/ns/jakartaee">
<head>
    <title>GetDate!</title>
    <%@ include file="WEB-INF/jsp/style.html" %>
</head>
<body>
<%@ include file="WEB-INF/jsp/header.jsp" %>
<div>
    <h2 style="color: red">${wordBundle.getWord("welcome-to")} GetDate!</h2>
    <c:if test="${sessionScope.user != null}">
        <form method="get" action="/getdate">
            <button type="submit">${wordBundle.getWord("search")}</button>
        </form>
    </c:if>
    <c:if test="${sessionScope.user == null}">
        <form method="get" action="/login">
            <button type="submit">${wordBundle.getWord("to-login")}</button>
        </form>
    </c:if>
</div>
<%@ include file="WEB-INF/jsp/footer.jsp" %>
</body>
</html>
