<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <title>GetDate!</title>
    <%@ include file="WEB-INF/jsp/style.html" %>
</head>
<body>
<%@ include file="WEB-INF/jsp/header.jsp" %>
<div>
    <h2 style="color: red">${wordBundle.getWord("welcome-to")} GetDate!</h2>
    <h3><a href="/profile">${wordBundle.getWord("vew-profiles")}</a></h3>
</div>
<%@ include file="WEB-INF/jsp/footer.jsp" %>
</body>
</html>
