<%--
  Created by IntelliJ IDEA.
  User: He Zhenyong
  Date: 4/16/2020
  Time: 5:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<a href="/account/findAll">新增</a>
<form action="/account/add" method="post">
    姓名：<input type="text" name="name" /><br/>
    金额：<input type="text" name="money"/><br/>
    <input type="submit" value="保存"/><br/>
</form>

</body>
</html>
