/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var REFRESH_YOUTUBE = parseInt(GetURLParameter('refresh')); //ms
console.log("Refresh Youtube: " + REFRESH_YOUTUBE);

var UPDATE_FUNCTION_CALL = 0;

// 2. This code loads the IFrame Player API code asynchronously.
var tag = document.createElement('script');

tag.src = "https://www.youtube.com/iframe_api";
var firstScriptTag = document.getElementsByTagName('script')[0];
firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

// 3. This function creates an <iframe> (and YouTube player)
//    after the API code downloads.
var player;
function onYouTubeIframeAPIReady() {
    player = new YT.Player('web_page', {
        height: '1100',
        width: '500',
        videoId: WEBSITE,
        events: {
            'onStateChange': onPlayerStateChange
        }
    });
}

// 4. The API will call this function when the video player is ready.
function onPlayerReady(event) {
    event.target.playVideo();
}

// 5. The API calls this function when the player's state changes.
//    The function indicates that when playing a video (state=1),
//    the player should play for six seconds and then stop.
var done = false;
function onPlayerStateChange(event) {
    if (event.data === YT.PlayerState.PLAYING) {
        if (UPDATE_FUNCTION_CALL === 0) {
            startTime = new Date().getTime();
            for (var i = 0; i < EXPERIMENT_TIME / REFRESH_ONLINE; i++) {
                checkHeatmap[i] = 0;
                baseTimeHeatmap[i] = startTime + (i + 1) * REFRESH_ONLINE;
            }
            getOnlineHeatmapVariable = setInterval(getOnlineHeatmap, REFRESH_ONLINE);
            UPDATE_FUNCTION_CALL = 1;
        }
    }
}
function stopVideo() {
    player.stopVideo();
}

// Variables
var sentOnline = 0;
var totalOnline = 0;

/**
 * This function is temporarily used
 * @returns {undefined}
 */
function updateYoutubeTime() {
    $("#youtube_time").html(player.getCurrentTime());
    var start = new Date().getTime();
    var currenTime = player.getCurrentTime().toFixed(1);
    var roundTime = parseInt(currenTime * 1000 / REFRESH_YOUTUBE) + 1;
    $("#overlay").fadeOut("fast").attr("src", "upload/" + roundTime + ".png").fadeIn("fast");
    var end = new Date().getTime();
    sentOnline++;
    console.log("Start: " + start);
    console.log("End: " + end);
    console.log(end - start);
    console.log("upload/" + roundTime + ".png");
    totalOnline += (end - start);
    console.log(totalOnline + " - " + sentOnline + " - " + parseFloat(totalOnline / sentOnline));
}


