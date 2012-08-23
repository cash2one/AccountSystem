HTTP_PROTOCOL = "http";
HTTPS_PROTOCOL = "https";
SIGNATURE_METHOD = "MD5";
MERCHANT_NAME = "shit";

function pair(key, value) {
	this.key = key;
	this.value = value;
};

function getHttpRequestParameters(requestUri) {
	var parameterArray = new Array();
	var uri = requestUri.toLowerCase();
	if (uri.indexOf(HTTP_PROTOCOL) == 0 || uri.indexOf(HTTPS_PROTOCOL) == 0) {
		var position = uri.indexOf("?");
		var parastr = uri.substring(position + 1);
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
	alert("The request uri is not http or https protocol.");
	return;
}

function makeSignedUrl(appArea, appId, customSecurityLevel, ticket) {
	var signedUrl = "http://hps.sdo.com/cas/validate.signature?";
	var timestamp = (new Date()).getTime();

	var array = new Array(
			      new pair("appArea", appArea),
			      new pair("appId", appId), 
			      new pair("customSecurityLevel", customSecurityLevel), 
			      new pair("merchant_name", MERCHANT_NAME), 
			      new pair("ticket", ticket), 
			      new pair("signature_method", SIGNATURE_METHOD),
			      new pair("timestamp", timestamp));
	var arr = quickSort(array);
	var stringToSign = "";
	var i = 0;
	for (i = 0; i < arr.length; i++) {
		signedUrl = signedUrl + arr[i].key + "=" + arr[i].value + "&";
		stringToSign = stringToSign + arr[i].key + "=" + arr[i].value;
	}
	var hash = CryptoJS.MD5(stringToSign);
	signedUrl = signedUrl + "signature=" + hash;
	return signedUrl;
}

function quickSort(arr) {
	if (arr.length <= 1) {
		return arr;
	}
	var pivotIndex = Math.floor(arr.length / 2);
	var pivot = arr.splice(pivotIndex, 1)[0];
	var left = [];
	var right = [];
	for ( var i = 0; i < arr.length; i++) {
		if (compare(arr[i].key, pivot.key) == -1) {
			left.push(arr[i]);
		} else {
			right.push(arr[i]);
		}
	}
	return quickSort(left).concat([ pivot ], quickSort(right));
};

function compare(string1, string2) {
	length1 = string1.length;
	length2 = string2.length;
	length = length1 < length2 ? length1 : length2;
	var i = 0;
	for (i = 0; i < length; i++) {
		var char1 = string1.charAt(i);
		var char2 = string2.charAt(i);
		if (char1 < char2) {
			return -1;
		}
		if (char1 > char2) {
			return 1;
		}
	}
	if (length1 < length2) {
		return -1;
	}
	if (length1 > length2) {
		return 1;
	}
	return 0;
};