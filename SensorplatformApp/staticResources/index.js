var exampleConfig = [{
	"name" : "PolarH7",
	"bdaddress" : "00:22:D0:AA:1F:B1",
	"addresstype" : "PUBLIC",
	"settings" : {}
}, {
	"name" : "CC2650",
	"bdaddress" : "A0:E6:F8:B6:37:05",
	"addresstype" : "PUBLIC",
	"settings" : {
		"irtemperature" : 64,
		"humidity" : 64,
		"ambientlight" : 50,
		"pressure" : 64,
		"movement" : 64
	}
}];

window.setInterval(function() {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {
			response = JSON.parse(request.responseText);
			if (response["recording"]) {
				document.getElementById('recording').textContent = "(recording)";
			} else {
				document.getElementById('recording').textContent = "(not recording)";
			}
			console.log("ping");
		}
		if (request.readyState == 4 && request.status == 500) {
			document.getElementById('recording').textContent = "(server error)";
			console.log("Server error!");
		}
	};
	request.open("GET", "info", true);
	request.send();
}, 3000);

function writeExampleConfig() {
	document.getElementById('config').value = JSON.stringify(exampleConfig, undefined, 4);
}

function startFromWebConfig() {
	var requestentity = document.getElementById('config').value;

	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {
			console.log("Running web config now!");
			console.log(request.responseText);
		}
		if (request.readyState == 4 && request.status == 500) {
			console.log("Server error!");
			console.log(request.responseText);
		}
	};
	console.log(requestentity);
	request.open("POST", "startup/fromwebapp", true);
	request.setRequestHeader("Content-Type", "application/json;");
	request.send(requestentity);
}

function startFromMavenBuild() {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {
			console.log("Running build config now!");
		}
		if (request.readyState == 4 && request.status == 500) {
			console.log("Server error!");
		}
	};
	request.open("GET", "startup/frombuild", true);
	request.send();
}
