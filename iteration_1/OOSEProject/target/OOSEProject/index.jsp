<%--
  Created by IntelliJ IDEA.
  User: He Zhenyong
  Date: 4/13/2020
  Time: 3:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<a href="/account/test"> test </a>

<h3>Test Save function</h3>
<form action="account/save" method="post">
    name：<input type="text" name="username" /><br/>
    password：<input type="text" name="registerData"/><br/>
    <input type="submit" value="save"/><br/>
</form>
</body>

</html>
