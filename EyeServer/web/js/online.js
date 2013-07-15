// Constants
var REFRESH_ONLINE = parseInt(GetURLParameter('refresh')); //ms
console.log("Refresh online: " + REFRESH_ONLINE);

// Variables
var sentOnline = 0;
var totalOnline = 0;

var auto_refresh = setInterval(
        function() {
    
        var start = new Date().getTime();
        $.ajax({
            dataType: 'jsonp',
            data: "",
            url: "GetImage",
            success: function(result) {
                $("#send_status").html("Ok");
                $("#overlay").fadeOut("slow").attr("src", "upload/" + result).fadeIn("slow");
                
                console.log("Ok sending");
                var end = new Date().getTime();
                sentOnline++;
                console.log("Start: " + start);
                console.log("End: " + end);
                console.log(end - start);
                totalOnline += (end - start);
                console.log(totalOnline + " - " + sentOnline + " - " + parseFloat(totalOnline/sentOnline));
            },
            error: function(jqXHR, textStatus, errorThrown) {
                $("#send_status").html("Failed: " + errorThrown);
                console.log("Fail to send");
            }
        });            
            
        }, REFRESH_ONLINE
);
    
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