/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
        videoId: 'K0-ucWKiTps',
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
    }
}
function stopVideo() {
    player.stopVideo();
}

function updateYoutubeTime(){
    $("#youtube_time").html(player.getCurrentTime());
}

setInterval(updateYoutubeTime, 500);


