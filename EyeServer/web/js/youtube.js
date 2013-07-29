/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Refresh youtube when playing
 * @type type
 */
var REFRESH_YOUTUBE = parseInt(GetURLParameter('refresh')); //ms
console.log("Refresh Youtube: " + REFRESH_YOUTUBE);

/**
 * Check Youtube state is playing or stopping
 * 
 * @type Number
 */
var YOUTUBE_STATE = 0;
var UPDATE_FUNCTION_CALL = 0;

/**
 * Youtube setup
 */
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
        YOUTUBE_STATE = 1;
        if (UPDATE_FUNCTION_CALL === 0){
            sendToServerVariable = setInterval(sendToServer, REFRESH_RATE);
            UPDATE_FUNCTION_CALL = 1;
        }
    }else{
        YOUTUBE_STATE = 0;
    }
}

/**
 * When video is stopped
 * 
 * @returns {undefined}
 */
function stopVideo() {
    player.stopVideo();
}

/**
 * Update Youtube status
 * 
 * @returns {undefined}
 */
function updateYoutubeTime(){
    if (YOUTUBE_STATE === 1){
        var currenTime = player.getCurrentTime().toFixed(1);
        var round = parseInt(currenTime*1000/REFRESH_YOUTUBE) + 1;
        $("#youtube_time").html(currenTime);
        console.log(currenTime + " - " + round);
    }
}

