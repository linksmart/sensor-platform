var exampleConfig = [{
	"name" : "Luminox",
	"bdaddress" : "00:07:80:C8:45:FC",
	"settings" : {
        "id":"S01"
    }
}, {
    "name" : "Luminox",
    "bdaddress" : "00:07:80:C8:45:F4",
    "settings" : {
        "id":"S02"
    }
},
    {
        "name" : "Luminox",
        "bdaddress" : "00:07:80:C8:45:FF",
        "settings" : {
            "id":"S03"
        }
    },
    {
        "name" : "Luminox",
        "bdaddress" : "00:07:80:C8:45:F3",
        "settings" : {
            "id":"S04"
        }
    }];

var exampleConfigWLAN = [{
        "name" : "WLAN",
        "settings" : {
            "id":"S04",
            "password":"your_password"
        }
    }];

var exampleConfigDongle = [
    {
        "name" : "Dongle",
        "settings" : {
            "APN":"web.colombiamovil.com.co",
            "PIN":"S04"
        }
    }];


window.setInterval(function() {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {
			response = JSON.parse(request.responseText);
			$("#recording").text(response["recording"]);
			$("#device").text(response["sensorplatform"]);
			$("#uploader").text(response["uploader"]);
			$("#mobileinet").text(response["mobileinternet"]);
			$("#overallrssi").text(response["overallrssi"]);
			$("#rscp").text(response["rscp"]);
			$("#ecio").text(response["ecio"]);
			$("#rssi").text(response["rssi"]);
		}
		if (request.readyState == 4 && request.status == 500) {
			$("#recording").text("(server error)");
			$("#device").text("(server error)");
			$("#uploader").text("(server error)");
		}
	};
	request.open("GET", "controller/info", true);
	request.send();
}, 3000);

function writeExampleConfig() {
	$("#configview").val(JSON.stringify(exampleConfig, undefined, 4));
	$("#uptime").val(600);
	$("#firstname").val("Gustavo");
	$("#lastname").val("Aragon");
}

function writeExampleConfigWLAN() {
    $("#configWLAN").val(JSON.stringify(exampleConfigWLAN, undefined, 4));
}
function writeExampleConfigDongle() {
    $("#configDongle").val(JSON.stringify(exampleConfigDongle, undefined, 4));
}

function startRecording() {
	var uptime = $("#uptime").val();
	var firstname = $("#firstname").val();
	var lastname = $("#lastname").val();
	var uptime = $("#uptime").val();
	if (uptime == ""){
		$("#modalmessage").text("No uptime specified!");
		$('#message').modal('show');
		return;
	}
	try {
		var configuration = JSON.parse($("#configview").val());
	} catch (err) {
		$("#modalmessage").text("Bad sensor configuration JSON syntax!");
		$('#message').modal('show');
		return;
	}
	var requestentity = {
		"uptime" : uptime,
		"firstname": firstname,
		"lastname": lastname,
		"configuration" : configuration
	};

	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {
			response = JSON.parse(request.responseText);
			$("#modalmessage").text(response["result"]);
			$('#message').modal('show');
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


function startRecordingWLAN() {

    try {
        var configurationWlan = JSON.parse($("#configWLAN").val());
    } catch (err) {
        $("#modalmessage").text("Bad WLAN configuration JSON syntax!");
        $('#message').modal('show');
        return;
    }
    var requestentityWlan = {
        "configurationWlan" : configurationWlan
    };

	var request = new XMLHttpRequest();
    request.open("POST", "controller/wlan",true);
    request.setRequestHeader("Content-Type", "application/json;");
    request.send(JSON.stringify(requestentityWlan));
}

function startRecordingDongle() {

    try {
        var configuration = JSON.parse($("#configDongle").val());
    } catch (err) {
        $("#modalmessage").text("Bad WLAN configuration JSON syntax!");
        $('#message').modal('show');
        return;
    }
    var requestentity = {
        "configuration" : configuration
    };

    var request = new XMLHttpRequest();
    request.onreadystatechange = function() {
        if (request.readyState == 4 && request.status == 200) {
            response = JSON.parse(request.responseText);
            $("#modalmessage").text(response["result"]);
            $('#message').modal('show');
        }
    };
    request.open("POST", "controller/dongle", true);
    request.setRequestHeader("Content-Type", "application/json;");
    request.send(JSON.stringify(requestentity));
}

function loadHrs() {
	getNumbersHrs();
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {
			$("#dbcontentview").val(JSON.stringify(JSON.parse(request.responseText), undefined, 4));
		}
	};
	request.open("GET", "hrs", true);
	request.send();
}

function deleteAllHrs() {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {
			getNumbersHrs();
		}
	};
	request.open("GET", "hrs/delete", true);
	request.send();
}

function manualHrsUpload() {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {
			response = JSON.parse(request.responseText);
			$("#modalmessage").text(response["result"]);
			$('#message').modal('show');
			getNumbersHrs();
		}
	};
	request.open("GET", "hrs/manualupload", true);
	request.send();
}

function getNumbersHrs() {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {
			response = JSON.parse(request.responseText);
			$("#totalhrs").text(response["total"]);
			$("#nottransmitted").text(response["nottransmitted"]);
			$("#dbcontentview").val("");
		}
	};
	request.open("GET", "hrs/numberofnottransmitted", true);
	request.send();
}

function loadPos() {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {
			$("#dbcontentview").val(JSON.stringify(JSON.parse(request.responseText), undefined, 4));
		}
	};
	request.open("GET", "pos", true);
	request.send();
}

function deleteAllPos() {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {
		}
	};
	request.open("GET", "pos/delete", true);
	request.send();
}

function loadCC2650TemperatureSamples() {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {
			$("#dbcontentview").val(JSON.stringify(JSON.parse(request.responseText), undefined, 4));
		}
	};
	request.open("GET", "cc2650/temperature", true);
	request.send();
}

function loadCC2650HumiditySamples() {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {
			$("#dbcontentview").val(JSON.stringify(JSON.parse(request.responseText), undefined, 4));
		}
	};
	request.open("GET", "cc2650/humidity", true);
	request.send();
}

function loadCC2650PressureSamples() {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {
			$("#dbcontentview").val(JSON.stringify(JSON.parse(request.responseText), undefined, 4));
		}
	};
	request.open("GET", "cc2650/pressure", true);
	request.send();
}

function loadCC2650AmbientlightSamples() {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {
			$("#dbcontentview").val(JSON.stringify(JSON.parse(request.responseText), undefined, 4));
		}
	};
	request.open("GET", "cc2650/ambientlight", true);
	request.send();
}

function loadCC2650MovementSamples() {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {
			$("#dbcontentview").val(JSON.stringify(JSON.parse(request.responseText), undefined, 4));
		}
	};
	request.open("GET", "cc2650/movement", true);
	request.send();
}

function scan() {
	var duration = $("#duration").val();
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {
			$("#detected").val(JSON.stringify(JSON.parse(request.responseText), undefined, 4));
		}
	};
	request.open("GET", "controller/scan?duration=" + duration, true);
	request.send();
}
