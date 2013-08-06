// Constants
/**
 * Reference report about refresh rate
 * @type Number
 */
var REFRESH_RATE = parseInt(GetURLParameter('refresh')); //ms
console.log("Refresh rate: " + REFRESH_RATE);
/**
 * Participants
 * @type Number
 */
var NUMBER_PARTICIPANT = parseInt(GetURLParameter('np'));
console.log("Number of participants: " + NUMBER_PARTICIPANT);

/**
 * Experiment time
 * @type Number
 */
var EXPERIMENT_TIME = parseInt(GetURLParameter('time'));
console.log("Experiment time: " + EXPERIMENT_TIME);

/**
 * Website name
 * @type type
 */
var WEBSITE = GetURLParameter('site');
console.log("Website: " + WEBSITE);

/**
 * Generating ID
 * @type type
 */
var HEATMAP_ID = parseInt(GetURLParameter('id'));
console.log("Heatmap ID: " + HEATMAP_ID);

/**
 * Sample rate
 * 
 * @type Number
 */
var SAMPLE_RATE = 120;
/**
 * Duration of each data
 * @type int
 */
var DURATION = parseInt(1000 / 120);
/**
 * When number of packets reaches to this number, data is sent
 * @type int
 */
var PACKET_NUMBER = parseInt(REFRESH_RATE / DURATION);

var PARAMETER_SPLIT = ",";
/**
 * Number of part is divided in cloud cluster, normally it is set to 1
 * @type Number
 */
var NUMBER_PART = 1;

/**
 * Type of data
 * 1: send normal packets
 * 2: send the last packet
 * 
 * @type Number
 */
var TYPE = 1;

/**
 * Control error for each screen in aspect of x,y axis
 * @type Number
 */
var ERROR = 7;
var EYE_ERROR = 0;

// Variables
var sendData = "";
var count = 0;
var frameWidth = $("#web_page").width();
var frameHeight = $("#web_page").height();
var sentCount = 0;
var totalDelay = 0;

/**
 * Screen Width
 * 
 * @returns
 */
function screenWidth() {
    return screen.width;
}

/**
 * Screen Height
 * 
 * @returns
 */
function screenHeight() {
    return screen.height;
}

/**
 * Document Width
 * 
 * @returns
 */
function documentWidth() {
    var e = window, a = 'inner';
    if (!('innerWidth' in window)) {
        a = 'client';
        e = document.documentElement || document.body;
    }
    return e[a + 'Width'];
}

/**
 * Document Height
 * 
 * @returns
 */
function documentHeight() {
    var e = window, a = 'inner';
    if (!('innerWidth' in window)) {
        a = 'client';
        e = document.documentElement || document.body;
    }
    return e[a + 'Height'];
}

/**
 * Window width
 * 
 * @returns
 */
function windowWidth() {
    return window.outerWidth;
}

/**
 * Window height
 * 
 * @returns
 */
function windowHeight() {
    return window.outerHeight;
}

/**
 * Window position left
 * 
 * @returns
 */
function posLeft() {
    return window.screenX;
}

/**
 * Window position top
 * 
 * @returns
 */
function posTop() {
    return window.screenY;
}

/**
 * Window on resizing
 * 
 */
window.onresize = function() {
    $("#window_size").html(windowWidth() + " - " + windowHeight());
    $("#document_size").html(documentWidth() + " - " + documentHeight());
};

/**
 * Update position of window
 * 
 */
function updatePosition() {
    $("#window_position").html(posLeft() + " - " + posTop());
    $("#web_position").html(
            $("#web_page").position().left + " - " + $("#web_page").position().top
            + " - " + $("#web_page").width());
}

/**
 * convert x,y coordinate in screen to coordinate in frame window
 * 
 * @param {type} x
 * @param {type} y
 * @returns {Number|String|int}
 */
function convertData(x, y) {
    var newX = x;
    var newY = y;

    // Position in window
    newX = newX - posLeft();
    newY = newY - posTop();

    // Position in document
    newX = newX - (windowWidth() - documentWidth());
    newY = newY - (windowHeight() - documentHeight());

    // Position in iFrame
    newX = newX - $("#web_page").position().left;
    newY = newY - $("#web_page").position().top;

    // Error
    if (windowWidth() - documentWidth() !== 0) {
        newX = newX + ERROR;
        newY = newY + ERROR;
    }

    // Eye tracker error
    newY = newY - EYE_ERROR;

    // Check whether this coordinate belongs to frame window or not
    if (newX < 0 || newY < 0 || newX > frameWidth || newY > frameHeight) {
        newX = -1;
        newY = -1;
        return "-1";
    }

    // Scroll is implemented here
    //newY = newY + window.scrollY;

    // For Youtube data when buffering or stopped, but not used now
    //if (typeof YOUTUBE_STATE !== 'undefined' && YOUTUBE_STATE === 0) {
    //    return "-1";
    //}

    return newX + PARAMETER_SPLIT + newY + PARAMETER_SPLIT + DURATION + PARAMETER_SPLIT;
}

var startTime;
var sendToServerVariable = null;
var checkSendDone;
var checkStop;
function startTracking() {
    player.playVideo();
    startTime = new Date().getTime();
    checkSendDone = 0;
    checkStop = 0;
}

function sendToServer() {
    // Set type of data
    TYPE = 1;

    if (typeof YOUTUBE_STATE !== 'undefined' && YOUTUBE_STATE === 1) {
        var currenTime = player.getCurrentTime().toFixed(1);
        $("#youtube_time").html(currenTime);
        var roundTime = parseInt(currenTime * 1000 / REFRESH_YOUTUBE) + 1;
        console.log(currenTime + " - " + roundTime);
        sendData = sendData + HEATMAP_ID + PARAMETER_SPLIT;
        sendData = sendData + frameWidth + PARAMETER_SPLIT + frameHeight + PARAMETER_SPLIT + roundTime;
        sendData = sendData + PARAMETER_SPLIT + NUMBER_PARTICIPANT + PARAMETER_SPLIT + TYPE;
        
        // Check experiment time, must be multiplied by 1000
        if (currenTime*1000 > EXPERIMENT_TIME) {
            // Set the last packet
            TYPE = 2;
            clearInterval(sendToServerVariable);
            $("#disable").css("opacity", "0.95");
            $("#thankBtn").show();
            $("#offlineBtn").show();
            player.stopVideo();
            ETUDPlugin.stop();
            checkStop = 1;
        }
    } 
    
    sendData = "data=" + sendData;
    //console.log(sendData);
    
    var start = new Date().getTime();
    checkSendDone++;
    $.ajax({
        dataType: 'jsonp',
        data: sendData,
        url: "DRPC",
        success: function(result) {
            $("#send_status").html(result);
            console.log("Ok sending: " + result);
            var end = new Date().getTime();
            checkSendDone--;
            sentCount++;
            console.log("Start: " + start);
            console.log("End: " + end);
            console.log(end - start);
            totalDelay += (end - start);
            console.log(totalDelay + " - " + sentCount + " - " + parseFloat(totalDelay / sentCount));
            if (checkStop === 1 && checkSendDone === 0) {
                $("#thankBtn").val("Delay: " + parseFloat(totalDelay / sentCount) + " Thank you again!");
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            $("#send_status").html("Failed: " + errorThrown);
            console.log("Fail to send");
            checkSendDone--;
            if (checkStop === 1 && checkSendDone === 0) {
                $("#thankBtn").val("Delay: " + parseFloat(totalDelay / sentCount) + " ^_^ Done, Thank you again!");
            }
        }
    });
    
    count = 0;
    sendData = "";
}

/**
 * Add data to send string, if number of small data is bigger than Packet number, data is sent
 * 
 * @param {type} s
 * @returns {undefined}
 */
function addData(s) {
    sendData = sendData + s;
    //count = count + 1;
    //console.log(count);
}

function GetURLParameter(sParam) {
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] === sParam) {
            return sParameterName[1];
        }
    }
}

/**
 * Mouse position
 * 
 */
window.onmousemove = function(event) {
    event = event || window.event;
    $("#mouse_position").html(event.clientX + " - " + event.clientY);
};

/**
 * Call each 2 seconds
 * 
 */
setInterval(updatePosition, 2000);

// Move to center position
if (documentWidth() > $("#web_page").width()) {
    $("#web_page").css("left", (documentWidth() - $("#web_page").width()) / 2);
    $("#info").css("left", (documentWidth() - $("#web_page").width()) / 2);
    $("#disable").css("left", (documentWidth() - $("#web_page").width()) / 2);
    $("#overlay").css("left", (documentWidth() - $("#web_page").width()) / 2);
}

// Page setup


// Update div elements
$("#screen_size").html(screenWidth() + " - " + screenHeight());
$("#window_size").html(windowWidth() + " - " + windowHeight());
$("#document_size").html(documentWidth() + " - " + documentHeight());
$("#frame_size").html(frameWidth + " - " + frameHeight);
$("#window_position").html(posLeft() + " - " + posTop());
$("#web_position").html(
        $("#web_page").position().left + " - " + $("#web_page").position().top + " - "
        + $("#web_page").width());
$(".info_div").hide();
$("#info").click(function() {
    $(".info_div").toggle();
    if (!$('.info_div').is(':visible')) {
        $("#etudPlugin_panel").css("opacity", "0");
    } else {
        $("#etudPlugin_panel").css("opacity", "1");
    }
});
$("#thankBtn").hide();
$("#offlineBtn").hide();
$("#offlineBtn").click(function(){
    var refresh = REFRESH_RATE;
    var np = NUMBER_PARTICIPANT;
    var time = EXPERIMENT_TIME;
    var site = WEBSITE;
    var id = HEATMAP_ID;

    var data = "static_view.html?refresh=" + refresh + "&np=" + np + "&time=" + time + "&site=" + site + "&id=" + id;
    window.open(data);    
});
$("#web_page").attr("src", WEBSITE);