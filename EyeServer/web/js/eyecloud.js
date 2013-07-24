// Constants
/**
 * Reference report about refresh rate
 * @type Number
 */
var REFRESH_RATE = parseInt(GetURLParameter('refresh')); //ms
console.log("Refresh rate: " + REFRESH_RATE);

var NUMBER_PARTICIPANT = 1;

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
var DURATION = parseInt(1000/120);
/**
 * When number of packets reaches to this number, data is sent
 * @type int
 */
var PACKET_NUMBER = parseInt(REFRESH_RATE/DURATION);

var PARAMETER_SPLIT = ",";
/**
 * Number of part is divided in cloud cluster, normally it is set to 1
 * @type Number
 */
var NUMBER_PART = 1;

/**
 * Type of data
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
            $("#wrap").position().left + " - " + $("#wrap").position().top
            + " - " + $("#web_page").width());
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
    newX = newX - $("#wrap").position().left;
    newY = newY - $("#wrap").position().top;

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
    
    if (typeof YOUTUBE_STATE !== 'undefined' && YOUTUBE_STATE === 0){
        return "-1";
    }
    
    return newX + PARAMETER_SPLIT + newY + PARAMETER_SPLIT + DURATION + PARAMETER_SPLIT;
}

/**
 * Add data to send string, if number of small data is bigger than Packet number, data is sent
 * 
 * @param {type} s
 * @returns {undefined}
 */
function addData(s) {
    sendData = sendData + s + s + s + s;
    count = count + 4;
    console.log(count);

    if (count >= PACKET_NUMBER) {
        if (typeof YOUTUBE_STATE !== 'undefined'){
            var currenTime = player.getCurrentTime().toFixed(1);
            var roundTime = parseInt(currenTime*1000/REFRESH_YOUTUBE) + 1;
            sendData = sendData + frameWidth + PARAMETER_SPLIT + frameHeight + PARAMETER_SPLIT + roundTime;
            sendData = sendData + PARAMETER_SPLIT + NUMBER_PARTICIPANT + PARAMETER_SPLIT + TYPE;
        }else{
            sendData = sendData + frameWidth + PARAMETER_SPLIT + frameHeight + PARAMETER_SPLIT + TYPE;
        }
        sendData = "data=" + sendData;
        console.log(sendData);
        
        var start = new Date().getTime();
        $.ajax({
            dataType: 'jsonp',
            data: sendData,
            url: "DRPC",
            success: function(result) {
                $("#send_status").html(result);
                console.log("Ok sending: " + result);
                var end = new Date().getTime();
                sentCount++;
                console.log("Start: " + start);
                console.log("End: " + end);
                console.log(end - start);
                totalDelay += (end - start);
                console.log(totalDelay + " - " + sentCount + " - " + parseFloat(totalDelay/sentCount));
            },
            error: function(jqXHR, textStatus, errorThrown) {
                $("#send_status").html("Failed: " + errorThrown);
                console.log("Fail to send");
            }
        });
        
        count = 0;
        sendData = "";
    }
}

function GetURLParameter(sParam){
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++){
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] === sParam){
            return sParameterName[1];
        }
    }
}

/**
 * Call each 2 seconds
 * 
 */
setInterval(updatePosition, 2000);

// Move to center position
console.log($("#web_page").width());
if (documentWidth() > $("#web_page").width()){
    $("#wrap").css("left", (documentWidth()-$("#web_page").width())/2);
    $("#info").css("left", (documentWidth()-$("#web_page").width())/2);
}

// Page setup


// Update div elements
$("#screen_size").html(screenWidth() + " - " + screenHeight());
$("#window_size").html(windowWidth() + " - " + windowHeight());
$("#document_size").html(documentWidth() + " - " + documentHeight());
$("#frame_size").html(frameWidth + " - " + frameHeight);
$("#window_position").html(posLeft() + " - " + posTop());
$("#web_position").html(
        $("#wrap").position().left + " - " + $("#wrap").position().top + " - "
        + $("#web_page").width());
$(".info_div").hide();
$("#info").click(function (){
    $(".info_div").toggle();
});