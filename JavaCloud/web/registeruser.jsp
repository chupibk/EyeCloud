<%-- 
    Document   : registeruser
    Created on : Aug 24, 2013, 8:44:24 PM
    Author     : samsalman
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <meta http-equiv="imagetoolbar" content="no" />
        <link media="screen" rel="stylesheet" type="text/css" href="css/admin-login.css"  />
        <link media="screen" rel="stylesheet" type="text/css" href="css/admin.css"  />
        
        <title>Create Account</title>
        <script type="text/javascript">
            function setvalues() {
                var name = '${name}';
                var email = '${email}';
                var country = '${country}';
                var state = '${state}';
                var city = '${city}';
                var address = '${address}';
                var mobile = '${mobile}';
                var phone = '${phone}';
                var postal = '${postal}';
                var error = '${error}';

                if (name !== null) {
                    var elem = document.getElementById("txtfname");
                    elem.value = name;
                }
                if (email !== null) {
                    var elem = document.getElementById("txtemail");
                    elem.value = email;
                }
                if (country !== null) {
                    var elem = document.getElementById("txtcountry");
                    elem.value = country;
                }
                if (state !== null) {
                    var elem = document.getElementById("txtstate");
                    elem.value = state;
                }
                if (city !== null) {
                    var elem = document.getElementById("txtcity");
                    elem.value = city;
                }
                if (address !== null) {
                    var elem = document.getElementById("txtadd");
                    elem.value = address;
                }
                if (mobile !== null) {
                    var elem = document.getElementById("txtmob");
                    elem.value = mobile;
                }
                if (phone !== null) {
                    var elem = document.getElementById("txtphone");
                    elem.value = phone;
                }
                if (postal !== null) {
                    var elem = document.getElementById("txtpostal");
                    elem.value = postal;
                }
                if (error !== null) {
                    if (error === "1")
                    {
                        document.getElementById('lblerror').innerHTML = 'Please fill all required fields!';
                    } else if (error === "2")
                    {
                        document.getElementById('lblerror').innerHTML = 'Password should be matched!';
                    }else if (error === "3")
                    {
                        document.getElementById('lblerror').innerHTML = 'This account already exists!';
                    }
                    else {
                        document.getElementById('lblerror').innerHTML = '';
                    }

                }
            }
            window.onload = setvalues;
        </script>
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
                    <div class="inner-reg">
                      

                        <table style="width: 60%; ">
                            <tr>
                                <td>
                                    <div class="section">
                                        <!--[if !IE]>start title wrapper<![endif]-->
                                        <div class="title_wrapper">
                                            <h2>Create Account</h2>
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
                                                                <form id="form1" method="POST" action="./registeruser" >
                                                                    <center>
                                                                        <table>
                                                                            <tr><td colspan="2">
                                                                                    <div class="title_wrapper">
                                                                                        <h2>Account Information</h2>
                                                                                        <span class="title_wrapper_left"></span>
                                                                                        <span class="title_wrapper_right"></span>
                                                                                    </div>
                                                                                </td>
                                                                            </tr>
                                                                            <tr><td>* Email:</td> <td> <span class="input_wrapper"> <input name="txtemail" id="txtemail" type="text" size="30px" /> </span></td></tr>
                                                                            <tr><td>* Password: </td> <td>  <span class="input_wrapper"><input name="txtpass" type="password" size="30px"/></span> </td></tr>
                                                                            <tr><td>* Confirm Password: </td> <td> <span class="input_wrapper"><input name="txtCpass" type="password" size="30px"/> </span></td></tr>
                                                                            <tr><td colspan="2">
                                                                                    <div class="title_wrapper">
                                                                                        <h2>Personal Information</h2>
                                                                                        <span class="title_wrapper_left"></span>
                                                                                        <span class="title_wrapper_right"></span>
                                                                                    </div>
                                                                                </td>
                                                                            </tr>
                                                                            <tr><td>Full Name:</td> <td>  <span class="input_wrapper"> <input id="txtfname" name="txtfname" type="text" size="30px" /></span> </td></tr>
                                                                            <tr><td>Country:</td> <td>  <span class="input_wrapper"> <input name="txtcountry" id="txtcountry" type="text" size="30px" /></span> </td></tr>
                                                                            <tr><td>State/Province:</td> <td>  <span class="input_wrapper"><input name="txtstate" id="txtstate" type="text" size="30px" /> </span></td></tr>
                                                                            <tr><td>City:</td> <td>  <span class="input_wrapper"> <input name="txtcity" id="txtcity" type="text" size="30px" /></span> </td></tr>
                                                                            <tr><td>Address:</td> <td> <span class="input_wrapper"> <input name="txtadd" id="txtadd" type="text" size="30px" /></span> </td></tr>
                                                                            <tr><td>Phone No:</td> <td> <span class="input_wrapper"> <input name="txtphone" id="txtphone" type="text" size="30px" /></span> </td></tr>
                                                                            <tr><td>Mobile No:</td> <td> <span class="input_wrapper"> <input name="txtmob" id="txtmob" type="text" size="30px" /></span></td></tr>

                                                                            <tr><td>Postal Code:</td> <td> <span class="input_wrapper"> <input name="txtpostal" id="txtpostal" type="text" size="30px" /></span></td></tr>
                                                                            <br/>

                                                                            <tr><td colspan="2" style="text-align: center"><label id="lblerror" name="lblerror" style="color: red" > </label></td></tr>
                                                                            <tr><td></td> <td style="text-align: right">
                                                                                    <span class="button approve"><span><span>Register</span></span><input type="submit" value="Create" id="btncreate" name="btncreate"/></span>
                                                                                </td></tr>
                                                                        </table>
                                                                    </center>
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
                                        <!--[if !IE]>end section content<![endif]-->
                                    </div>
                                </td>
                            </tr>
                        </table>


                    </div>
                </div>
            </div>
        </div>



    </body>
</html>
