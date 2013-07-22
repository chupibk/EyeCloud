/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var REFRESH_YOUTUBE = parseInt(GetURLParameter('refresh')); //ms
console.log("Refresh Youtube: " + REFRESH_YOUTUBE);

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
        $("#wrap").append("<img id=\"overlay\" src=\"images/noimage.png\" />");
        setInterval(updateYoutubeTime, REFRESH_YOUTUBE);
    }
}
function stopVideo() {
    player.stopVideo();
}

// Variables
var sentOnline = 0;
var totalOnline = 0;

function updateYoutubeTime() {
    $("#youtube_time").html(player.getCurrentTime());
    var start = new Date().getTime();
    /*
    $.ajax({
        dataType: 'jsonp',
        data: "",
        url: "GetImage",
        success: function(result) {
            $("#send_status").html("Ok");
            $("#overlay").fadeOut("fast").attr("src", "upload/" + result).fadeIn("fast");

            console.log("Ok sending");
            var end = new Date().getTime();
            sentOnline++;
            console.log("Start: " + start);
            console.log("End: " + end);
            console.log(end - start);
            totalOnline += (end - start);
            console.log(totalOnline + " - " + sentOnline + " - " + parseFloat(totalOnline / sentOnline));
        },
        error: function(jqXHR, textStatus, errorThrown) {
            $("#send_status").html("Failed: " + errorThrown);
            console.log("Fail to send");
        }
    });
    */
   var currenTime = player.getCurrentTime().toFixed(1);
   var roundTime = parseInt(currenTime*1000/REFRESH_YOUTUBE) + 1;
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


