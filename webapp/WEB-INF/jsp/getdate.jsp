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
    <form method="post" action="/getdate">
        <input type="hidden" name="toId" value="${next.id}">
        <div>
            ${wordBundle.getWord("what-about")} ${next.name}, ${next.age}?<br>
            <img src="/content/${next.photoFileName}" width="300"><br>
            <br>
            ${next.info}
            <br>
        </div>
        <table>
            <tr class="hiddenRow">
                <td>
                    <button type="submit" name="action" value="DISLIKE" class="hiddenButton">
                        ${wordBundle.getWord("dislike")}
                    </button>
                </td>
                <td>
                    <button type="submit" name="action" value="SKIP" class="hiddenButton">
                        ${wordBundle.getWord("skip")}
                    </button>
                </td>
                <td>
                    <button type="submit" name="action" value="LIKE" class="hiddenButton">
                        ${wordBundle.getWord("like")}
                    </button>
                </td>
            </tr>
        </table>
    </form>
</div>
<%@ include file="footer.jsp" %>
</body>
</html>