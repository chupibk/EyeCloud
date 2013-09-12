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
        <link media="screen" rel="stylesheet" type="text/css" href="css/admin-login.css"  />
        <link media="screen" rel="stylesheet" type="text/css" href="css/admin.css"  />
        <title>Valid Data</title>
    </head>
    <body>
        <%  ArrayList<String> Alrd_column = (ArrayList) request.getAttribute("arrColumn");
            ArrayList<String> Alrd_value = (ArrayList) request.getAttribute("arrValue");
            ArrayList<String> arrTime = (ArrayList) request.getAttribute("arrTime");

            ArrayList<String> Alrd_lbl_column = (ArrayList) request.getAttribute("arrColumn_lbl");
            ArrayList<String> Alrd_lbl_value = (ArrayList) request.getAttribute("arrValue_lbl");

            //out.println(session.getAttribute("hdnxleft")): 
        %>
        <div id="wrapper">
            <!--[if !IE]>start login wrapper<![endif]-->
            <div id="content">

                <div class="inner-image">
                    &nbsp;&nbsp;&nbsp;&nbsp;<img id="g1" src="img/logo1.png" />
                    <div style="color: #194d65; font-weight: bold; font-size: 12px; float:right; margin-top:30px; margin-right:20px">
                        Welcome : "${username}"
                        </br>
                        </br>
                        <a style="float: right" href="Dashboard.jsp?kill" class="d5"><span>Log Out &nbsp;&nbsp;&nbsp;</span></a>
                    </div>
                    <div style="color: #194d65; font-weight: bold; font-size: 30px; float:right; margin-top:-50px; margin-right:550px">
                        Interactive Technologies Research Group
                    </div>

                </div>
                <!--[if !IE]>start page<![endif]-->
                <div id="page">
                    <div class="inner-data">

                        <div class="section">
                            <!--[if !IE]>start title wrapper<![endif]-->
                            <div class="title_wrapper">
                                <h2>Valid Data</h2>
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
                                                        <table><tr><td>
                                                                    <div class="table_wrapper">
                                                                        <div class="table_wrapper_inner">

                                                                            <div style="overflow: scroll; height: 400px; width: 1080px; background: transparent">
                                                                                <table border="1" style='table-layout:fixed'><tr> <td style=" vertical-align: top"> 
                                                                                            <table >
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
                                                                                            <table>
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
                                                                            <div class="table_menu">
                                                                                <ul class="right"  >
                                                                                    <li><span class="button approve"><span><span>Run Fixation</span></span> <input type="submit" value="Run Fixation" name="btnRun" /></span></li>
                                                                                </ul>

                                                                                <ul class="right" style="margin-right: 5%">
                                                                                    <li><span class="button approve"><span><span>Next</span></span> <input type="submit" value="Next" name="btnNext" /></span></li>
                                                                                </ul>
                                                                                
                                                                            </div>
                                                                        </div>
                                                                    </div>

                                                                </td>
                                                                <td>
                                                                    <!--                                                                    <div class="table_wrapper">
                                                                                                                                            <div class="table_wrapper_inner">
                                                                                                                                                <div style="overflow: scroll; height: 400px; width: 500px; background: transparent">
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
                                                                                                                           <div class="table_menu">

                                                           
                                                        </div>
                                                    </div>
                                                </div>-->

                                                                </td>

                                                            </tr></table>


                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div></div></div></div></div>
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
            </div></div>
    </body>
</html>
