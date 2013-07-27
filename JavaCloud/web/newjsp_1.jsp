<%-- 
    Document   : newjsp
    Created on : Jul 1, 2013, 5:06:32 PM
    Author     : samsalman
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Hbase Connectivity</title>
    </head>
    <body>
        <h1>Hbase CRUD Operations</h1>
        <form action="./HbaseServlet" method="POST">
            <table>
                <tr>
                    <td>Row:</td>
                    <td><input type="text" name="row" value="${row}" </td>
                    <td>
                        <ul>
                            <li>2nd </li>
                            <li>2nd</li>
                        </ul>

                    </td>
                </tr>
                <tr>
                    <td>Family:</td>
                    <td><input type="text" name="famIly" value="${famIly}" </td>
                </tr>
                <tr>
                    <td>Qualifier:</td>
                    <td><input type="text" name="qualifier" value="${qualifier}" </td>
                </tr>
                <tr>
                    <td>Value:</td>
                    <td><input type="text" name="value" value="${value}" </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input type="submit" name="action" value="Add"/>    
                        <input type="submit" name="action" value="Edit"/>    
                        <input type="submit" name="action" value="Delete"/>    
                        <input type="submit" name="action" value="Search"/>    
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
