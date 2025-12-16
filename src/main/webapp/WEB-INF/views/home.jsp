<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Security Demo</title>
</head>
    <body>
        <h1>Spring Security 실습</h1>

        <sec:authorize access="isAnonymous()">
            <p>로그인이 필요합니다.</p>
            <a href="<c:url value="/auth/login" />">로그인</a> |
            <a href="<c:url value="/auth/signup" />">회원가입</a>
        </sec:authorize>

        <sec:authorize access="isAuthenticated()">
            <p>
                환영합니다,
                <sec:authentication property="name"/>님!
            </p>

            <p>권한: <sec:authentication property="authorities"/></p>

            <a href="<c:url value="/memo" />">내 메모</a> |

            <sec:authorize access="hasRole('ADMIN')">
                <a href="<c:url value="/admin" />">관리자 페이지</a> |
            </sec:authorize>

            <form action="<c:url value="/auth/logout" />" method="post" style="display:inline;">
                    <%-- CSRF 토큰: 보안을 위해 필수 (자동 생성됨) --%>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit">로그아웃</button>
            </form>
        </sec:authorize>
    </body>
</html>
