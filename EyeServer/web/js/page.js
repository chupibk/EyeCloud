/* 
 * Common js is put here
 */

/* Index JS */

/**
 * All functions of Multi-sourcre gaze visualization
 */
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
    $("#offlineDlg-s").dialog("open");
});

$("#viewBtn-s").click(function() {
    var refresh = $('select[name="refresh-s"]').val();
    var np = $('select[name="np-s"]').val();
    var time = $('select[name="time-s"]').val();
    var site = $('input[name="site-s"]').val();
    var id = $('select[name="off-s"]').val();

    var data = "static_view.html?refresh=" + refresh + "&np=" + np + "&time=" + time + "&site=" + site + "&id=" + id;
    window.open(data);
    $("#offlineDlg-s").dialog("close");
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
    $("#offlineDlg-v").dialog("open");
});

$("#viewBtn-v").click(function() {
    var refresh = $('select[name="refresh-v"]').val();
    var np = $('select[name="np-v"]').val();
    var time = $('select[name="time-v"]').val();
    var site = $('input[name="site-v"]').val();
    var id = $('select[name="off-v"]').val();

    var data = "youtube_online.html?refresh=" + refresh + "&np=" + np + "&time=" + time + "&site=" + site + "&id=" + id;
    window.open(data);
    $("#offlineDlg-v").dialog("close");
});

// Set max ID
$.ajax({
    dataType: 'jsonp',
    data: "",
    url: "GetAvailableID",
    success: function(result) {
        var start = parseInt(result) + 1;
        $('select[name="id-s"]').append('<option value="' + start + '">' + start + '</option>');
        $('select[name="id-v"]').append('<option value="' + (start + 1) + '">' + (start + 1) + '</option>');

        for (var i = start - 1; i > 0; i--) {
            $('select[name="off-s"]').append('<option value="' + i + '">' + i + '</option>');
            $('select[name="off-v"]').append('<option value="' + i + '">' + i + '</option>');
        }
    },
    error: function(jqXHR, textStatus, errorThrown) {
        alert("Error, please refresh!!!");
    }
});

$("#offlineDlg-s").dialog({
    autoOpen: false
});

$("#offlineDlg-v").dialog({
    autoOpen: false
});

/**
 * All function of fixation computation
 */

$("#fixationBtn").click(function(){
    $("#fixation-status").text("Uploading... wait for a while");
});

$('#fixationForm').ajaxForm(function(result) {
    $("#fixation-status").text(result["message"]);
    if (result["OK"] === "1"){
        $("#fixation-return").attr("href", result["path"]);
        $("#fixation-return").text(result["filename"]);
        $("#fixation-status").text(result["message"] + " Running time: " + result["time"] + "ms");
    }
}); 

/**
 * All function of offline heatmap
 */

$("#heatmapBtn").click(function(){
    $("#heatmap-status").text("Uploading... wait for a while");
});

$('#heatmapForm').ajaxForm(function(result) {
    $("#heatmap-status").text(result["message"]);
    if (result["OK"] === "1"){
        $("#heatmap-return").attr("href", result["path"]);
        $("#heatmap-return").text(result["filename"]);
        $("#heatmap-status").text(result["message"] + " Running time: " + result["time"] + "ms");
    }
}); 