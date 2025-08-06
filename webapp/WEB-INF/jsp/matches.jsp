<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en" xmlns:c="https://jakarta.ee/xml/ns/jakartaee">
<head>
    <title>GetDate Matches</title>
    <%@ include file="style.html" %>
    <c:url var="baseUrl" value="${requestScope['javax.servlet.forward.servlet_path']}">
        <c:param name="nameStartWith" value="${filter.nameStartWith}"/>
        <c:param name="lowAge" value="${filter.lowAge}"/>
        <c:param name="highAge" value="${filter.highAge}"/>
        <c:param name="sortBy" value="id"/>
    </c:url>
    <c:url var="sortByName" value="${baseUrl}"><c:param name="sortBy" value="name"/></c:url>
    <c:url var="sortByAge" value="${baseUrl}"><c:param name="sortBy" value="birth_date"/></c:url>
</head>
<body>
<%@ include file="header.jsp" %>
<div>
    <table>
        <tr>
            <th><a href="${sortByName}" class="hiddenLink">${wordBundle.getWord("name")}<c:if test="${filter.sortBy == 'name'}"> ^ </c:if></a></th>
            <th><a href="${sortByAge}" class="hiddenLink">${wordBundle.getWord("age")}<c:if test="${filter.sortBy == 'birth_date'}"> ^ </c:if></a></th>
            <th><a>${wordBundle.getWord("info")}</a></th>
            <th><a>${wordBundle.getWord("photo")}</a></th>
        </tr>
        <tr>
            <form action="/matches" method="get">
                <td><input type="text" name="nameStartWith" value="${filter.nameStartWith}" class="filterInput" style="width: 150px"></td>
                <td><input type="number" name="lowAge" min="18" max="100" value="${filter.lowAge}" style="width: 50px;"> - <input type="number" name="highAge" min="18" max="100" value="${filter.highAge}" style="width: 50px;"></td>
                <td><button type="submit">${wordBundle.getWord("find")}</button></td>
            </form>
        </tr>
        <c:forEach var="profile" items="${profiles}">
            <tr>
                <form action="/profile" method="post" enctype="multipart/form-data">
                    <td><a href="/profile?id=${profile.id}">${profile.name}</a></td>
                    <td style="text-align: center">${profile.age}</td>
                    <td>${profile.info}</td>
                    <td style="align-content: center"><img src="/content/${profile.photoFileName}" width="50"></td>
                </form>
            </tr>
        </c:forEach>
    </table>
    <br>
    <form action="/matches" method="get">
        <a>${wordBundle.getWord("page")}</a>
        <input type="number" name="pageNo" min="1" value="${filter.pageNo}" style="width: 50px">
        <a>${wordBundle.getWord("by")}</a>
        <select name="pageSize" class="filterInput" style="width: 50px">
            <c:forEach var="size" items="${applicationScope.availPageSizes}">
                <c:if test="${size == filter.pageSize}">
                    <option value="${size}" selected>${size}</option>
                </c:if>
                <c:if test="${size != filter.pageSize}">
                    <option value="${size}">${size}</option>
                </c:if>
            </c:forEach>
        </select>
        <a>${wordBundle.getWord("on-page")}</a>
        <button type="submit">${wordBundle.getWord("view")}</button>
    </form>
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
