<%-- 
    Document   : loginuser
    Created on : Aug 26, 2013, 9:41:51 PM
    Author     : samsalman
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <meta http-equiv="imagetoolbar" content="no" />
        <title>Login Panel</title>
        <link media="screen" rel="stylesheet" type="text/css" href="css/admin-login.css"  />
    </head>
    <body>
        <div id="wrapper">
            <!--[if !IE]>start login wrapper<![endif]-->
            <div id="login_wrapper">			
                
                <!--[if !IE]>start login<![endif]-->
                <form id="form1" method="POST" action="./registeruser">
                    <fieldset>

                        <h1 id="logo"><a href="#">JavaCloud Administration Panel</a></h1>
                        <div class="formular">
                            <div class="formular_inner">

                                <label>
                                    <strong>Email:</strong>
                                    <span class="input_wrapper">
                                        <input name="txtemail" id="txtemail" type="text" />
                                    </span>
                                </label>
                                <label>
                                    <strong>Password:</strong>
                                    <span class="input_wrapper">
                                        <input name="txtpass" id="txtpass" type="password" />
                                    </span>
                                </label>
                                <ul class="form_menu">
                                    <li><span class="button"><span><span>Login</span></span><input type="submit" value="login" name="btnlogin"/></span></li>
                                    <li><span class="button"><span><span>Forgot Pass</span></span><input type="submit" value="forget" name="btnforget"/></span></li>
                                </ul>

                            </div>
                        </div>
                    </fieldset>
                </form>
                <!--[if !IE]>end login<![endif]-->
            </div>
            <!--[if !IE]>end login wrapper<![endif]-->
        </div>
    </body>
</html>