<%-- 
    Document   : newjsp
    Created on : Jul 11, 2013, 5:56:49 PM
    Author     : samsalman
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <%  
             out.println(session.getAttribute("hdnxleft"));
             
              out.println(session.getAttribute("hdnyleft"));
%>
    <body>
        <h1>Hello World!</h1>
    </body>
</html>
