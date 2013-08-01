<%-- 
    Document   : index
    Created on : Jul 5, 2013, 3:43:36 PM
    Author     : chung-pi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="css/page.css">
        <script type="text/javascript" src="js/lib/jquery-1.10.2.min.js"></script>
        <title>Eye Cloud</title>
    </head>
    <body>
        <div id="container">
            <div id="header">
                <div id="logo">
                    EyeCloud
                </div>
                <div id="page_title">
                    Cloud computing for Eye Tracking
                </div>                
                <div id="menu">
                    <div class="item">
                        Home
                    </div>
                    <div class="item">
                        Documentation
                    </div>         
                    <div class="item">
                        Contact
                    </div>                       
                </div>
            </div>

            <div id='content'>
                <div class='apptitle'>
                    Multi-source gaze visualization with real-time heatmap
                </div>
                <div id="app1">
                    <iframe src="//www.youtube.com/embed/Sxd6rMHAfiE" frameborder="0" allowfullscreen></iframe>
                    <div id="appbody">
                        <div id='static'>
                            Static content heatmap rendering <br/>
                            Link: <input type="text" value="http://www.bloomberg.com/" name="site-s">
                            Participants:
                            <select name="np-s">
                                <option value="1">1</option>
                                <option value="2">2</option>
                                <option value="3">3</option>
                                <option value="4">4</option>
                                <option value="5">5</option>
                            </select>
                            Available ID:
                            <select name="id-s">
                            </select>        
                            Refresh rate:
                            <select name="refresh-s">
                                <option value="40">40 seconds</option>
                                <option value="100">100 seconds</option>
                                <option value="500">500 seconds</option>
                                <option value="1000">1000 seconds</option>
                                <option value="2000">2000 seconds</option>
                            </select>       
                            Testing time:
                            <select name="time-s">
                                <option value="30000">30 seconds</option>
                                <option value="60000">1 minute</option>
                                <option value="120000">2 minutes</option>
                                <option value="300000">5 minutes</option>
                                <option value="600000">10 minutes</option>
                            </select>
                            <br/><br/>
                            <input id='clientBtn-s' type="button" value="Participant Screen">
                            <input id='onlineBtn-s' type="button" value="Online heatmap">
                            <input id='offlineBtn-s' type="button" value="Offline review">
                        </div>
                        <div id='video'>
                            Youtube video heatmap rendering <br/>
                            ID: <input type="text" value="K0-ucWKiTps" name="site-v">
                            Participants:
                            <select name="np-v">
                                <option value="1">1</option>
                                <option value="2">2</option>
                                <option value="3">3</option>
                                <option value="4">4</option>
                                <option value="5">5</option>
                            </select>
                            Available ID:
                            <select name="id-v">
                            </select>        
                            Refresh rate:
                            <select name="refresh-v">
                                <option value="40">40 seconds</option>
                                <option value="100">100 seconds</option>
                                <option value="500">500 seconds</option>
                                <option value="1000">1000 seconds</option>
                                <option value="2000">2000 seconds</option>
                            </select>       
                            Testing time:
                            <select name="time-v">
                                <option value="30000">30 seconds</option>
                                <option value="60000">1 minute</option>
                                <option value="120000">2 minutes</option>
                                <option value="300000">5 minutes</option>
                                <option value="600000">10 minutes</option>
                            </select>
                            <br/><br/>
                            <input id='clientBtn-v' type="button" value="Participant Screen">
                            <input id='onlineBtn-v' type="button" value="Online heatmap">                        
                        </div>
                    </div>
                </div>

                <div id='app2'>
                    <div class='apptitle'>
                        Offline heatmap rendering
                    </div>   
                    <div class='upload'>
                        Select a file to upload: <br />
                        <form action="UploadServlet" method="post"
                              enctype="multipart/form-data">
                            <input type="file" name="gaze-h" size="50" />
                            <br />
                            <input type="submit" value="Upload File" />
                        </form>
                    </div>
                    <div class='result'></div>
                </div>
                <div id='app3'>
                    <div class='apptitle'>
                        Fixation computation
                    </div>
                   
                    <div class='upload'>
                        Select a file to upload: <br />
                        <form action="UploadServlet" method="post"
                              enctype="multipart/form-data">
                            <input type="file" name="gaze-f" size="50" />
                            <br />
                            <input type="submit" value="Upload File" />
                        </form>
                    </div>
                    <div class='result'></div>                    
                </div>

            </div>
            <div id='footer'>
                Copyright © 2013 <a href='http://cs.joensuu.fi/pages/int/'> Interactive Technologies Research Group, School of Computing, University of Eastern Finland</a>
            </div>
        </div>

        <script type="text/javascript" src="js/page.js"></script>
    </body>
</html>
