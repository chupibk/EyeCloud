var sendData = "";
var count = 0;
var frameWidth = $("#web_page").width();
var frameHeight = documentHeight() - $("#wrap").position().top;

// Constants
var PARAMETER_SPLIT = ",";
var NUMBER_PART = 1;
var PACKET_NUMBER = 50;
var ERROR = 7;

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

    if (newX < 0 || newY < 0 || newX > frameWidth || newY > frameHeight) {
        newX = -1;
        newY = -1;
        return "-1";
    }

    // Scroll
    //newY = newY + window.scrollY;

    return newX + PARAMETER_SPLIT + newY + PARAMETER_SPLIT + "20" + PARAMETER_SPLIT;
}

function addData(s) {
    sendData = sendData + s;
    count++;
    console.log(count);

    if (count >= PACKET_NUMBER) {
        sendData = sendData + frameWidth + PARAMETER_SPLIT + frameHeight + PARAMETER_SPLIT + NUMBER_PART;
        sendData = "data=" + sendData;
        console.log(sendData);
        
        $.ajax({
            dataType: 'jsonp',
            data: sendData,
            url: "DRPC",
            success: function(result) {
                $("#send_status").html(result);
                console.log("Ok");
            },
            error: function(jqXHR, textStatus, errorThrown) {
                $("#send_status").html("Send failed: " + errorThrown);
                console.log("Fail to send");
            }
        });
        
        count = 0;
        sendData = "";
    }
}

/**
 * Call each 2 seconds
 * 
 */
setInterval(updatePosition, 2000);

// Update div elements
$("#screen_size").html(screenWidth() + " - " + screenHeight());
$("#window_size").html(windowWidth() + " - " + windowHeight());
$("#document_size").html(documentWidth() + " - " + documentHeight());

$("#window_position").html(posLeft() + " - " + posTop());
$("#web_position").html(
        $("#wrap").position().left + " - " + $("#wrap").position().top + " - "
        + $("#web_page").width());
//$("#web_position").html(convertData(136,489));
