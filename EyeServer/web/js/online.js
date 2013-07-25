// Constants
var REFRESH_ONLINE = parseInt(GetURLParameter('refresh')); //ms
console.log("Refresh online: " + REFRESH_ONLINE);

/**
 * Experience time
 * @type Number
 */
var EXPERIMENT_TIME = 4000;

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

// Variables
var getOnlineHeatmapVariable = null;
var startTime;
var frameWidth = $("#web_page").width();
var frameHeight = $("#web_page").height();
var checkHeatmap = new Array();
var baseTimeHeatmap = new Array();
var totalDelayTime = 0;
var countHeatmap = 0;

/**
 * Start getting data
 * @returns {undefined}
 */
function startGet() {
    startTime = new Date().getTime();
    for (var i=0; i < EXPERIMENT_TIME/REFRESH_ONLINE; i++){
        checkHeatmap[i] = 0;
        baseTimeHeatmap[i] = startTime + (i+1)*REFRESH_ONLINE;
    }
    getOnlineHeatmapVariable = setInterval(getOnlineHeatmap, REFRESH_ONLINE);
}

/**
 * Get online heatmap
 * @returns {undefined}
 */
function getOnlineHeatmap() {
    $.ajax({
        dataType: 'jsonp',
        data: "",
        url: "GetImage",
        success: function(result) {
            $("#send_status").html("Getting list successfully");
            console.log("Getting list OK");
            var list = result[0];
            var currentTime = new Date().getTime();
            for (var i=0; i < list.length; i++){
                var id = parseInt(list[i]) - 1;
                if (checkHeatmap[id] === 0){
                    checkHeatmap[id] = 1;
                    countHeatmap++;
                    totalDelayTime += currentTime - baseTimeHeatmap[id];
                    console.log(countHeatmap + " - " + totalDelayTime + " - " + totalDelayTime/countHeatmap);
                    $("#slide").append('<img src="upload/' + (id + 1) + '.png" alt="' + (id + 1) + '">');
                }
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            $("#send_status").html("Failed: " + errorThrown);
            console.log("Fail to get list of images");
        }
    });
    
    if (countHeatmap === EXPERIMENT_TIME/REFRESH_ONLINE){
        clearInterval(getOnlineHeatmapVariable);
        console.log(countHeatmap + " - " + totalDelayTime + " - " + totalDelayTime/countHeatmap);
    }
}

// Move to center position
if (documentWidth() > $("#web_page").width()){
    $("#web_page").css("left", (documentWidth()-$("#web_page").width())/2);
    $("#info").css("left", (documentWidth()-$("#web_page").width())/2);
    $("#overlay_heatmap").css("left", (documentWidth()-$("#web_page").width())/2);
    $("#control").css("left", (documentWidth()-$("#web_page").width())/2);
    $("#slide").css("left", (documentWidth()-$("#web_page").width())/2);
}

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
$("#info").click(function (){
    $(".info_div").toggle();
});
$("#overlay_heatmap").hide();
$("#startBtn").click(function(){
    $("#control").hide();
    $("#overlay_heatmap").show();
    startGet();
});
