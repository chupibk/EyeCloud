/* 
 * Common js is put here
 */

/* Index JS */
$("#clientBtn-s").click(function() {
    var refresh = $('select[name="refresh-s"]').val();
    var np = $('select[name="np-s"]').val();
    var time = $('select[name="time-s"]').val();
    var site = $('input[name="site-s"]').val();
    var id = $('select[name="id-s"]').val();

    var data = "heatmap.html?refresh=" + refresh + "&np=" + np + "&time=" + time + "&site=" + site + "&id=" + id;
    window.open(data);
});

$("#onlineBtn-s").click(function() {
    var refresh = $('select[name="refresh-s"]').val();
    var np = $('select[name="np-s"]').val();
    var time = $('select[name="time-s"]').val();
    var site = $('input[name="site-s"]').val();
    var id = $('select[name="id-s"]').val();

    var data = "online.html?refresh=" + refresh + "&np=" + np + "&time=" + time + "&site=" + site + "&id=" + id;
    window.open(data);
});

$("#offlineBtn-s").click(function() {
    var refresh = $('select[name="refresh-s"]').val();
    var np = $('select[name="np-s"]').val();
    var time = $('select[name="time-s"]').val();
    var site = $('input[name="site-s"]').val();
    var id = $('select[name="off-s"]').val();

    var data = "static_view.html?refresh=" + refresh + "&np=" + np + "&time=" + time + "&site=" + site + "&id=" + id;
    window.open(data);
});

$("#clientBtn-v").click(function() {
    var refresh = $('select[name="refresh-v"]').val();
    var np = $('select[name="np-v"]').val();
    var time = $('select[name="time-v"]').val();
    var site = $('input[name="site-v"]').val();
    var id = $('select[name="id-v"]').val();

    var data = "youtube.html?refresh=" + refresh + "&np=" + np + "&time=" + time + "&site=" + site + "&id=" + id;
    window.open(data);
});

$("#onlineBtn-v").click(function() {
    var refresh = $('select[name="refresh-v"]').val();
    var np = $('select[name="np-v"]').val();
    var time = $('select[name="time-v"]').val();
    var site = $('input[name="site-v"]').val();
    var id = $('select[name="id-v"]').val();

    var data = "youtube_online.html?refresh=" + refresh + "&np=" + np + "&time=" + time + "&site=" + site + "&id=" + id;
    window.open(data);
});

$("#offlineBtn-v").click(function() {
    var refresh = $('select[name="refresh-v"]').val();
    var np = $('select[name="np-v"]').val();
    var time = $('select[name="time-v"]').val();
    var site = $('input[name="site-v"]').val();
    var id = $('select[name="off-v"]').val();

    var data = "youtube_online.html?refresh=" + refresh + "&np=" + np + "&time=" + time + "&site=" + site + "&id=" + id;
    window.open(data);
});

// Set max ID
$.ajax({
    dataType: 'jsonp',
    data: "",
    url: "GetAvailableID",
    success: function(result) {
        var start = parseInt(result) + 1;
        $('select[name="id-s"]').append('<option value="' + start +'">' + start + '</option>');
        $('select[name="id-v"]').append('<option value="' + (start+1) +'">' + (start+1) + '</option>');

        for (var i=start - 1; i > 0; i--){
            $('select[name="off-s"]').append('<option value="' + i +'">' + i + '</option>');
            $('select[name="off-v"]').append('<option value="' + i +'">' + i + '</option>');
        }
    },
    error: function(jqXHR, textStatus, errorThrown) {
        alert("Error, please refresh!!!");
    }
});

/* View JS */
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
if (documentWidth() > $("#web_page").width()) {
    $("#web_page").css("left", (documentWidth() - $("#web_page").width()) / 2);
    $("#control").css("left", (documentWidth() - $("#web_page").width()) / 2);
}
