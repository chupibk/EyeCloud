<%-- 
    Document   : upload
    Created on : Dec 26, 2012, 9:45:46 PM
    Author     : daothanhchung
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <title>File Uploading Form</title>
    </head>
    <body>
        <h3>File Upload:</h3>
        Select a file to upload: <br />
        <form action="UploadServlet" method="post"
              enctype="multipart/form-data">
            <input type="file" name="image" size="50" />
            <br />
            <input type="submit" value="Upload File" />
        </form>
    </body>
</html>
