<%-- 
    Document   : ShowRawData
    Created on : Jul 30, 2013, 3:24:55 PM
    Author     : samsalman
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h2>Raw Data!</h2>
        <% ArrayList<String> Alrd_column = (ArrayList) request.getAttribute("Alrd_column");
            ArrayList<String> Alrd_value = (ArrayList) request.getAttribute("Alrd_value");

            ArrayList<String> Alrd_lbl_column = (ArrayList) request.getAttribute("Alrd_lbl_column");
            ArrayList<String> Alrd_lbl_value = (ArrayList) request.getAttribute("Alrd_lbl_value");
            //out.print(Alrd_lbl_column +" " +  );
%>
        <table><tr><td>
                    <div style="overflow: scroll; height: 500px; width: 620px; background: White">
                        <table border="1">
                            <tr>
                                <% for (int a = 0; a <= Alrd_column.size() - 1; a++) {%>
                                <td> <%=Alrd_column.get(a)%>  </td>
                                <%
                                    }
                                %>
                            </tr>
                            <tr>
                                <% for (int a = 0; a <= Alrd_value.size() - 1; a++) {%>
                                <% if (Alrd_value.get(a).contains("/")) {%>
                            </tr>
                            <tr>
                                <% } else {%> 
                                <td> <%= Alrd_value.get(a)%> </td>
                                <%
                                        }
                                    }
                                %>
                            </tr>

                        </table>        
                    </div>
                </td>
                <td>
                    <div style="overflow: scroll; height: 500px; width: 620px; background: White">
                        <table border="1">
                            <tr>
                                <% for (int a = 0; a <= Alrd_lbl_column.size() - 1; a++) {%>
                                <td> <%=Alrd_lbl_column.get(a)%>  </td>
                                <%
                                    }
                                %>
                            </tr>
                            <tr>
                                <% for (int a = 0; a <= Alrd_lbl_value.size() - 1; a++) {%>
                                <% if (Alrd_lbl_value.get(a).contains("/")) {%>
                            </tr>
                            <tr>
                                <% } else {%> 
                                <td> <%= Alrd_lbl_value.get(a)%> </td>
                                <%
                                        }
                                    }
                                %>
                            </tr>

                        </table>        
                    </div>
                </td>
            </tr></table>

    </body>
</html>
