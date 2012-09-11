HTTP_PROTOCOL = "http";
HTTPS_PROTOCOL = "https";

function pair(key, value) {
	this.key = key;
	this.value = value;
};

function getHttpRequestParameters(requestUri) {
	var parameterArray = new Array();
	var uri = requestUri.toLowerCase();
	if (uri.indexOf(HTTP_PROTOCOL) == 0 || uri.indexOf(HTTPS_PROTOCOL) == 0) {
		var position = requestUri.indexOf("?");
		var parastr = requestUri.substring(position + 1);
		if (position > 0 && parastr.indexOf("&") > 0) {
			para = parastr.split("&");
			var i = 0;
			for (i = 0; i < para.length; i++) {
				var pos = para[i].indexOf("=");
				parameterArray.push(
						new pair(
								para[i].substring(0 ,pos),
								para[i].substring(pos + 1)));
			}
		}
		return parameterArray;
	}
	return;
}