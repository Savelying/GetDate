<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
    <head>
        <title>GetDate Not Found</title>
        <%@ include file="style.html" %>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div>
            <h3 style="color: red">404 - ${wordBundle.getWord("page-not-found")}</h3>
        </div>
        <%@ include file="footer.jsp" %>
    </body>
</html>