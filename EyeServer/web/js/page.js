/* 
 * Common js is put here
 */

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

// Move to center position
if (documentWidth() > $("#web_page").width()){
    $("#web_page").css("left", (documentWidth()-$("#web_page").width())/2);
    $("#control").css("left", (documentWidth()-$("#web_page").width())/2);
}
