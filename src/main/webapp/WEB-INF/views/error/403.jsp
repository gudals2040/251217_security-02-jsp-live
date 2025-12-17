<%-- #(1)-4 --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>접근 거부</title>
</head>
<body>
<h1>403 - 접근이 거부되었습니다</h1>

<%-- #(4)-4 --%>
<p style="color: red;">${errorMessage}</p>

<sec:authorize access="isAuthenticated()">
    <p>
        현재 로그인: <sec:authentication property="name"/><br/>
        보유 권한: <sec:authentication property="authorities"/>
    </p>
</sec:authorize>

<p>
    <a href="<c:url value="/" />">홈으로 돌아가기</a> |
    <a href="javascript:history.back();">이전 페이지로</a>
</p>
</body>
</html>