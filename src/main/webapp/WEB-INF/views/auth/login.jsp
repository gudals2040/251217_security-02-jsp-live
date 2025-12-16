<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>로그인</title>
    </head>
    <body>
        <h1>로그인</h1>

        <c:if test="${not empty errorMessage}">
            <p style="color: red;">${errorMessage}</p>
        </c:if>

        <c:if test="${not empty logoutMessage}">
            <p style="color: green;">${logoutMessage}</p>
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

        <p>
            <a href="<c:url value="/auth/signup" />">회원가입</a>
        </p>
        <p>
            <a href="<c:url value="/" />">홈으로</a>
        </p>
    </body>
</html>