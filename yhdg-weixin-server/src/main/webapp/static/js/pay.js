var secret = getSecret(),
	baseUrl = getBaseUrl(),
	openid = "ohxZKwSgUui2OCAwD_rU7ct56UzM", //测试
	//	openid=getParam('openId',location.href),//正式
	scanCode = "http://ccd.yusong.com.cn/scan_qrcode/index.htm?qrcode=EFGEFMMM", //测试
	//scanCode=getParam('qrcode',location.href),//正式
	balance = "",
	pid = "",
	pnum = "";

//全局保存openid，用于刷新token
localStorage.setItem('openid', openid);

$(function() {

	//动态设置字体大小
	useful()

	//点击取消提示框
	$('.cancel').on('click', function() {
		$('.js_dialog').css('display', 'none')
	})

	//登录
	if(openid != location.href) { //如果openid存在
		login()
	}

	//点击选择充值金额事件
	$('body').on('click', 'div.choice', function() {
		$('.choiced').removeClass('choiced')
		$(this).addClass('choiced');
		$('.weui-btn').removeClass('weui-btn_disabled')
	})

	//点击支付
	$('.btn .weui-btn').on('click', function() {
		if(!$(this).hasClass('weui-btn_disabled')) {
			//选择充电金额和时间
			var price = parseInt($('.choiced p span:first-child').html())
			var time = parseInt($('.choiced p span+span').html())

			//首先检测是否支持微信支付
			var reg = /MicroMessenger\/(\d+)/;
			if(!reg.test(navigator.userAgent)) {
				showError('当前浏览器不支持微信支付');
				return;
			}

			var version = RegExp.$1
			if(parseFloat(version) < 5.0) {
				showError('当前浏览器不支持微信支付');
				return;
			}

			$('.loadingToast').css('display', 'block')
			//创建订单，发起支付
			createOrder(price, time, pid, pnum)

			//测试余额支付
			//						balancePay(price, time, pid, pnum)

			//判断余额是否充足
			//			if(balance > price) {
			//				alert('余额支付')
			//				$('.loadingToast').css('display', 'block')
			//				//余额支付
			//				balancePay(price, time, pid, pnum)
			//			} else { //微信支付
			//				//首先检测是否支持微信支付
			//				var reg = /MicroMessenger\/(\d+)/;
			//				if(!reg.test(navigator.userAgent)) {
			//					showError('当前浏览器不支持微信支付');
			//					return;
			//				}
			//
			//				var version = RegExp.$1
			//				if(parseFloat(version) < 5.0) {
			//					showError('当前浏览器不支持微信支付');
			//					return;
			//				}
			//				
			//				$('.loadingToast').css('display', 'block')
			//				//创建订单，发起支付
			//				createOrder(price, time, pid, pnum)
			//			}
		}
	})

})

//登录获取到token
function login() {
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
		async: true,
		success: function(res) {
			res = JSON.parse(res)
			console.log('登陆')
			console.log(res)
			if(res.code == 0) {
				var expireIn = (new Date).getTime() + (res.data.expireIn - 200) * 1000

				//存储获取到的token,expireIn
				localStorage.setItem('uToken', res.data.token)
				localStorage.setItem('uExpireIn', expireIn)
				//检测当前设备是否可用
				checkPile(res.data.token)
			} else {
				console.log(res.message)
			}
		},
		error: function() {
			console.log('网路连接错误')
		}
	});
}

//获取jssdk参数
//function getJssdk(token) {
//	//根据openid获取jssdk配置参数
//	$.ajax({
//		type: "post",
//		url: baseUrl + "/api/v1/weixin/refresh_js_api_ticket.htm",
//		data: JSON.stringify({
//			"webPath": location.href.split('#')[0]
//		}),
//		contentType: 'application/json',
//		headers: {
//			"Authorization": "Bearer " + token
//		},
//		dataType: 'JSON',
//		success: function(res) {
//			console.log(res)
//			res=JSON.parse(res)
//			if(res.code == 0) {
//				//当前页面存储jssdk参数
//				sessionStorage.setItem('timestamp', res.data.timestamp)
//				sessionStorage.setItem('nonceStr', res.data.nonceStr)
//				sessionStorage.setItem('signature'.res.data.signature)
//			}
//		}
//	})
//}

//检测桩点是否可用
function checkPile(token) {
	$.ajax({
		type: "post",
		url: baseUrl + "/api/v1/charger/query_by_qrcode.htm",
		data: JSON.stringify({
			"qrcode": scanCode
		}),
		contentType: "application/json",
		dataType: 'json',
		async: true,
		headers: {
			"Authorization": "Bearer " + token
		},
		success: function(res) {
			console.log('获取桩点信息')
			console.log(res)
			if(res.code == 0) { //如果桩点可用

				//设置桩点信息
				//				$('.charge_num span').html(res.data.freePoint)
				$('.charge_addr').html(res.data.street + res.data.chargerName)

				//获取到余额
				balance = res.data.balance / 100 || 0;
				pid = res.data.id;
				pnum = res.data.pileNum;

				//获取页面套餐
				getPrice(res.data.id)
			} else { //桩点不可用
				$('.canNo').css('display', 'block');
				$('.canNo .weui-msg__desc').html(res.message)
			}
		},
		error: function() {
			alert('error')
			return false;
		}
	});
}

//获取充电桩价格信息
function getPrice(id) {
	$.ajax({
		type: "post",
		url: baseUrl + "/api/v1/charger/price_list.htm",
		data: JSON.stringify({
			"id": id
		}),
		contentType: "application/json",
		dataType: "JSON",
		async: true,
		headers: {
			"Authorization": "Bearer " + localStorage.getItem('uToken')
		},
		success: function(res) {
			res = JSON.parse(res)
			console.log('获取价格套餐')
			console.log(res)
			if(res.code == 0) {
				var str = ""
				res.data.map(function(obj) {
					str += '<div class="choice"><p><span>' + obj.price + '</span>元<span>' + obj.duration + '</span>小时</p></div>'
				})
				$('.choices').append(str)
				
				//扫码成功之后打开订单页面
				$('.canPay').css('display', 'block');
			} else {
				console.log(res.message)
				return false;
			}
		},
		error: function() {
			alert('网路连接错误')
			return false;
		}
	});
}

//余额支付
function balancePay(price, time, pid, pnum) {
	$.ajax({
		type: "post",
		url: baseUrl + "/api/v1/customer/charger_order/create_balance.htm",
		data: JSON.stringify({
			"id": pid,
			"num": pnum,
			"price": price * 100,
			"duration": time
		}),
		contentType: "application/json",
		dataType: "JSON",
		async: true,
		headers: {
			"Authorization": "Bearer " + localStorage.getItem('uToken')
		},
		success: function(res) {
			res = JSON.parse(res)
			console.log('余额支付')
			console.log(res)
			if(res.code == 0) {
				location.href = "pay_success.html"
			} else {
				console.log(res.message)
				$('.js_dialog').css('display', 'block');

				$('.goCharge').tap(function() { //点击充值，跳转到充值界面
					console.log('微信支付')
					createOrder(price, time, pid, pnum)
				})
				return false;
			}
		},
		error: function() {
			alert('网路连接错误')
			return false;
		}
	});
}

//创建订单
function createOrder(price, time, pid, pnum) {
	$.ajax({
		type: "post",
		url: baseUrl + "/api/v1/customer/charger_order/create_weixin_mp.htm",
		data: JSON.stringify({
			"id": pid,
			"num": pnum,
			"price": 1,
			"duration": time
		}),
		contentType: "application/json",
		dataType: "JSON",
		async: true,
		headers: {
			"Authorization": "Bearer " + localStorage.getItem('uToken')
		},
		success: function(res) {
			res = JSON.parse(res)
			console.log('获取支付参数')
			console.log(res)
			if(res.code == 0) {
				$('.loadingToast').css('display', 'none')
				//配置支付参数
				var data = {
					"appId": res.data.appId, //公众号名称，由商户传入     
					"timeStamp": res.data.timeStamp, //时间戳，自1970年以来的秒数     
					"nonceStr": res.data.nonceStr, //随机串     
					"package": res.data.package,
					"signType": res.data.signType, //微信签名方式：     
					"paySign": res.data.paySign //微信签名 
				}
				console.log(data)
				//订单创建成功之后发起支付
				pay(data)
			}
		},
		error: function() {
			$('.weui-toast__content').html('订单创建失败！');
			setTimeout(function() {
				$('.loadingToast').css('display', 'none')
			}, 1500)
			return false;
		}
	});
}

//调用微信支付
function pay(params) {
	if(typeof WeixinJSBridge == "undefined") {
		if(document.addEventListener) {
			document.addEventListener('WeixinJSBridgeReady', onBridgeReady(params), false);
		} else if(document.attachEvent) {
			document.attachEvent('WeixinJSBridgeReady', onBridgeReady(params));
			document.attachEvent('onWeixinJSBridgeReady', onBridgeReady(params));
		}
	} else {
		onBridgeReady(params);
	}
}

function onBridgeReady(params) {
	WeixinJSBridge.invoke(
		'getBrandWCPayRequest', params,
		function(res) {
			if(res.err_msg == "get_brand_wcpay_request:ok") {
				console.log('pay success!')
				alert('pay success!')
			} // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
		}
	);
}
//function checkMoney(price, time) { //查询余额
//	$.ajax({
//		type: "post",
//		url: baseUrl + "/api/v1/customer/balance.htm?openid=" + openid,
//		data: {},
//		dataType: "JSON",
//		contentType: "application/json",
//		async: true,
//		success: function(res) {
//			//隐藏提示框
//			$('.loadingToast').css('display', 'none')
//			if(res.code == 0) {
//				if(parseInt(price) > parseInt(res.data.balance)) { //余额不足，使用微信支付
//					//显示余额不足提示框
//					//					$('.js_dialog').css('display', 'block')
//					//					$('.goCharge').on('click', function() { //点击充值，跳转到充值界面
//					//						location.href = 'charge.html?scanCode=' + scanCode + '&price=' + price + '&time=' + time
//					//					})
//					//					$('.cancel').on('click', function() {
//					//						$('.js_dialog').css('display', 'none')
//					//					})
//					createOrder(time, price)
//				} else { //余额充足，就发起余额支付
//					balancePay(price, time)
//				}
//			} else {
//				alert(res.message)
//			}
//		},
//		error: function() {
//			//隐藏提示框
//			$('.loadingToast').css('display', 'none')
//			alert('网络连接失败')
//			return false;
//		}
//	});
//}

//function pay(paytime, paystr, paypack, paysigntype, paysign) { //支付
//	//配置jssdk
//	wx.config({
//		debug: true, // 开启调试模式
//		appId: localStorage.getItem("appid"), // 必填，公众号的唯一标识
//		timestamp: localStorage.getItem("timestamp"), // 必填，生成签名的时间戳
//		nonceStr: localStorage.getItem("nonceStr"), // 必填，生成签名的随机串
//		signature: localStorage.getItem("signature"), // 必填，签名，见附录1
//		jsApiList: ['chooseWXPay'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
//	});
//
//	//调用支付接口
//	wx.ready(function() {
//		wx.chooseWXPay({
//			"timestamp": paytime, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
//			"nonceStr": paystr, // 支付签名随机串，不长于 32 位
//			"package": paypack, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
//			"signType": paysigntype, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
//			"paySign": paysign, // 支付签名
//			success: function(res) {
//				// 支付成功后的回调函数
//				console.log(res.err_msg);
//				if(res.err_msg == "get_brand_wcpay_request：ok") {
//					// 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
//					console.log('success')
//					//					window.location.href = '跳转到付款成功页面';
//				} else {
//					console.log(res.errMsg);
//					console.log(res.err_msg);
//					return false;
//				}
//			}
//		});
//	});
//}