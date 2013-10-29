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
        <link media="screen" rel="stylesheet" type="text/css" href="css/admin-login.css"  />
        <link media="screen" rel="stylesheet" type="text/css" href="css/admin.css"  />
        <title>Eye Feature</title>
    </head>
    <body>
        <%  ArrayList<String> Alrd_column = (ArrayList) request.getAttribute("Alrd_column");
            ArrayList<String> Alrd_value = (ArrayList) request.getAttribute("Alrd_value");
            //out.print(Alrd_lbl_column +" " +  );
        %>

        <div id="wrapper">
            <!--[if !IE]>start login wrapper<![endif]-->
            <div id="content">

                <div class="inner-image">
                    &nbsp;&nbsp;&nbsp;&nbsp;<img id="g1" src="img/logo1.png" />
                     <div id="welcome">
                        Welcome : "${username}"
                        </br>
                        </br>
                        <a style="float: right" href="Dashboard.jsp?kill" class="d5"><span>Log Out &nbsp;&nbsp;&nbsp;</span></a>
                    </div>
                    <div id="title">
                        Interactive Technologies Research Group
                    </div>
                    
                </div>
                <!--[if !IE]>start page<![endif]-->
                <div id="page">
                    <div class="inner-data">

                        <div class="section">
                            <!--[if !IE]>start title wrapper<![endif]-->
                            <div class="title_wrapper">
                                <h2>Eye Feature</h2>
                                <span class="title_wrapper_left"></span>
                                <span class="title_wrapper_right"></span>
                            </div>
                            <!--[if !IE]>end title wrapper<![endif]-->
                            <!--[if !IE]>start section content<![endif]-->
                            <div class="section_content">
                                <!--[if !IE]>start section content top<![endif]-->
                                <div class="sct">
                                    <div class="sct_left">
                                        <div class="sct_right">
                                            <div class="sct_left">
                                                <div class="sct_right_load">

                                                    <form id="form1" method="POST" action="./ValidateData">
                                                        <table>
                                                            <tr><td>
                                                                    <div class="table_wrapper">
                                                                        <div class="table_wrapper_inner">
                                                                            <div style="overflow: scroll; height: 400px; width: 1080px; background: transparent; margin-top: 3%;">
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
                                                                        </div>
                                                                    </div>
                                                                </td>

                                                            </tr></table>
                                                       

                                                    </form>

                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <!--[if !IE]>end section content top<![endif]-->
                                <!--[if !IE]>start section content bottom<![endif]-->
                                <span class="scb"><span class="scb_left"></span><span class="scb_right"></span></span>
                                <!--[if !IE]>end section content bottom<![endif]-->

                            </div> 

                        </div>
                    </div>
                </div>
                <div id="sidebar">
                    <div class="inner">                                     

                        <div class="section">
                            <!--[if !IE]>start title wrapper<![endif]-->
                            <div class="title_wrapper">
                                <h2>Menu</h2>
                                <span class="title_wrapper_left"></span>
                                <span class="title_wrapper_right"></span>
                            </div>
                            <!--[if !IE]>end title wrapper<![endif]-->
                            <!--[if !IE]>start section content<![endif]-->
                            <div class="section_content">
                                <!--[if !IE]>start section content top<![endif]-->
                                <div class="sct">
                                    <div class="sct_left">
                                        <div class="sct_right">
                                            <div class="sct_left">
                                                <div class="sct_right">
                                                    <ul class="sidebar_menu">
                                                         <li><a href="Dashboard.jsp">Dash Board</a></li>
                                                        <li><a href="registeruser.jsp?edit" class="d1"><span>User Profile</span></a></li>

                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <!--[if !IE]>end section content top<![endif]-->
                                <!--[if !IE]>start section content bottom<![endif]-->
                                <span class="scb"><span class="scb_left"></span><span class="scb_right"></span></span>
                                <!--[if !IE]>end section content bottom<![endif]-->

                            </div>
                            <!--[if !IE]>end section content<![endif]-->
                        </div>
                    </div>
                </div>

            </div> 
        </div>
    </body>
</html>
