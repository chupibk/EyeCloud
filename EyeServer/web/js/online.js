// Constants
var REFRESH_ONLINE = 1000;

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
                $("#overlay").fadeOut("slow").attr("src", "data:image/png;base64," + result).fadeIn("slow");
                
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