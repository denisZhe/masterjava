<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Upload xml file</title>
</head>
<body>
    <a href="index.html">Home</a>
    <br>
    <br>
    <form action="upload" method="post" enctype="multipart/form-data">
        <input type="text" name="description"/>
        <input type="file" name="file"/>
        <input type="submit" value="Upload"/>
    </form>
</body>
</html>
