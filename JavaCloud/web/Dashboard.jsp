<%-- 
    Document   : Dashboard
    Created on : Aug 29, 2013, 7:09:40 PM
    Author     : samsalman
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <meta http-equiv="imagetoolbar" content="no" />
        <link media="screen" rel="stylesheet" type="text/css" href="css/admin-login.css"  />
        <link media="screen" rel="stylesheet" type="text/css" href="css/admin.css" />
        <script type="text/javascript" src="js/behaviour.js"></script>
        <script type="text/javascript">
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
                }

            }
        </script>
        <title>Dashboard Page</title>
    </head>
    <body>
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
                    <div class="inner-dash">

                        <div class="section">
                            <!--[if !IE]>start title wrapper<![endif]-->
                            <div class="title_wrapper">
                                <h2>Dashboard</h2>
                                <span class="title_wrapper_left"></span>
                                <span class="title_wrapper_right"></span>
                            </div>
                            <!--[if !IE]>end title wrapper<![endif]-->
                            <!--[if !IE]>start section content<![endif]-->
                            <div class="section_content">
                                <center>
                                    <!--[if !IE]>start section content top<![endif]-->
                                    <div class="sct">
                                        <div class="sct_left">
                                            <div class="sct_right">
                                                <div class="sct_left">
                                                    <div class="sct_right">
                                                        <form id="form1" method="POST" action="./Dashboard" >
                                                            <input type="hidden" name="hdnData" id="hdnData" />

                                                            <!--[if !IE]>start dashboard menu<![endif]-->
                                                            <div class="dashboard_menu_wrapper">

                                                                <ul class="dashboard_menu">
                                                                    <li><a href="LoadData.jsp" class="d2"><span>Setup Upload Files</span></a></li>
                                                                    <li><a href="#" onclick="submitForm('RD')"  class="d5"><span>Raw Data</span></a></li>
                                                                    <li><a href="#" onclick="submitForm('VD')" class="d4"><span>Valid Data</span></a></li>
                                                                    <li><a href="#" onclick="submitForm('FD')" class="d8"><span>Fixation & Saccade Data</span></a></li>
                                                                    <li><a href="registeruser.jsp" class="d1"><span>User Profile</span></a></li>

                                                                </ul>

                                                            </div>

                                                            <!--[if !IE]>end dashboard menu<![endif]-->
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </center>
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
