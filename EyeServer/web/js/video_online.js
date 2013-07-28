// Constants
var REFRESH_ONLINE = parseInt(GetURLParameter('refresh')); //ms
console.log("Refresh online: " + REFRESH_ONLINE);

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
    player.playVideo();
}

/**
 * Get online heatmap
 * @returns {undefined}
 */
function getOnlineHeatmap() {
    $("#youtube_time").html(player.getCurrentTime());
    var currenTime = player.getCurrentTime().toFixed(1);
    var roundTime = parseInt(currenTime * 1000 / REFRESH_YOUTUBE) + 1;
    var data = "heatmapid=" + HEATMAP_ID + "&frameid=" + roundTime;
    console.log(data);
    
    $.ajax({
        dataType: 'jsonp',
        data: data,
        url: "GetImageVideo",
        success: function(result) {
            $("#send_status").html("Getting checking successfully");
            console.log("Ok checking: " + result);
            if (parseInt(result) === 1) {
                var currentTime = new Date().getTime();
                countHeatmap++;
                totalDelayTime += currentTime - baseTimeHeatmap[roundTime];
                console.log(countHeatmap + " - " + totalDelayTime + " - " + totalDelayTime / countHeatmap);

                $("#slide").append('<div class="block"> <a id="img_' + roundTime + '" name="' +
                        roundTime + '.png" href="javascript:void(0);"><img src="upload/' + HEATMAP_ID + '/' + roundTime + '.png" alt="' + roundTime +
                        '"></a> <div class="text">' + (roundTime * REFRESH_ONLINE) + ' ms</div></div>');
                $("#img_" + roundTime).click(function() {
                    $("#overlay_heatmap").fadeOut("slow").attr("src", "upload/" + HEATMAP_ID + '/' + $(this).attr('name')).fadeIn("slow");
                });
                $("#overlay_heatmap").fadeOut("fast").attr("src", "upload/" + HEATMAP_ID + '/' + roundTime + '.png').fadeIn("fast");
                if (player.getPlayerState === YT.PlayerState.PAUSED) {
                    player.playVideo();
                }
            } else {
                player.pauseVideo();
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            $("#send_status").html("Failed: " + errorThrown);
            console.log("Fail to get checking of images");
            player.pauseVideo();
        }
    });

    if (countHeatmap === EXPERIMENT_TIME / REFRESH_ONLINE) {
        clearInterval(getOnlineHeatmapVariable);
        console.log(countHeatmap + " - " + totalDelayTime + " - " + totalDelayTime / countHeatmap);
        $("#result_status").html(countHeatmap + " - " + totalDelayTime + " - " + totalDelayTime / countHeatmap);
        player.stopVideo();
    }
}

// Move to center position
if (documentWidth() > $("#web_page").width()) {
    $("#web_page").css("left", (documentWidth() - $("#web_page").width()) / 2);
    $("#info").css("left", (documentWidth() - $("#web_page").width()) / 2);
    $("#overlay_heatmap").css("left", (documentWidth() - $("#web_page").width()) / 2);
    $("#control").css("left", (documentWidth() - $("#web_page").width()) / 2);
    $("#slide").css("left", (documentWidth() - $("#web_page").width()) / 2);
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
$("#info").click(function() {
    $(".info_div").toggle();
});
$("#overlay_heatmap").hide();
$("#startBtn").click(function() {
    $("#control").hide();
    $("#overlay_heatmap").show();
    startGet();
});

$("#web_page").attr("src", WEBSITE);
