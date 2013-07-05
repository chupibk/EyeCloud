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
	if (windowWidth() - documentWidth() != 0) {
		newX = newX + 7;
		newY = newY + 7;
	}

	var frameWidth = $("#web_page").width();
	var frameHeight = documentHeight() - $("#wrap").position().top;

	if (newX < 0 || newY < 0 || newX > frameWidth || newY > frameHeight) {
		newX = -1;
		newY = -1;
	}

	// Scroll
	//newY = newY + window.scrollY;

	return newX + "," + newY;
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

// RPC
$.ajax({
    dataType: 'jsonp',
    data: "",
    url: "http://localhost:8080/EyeServer/DRPC",
    success: function(result) {
    	alert(result);
    },
    error: function(jqXHR, textStatus, errorThrown) {
        alert("error " + errorThrown);
    }
});



