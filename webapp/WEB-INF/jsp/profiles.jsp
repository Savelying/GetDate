<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en" xmlns:c="https://jakarta.ee/xml/ns/jakartaee">
<head>
    <title>GetDate Profiles</title>
    <%@ include file="style.html" %>
    <c:url var="baseUrl" value="${requestScope['javax.servlet.forward.servlet_path']}">
        <c:param name="emailStartWith" value="${filter.emailStartWith}"/>
        <c:param name="nameStartWith" value="${filter.nameStartWith}"/>
        <c:param name="status" value="${filter.status}"/>
        <c:param name="lowAge" value="${filter.lowAge}"/>
        <c:param name="highAge" value="${filter.highAge}"/>
        <c:param name="sortBy" value="id"/>
    </c:url>
    <c:url var="sortById" value="${baseUrl}"><c:param name="sortBy" value="id"/></c:url>
    <c:url var="sortByName" value="${baseUrl}"><c:param name="sortBy" value="name"/></c:url>
    <c:url var="sortByAge" value="${baseUrl}"><c:param name="sortBy" value="birth_date"/></c:url>
    <c:url var="sortByEmail" value="${baseUrl}"><c:param name="sortBy" value="email"/></c:url>
    <c:url var="sortByRole" value="${baseUrl}"><c:param name="sortBy" value="role"/></c:url>
    <c:url var="sortByStatus" value="${baseUrl}"><c:param name="sortBy" value="status"/></c:url>
</head>
<body>
<%@ include file="header.jsp" %>
<div>
    <form action="profiles" method="post">
        <input type="text" name="n" style="width: 50px"> profiles <button type="submit">generate</button>
    </form>
    <table>
        <tr>
            <th><a href="${sortById}" class="hiddenLink">id<c:if test="${filter.sortBy == 'id' or filter.sortBy == null}"> ^ </c:if></a></th>
            <th><a href="${sortByName}" class="hiddenLink">${wordBundle.getWord("name")}<c:if test="${filter.sortBy == 'name'}"> ^ </c:if></a></th>
            <th><a href="${sortByAge}" class="hiddenLink">${wordBundle.getWord("age")}<c:if test="${filter.sortBy == 'birth_date'}"> ^ </c:if></a></th>
            <th><a href="${sortByEmail}" class="hiddenLink">${wordBundle.getWord("email")}<c:if test="${filter.sortBy == 'email'}"> ^ </c:if></a></th>
            <th><a href="${sortByRole}" class="hiddenLink">${wordBundle.getWord("role")}<c:if test="${filter.sortBy == 'role'}"> ^ </c:if></a></th>
            <th><a href="${sortByStatus}" class="hiddenLink">${wordBundle.getWord("status")}<c:if test="${filter.sortBy == 'status'}"> ^ </c:if></a></th>
        </tr>
        <tr>
            <form action="/profiles" method="get">
                <td>
                    <button type="submit">Y</button>
                </td>
                <td><input type="text" name="nameStartWith" value="${filter.nameStartWith}" class="filterInput" style="width: 150px"></td>
                <td><input type="number" name="lowAge" min="18" max="100" value="${filter.lowAge}" style="width: 50px;"> - <input type="number" name="highAge" min="18" max="100" value="${filter.highAge}" style="width: 50px;"></td>
                <td><input type="text" name="emailStartWith" value="${filter.emailStartWith}" class="filterInput" style="width: 150px"></td>
                <td><select name="role" class="filterInput" style="width: 100px">
                        <option selected value></option>
                        <c:forEach var="roleVar" items="${applicationScope.roles}">
                            <c:if test="${roleVar == filter.role}">
                                <option value="${roleVar}" selected>${wordBundle.getWord(roleVar)}</option>
                            </c:if>
                            <c:if test="${roleVar != filter.role}">
                                <option value="${roleVar}">${wordBundle.getWord(roleVar)}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </td>
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
                    <td style="text-align: center">${profile.role}</td>
                    <td>
                        <input type="hidden" name="_method" value="put"/>
                        <input type="hidden" name="id" value="${profile.id}">
                        <input type="hidden" name="version" value="${profile.version}">
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
    <br>
    <form action="/profiles" method="get">
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
