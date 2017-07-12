var resourceWait = 300, maxRenderWait = 10000, url = 'https://www.datalicious.com';

var page = require('webpage').create(), count = 0, forcedRenderTimeout, renderTimeout;

page.viewportSize = {
	width : 1280,
	height : 1024
};

function doRender() {
	page.render('Datalicious.png');
	phantom.exit();
}
var fs = require('fs');
var path = 'E:\output.txt';
var content = "";
var content1 = "";
page.onResourceRequested = function(req) {
	count += 1;
	console.log('> ' + req.id + ' - ' + req.url);
	content = content + req.id;
	content1 = content1 + req.url;
	fs.write(path, content + content1, 'w');
	clearTimeout(renderTimeout);
};

page.onResourceReceived = function(res) {
	if (!res.stage || res.stage === 'end') {
		count -= 1;
		console.log(res.id + ' ' + res.status + ' - ' + res.url);

		if (count === 0) {
			renderTimeout = setTimeout(doRender, resourceWait);
		}
	}
};

page.open(url, function(status) {
	if (status !== "success") {
		console.log('Unable to load url');
		phantom.exit();
	} else {
		forcedRenderTimeout = setTimeout(function() {
			console.log(count);
			doRender();
		}, maxRenderWait);
	}
});