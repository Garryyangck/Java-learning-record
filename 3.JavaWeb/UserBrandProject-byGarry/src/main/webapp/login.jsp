<%--
  Created by IntelliJ IDEA.
  User: 杨宸楷
  Date: 2024/2/28
  Time: 19:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>登录页面</title>
    <link href="css/login.css" rel="stylesheet">
</head>

<body>
<div id="loginDiv" style="height: 350px">
    <form action="/UserBrandProject-byGarry/loginServlet" method="post" id="form">
        <h1 id="loginMsg">LOGIN IN</h1>
        <div id="errorMsg">${login_msg} ${register_msg}</div>
        <p>Username:<input id="username" name="username" type="text" value="${cookie.username.value}"></p>
        <p>Password:<input id="password" name="password" type="password" value="${cookie.password.value}"></p>
        <p>Remember:<input id="remember" name="remember" value="1" type="checkbox"></p>
        <div id="subDiv">
            <input type="submit" class="button" value="login up">
            <input type="reset" class="button" value="reset">&nbsp;&nbsp;&nbsp;
            <a href="/UserBrandProject-byGarry/register.jsp">没有账号？</a>
        </div>
    </form>
</div>

</body>
</html>
