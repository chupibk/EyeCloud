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
                    Cloud computing for Eye Tracking Analysis
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
                <div id="intro">
                    <blockquote class="style1">
                        <span>
                            Recently, eye tracking has been receiving more attention, especially debuting of <a href='http://www.google.com/glass/start/'>Google glass</a> - a
                            wearable device showing that eye tracking technology is coming to use in real life. Traditionally, eye
                            tracking is known as psychology study and used in laboratory for researches related to people’s visual
                            system and cognitive process. It is time for eye tracking technology to escape from laboratory
                            environment and applied widely in the wild. However, eye tracking is suffering from slow local
                            computation and huge eye data collected and analyzed that are preventing it from online processing. 
                            Cloud computing and storage that means everything can be accessed and controlled via the global
                            Internet is becoming increasingly popular. Many research fields such as video, image, and speech
                            processing as well as machine learning and human-robot interaction are testing its feasibility and
                            benefiting from cloud solutions. Eye tracking is not an exception to follow that mainstream. We
                            propose the cloud computing solution to break out slow computation and speed up analysis. We name
                            it as EyeCloud service.
                            <br/><br/>
                            The main purpose of EyeCloud is step by step to apply cloud computing to eye tracking research
                            firstly and gradually to real life usage. First of all, EyeCloud helps tasks running slowly in local
                            computing be processed faster in cloud environment. Secondly, EyeCloud plays a role as platform in
                            which real-time eye tracking applications can be deployed.
                            We base on <a href="http://hadoop.apache.org/">Hadoop</a> framework to build EyeCloud and applications developed 
                            with <a href="http://storm-project.net/">Storm</a> and
                            MapReduce models can run on EyeCloud. There are 3 applications deployed successfully on EyeCloud.
                        </span>
                    </blockquote>
                </div>
                <div id="app1">
                    <div class='apptitle'>
                        Multi-source gaze visualization with real-time heatmap
                    </div>
                    <iframe src="//www.youtube.com/embed/Sxd6rMHAfiE" frameborder="0" allowfullscreen></iframe>
                    <div id="description">
                        <blockquote class="style2">
                            <span>
                                "Based on a problem of how to aggregate and visualize gaze data from many sources of eye trackers which
                                many scenarios might require such as in re mote conference, when we want to know participants’ point of
                                interests in a slide show or when we want to summarize gaze data from million viewers of a website. 
                                Multi-source gaze visualization is absolutely relevant for those cases. This application provides two kinds
                                of experiment: one for static contents and one used for dynamic contents."
                            </span>
                        </blockquote>
                    </div>
                    <div id="appbody">
                        <div id='static'>
                            <h4>Static content heatmap rendering </h4>
                            Link: <input type="text" value="http://www.bloomberg.com/" name="site-s">
                            Participants:
                            <select name="np-s">
                                <option value="1">1</option>
                                <option value="2">2</option>
                                <option value="3">3</option>
                                <option value="4">4</option>
                                <option value="5">5</option>
                            </select>
                            Experiment ID:
                            <select name="id-s" disabled="true">
                            </select>       
                            <br/>
                            Gaze segment:
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
                            Offline ID:
                            <select name="off-s">
                            </select> 
                            <br/><br/>
                            <input id='clientBtn-s' type="button" value="Participant Screen">
                            <input id='onlineBtn-s' type="button" value="Online Heatmap Screen">
                            <input id='offlineBtn-s' class="offline" type="button" value="Offline review">
                        </div>
                        <div id='video'>
                            <h4>Youtube video heatmap rendering</h4>
                            Youtube ID: <input type="text" value="K0-ucWKiTps" name="site-v">
                            Participants:
                            <select name="np-v">
                                <option value="1">1</option>
                                <option value="2">2</option>
                                <option value="3">3</option>
                                <option value="4">4</option>
                                <option value="5">5</option>
                            </select>
                            Experiment ID:
                            <select name="id-v" disabled="true">
                            </select>      
                            <br/>
                            Gaze segment:
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
                            Offline ID:
                            <select name="off-v">
                            </select>                             
                            <br/><br/>
                            <input id='clientBtn-v' type="button" value="Participant Screen">
                            <input id='onlineBtn-v' type="button" value="Online Heatmap Screen">
                            <input id='offlineBtn-v' class="offline" type="button" value="Offline review">
                        </div>
                        <div id="note1">
                            Note:
                            <br/>
                            - Link: The address of content that you want to conduct experiment.
                            <br/>
                            - Participants: Number of participants of your experiment.
                            <br/>
                            - Gaze segment: Time segment of heatmap results.
                        </div>
                        <div id="note2">
                            <br/>
                            - Testing time: That is duration of your experiment.
                            <br/>
                            - Offline ID: ID of the experiment that you want to review again.
                            <br/>
                            - Experiment ID: ID of your current experiment.
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
