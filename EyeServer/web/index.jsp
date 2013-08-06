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
        <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
        <script type="text/javascript" src="js/lib/jquery-1.10.2.min.js"></script>
        <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
        <script src="js/lib/ajaxForm.js"></script> 
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
                            Cloud computing and storage that means everything can be accessed, controlled, and processed via the global
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
                            MapReduce models can run on EyeCloud. Beneath are 3 applications deployed successfully on EyeCloud.
                        </span>
                    </blockquote>
                </div>
                <div id="app1">
                    <div class='apptitle'>
                        Multi-source gaze visualization with real-time heatmap
                    </div>
                    <iframe src="//www.youtube.com/embed/Sxd6rMHAfiE" frameborder="0" allowfullscreen></iframe>
                    <div class="description">
                        <blockquote class="style2">
                            <span>
                                "Based on a problem of how to aggregate and visualize gaze data from many sources of eye trackers which
                                many scenarios might require such as in re mote conference, when we want to know participants’ point of
                                interests in a slide show or when we want to summarize gaze data from million viewers of a website. 
                                Multi-source gaze visualization is absolutely relevant for those cases. This application provides two kinds
                                of experiment: one for static stimulus like a web page or slide and one used for dynamic stimulus like video."
                            </span>
                        </blockquote>
                    </div>
                    <div class="appbody">
                        <div id='static'>
                            <h4>Static stimulus heatmap rendering (<a href="exp/website.html">Example</a>)</h4>
                            <table>
                                <tr>
                                    <td>Link: <input type="text" value="http://www.bloomberg.com/" name="site-s" size="30"></td>
                                    <td>
                                        Participants:
                                        <select name="np-s">
                                            <option value="1">1</option>
                                            <option value="2">2</option>
                                            <option value="3">3</option>
                                            <option value="4">4</option>
                                            <option value="5">5</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Time segment:
                                        <select name="refresh-s">
                                            <option value="40">40 milliseconds</option>
                                            <option value="100">100 milliseconds</option>
                                            <option value="500" selected>500 milliseconds</option>
                                            <option value="1000">1 second</option>
                                            <option value="2000">2 seconds</option>
                                        </select>       
                                    </td>
                                    <td>
                                        Testing time:
                                        <select name="time-s">
                                            <option value="15000">15 seconds</option>
                                            <option value="30000" selected>30 seconds</option>
                                            <option value="60000">1 minute</option>
                                            <option value="120000">2 minutes</option>
                                            <option value="300000">5 minutes</option>
                                            <option value="600000">10 minutes</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Your experiment ID:
                                        <select name="id-s" disabled="true">
                                        </select>     
                                    </td>
                                    <td>
                                        <div id="offlineDlg-s" title="Offline review">
                                            Your experiment ID:
                                            <select name="off-s">
                                            </select> 
                                            <input type="button" value="View" id="viewBtn-s">
                                        </div>
                                    </td>
                                </tr>
                            </table>
                            <br/>
                            <input id='clientBtn-s' type="button" value="Participant Screen">
                            <input id='onlineBtn-s' type="button" value="Online Heatmap Screen">
                            <input id='offlineBtn-s' class="offline" type="button" value="Offline review">
                        </div>

                        <div id='video'>
                            <h4>Youtube video heatmap rendering (<a href="exp/youtube.html">Example</a>)</h4>
                            <table>
                                <tr>
                                    <td>Youtube Link: <input type="text" value="K0-ucWKiTps" name="site-v" size="30"></td>
                                    <td>Participants:
                                        <select name="np-v">
                                            <option value="1">1</option>
                                            <option value="2">2</option>
                                            <option value="3">3</option>
                                            <option value="4">4</option>
                                            <option value="5">5</option>
                                        </select>     
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Time segment:
                                        <select name="refresh-v">
                                            <option value="40">40 milliseconds</option>
                                            <option value="100">100 milliseconds</option>
                                            <option value="500" selected>500 milliseconds</option>
                                            <option value="1000">1 second</option>
                                            <option value="2000">2 seconds</option>
                                        </select>     
                                    </td>
                                    <td>
                                        Testing time:
                                        <select name="time-v">
                                            <option value="15000">15 seconds</option>
                                            <option value="30000" selected>30 seconds</option>
                                            <option value="60000">1 minute</option>
                                            <option value="120000">2 minutes</option>
                                            <option value="300000">5 minutes</option>
                                            <option value="600000">10 minutes</option>
                                        </select>  
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Your experiment ID:
                                        <select name="id-v" disabled="true">
                                        </select>
                                    </td>
                                    <td>
                                        <div id="offlineDlg-v" title="Offline review">
                                            Your experiment ID:
                                            <select name="off-v">
                                            </select> 
                                            <input type="button" value="View" id="viewBtn-v">
                                        </div>
                                    </td>
                                </tr>
                            </table>
                            <br/>
                            <input id='clientBtn-v' type="button" value="Participant Screen">
                            <input id='onlineBtn-v' type="button" value="Online Heatmap Screen">
                            <input id='offlineBtn-v' class="offline" type="button" value="Offline review">
                        </div>
                        <div class="note1">
                            Note:
                            <br/>
                            - Link: the address of content that you want to conduct experiment.
                            <br/>
                            - Participants: number of participants of your experiment.
                            <br/>
                            - Time segment: time segment of heatmap results, 500ms is recommended.
                            <br/>
                            - Testing time: that is duration of your experiment. 
                        </div>
                        <div class="note2">
                            <br/>
                            - Experiment ID: id of your current experiment.
                            <br/>
                            - Participant screen: click here to open experiment screen on participants' PC.
                            <br/>
                            - Online heatmap screen: click here to view online heatmaps rendered.
                            <br/>
                            - Offline review: click here to review again experiments that you did.                            
                        </div>                        
                    </div>
                </div>

                <div class='app2'>
                    <div class='apptitle'>
                        Fixation computation
                    </div>   
                    <div class="description2">
                        <blockquote class="style2">
                            <span>
                                "Fixation is the most important classification of eye tracking analysis and fast fixation
                                detection improves analysis efficiency. We implemented I-VT algorithm on EyeCloud and it shows
                                fast detection, but slow uploading gaze data to EyeCloud is a problem."
                            </span>
                        </blockquote>
                    </div>
                    <div class="demo">
                        <table>
                            <tr>
                                <td><img src="images/raw_fixation.png"/></td>
                                <td><img src="images/fixation.png"/></td>
                            </tr>
                            <tr>
                                <td> Raw gaze data</td>
                                <td> Fixations</td>
                            </tr>
                        </table>
                    </div>

                    <div class="appbody">
                        <div class='upload'>
                            <form id="fixationForm" action="UploadFixation" method="post"
                                  enctype="multipart/form-data">
                                <table>
                                    <tr>
                                        <td>
                                            Select gaze data:
                                        </td>
                                        <td>
                                            <input type="file" name="gaze-data-f" size="50" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            Select media file (optional):
                                        </td>
                                        <td>
                                            <input type="file" name="media-data-f" size="50" />
                                        </td>
                                    </tr>
                                </table>
                                <input type="submit" id="fixationBtn" value="Upload and Detect" />
                            </form>
                        </div>
                        <div class="process">
                            <div class='status'>
                                Status: <a id="fixation-status" href="javascript:void(0)">none</a>
                            </div>
                            <div class="result">
                                Result: <a id="fixation-return" href="javascript:void(0)" target="_blank">empty</a>
                            </div>
                        </div>
                        <div class="note1">
                            Note: put the header of your file like below
                            <div class="code">
                                <table>
                                    <tr>
                                        <td>    RecordingTimestamp </td>	
                                        <td>    GazePointIndex	</td>
                                        <td>    GazePointX	</td>
                                        <td>    GazePointY	</td>
                                        <td>    DistanceLeft	</td>
                                        <td>    DistanceRight	</td>
                                    </tr>
                                    <tr>
                                        <td>9	</td>
                                        <td>1	</td>
                                        <td>850	</td>
                                        <td>369	</td>
                                        <td>558.48	</td>
                                        <td>558.48	</td>
                                    </tr>
                                    <tr>
                                        <td>13	</td>
                                        <td>2	</td>
                                        <td>849</td>
                                        <td>368	</td>
                                        <td>558.42	</td>
                                        <td>558.42	</td>
                                    </tr>
                                    <tr>
                                        <td>16	</td>
                                        <td>3	</td>
                                        <td>852	</td>
                                        <td>365	</td>
                                        <td>558.43	</td>
                                        <td>558.43</td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                        <div class="note2">
                            <br/>
                            - Gaze data: text file of your data.
                            <br/>
                            - Media file: your stimulus, e.g a image (optional).
                            <br/>
                            - Upload and Detect: click here to start uploading and detecting.
                            <br/>
                            - Result: a downloadable link file returned.                        
                        </div>      
                    </div>                
                </div>

                <div class='app2'>
                    <div class='apptitle'>
                        Offline heatmap renderinig
                    </div>   
                    <div class="description2">
                        <blockquote class="style2">
                            <span>
                                "Heatmap is a useful visualization tool that gives us which parts of a stimulus that are paid attention much
                                or many viewers were looking at. Normally, heatmap rendering with Gaussian point spread function runs slowly
                                on a local PC. EyeCloud can speed up such rendering by distributed computing on a cloud cluster. 10~20 times faster
                                than local PCs can be feasible."
                            </span>
                        </blockquote>
                    </div>
                    <div class="demo2">
                        <table>
                            <tr>
                                <td><img src="images/raw_heat.png"/></td>
                                <td><img src="images/heatmap.png"/></td>
                            </tr>
                            <tr>
                                <td> Raw gaze data</td>
                                <td> Heatmap</td>
                            </tr>
                        </table>
                    </div>

                    <div class="appbody">
                        <div class='upload'>
                            <form id="heatmapForm" action="UploadHeatmap" method="post"
                                  enctype="multipart/form-data">
                                <table>
                                    <tr>
                                        <td>
                                            Select gaze data:
                                        </td>
                                        <td>
                                            <input type="file" name="gaze-data-h" size="50" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            Select media file (compulsory):
                                        </td>
                                        <td>
                                            <input type="file" name="media-data-h" size="50" />
                                        </td>
                                    </tr>
                                </table>
                                <input type="submit" id="heatmapBtn" value="Upload and Render" />
                            </form>
                        </div>
                        <div class="process">
                            <div class='status'>
                                Status: <a id="heatmap-status" href="javascript:void(0)">none</a>
                            </div>
                            <div class="result">
                                Result: <a id="heatmap-return" href="javascript:void(0)" target="_blank">empty</a>
                            </div>
                        </div>
                        <div class="note1">
                            Note: put the header of your file like below
                            <div class="code">
                                <table>
                                    <tr>
                                        <td>    RecordingTimestamp </td>	
                                        <td>    GazePointIndex	</td>
                                        <td>    GazePointX	</td>
                                        <td>    GazePointY	</td>
                                        <td>    DistanceLeft	</td>
                                        <td>    DistanceRight	</td>
                                    </tr>
                                    <tr>
                                        <td>9	</td>
                                        <td>1	</td>
                                        <td>850	</td>
                                        <td>369	</td>
                                        <td>558.48	</td>
                                        <td>558.48	</td>
                                    </tr>
                                    <tr>
                                        <td>13	</td>
                                        <td>2	</td>
                                        <td>849</td>
                                        <td>368	</td>
                                        <td>558.42	</td>
                                        <td>558.42	</td>
                                    </tr>
                                    <tr>
                                        <td>16	</td>
                                        <td>3	</td>
                                        <td>852	</td>
                                        <td>365	</td>
                                        <td>558.43	</td>
                                        <td>558.43</td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                        <div class="note2">
                            <br/>
                            - Gaze data: text file of your data.
                            <br/>
                            - Media file: your stimulus, e.g a image (compulsory).
                            <br/>
                            - Upload and Render: click here to start uploading and rendering.
                            <br/>
                            - Result: a downloadable link file returned.                        
                        </div>      
                    </div>                    
                    
                </div>

            </div>
            <div id='footer'>
                Copyright © 2013 <a href='http://cs.joensuu.fi/pages/int/'> Interactive Technologies Research Group, School of Computing, University of Eastern Finland</a>
            </div>
        </div>

        <script type="text/javascript" src="js/page.js"></script>
    </body>
</html>
