<%-- 
    Document   : RawDataList
    Created on : Sep 1, 2013, 2:42:12 PM
    Author     : samsalman
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link media="screen" rel="stylesheet" type="text/css" href="css/admin-login.css"  />
        <link media="screen" rel="stylesheet" type="text/css" href="css/admin.css"  />
        <title>Raw Data List</title>
       

    </head>
    <body>
        <%  ArrayList<String> Alrd_column = (ArrayList) request.getAttribute("Alrd_column");
            ArrayList<String> Alrd_value = (ArrayList) request.getAttribute("Alrd_value");

//            ArrayList<String> Alrd_lbl_column = (ArrayList) request.getAttribute("Alrd_lbl_column");
//            ArrayList<String> Alrd_lbl_value = (ArrayList) request.getAttribute("Alrd_lbl_value");
            //out.print(Alrd_lbl_column +" " +  );
        %>

        <div id="wrapper">
            <!--[if !IE]>start login wrapper<![endif]-->
            <div id="content">

                <div class="inner-image">
                    &nbsp;&nbsp;&nbsp;&nbsp;<img id="g1" src="img/logo1.png" />
                    <div style="color: #194d65; font-weight: bold; font-size: 30px; float:right; margin-top:50px; margin-right:550px">
                        Interactive Technologies Research Group
                    </div>
                </div>
                <!--[if !IE]>start page<![endif]-->
                <div id="page">
                    <div class="inner-data">

                        <div class="section">
                            <!--[if !IE]>start title wrapper<![endif]-->
                            <div class="title_wrapper">
                                <h2>Raw Data List</h2>
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

                                                    <form id="form1" method="POST" action="./RawDataList">
                                                        <table>
                                                            <tr><td>Select File: <select style="margin-bottom: 10px" name="ddlfile" onchange="Setdropdownvalue()">
                                                                        <c:forEach items="${arrls}" var="arrl">
                                                                            <option value="${arrl.key}">
                                                                            <c:out value="${arrl.value}" />
                                                                            </option>
                                                                        </c:forEach>
                                                                    </select> </td> <td></td> </tr>
                                                            <tr><td>
                                                                    <div class="table_wrapper">
                                                                        <div class="table_wrapper_inner">
                                                                            <div style="overflow: scroll; height: 400px; width: 1080px; background: transparent">
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
                                                                <td style="vertical-align: bottom;">
                                                                </td>                                                                    
                                                            <div class="table_wrapper">
                                                            </tr></table>
                                                        <div class="table_menu">

                                                            <ul class="right">
                                                                <li><span class="button approve"><span><span>Start Validating..</span></span><input id="btnvalidate" type="submit" value="Start Validating.."/></span></li>
                                                            </ul>
                                                        </div>

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
                                                        <li><a href="#">Dash Board</a></li>
                                                        <li><a href="#">Raw Data</a></li>
                                                        <li><a href="#">Valid Data</a></li>
                                                        <li><a href="#">Fixation & Saccade Data</a></li>
                                                        <li><a href="#">User Profile</a></li>

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
