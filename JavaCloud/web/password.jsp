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
        <script type="text/javascript">

            function setvalues() {
                var error = '${error}';
                var email='${txtemail}';
                var pass = '${password}';
                
                 if (email !== null) {
                    var elem = document.getElementById("txtemail");
                    elem.value = email;
                }

                if (error !== null) {
                    if (error === "1")
                    {
                        document.getElementById('lblerror').innerHTML = 'Email is incorrect!';
                    }
                    else {
                        document.getElementById('lblerror').innerHTML = '';
                    }
                }

                if (pass === '') {
                    document.getElementById('lblpass').innerHTML = '';
                } else {
                    document.getElementById('lblpass').innerHTML = 'Your Password Is:  ' + "${password}";
                }
            }
            window.onload = setvalues;
        </script>
    </head>
    <body>
        <div id="wrapper">
            <!--[if !IE]>start login wrapper<![endif]-->
            <div id="login_wrapper">			
                <% String pass = String.valueOf(request.getAttribute("password"));
                    System.out.println(pass);%>
                <!--[if !IE]>start login<![endif]-->
                <form id="form1" method="POST" action="./registeruser">
                    <fieldset>
                        <input type="hidden" name="hdnData" id="hdnData" />
                        <h1 id="logo"><a href="#">JavaCloud Administration Panel</a></h1>
                        <div class="formular">
                            <div class="formular_inner">

                                <label>
                                    <strong>Email:</strong>
                                    <span class="input_wrapper">
                                        <input name="txtemail" id="txtemail" type="text" />
                                    </span>
                                </label>

                                <label id="lblpass">
                                    <strong> </strong>

                                </label>

                                <center>
                                    <label id="lblerror" name="lblerror" style="color: red" > </label>
                                </center>
                                <ul class="form_menu">
                                    <li><span class="button"><span><span>Submit</span></span><input type="submit" value="submit" name="btnsubmit"/></span></li>

                                    <li><span class="button"><a href="loginuser.jsp"><span><span>Login</span></span></a></span></li>
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