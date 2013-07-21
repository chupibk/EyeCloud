<%-- 
    Document   : LoadData
    Created on : Jul 9, 2013, 3:21:46 PM
    Author     : samsalman
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page language="java" import="java.util.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>

    <script type="text/javascript" >
        function selectedValue() {

            var hdntimestamp = '${hdntimestamp}';
            var hdnxleft = '${hdnxleft}';
            var hdnxright = '${hdnxright}';
            var hdnyleft = '${hdnyleft}';
            var hdnyright = '${hdnyright}';
            var hdndleft = '${hdndleft}';
            var hdndright = '${hdndright}';
            var hdnvleft = '${hdnvleft}';
            var hdnvright = '${hdnvright}';
            var hdnstname = '${hdnstname}';
            // var tt = <%=(String) request.getParameter("selectedValue")%>
            if (hdntimestamp !== null)
            {
                document.form1.ddltimestamp.value = hdntimestamp;
            }
            if (hdnxleft !== null)
            {
                document.form1.ddlxleft.value = hdnxleft;
            }
            if (hdnxright !== null)
            {
                document.form1.ddlxright.value = hdnxright;
            }
            if (hdnyleft !== null)
            {
                document.form1.ddlyleft.value = hdnyleft;
            }
            if (hdnyright !== null)
            {
                document.form1.ddlyright.value = hdnyright;
            }
            if (hdndleft !== null)
            {
                document.form1.ddldistleft.value = hdndleft;
            }
            if (hdndright !== null)
            {
                document.form1.ddldistright.value = hdndright;
            }
            if (hdnvleft !== null)
            {
                document.form1.ddlvleft.value = hdnvleft;
            }
            if (hdnvright !== null)
            {
                document.form1.ddlvrght.value = hdnvright;
            }
            if (hdnstname !== null)
            {
                document.form1.ddlstmName.value = hdnstname;
            }
          
        }

        function  Setdropdownvalue() {
            document.form2.hdntimestamp.value = document.form1.ddltimestamp.value;
            document.form2.hdnxleft.value = document.form1.ddlxleft.value;
            document.form2.hdnxright.value = document.form1.ddlxright.value;
            document.form2.hdnyleft.value = document.form1.ddlyleft.value;
            document.form2.hdnyright.value = document.form1.ddlyright.value;
            document.form2.hdndleft.value = document.form1.ddldistleft.value;
            document.form2.hdndright.value = document.form1.ddldistright.value;
            document.form2.hdnvleft.value = document.form1.ddlvleft.value;
            document.form2.hdnvright.value = document.form1.ddlvrght.value;
            document.form2.hdnstname.value = document.form1.ddlstmName.value;
        }
        
        function  SetPartddlvalue(){
            document.form2.hdnpart.value = document.form2.ddlpart.value;
        }

        window.onload = selectedValue;
    </script>
    <body>  

        <% String fileload = (String) request.getAttribute("fileload");%>
        <form name="form1" action="./LoadData" method="POST" enctype="multipart/form-data" >
            <center>
                <table>
                    <tbody>
                        <tr>
                            <td style="text-align: right">Select Eye Tracking Folder: <input type="file" size="50" name="file1"> <input type="submit" name="btnload" id="btnload" value="Load file" style="margin-bottom: 10px"/><br/></td>
                            <td colspan="2"> <% if (fileload == "1") {
                                    out.print("Please Select File to Load");

                                } else {
                                    out.print("");

                                }%>  </td>
                    <br/><br/><br/>
                    </tr>

                    <tr>

                        <td style="text-align: right">* Time Stamp: <select style="margin-bottom: 10px" name="ddltimestamp" onchange="Setdropdownvalue()">

                                <c:forEach items="${arrls}" var="arrl">
                                    <option value="${arrl.key}">
                                        <c:out value="${arrl.value}" />
                                    </option>
                                </c:forEach>
                            </select> 


                            <br/>
                            * X Left: <select style="margin-bottom: 10px" name="ddlxleft" onchange="Setdropdownvalue()">
                                <c:forEach items="${arrls}" var="arrl">
                                    <option value="${arrl.key}">
                                        <c:out value="${arrl.value}" />
                                    </option>
                                </c:forEach>
                            </select>
                            <br/>                    
                            * X Right: <select name="ddlxright" style="margin-bottom: 10px" onchange="Setdropdownvalue()">

                                <c:forEach items="${arrls}" var="arrl">
                                    <option value="${arrl.key}">
                                        <c:out value="${arrl.value}" /></option>
                                </c:forEach></select>
                            <br/>
                            * Y Left: <select name="ddlyleft" style="margin-bottom: 10px" onchange="Setdropdownvalue()">

                                <c:forEach items="${arrls}" var="arrl">
                                    <option value="${arrl.key}">
                                        <c:out value="${arrl.value}" /></option>
                                </c:forEach></select> 
                            <br/>
                            Y Right: <select name="ddlyright" style="margin-bottom: 10px" onchange="Setdropdownvalue()">

                                <c:forEach items="${arrls}" var="arrl">
                                    <option value="${arrl.key}">
                                        <c:out value="${arrl.value}" /></option>
                                </c:forEach></select> 
                            <br/>
                            Distance Left: <select name="ddldistleft" style="margin-bottom: 10px" onchange="Setdropdownvalue()">

                                <c:forEach items="${arrls}" var="arrl">
                                    <option value="${arrl.key}">
                                        <c:out value="${arrl.value}" /></option>
                                </c:forEach></select> 
                            <br/>
                            Distance Right: <select name="ddldistright" style="margin-bottom: 10px" onchange="Setdropdownvalue()">

                                <c:forEach items="${arrls}" var="arrl">
                                    <option value="${arrl.key}">
                                        <c:out value="${arrl.value}" /></option>
                                </c:forEach></select> 
                            <br/>
                            Validity Left: <select name="ddlvleft" style="margin-bottom: 10px" onchange="Setdropdownvalue()">

                                <c:forEach items="${arrls}" var="arrl">
                                    <option value="${arrl.key}">
                                        <c:out value="${arrl.value}" /></option>
                                </c:forEach></select> 
                            <br/>
                            Validity Right: <select name="ddlvrght" style="margin-bottom: 10px" onchange="Setdropdownvalue()">

                                <c:forEach items="${arrls}" var="arrl">
                                    <option value="${arrl.key}">
                                        <c:out value="${arrl.value}" /></option>
                                </c:forEach></select> <br/>
                            * Stimuli Name/ID: <select name="ddlstmName" style="margin-bottom: 10px" onchange="Setdropdownvalue()">

                                <c:forEach items="${arrls}" var="arrl">
                                    <option value="${arrl.key}">
                                        <c:out value="${arrl.value}" /></option>
                                </c:forEach></select> <br/> <br/>
                            </form>
                            <form name="form2" action="./LoadData" method="POST" enctype="multipart/form-data">
                                Load Participant identity Excel file: <input type="file" name="loadpart" size="50" /> <input type="submit" name="btnpart" value="Load" onclick="Setdropdownvalue()" style="margin-bottom: 10px" />
                                <br/>
                                <input type="hidden" name="hdntimestamp"/>
                                <input type="hidden" name="hdnxleft"/>
                                <input type="hidden" name="hdnxright"/>
                                <input type="hidden" name="hdnyleft"/>
                                <input type="hidden" name="hdnyright"/>
                                <input type="hidden" name="hdndleft"/>
                                <input type="hidden" name="hdndright"/>
                                <input type="hidden" name="hdnvleft"/>
                                <input type="hidden" name="hdnvright"/>
                                <input type="hidden" name="hdnstname"/>
                                <input type="hidden" name="hdnpart"/>

                                Participant ID: <select name="ddlpart" style="margin-bottom: 10px" onchange="SetPartddlvalue(),this.form.submit()">

                                    <c:forEach items="${partarrls}" var="partarrl">
                                        <option value="${partarrl.key}" ${partarrl.key == selectedDept ? 'selected="selected"' : ''}>

                                            <c:out value="${partarrl.value}" /></option>
                                    </c:forEach></select> </td> 
                        </form> 
                        <td style="vertical-align: top"> 
                            <table>
                                <c:forEach items="${lstpart}" var="DemoNames">
                                    <tr>
                                        <td>${DemoNames}</td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </td>
                        <td style="vertical-align: top"> <table border="1" > <tr><td>Test11</td> <td>Test12</td></tr>
                                <tr><td>Test21</td> <td>Test22</td></tr></table></td>
                        <td style="text-align: right; vertical-align: top"> Select Label files folder: <input type="file" name="loadlabel" size="30" /> <input type="button" name="btnlblfiles" style="margin-bottom: 10px" value="Load" /> <br/>
                            * Time Stamp Start Point: <select name="ddlstartpoint" style="margin-bottom: 10px"><option>Select </option></select> <br/>
                            * Duration: <select name="ddlduration" style="margin-bottom: 10px"><option>Select </option></select> <br/>
                            * Stimuli Name/ID: <select name="ddlstimuli" style="margin-bottom: 10px"><option>Select </option></select> <br/>
                            * Screen Size: x: <input type="text" name="txtxscreen" size="5" style="margin-bottom: 10px" /> y: <input type="text" name="txtyscreen" size="5" style="margin-bottom: 10px" /> <br/>
                            * Resolution x: <input type="text" name="txtxresol" size="5" style="margin-bottom: 10px" /> y: <input type="text" name="txtyresol" size="5" style="margin-bottom: 10px" /> 
                            <br/><br/><br/><br/><br/><br/><br/><br/>
                            <input type="button" name="btnloadsavedfiles" value="Load" style="margin-bottom: 10px" /> <br/>
                            <input type="button" name="btnsave" value="Save and Start" style="margin-bottom: 10px" /></td>
                    </tr>
                    </tbody>
                </table>
            </center>

    </body>
</html>