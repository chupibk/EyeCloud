<%-- 
    Document   : registeruser
    Created on : Aug 24, 2013, 8:44:24 PM
    Author     : samsalman
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
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
                    var elem = document.getElementById("lblerror");
                    if (error === "1")
                    {
                        alert(error);
                        elem.value = "Please fill required fields";
                    }
                    else {
                        elem.value = "";
                    }
                }
            }
            window.onload = setvalues;
        </script>
    </head>

    <body>
        <h2>Create your Account!</h2>
        <form id="form1" method="POST" action="./registeruser" >
            <center>        
                <table>
                    <tr><td><h4>Account Information</h4></td>
                    </tr>
                    <tr><td>* Email:</td> <td> <input name="txtemail" id="txtemail" type="text" size="30px" /></td></tr>
                    <tr><td>* Password: </td> <td><input name="txtpass" type="password" size="30px"/></td></tr>
                    <tr><td>* Confirm Password: </td> <td><input name="txtCpass" type="password" size="30px"/></td></tr>
                    <tr><td><h4>Personal Information</h4></td></tr>
                    <tr><td>* Full Name:</td> <td> <input id="txtfname" name="txtfname" type="text" size="30px" /></td></tr>
                    <tr><td>* Country:</td> <td> <input name="txtcountry" id="txtcountry" type="text" size="30px" /></td></tr>
                    <tr><td>* State/Province:</td> <td> <input name="txtstate" id="txtstate" type="text" size="30px" /></td></tr>
                    <tr><td>* City:</td> <td> <input name="txtcity" id="txtcity" type="text" size="30px" /></td></tr>
                    <tr><td>* Address:</td> <td> <input name="txtadd" id="txtadd" type="text" size="30px" /></td></tr>
                    <tr><td>* Phone No:</td> <td> <input name="txtphone" id="txtphone" type="text" size="30px" /></td></tr>
                    <tr><td>&nbsp;&nbsp;&nbsp;Mobile No:</td> <td> <input name="txtmob" id="txtmob" type="text" size="30px" /></td></tr>

                    <tr><td>&nbsp;&nbsp;&nbsp;Postal Code:</td> <td> <input name="txtpostal" id="txtpostal" type="text" size="30px" /></td></tr>
                    <br/>

                    <tr><td colspan="2"><label id="lblerror" name="lblerror" style="color: red" > </label></td></tr>
                    <tr><td></td> <td style="text-align: right"> <input id="btncreate" type="submit" value="Create"/></td></tr>


                </table>
            </center>
        </form>

    </body>
</html>
