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
        <tr>
            <form action="/profiles" method="get">
                <td>
                    <button type="submit">Y</button>
                </td>
                <td><input type="text" name="nameStartWith" value="${filter.nameStartWith}" class="filterInput" style="width: 150px"></td>
                <td><input type="number" name="lowAge" min="18" max="100" value="${filter.lowAge}" style="width: 50px;"> - <input type="number" name="highAge" min="18" max="100" value="${filter.highAge}" style="width: 50px;"></td>
                <td><input type="text" name="emailStartWith" value="${filter.emailStartWith}" class="filterInput" style="width: 150px"></td>
                <td><select name="status" class="filterInput" style="width: 150px">
                        <option selected value></option>
                        <c:forEach var="statusVar" items="${applicationScope.statuses}">
                            <c:if test="${statusVar == filter.status}">
                                <option value="${statusVar}" selected>${wordBundle.getWord(statusVar)}</option>
                            </c:if>
                            <c:if test="${statusVar != filter.status}">
                                <option value="${statusVar}">${wordBundle.getWord(statusVar)}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                    <button type="submit">${wordBundle.getWord("find")}</button>
                </td>
            </form>
        </tr>
        <c:forEach var="profile" items="${profiles}">
            <tr>
                <form action="/profile" method="post" enctype="multipart/form-data">
                    <td style="text-align: center">${profile.id}</td>
                    <td><a href="/profile?id=${profile.id}">${profile.name}</a></td>
                    <td style="text-align: center">${profile.age}</td>
                    <td><a href="/profile?id=${profile.id}">${profile.email}</a></td>
                    <td>
                        <input type="hidden" name="_method" value="put"/>
                        <input type="hidden" name="id" value="${profile.id}">
                        <select name="status" style="width: 150px">
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
