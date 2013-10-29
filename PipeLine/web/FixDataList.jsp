<%-- 
    Document   : RawDataList
    Created on : Sep 1, 2013, 2:42:12 PM
    Author     : samsalman
--%>

<%@page import="java.io.PrintWriter"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.ArrayList" %>
<%@page language="java" import="java.util.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link media="screen" rel="stylesheet" type="text/css" href="css/admin-login.css"  />
        <link media="screen" rel="stylesheet" type="text/css" href="css/admin.css"  />
        <title>Fixation & Saccade Data List</title>

        <script type="text/javascript">
            function SetSelectedValue() {
                var filename = '${fileName}';
                if (filename !== "")
                {
                    document.getElementById('ddlfile').value = filename;
                }
            }
            function Setdropdownvalue() {
                document.getElementById('hdnselectvalue').value = document.getElementById('ddlfile').value;
                document.getElementById('hdnselectText').value = document.getElementById('ddlfile').options[document.getElementById('ddlfile').selectedIndex].text;
            }

            function submitForm(val) {

                if (val === 'RD')
                {
                    document.getElementById('hdnData').value = val;
                    document.getElementById('form1').submit();

                } else if (val === 'VD')
                {
                    document.getElementById('hdnData').value = val;
                    document.getElementById('form1').submit();
                } else if (val === 'FD')
                {
                    document.getElementById('hdnData').value = val;
                    document.getElementById('form1').submit();
                }  else if (val === 'EF')
                {
                    document.getElementById('hdnData').value = val;
                    document.getElementById('form1').submit();
                }else if (val === 'kill')
                {
                    document.getElementById('hdnData').value = val;
                    document.getElementById('form1').submit();
                } else if (val === 'DF')
                {
                    document.getElementById('hdnData').value = val;
                    document.getElementById('form1').submit();
                } else if (val === 'DS')
                {
                    document.getElementById('hdnData').value = val;
                    document.getElementById('form1').submit();
                }

            }

            window.onload = SetSelectedValue;
        </script>


    </head>
    <body>
        <%  ArrayList<String> Alrd_column = (ArrayList) request.getAttribute("Alrd_column");
            ArrayList<String> Alrd_value = (ArrayList) request.getAttribute("Alrd_value");

            ArrayList<String> Alrd_lbl_column = (ArrayList) request.getAttribute("Alrd_lbl_column");
            ArrayList<String> Alrd_lbl_value = (ArrayList) request.getAttribute("Alrd_lbl_value");             
             
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
                                <h2>Fixation & Saccade Data List</h2>
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

                                                    <form id="form1" method="POST" action="./Dashboard">
                                                        <input id="hdnselectvalue" name="hdnselectvalue" type="hidden" />
                                                        <input id="hdnselectText" name="hdnselectText" type="hidden" />
                                                        <input type="hidden" name="hdnData" id="hdnData" />
                                                        <table>
                                                            <tr><td style="float: left; width: 30%; margin-top: 3%" >Select File: <select style="margin-bottom: 10px" id="ddlfile" name="ddlfile" onchange="Setdropdownvalue()">
                                                                        <c:forEach items="${arrls}" var="arrl">
                                                                            <option value="${arrl.key}">
                                                                                <c:out value="${arrl.value}" />
                                                                            </option>
                                                                        </c:forEach>
                                                                    </select> 
                                                                    <span class="button approve"><span><span>Search</span></span><input name="btnFixsearch" id="btnFixsearch" type="submit" value="Fix"/></span>
                                                                </td> 
                                                                <td style="float: left; width: 60%; margin-left: 40px; margin-top: 3%" >
                                                                    <br/>&nbsp;&nbsp;&nbsp;
                                                                    <% if (Alrd_value.size() != 0) {%>
                                                                    <a href="#" onclick="submitForm('DF')">Download Fixation</a>
                                                                    <a style="margin-left: 65%" href="#" onclick="submitForm('DS')">Download Saccade</a>
                                                                    <%}%>
                                                                </td> </tr>


                                                            <tr>
                                                                <td colspan="2">
                                                                    <table><tr><td>
                                                                                <div class="table_wrapper">
                                                                                    <div class="table_wrapper_inner">

                                                                                        <div style="overflow: scroll; height: 400px; width: 450px; background: transparent">
                                                                                            <% if (Alrd_column.size() == 0) {
                                                                                                } else {%>
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
                                                                                            <%}%> 
                                                                                        </div>

                                                                                    </div>
                                                                                </div>
                                                                            </td>
                                                                            <td>
                                                                                <div class="table_wrapper">
                                                                                    <div class="table_wrapper_inner">

                                                                                        <div style="overflow: scroll; height: 400px; width: 620px; background:transparent">
                                                                                            <% if (Alrd_lbl_column.size() == 0) {
                                                                                                } else {%>
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
                                                                                            <%}%>
                                                                                        </div>

                                                                                    </div>
                                                                                </div>
                                                                            </td>

                                                                        </tr></table>
                                                                </td>

                                                            <div class="table_wrapper">
                                                                </tr>

                                                        </table>


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
                                                        <li><a href="LoadData.jsp" class="d2"><span>Setup Upload Files</span></a></li>
                                                        <li><a href="#" onclick="submitForm('RD')"  class="d5"><span>Raw Data</span></a></li>
                                                        <li><a href="#" onclick="submitForm('VD')" class="d4"><span>Valid Data</span></a></li>
                                                        <li><a href="#" onclick="submitForm('EF')" class="d8"><span>Eye Feature</span></a></li>
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