<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>로그인</title>
</head>
<body>
<h1>로그인</h1>

<%-- #(4)-5 --%>

<%-- 리다이렉트 안내 메시지 --%>
<c:if test="${param.redirect == 'true'}">
    <p style="color: blue;">로그인이 필요한 페이지입니다. 로그인 후 이동합니다.</p>
</c:if>

<%-- 에러 메시지 (커스텀 메시지 또는 기본 메시지) --%>
<c:if test="${param.error == 'true'}">
    <p style="color: red;">
        <c:choose>
            <c:when test="${not empty param.message}">
                ${param.message}
            </c:when>
            <c:otherwise>
                아이디 또는 비밀번호가 올바르지 않습니다.
            </c:otherwise>
        </c:choose>
    </p>
</c:if>

<%-- 회원가입 성공 메시지 --%>
<c:if test="${param.signup == 'success'}">
    <p style="color: green;">회원가입이 완료되었습니다. 로그인해주세요.</p>
</c:if>

<%-- 로그아웃 메시지 --%>
<c:if test="${param.logout != null}">
    <p style="color: green;">로그아웃되었습니다.</p>
</c:if>

<form action="<c:url value="/auth/login" />" method="post">

    <div>
        <label>아이디:</label>
        <input type="text" name="username" required/>
    </div>
    <div>
        <label>비밀번호:</label>
        <input type="password" name="password" required/>
    </div>

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <button>로그인</button>
</form>

<%-- #(5)-6 --%>
<%-- 소셜 로그인 버튼 --%>
<h2>소셜 로그인</h2>
<p>
    <%--
        OAuth2 로그인 URL 형식: /oauth2/authorization/{provider}
        Spring Security가 자동으로 이 URL을 처리합니다.
    --%>
    <a href="<c:url value="/oauth2/authorization/github" />">
        <button type="button">GitHub으로 로그인</button>
    </a>
    <%-- #(6)-3  --%>
    <a href="<c:url value="/oauth2/authorization/google" />">
        <button type="button">Google으로 로그인</button>
    </a>
</p>


<p>
    <a href="<c:url value="/auth/signup" />">회원가입</a>
</p>
<p>
    <a href="<c:url value="/" />">홈으로</a>
</p>
</body>
</html>