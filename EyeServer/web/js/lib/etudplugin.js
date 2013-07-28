/*
 * This file is modified based on ETUD Plugin (Oleg Špakov) example: http://www.sis.uta.fi/~csolsp/downloads.php
 */
if(this.ETUDPlugin === undefined)
{

var ETUDPlugin = null;
var ETUDPlugin_dataHandler = null;
var ETUDPlugin_trackingTriggerHandler = null;

function ETUDPlugin_addEvent(obj, name, func)
{
	if(!obj)
		return;
	
	if(window.addEventListener)
		obj.addEventListener(name, func, false);
	else
		obj.attachEvent("on"+name, func);
}

function ETUDPlugin_isMSIE()
{
	return navigator.userAgent.indexOf('MSIE') != -1;
}

function ETUDPlugin_isInstalled()
{
	var result = false;
	
	if (ETUDPlugin_isMSIE()) {
		for (var i = 0; i < document.applets.length; i++) {
			if(document.applets[i].type == "application/x-etudplugin" &&
				 document.applets[i].object != null) {
				result = true;
				break;
			}
		}
  } else {
		for (var i = 0; i < navigator.plugins.length; i++) {
			if(navigator.plugins[i].name == "ETUDPlugin") {
				result = true;
				break;
			}
		}
	}
	return result;
}

function ETUDPlugin_init()
{
	var controlPanel = document.createElement("div");
	controlPanel.id = "etudPlugin_panel";
	with(controlPanel.style)
	{
		position = "fixed";
		top = "0";
		left = "0";
		right = "0";
		borderBottom = "2px solid black";
		backgroundColor = "#CCCCCC";
		zIndex = 2147483647;
	}
	
	var etudPluginContainer = document.createElement("span");
	etudPluginContainer.innerHTML = "<object id='etudPlugin' type='application/x-etudplugin' width='1' height='1'><param name='onload' value='ETUDPlugin_loaded' /></object>";
		
	var etudPluginInfo = document.getElementsByClassName("logo")[0];
        etudPluginInfo.id = "etudPlugin_logo";
        /*
	with(etudPluginInfo.style)
	{
		paddingLeft = "20px";
		color = "#600000";
		fontWeight = "bold";
		fontFamily = "Arial";
	}
	*/
	var etudPluginDevice = document.getElementsByClassName("device")[0];
	etudPluginDevice.id = "etudPlugin_device";
        /*
	with(etudPluginDevice.style)
	{
		padding = "0px 20px 0px 20px";
		color = "#600000";
		fontFamily = "Arial";
	}
	*/
	var btnShowOptions = document.getElementsByName("option")[0];
	btnShowOptions.id = "etudPlugin_showOptions";
	btnShowOptions.type = "button";
	btnShowOptions.value = "Options...";
	btnShowOptions.disabled = true;
	ETUDPlugin_addEvent(btnShowOptions, "click", ETUDPlugin_showOptions);

	var btnCalibrate = document.getElementsByName("calibrate")[0];
	btnCalibrate.id = "etudPlugin_calibrate";
	btnCalibrate.type = "button";
	btnCalibrate.value = "Calibrate";
	btnCalibrate.disabled = true;
	ETUDPlugin_addEvent(btnCalibrate, "click", ETUDPlugin_calibrate);

	var btnStartStop = document.getElementsByName("start")[0];
	btnStartStop.id = "etudPlugin_startStop";
	btnStartStop.type = "button";
	btnStartStop.value = "Start";
	btnStartStop.disabled = true;
	ETUDPlugin_addEvent(btnStartStop, "click", ETUDPlugin_startStop);
	
	var log = document.createElement("span");
	log.id = "etudPlugin_log";
	
	controlPanel.appendChild(etudPluginContainer);
	//controlPanel.appendChild(etudPluginInfo);
	//controlPanel.appendChild(etudPluginDevice);
	//controlPanel.appendChild(btnShowOptions);
	//controlPanel.appendChild(btnCalibrate);
	//controlPanel.appendChild(btnStartStop);
	controlPanel.appendChild(log);

	document.body.insertBefore(controlPanel, document.body.firstChild);
	
	etudPluginInfo.innerHTML = ETUDPlugin_isInstalled() ? "" : "<a href='files/ETUDPlugin.exe'>ETUDPlugin</a> is not installed";
	etudPluginDevice.innerHTML = ETUDPlugin_isInstalled() ? "<span style='font: 8pt monospace; color: #666'>loading...</span>" : "";
	if(!ETUDPlugin_isInstalled()) {
		log.style.display = "none";
	}
        
        $("#etudPlugin_panel").css("opacity", "0");
}
		
function ETUDPlugin_free()
{
	ETUDPlugin = null;
}

function ETUDPlugin_showOptions()
{
	if(ETUDPlugin) 
	{
		ETUDPlugin.showOptions();
		document.getElementById("etudPlugin_device").innerHTML = "(" + ETUDPlugin.Device + ")";
	}
}
		
function ETUDPlugin_calibrate()
{
	if(ETUDPlugin && ETUDPlugin.Ready) ETUDPlugin.calibrate();
}

/**
 * Called when start button is clicked
 * @returns {undefined}
 */
function ETUDPlugin_startStop()
{
	if(ETUDPlugin && ETUDPlugin.Calibrated)
	{
		ETUDPlugin.Tracking ? ETUDPlugin.stop() : ETUDPlugin.start();
	}
        
        $("#overlay").hide();
        startTracking();
}
		
function ETUDPlugin_loaded() 
{
	if(ETUDPlugin)
		return;
	
	ETUDPlugin = document.getElementById("etudPlugin");
            
	if(ETUDPlugin && ETUDPlugin.valid)
	{
		document.body.style.margin = "0px";
		if(!ETUDPlugin_isMSIE()) {
			document.body.style.paddingTop = document.getElementById("etudPlugin_panel").offsetHeight + "px";
		}
		ETUDPlugin_addEvent(ETUDPlugin, "state", ETUDPlugin_state);
		ETUDPlugin_addEvent(ETUDPlugin, "data", ETUDPlugin_data);
	}
	else
	{
		if(!ETUDPlugin || ETUDPlugin.Device === undefined)
			document.getElementById("etudPlugin_logo").innerHTML = "<a href='files/ETUDPlugin.exe'>ETUDPlugin</a> is not intalled";
		else
			document.getElementById("etudPlugin_logo").innerHTML = "ETUDPlugin: <a href='files/ETUDriver.exe'>ETU-Driver</a> is not intalled";
		document.getElementById("etudPlugin_panel").style.backgroundColor = "#FFE0E0";
	}

	ETUDPlugin_updateControlPanel(-1);
}
        
function ETUDPlugin_state(state)
{
	ETUDPlugin_updateControlPanel(state);
	if(ETUDPlugin_trackingTriggerHandler && (state == 1 || state == 2))
	{
		ETUDPlugin_trackingTriggerHandler(state == 2);
	}
}
		
function ETUDPlugin_data(timestamp, x, y)
{
	if(ETUDPlugin_dataHandler)
		ETUDPlugin_dataHandler(timestamp, x, y);
	else{
                // Convert data here
		var result = convertData(x,y);
		document.getElementById("etudPlugin_log").innerHTML = timestamp + " --- " + x + ", " + y + " --- " + result;
                // Add to send string if returned result is not -1
                if (result != "-1")
                   addData(result);
        }
}

function ETUDPlugin_updateControlPanel(state)
{
	var allInstalled = ETUDPlugin && ETUDPlugin.valid;
	document.getElementById("etudPlugin_device").innerHTML = allInstalled ? "(" + ETUDPlugin.Device + ")" : "";
	document.getElementById("etudPlugin_showOptions").disabled = !allInstalled || ETUDPlugin.Tracking;
	document.getElementById("etudPlugin_calibrate").disabled = !allInstalled || !ETUDPlugin.Ready || ETUDPlugin.Tracking;
	document.getElementById("etudPlugin_startStop").disabled = !allInstalled || !ETUDPlugin.Calibrated;
	document.getElementById("etudPlugin_startStop").value = state == 2 ? "Stop" : "Start";
}

ETUDPlugin_addEvent(window, "load", ETUDPlugin_init);
ETUDPlugin_addEvent(window, "unload", ETUDPlugin_free);
}