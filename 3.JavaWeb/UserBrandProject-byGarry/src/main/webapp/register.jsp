<%--
  Created by IntelliJ IDEA.
  User: 杨宸楷
  Date: 2024/2/28
  Time: 21:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>注册页面</title>
    <link href="css/register.css" rel="stylesheet">
</head>
<body>

<div class="form-div">
    <div class="reg-content">
        <h1>欢迎注册</h1>
        <span>已有帐号？</span> <a href="login.html">登录</a>
    </div>
    <form id="reg-form" action="/UserBrandProject-byGarry/registerServlet" method="post">
        <table>
            <tr>
                <td>用户名</td>
                <td class="inputs">
                    <input name="username" type="text" id="username">
                    <br>
                    <span id="username_err" class="err_msg">${register_msg}</span>
                </td>
            </tr>

            <tr>
                <td>密码</td>
                <td class="inputs">
                    <input name="password" type="password" id="password">
                </td>
            </tr>

            <tr>
                <td>验证码</td>
                <td class="inputs">
                    <input name="checkCode" type="text" id="checkCode">
                    <img id="checkCodeImg" src="/UserBrandProject-byGarry/checkCodeServlet">
                    <a href="#" id="changeImg">看不清？</a>
                </td>
            </tr>
        </table>
        <div class="buttons">
            <input value="注 册" type="submit" id="reg_btn">
        </div>
        <br class="clear">
    </form>

</div>

<script>
    document.getElementById("changeImg").onclick = function () {
        //注意路径末尾有个"?"
        document.getElementById("checkCodeImg").src = "/UserBrandProject-byGarry/checkCodeServlet?"
            + new Date().getMilliseconds();//加上永远不会相同的时间，避免名字一样导致缓存而更换图片失败
    }

    document.getElementById("checkCodeImg").onclick = function () {
        //注意路径末尾有个"?"
        document.getElementById("checkCodeImg").src = "/UserBrandProject-byGarry/checkCodeServlet?"
            + new Date().getMilliseconds();//加上永远不会相同的时间，避免名字一样导致缓存而更换图片失败
    }
</script>

</body>
</html>
