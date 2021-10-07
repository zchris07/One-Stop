<%--
  Created by IntelliJ IDEA.
  User: He Zhenyong
  Date: 4/13/2020
  Time: 2:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h3>查询所有账号信息</h3>

    <c:forEach items="${list}" var="account">
        ${account.name} ${account.money}
    </c:forEach>
    <a href="/account/save">新增</a>

</body>
</html>
