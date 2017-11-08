<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Users</title>
</head>
<body>
    <ul>
        <li><a href="index.html">Home</a></li>
        <li><a href="fileUpload.jsp">Upload xml file</a></li>
    </ul>
    <h1>Users</h1>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
          <th>Name</th>
          <th>E-mail</th>
          <th>Flag</th>
        </tr>
        <c:forEach items="${requestScope.users}" var="user">
            <jsp:useBean id="user" scope="page" type="ru.javaops.masterjava.xml.schema.User"/>
            <tr>
                <td>${user.getValue()}</td>
                <td>${user.getEmail()}</td>
                <td>${user.getFlag()}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
