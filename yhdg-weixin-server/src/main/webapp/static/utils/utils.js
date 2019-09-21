//sha1

var hexcase = 0; /* hex output format. 0 - lowercase; 1 - uppercase */
var chrsz = 8; /* bits per input character. 8 - ASCII; 16 - Unicode */

function sha1(s) {
	return binb2hex(core_sha1(AlignSHA1(s))).toUpperCase();
}

function sha1_vm_test() {

	return sha1("abc") == "a9993e364706816aba3e25717850c26c9cd0d89d";

}

function core_sha1(blockArray) {

	var x = blockArray; // append padding
	var w = Array(80);

	var a = 1732584193;

	var b = -271733879;

	var c = -1732584194;

	var d = 271733878;

	var e = -1009589776;

	for(var i = 0; i < x.length; i += 16) // 每次处理512位 16*32
	{

		var olda = a;

		var oldb = b;

		var oldc = c;

		var oldd = d;

		var olde = e;

		for(var j = 0; j < 80; j++) // 对每个512位进行80步操作
		{

			if(j < 16)
				w[j] = x[i + j];

			else
				w[j] = rol(w[j - 3] ^ w[j - 8] ^ w[j - 14] ^ w[j - 16], 1);

			var t = safe_add(safe_add(rol(a, 5), sha1_ft(j, b, c, d)), safe_add(safe_add(e, w[j]), sha1_kt(j)));

			e = d;

			d = c;

			c = rol(b, 30);

			b = a;

			a = t;

		}

		a = safe_add(a, olda);

		b = safe_add(b, oldb);

		c = safe_add(c, oldc);

		d = safe_add(d, oldd);

		e = safe_add(e, olde);

	}

	return new Array(a, b, c, d, e);

}

/*
 *
 * Perform the appropriate triplet combination function for the current
 * iteration
 *
 * 返回对应F函数的值
 *
 */
function sha1_ft(t, b, c, d) {

	if(t < 20)
		return(b & c) | ((~b) & d);

	if(t < 40)
		return b ^ c ^ d;

	if(t < 60)
		return(b & c) | (b & d) | (c & d);

	return b ^ c ^ d; // t<80
}

/*
 *
 * Determine the appropriate additive constant for the current iteration
 *
 * 返回对应的Kt值
 *
 */
function sha1_kt(t) {

	return(t < 20) ? 1518500249 : (t < 40) ? 1859775393 : (t < 60) ? -1894007588 : -899497514;

}

/*
 *
 * Add integers, wrapping at 2^32. This uses 16-bit operations internally
 *
 * to work around bugs in some JS interpreters.
 *
 * 将32位数拆成高16位和低16位分别进行相加，从而实现 MOD 2^32 的加法
 *
 */
function safe_add(x, y) {

	var lsw = (x & 0xFFFF) + (y & 0xFFFF);

	var msw = (x >> 16) + (y >> 16) + (lsw >> 16);

	return(msw << 16) | (lsw & 0xFFFF);

}

/*
 *
 * Bitwise rotate a 32-bit number to the left.
 *
 * 32位二进制数循环左移
 *
 */
function rol(num, cnt) {

	return(num << cnt) | (num >>> (32 - cnt));

}

/*
 *
 * The standard SHA1 needs the input string to fit into a block
 *
 * This function align the input string to meet the requirement
 *
 */
function AlignSHA1(str) {

	var nblk = ((str.length + 8) >> 6) + 1,
		blks = new Array(nblk * 16);

	for(var i = 0; i < nblk * 16; i++)
		blks[i] = 0;

	for(i = 0; i < str.length; i++)

		blks[i >> 2] |= str.charCodeAt(i) << (24 - (i & 3) * 8);

	blks[i >> 2] |= 0x80 << (24 - (i & 3) * 8);

	blks[nblk * 16 - 1] = str.length * 8;

	return blks;

}

/*
 *
 * Convert an array of big-endian words to a hex string.
 *
 */
function binb2hex(binarray) {

	var hex_tab = hexcase ? "0123456789ABCDEF" : "0123456789abcdef";

	var str = "";

	for(var i = 0; i < binarray.length * 4; i++) {

		str += hex_tab.charAt((binarray[i >> 2] >> ((3 - i % 4) * 8 + 4)) & 0xF) +

			hex_tab.charAt((binarray[i >> 2] >> ((3 - i % 4) * 8)) & 0xF);

	}

	return str;

}

//md5
function safe_add(x, y) { //需要
	var lsw = (x & 0xFFFF) + (y & 0xFFFF)
	var msw = (x >> 16) + (y >> 16) + (lsw >> 16)
	return(msw << 16) | (lsw & 0xFFFF)
}

function rol(num, cnt) {
	return(num << cnt) | (num >>> (32 - cnt))
}

function cmn(q, a, b, x, s, t) {
	return safe_add(rol(safe_add(safe_add(a, q), safe_add(x, t)), s), b)
}

function ff(a, b, c, d, x, s, t) { //需要
	return cmn((b & c) | ((~b) & d), a, b, x, s, t)
}

function gg(a, b, c, d, x, s, t) { //需要
	return cmn((b & d) | (c & (~d)), a, b, x, s, t)
}

function hh(a, b, c, d, x, s, t) { //需要
	return cmn(b ^ c ^ d, a, b, x, s, t)
}

function ii(a, b, c, d, x, s, t) { //需要
	return cmn(c ^ (b | (~d)), a, b, x, s, t)
}

function coreMD5(x) { //需要
	var a = 1732584193
	var b = -271733879
	var c = -1732584194
	var d = 271733878

	for(var i = 0; i < x.length; i += 16) {
		var olda = a
		var oldb = b
		var oldc = c
		var oldd = d

		a = ff(a, b, c, d, x[i + 0], 7, -680876936)
		d = ff(d, a, b, c, x[i + 1], 12, -389564586)
		c = ff(c, d, a, b, x[i + 2], 17, 606105819)
		b = ff(b, c, d, a, x[i + 3], 22, -1044525330)
		a = ff(a, b, c, d, x[i + 4], 7, -176418897)
		d = ff(d, a, b, c, x[i + 5], 12, 1200080426)
		c = ff(c, d, a, b, x[i + 6], 17, -1473231341)
		b = ff(b, c, d, a, x[i + 7], 22, -45705983)
		a = ff(a, b, c, d, x[i + 8], 7, 1770035416)
		d = ff(d, a, b, c, x[i + 9], 12, -1958414417)
		c = ff(c, d, a, b, x[i + 10], 17, -42063)
		b = ff(b, c, d, a, x[i + 11], 22, -1990404162)
		a = ff(a, b, c, d, x[i + 12], 7, 1804603682)
		d = ff(d, a, b, c, x[i + 13], 12, -40341101)
		c = ff(c, d, a, b, x[i + 14], 17, -1502002290)
		b = ff(b, c, d, a, x[i + 15], 22, 1236535329)

		a = gg(a, b, c, d, x[i + 1], 5, -165796510)
		d = gg(d, a, b, c, x[i + 6], 9, -1069501632)
		c = gg(c, d, a, b, x[i + 11], 14, 643717713)
		b = gg(b, c, d, a, x[i + 0], 20, -373897302)
		a = gg(a, b, c, d, x[i + 5], 5, -701558691)
		d = gg(d, a, b, c, x[i + 10], 9, 38016083)
		c = gg(c, d, a, b, x[i + 15], 14, -660478335)
		b = gg(b, c, d, a, x[i + 4], 20, -405537848)
		a = gg(a, b, c, d, x[i + 9], 5, 568446438)
		d = gg(d, a, b, c, x[i + 14], 9, -1019803690)
		c = gg(c, d, a, b, x[i + 3], 14, -187363961)
		b = gg(b, c, d, a, x[i + 8], 20, 1163531501)
		a = gg(a, b, c, d, x[i + 13], 5, -1444681467)
		d = gg(d, a, b, c, x[i + 2], 9, -51403784)
		c = gg(c, d, a, b, x[i + 7], 14, 1735328473)
		b = gg(b, c, d, a, x[i + 12], 20, -1926607734)

		a = hh(a, b, c, d, x[i + 5], 4, -378558)
		d = hh(d, a, b, c, x[i + 8], 11, -2022574463)
		c = hh(c, d, a, b, x[i + 11], 16, 1839030562)
		b = hh(b, c, d, a, x[i + 14], 23, -35309556)
		a = hh(a, b, c, d, x[i + 1], 4, -1530992060)
		d = hh(d, a, b, c, x[i + 4], 11, 1272893353)
		c = hh(c, d, a, b, x[i + 7], 16, -155497632)
		b = hh(b, c, d, a, x[i + 10], 23, -1094730640)
		a = hh(a, b, c, d, x[i + 13], 4, 681279174)
		d = hh(d, a, b, c, x[i + 0], 11, -358537222)
		c = hh(c, d, a, b, x[i + 3], 16, -722521979)
		b = hh(b, c, d, a, x[i + 6], 23, 76029189)
		a = hh(a, b, c, d, x[i + 9], 4, -640364487)
		d = hh(d, a, b, c, x[i + 12], 11, -421815835)
		c = hh(c, d, a, b, x[i + 15], 16, 530742520)
		b = hh(b, c, d, a, x[i + 2], 23, -995338651)

		a = ii(a, b, c, d, x[i + 0], 6, -198630844)
		d = ii(d, a, b, c, x[i + 7], 10, 1126891415)
		c = ii(c, d, a, b, x[i + 14], 15, -1416354905)
		b = ii(b, c, d, a, x[i + 5], 21, -57434055)
		a = ii(a, b, c, d, x[i + 12], 6, 1700485571)
		d = ii(d, a, b, c, x[i + 3], 10, -1894986606)
		c = ii(c, d, a, b, x[i + 10], 15, -1051523)
		b = ii(b, c, d, a, x[i + 1], 21, -2054922799)
		a = ii(a, b, c, d, x[i + 8], 6, 1873313359)
		d = ii(d, a, b, c, x[i + 15], 10, -30611744)
		c = ii(c, d, a, b, x[i + 6], 15, -1560198380)
		b = ii(b, c, d, a, x[i + 13], 21, 1309151649)
		a = ii(a, b, c, d, x[i + 4], 6, -145523070)
		d = ii(d, a, b, c, x[i + 11], 10, -1120210379)
		c = ii(c, d, a, b, x[i + 2], 15, 718787259)
		b = ii(b, c, d, a, x[i + 9], 21, -343485551)

		a = safe_add(a, olda)
		b = safe_add(b, oldb)
		c = safe_add(c, oldc)
		d = safe_add(d, oldd)
	}
	return [a, b, c, d]
}

function binl2hex(binarray) {
	var hex_tab = "0123456789abcdef"
	var str = ""
	for(var i = 0; i < binarray.length * 4; i++) {
		str += hex_tab.charAt((binarray[i >> 2] >> ((i % 4) * 8 + 4)) & 0xF) +
			hex_tab.charAt((binarray[i >> 2] >> ((i % 4) * 8)) & 0xF)
	}
	return str
}

function str2binl(str) { //需要
	var nblk = ((str.length + 8) >> 6) + 1 // number of 16-word blocks  
	var blks = new Array(nblk * 16)
	for(var i = 0; i < nblk * 16; i++) blks[i] = 0
	for(var i = 0; i < str.length; i++)
		blks[i >> 2] |= (str.charCodeAt(i) & 0xFF) << ((i % 4) * 8)
	blks[i >> 2] |= 0x80 << ((i % 4) * 8)
	blks[nblk * 16 - 2] = str.length * 8
	return blks
}

function md5(str) { //需要
	return binl2hex(coreMD5(str2binl(str))).toUpperCase()
}

function hexMD5w(str) {
	return binl2hex(coreMD5(strw2binl(str)))
}

function b64MD5(str) {
	return binl2b64(coreMD5(str2binl(str)))
}

function b64MD5w(str) {
	return binl2b64(coreMD5(strw2binl(str)))
}
/* Backward compatibility */
function calcMD5(str) {
	return binl2hex(coreMD5(str2binl(str)))
}

var openid = localStorage.getItem('openid');
var secret = "67884E9671AECAC98A89E9A26C18DC28";
//更新token
function updateToken(next) {
	//如果过期
	if(!localStorage.getItem('oExpireIn')||(new Date).getTime() > localStorage.getItem('uExpireIn')) {
		var pwd = md5(secret + sha1(secret + openid));
		$.ajax({
			type: "post",
			url: baseUrl + "/api/v1/customer/mp_login.htm",
			contentType: "application/json",
			data: JSON.stringify({
				"openId": openid,
				"password": pwd
			}),
			dataType: "JSON",
			async: false,
			success: function(res) {
				res = JSON.parse(res)
				console.log('过期更新')
				if(res.code == 0) {
					var expireIn = (new Date).getTime() + (res.data.expireIn - 200) * 1000

					//存储获取到的token,expireIn
					localStorage.setItem('uToken', res.data.token)
					localStorage.setItem('uExpireIn', expireIn)

					//执行接下来的操作
					next

				} else {

					console.log(res.message)
					return false
				}
			},
			error: function() {
				alert('网路错误！')
				return false;
			}
		});
	} else {
		console.log('未过期')
		next
	}
}

//获取当前时间，格式YYYY-MM-DD
function getNow() {
	var date = new Date();
	var seperator1 = "-";
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var strDate = date.getDate();
	if(month >= 1 && month <= 9) {
		month = "0" + month;
	}
	if(strDate >= 0 && strDate <= 9) {
		strDate = "0" + strDate;
	}
	var currentdate = year + seperator1 + month + seperator1 + strDate;
	return currentdate;
}

//获取url参数的方法
function getParam(key, str) {
	var lot = str ? str : location.search;
	var reg = new RegExp(".*" + key + "\\s*=([^=&#]*)(?=&|#|).*", "g");
	return decodeURIComponent(lot.replace(reg, "$1"));
}

function useful() {
	//根据屏幕高度设置字体大小
	if($(window).height() < 527) {
		$('html').css('font-size', '15px');
	}
	if(526 < $(window).height() && $(window).height() < 627) {
		$('html').css('font-size', '17px');
	}
	if($(window).height() > 626) {
		$('html').css('font-size', '19px');
	}

	if(!Array.prototype.map) {
		Array.prototype.map = function(fun /*, thisp*/ ) {
			var len = this.length;
			if(typeof fun != "function")
				throw new TypeError();

			var res = new Array(len);
			var thisp = arguments[1];
			for(var i = 0; i < len; i++) {
				if(i in this)
					res[i] = fun.call(thisp, this[i], i, this);
			}

			return res;
		};
	}
}

function getSecret() {
	return "67884E9671AECAC98A89E9A26C18DC28";
}

function getBaseUrl() {
	return "https://192.9.198.213:8080";
}

function getUploadUrl() {
	return "https://192.9.198.244:8442"
}

function update(next) {
	var pwd = md5(secret + sha1(secret + openid));
	$.ajax({
		type: "post",
		url: baseUrl + "/api/v1/customer/mp_login.htm",
		contentType: "application/json",
		data: JSON.stringify({
			"openId": openid,
			"password": pwd
		}),
		dataType: "JSON",
		async: false,
		success: function(res) {
			res = JSON.parse(res)
			console.log('过期更新')
			if(res.code == 0) {
				var expireIn = (new Date).getTime() + (res.data.expireIn - 200) * 1000

				//存储获取到的token,expireIn
				localStorage.setItem('uToken', res.data.token)
				localStorage.setItem('uExpireIn', expireIn)

				//执行接下来的操作
				next

			} else {

				console.log(res.message)
				return false
			}
		},
		error: function() {
			alert('网路错误！')
			return false;
		}
	});
}