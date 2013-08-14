<%-- 
    Document   : ShowValidData
    Created on : Aug 12, 2013, 1:23:25 PM
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
        <h2>Valid Data!</h2>
        <%  ArrayList<String> Alrd_column = (ArrayList) request.getAttribute("arrColumn");
            ArrayList<String> Alrd_value = (ArrayList) request.getAttribute("arrValue");
            ArrayList<String> arrTime = (ArrayList) request.getAttribute("arrTime");

            ArrayList<String> Alrd_lbl_column = (ArrayList) request.getAttribute("arrColumn_lbl");
            ArrayList<String> Alrd_lbl_value = (ArrayList) request.getAttribute("arrValue_lbl");
            
             //out.println(session.getAttribute("hdnxleft"));
             
              
%>
        <form id="form1" method="POST" action="./ValidateData">
            <table><tr><td>
                        <div style="overflow: scroll; height: 500px; width: 620px; background: White">
                            <table ><tr> <td style=" vertical-align: top"> 
                                        <table border="1" style='table-layout:fixed'>
                                            <tr><td style='text-overflow: ellipsis; overflow: hidden; white-space: nowrap;'>Timestamp</td> </tr>
                                            <tr>
                                                <% for (int a = 0; a <= arrTime.size() - 1; a++) {%>
                                            <tr>
                                                <td style="width: 30%"> <%= arrTime.get(a)%> </td>
                                                <%
                                                    }
                                                %>
                                            </tr>
                                        </table> </td> 
                                    <td style=" vertical-align: top">
                                        <table border="1" style='table-layout:fixed'>
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
                                                <%                                                } else {%>

                                                <td style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;"> <%= Alrd_value.get(a)%> </td>
                                                <%
                                                        }
                                                    }
                                                %>
                                            </tr>

                                        </table>        
                                    </td></tr></table>
                        </div>
                                            <input type="submit" value="Next" name="btnNext"
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

        </form>
    </body>
</html>
