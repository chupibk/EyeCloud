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
        <script type="text/javascript" src="js/si.files.js"></script>
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
                var hdnfilename = '${hdnfilename}';
                var hdnlblfilename = '${hdnlblfilename}';
                var hdnstartpnt = '${hdnstartpnt}';
                var hdnduration = '${hdnduration}';
                var hdnlblstlname = '${hdnlblstlname}';
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
                if (hdnfilename !== null) {
                    document.form1.hdnfilename.value = hdnfilename;
                }
                if (hdnlblfilename !== null) {
                    document.form1.hdnlblfilename.value = hdnlblfilename;
                }
                if (hdnstartpnt !== null) {
                    document.form1.hdnstartpnt.value = hdnstartpnt;
                }
                if (hdnduration !== null) {
                    document.form1.hdnduration.value = hdnduration;
                }
                if (hdnlblstlname !== null) {
                    document.form1.hdnlblstlname.value = hdnlblstlname;
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
                document.form2.hdnpart.value = document.form2.ddlpart.value;
            }

            function Sethdnfield() {
                document.form1.hdnfilename.value = document.form1.file1.value;
            }
            function  setlblhdnfield() {
                document.form1.hdnlblfilename.value = document.form1.loadlabel.value;
            }

            window.onload = selectedValue;
        </script>
        <style type="text/css" title="text/css">
            /* <![CDATA[ */

            .SI-FILES-STYLIZED label.cabinet
            {
                width: 78px;
                height: 22px;
                background: url(img/btn-choose-file.gif) 0 0 no-repeat;

                display: inline-block;
                overflow: hidden;
                cursor: pointer;
            }

            .SI-FILES-STYLIZED label.cabinet input.file
            {
                position: relative;
                height: 100%;
                width: auto;
                opacity: 0;
                -moz-opacity: 0;
                filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);
            }

            /* ]]> */
        </style>
    </head>

    <body>  

        <% String fileload = (String) request.getAttribute("fileload");%>
        <form name="form1" action="./LoadData" method="POST" enctype="multipart/form-data" >
            <center>
                <table>
                    <tbody>
                        <tr>
                    <input type="hidden" name="hdnfilename"/>
                    <input type="hidden" name="hdnlblfilename"/>
                    <input type="hidden" name="hdnstartpnt"/>
                    <input type="hidden" name="hdnduration"/>
                    <input type="hidden" name="hdnlblstlname"/>

                    <td style="text-align: right; font-size:14px; ">Select Eye Tracking Folder: 
                        <label class="cabinet">  <input type="file" class="file" name="file1"/> </label>
                        <input type="submit" name="btnload" id="btnload" onclick="Sethdnfield()" value="Load file" style="margin-bottom: 10px"/>
                        <br/></td>
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
                            X Right: <select name="ddlxright" style="margin-bottom: 10px" onchange="Setdropdownvalue()">

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
                                Load Participant identity Excel file: 
                                <label class="cabinet">  <input type="file" name="loadpart" class="file" /> </label> <input type="submit" name="btnpart" value="Load" onclick="Setdropdownvalue()" style="margin-bottom: 10px" />
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

                                Participant ID: <select name="ddlpart" style="margin-bottom: 10px" onchange="Setdropdownvalue(), this.form.submit()">

                                    <c:forEach items="${partarrls}" var="partarrl">
                                        <option value="${partarrl.key}" ${partarrl.key == selectedpart ? 'selected="selected"' : ''}>

                                            <c:out value="${partarrl.value}" /></option>
                                    </c:forEach></select> </td> 

                        <td style="vertical-align: top"> 
                            <table border="1">
                                <tr><td style="font-size:12px;">File Name</td> <td style="font-size:12px;">Participant ID</td></tr>
                                <tr><td style="vertical-align: top">
                                        <table border="1">
                                            <c:forEach items="${hdnfilename}" var="hdnfilename">
                                                <tr>
                                                    <td style="font-size:12px;"> '${hdnfilename}'</td>
                                                </tr>    

                                            </c:forEach>
                                        </table>
                                    </td>
                                    <td>
                                        <table border="1">
                                            <c:forEach items="${lstpart}" var="DemoNames">
                                                <tr>

                                                    <td style="font-size:12px;"><input type="text" size="5" value="${DemoNames}"</td>
                                                </tr>
                                            </c:forEach>
                                        </table>
                                    </td></tr>
                            </table>
                        </td>

                        </form> 
                        <td style="vertical-align: top"> <table border="1">
                                <tr><td style="font-size:12px;">File Name</td> <td style="font-size:12px;">Participant ID</td></tr>
                                <% String hdnlblfilename = (String) request.getAttribute("hdnlblfilename");
                                    if (hdnlblfilename != null && !hdnlblfilename.isEmpty()) {%>

                                <tr><td style="vertical-align: top">
                                        <table border="1">
                                            <c:forEach items="${hdnlblfilename}" var="hdnlblfilename">
                                                <tr>
                                                    <td style="font-size:12px;"> '${hdnlblfilename}'</td>
                                                </tr>    
                                            </c:forEach>
                                        </table>
                                    </td>
                                    <td>
                                        <table border="1">
                                            <c:forEach items="${lstpart}" var="DemoNames">
                                                <tr>
                                                    <td style="font-size:12px;"><input type="text" size="5" value="${DemoNames}"</td>
                                                </tr>
                                            </c:forEach>
                                                
                                        </table>
                                    </td></tr>

                                <%}%>
                            </table></td>
                            <td style="text-align: right; vertical-align: top;font-size:14px;"> Select Label files folder:  <label class="cabinet"> <input type="file"  name="loadlabel" class="file" /> </label>
                            <input type="submit" name="btnlblfiles" style="margin-bottom: 10px" value="Load" onclick="setlblhdnfield()"  /> <br/>

                            * Time Stamp Start Point: <select name="ddlstartpoint" style="margin-bottom: 10px" onchange="Setdropdownvalue()">
                                <c:forEach items="${lblarrls}" var="lblarrl">
                                    <option value="${lblarrl.key}">
                                        <c:out value="${lblarrl.value}" />
                                    </option>
                                </c:forEach></select> <br/>


                            * Duration: <select name="ddlduration" style="margin-bottom: 10px"><c:forEach items="${lblarrls}" var="lblarrl">
                                    <option value="${lblarrl.key}">
                                        <c:out value="${lblarrl.value}" />
                                    </option>
                                </c:forEach></select> <br/>
                            * Stimuli Name/ID: <select name="ddlstimuli" style="margin-bottom: 10px">
                                <c:forEach items="${lblarrls}" var="lblarrl">
                                    <option value="${lblarrl.key}">
                                        <c:out value="${lblarrl.value}" />
                                    </option>
                                </c:forEach></select> <br/>
                            * Screen Size: x: <input type="text" name="txtxscreen" size="5" value="35" style="margin-bottom: 10px" /> y: <input type="text" name="txtyscreen" size="5" value="45" style="margin-bottom: 10px" /> <br/>
                            * Resolution x: <input type="text" name="txtxresol" size="5" value="1024" style="margin-bottom: 10px" /> y: <input type="text" name="txtyresol" value="768" size="5" style="margin-bottom: 10px" /> 
                            <br/><br/><br/><br/><br/><br/><br/><br/>
                            <input type="button" name="btnloadsavedfiles" value="Load" style="margin-bottom: 10px" /> <br/>
                            <input type="button" name="btnsave" value="Save and Start" style="margin-bottom: 10px" /></td>
                    </tr>
                    </tbody>
                </table>
            </center>
            <script type="text/javascript" language="javascript">
            // <![CDATA[

            SI.Files.stylizeAll();

            /*
             --------------------------------
             Known to work in:
             --------------------------------
             - IE 5.5+
             - Firefox 1.5+
             - Safari 2+
             
             --------------------------------
             Known to degrade gracefully in:
             --------------------------------
             - Opera
             - IE 5.01
             
             --------------------------------
             Optional configuration:
             
             Change before making method calls.
             --------------------------------
             SI.Files.htmlClass = 'SI-FILES-STYLIZED';
             SI.Files.fileClass = 'file';
             SI.Files.wrapClass = 'cabinet';
             
             --------------------------------
             Alternate methods:
             --------------------------------
             SI.Files.stylizeById('input-id');
             SI.Files.stylize(HTMLInputNode);
             
             --------------------------------
             */

            // ]]>
            </script>

    </body>
</html>