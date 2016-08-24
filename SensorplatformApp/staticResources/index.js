var exampleConfig = [{
	"name" : "PolarH7",
	"bdaddress" : "00:22:D0:AA:1F:B1",
	"settings" : {}
}, {
	"name" : "CC2650",
	"bdaddress" : "A0:E6:F8:B6:37:05",
	"settings" : {
		"irtemperature" : "64",
		"humidity" : "64",
		"ambientlight" : "50",
		"pressure" : "64",
		"movement" : "64"
	}
}];

window.setInterval(function() {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {
			response = JSON.parse(request.responseText);
			if (response["status"]) {
				$("#status").text("(recording)");
			} else {
				$("#status").text("(not recording)");
			}
			$("#device").text(response["sensorplatform"]);
			$("#uploader").text(response["uploader"]);
		}
		if (request.readyState == 4 && request.status == 500) {
			$("#status").text("(server error)");
			$("#device").text("(server error)");
			$("#uploader").text("(server error)");
		}
	};
	request.open("GET", "info", true);
	request.send();
}, 3000);

function writeExampleConfig() {
	$("#configview").val(JSON.stringify(exampleConfig, undefined, 4));
	$("#uptime").val(10);
}

function startRecording() {
	var uptime = $("#uptime").val();
	var configuration = JSON.parse($("#configview").val());
	var requestentity = {
		"uptime" : uptime,
		"configuration" : configuration
	};

	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {
			console.log("Recording now!");
		}
	};
	request.open("POST", "controller/start", true);
	request.setRequestHeader("Content-Type", "application/json;");
	request.send(JSON.stringify(requestentity));
}

function stopRecording() {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {
			console.log("Stopping recording!");
		}
	};
	request.open("GET", "controller/stop", true);
	request.send();
}

function loadHrs() {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {
			$("#dbcontentview").val(JSON.stringify(JSON.parse(request.responseText)));
		}
	};
	request.open("GET", "hrs", true);
	request.send();
}

function getNumbersHrs() {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {
			response = JSON.parse(request.responseText);
			$("#totalhrs").text(response["total"]);
			$("#nottransmitted").text(response["nottransmitted"]);
		}
	};
	request.open("GET", "hrs/numberofnottransmitted", true);
	request.send();
}
