var baseUrl = getBaseUrl(),
	token = localStorage.getItem('uToken');

$(function() {
	
	//动态设置字体大小
	useful()
	
	//获取充值套餐
	updateToken(getList())

	//其他金额获得焦点
	$('.other').on('focus', function() {
		console.log('focus')
		$('.choiced') && $('.choiced').removeClass('choiced')
	})

	//其他金额失去焦点
	$('.other').on('blur', function() {
		var charge = $(this).val()
		if(charge) {
			$('.weui-btn_disabled') && $('.weui-btn_disabled').removeClass('weui-btn_disabled')
		} else {
			//如果费用为空
			if(!$('.weui-btn_blue').hasClass('weui-btn_disabled')) {
				$('.weui-btn_blue').addClass('weui-btn_disabled')
			}

		}
	})

	//充值
	$('.weui-btn_blue').on('click', function() {
		if(!$(this).hasClass('weui-btn_disabled')) { //如果可以点击

			$('.loadingToast').css('display', 'block') //等待框

			console.log('提交')
			var money = "";
			if($('.choiced').length > 0) {
				money = parseInt($('.choiced p span').html()) * 100
			} else {
				money = parseInt($('.other').val()) * 100
				console.log(money)
			}
			//创建充值订单
			updateToken(createOrder(money))
		}
	})

	//支付方式选中状态切换
	$('.weui-check').on('change', function() {
		var checkes = document.querySelectorAll('.weui-check');
		for(var i = 0; i < checkes.length; i++) {
			checkes[i].checked = false;
			if(!this.checked) this.checked = true;
		}
	})

	//清除卡号
	//	$('.weui-icon-cancel').on('click', function() {
	//		var that = this;
	//		$(that).css('opacity', '.7')
	//		setTimeout(function() {
	//			$(that).css('opacity', '1')
	//		}, 500)
	//		if($('.weui-input').val()) {
	//			$('.weui-input').val('')
	//		}
	//	})
})

//获取充值套餐
function getList() {
	$.ajax({
		type: "post",
		url: baseUrl + "/api/v1/deposit_gift/list.htm",
		contentType: "application/json",
		data: {},
		dataType: "JSON",
		async: true,
		headers: {
			"Authorization": "Bearer " + token
		},
		success: function(res) {
			res = JSON.parse(res)
			console.log(res)
			if(res.code == 0) {
				var data = res.data;
				for(var i = 0, str = ""; i < data.length; i++) {
					var p1 = parseInt(data[i].money) + parseInt(data[i].gift);
					var p2 = parseInt(data[i].money).toFixed(2);
					str += '<div class="choice"><p><span>' + p1 + '</span>元</p><p>售价:<span>' + p2 + '</span>元</p></div>';
				}
				$('.choices').append(str)

				//点击切换金额事件
				$('.choice').on('click', function() {
					$('.choiced').removeClass('choiced ')
					$(this).addClass('choiced');
					$('.weui-btn_blue').removeClass('weui-btn_disabled')
				})
			}else if (res.code==3){
				update(getList())
			}
		}
	});
}

//创建订单
function createOrder(money) {
	$.ajax({
		type: "post",
		url: baseUrl + "/api/v1/customer_deposit_order/create_weixin_mp.htm",
		data: JSON.stringify({
			"money": money
		}),
		contentType: "application/json",
		dataType: "JSON",
		async: true,
		headers: {
			"Authorization": "Bearer " + token
		},
		success: function(res) {
			res = JSON.parse(res)
			console.log(res)
			if(res.code == 0) {
				$('.loadingToast').css('display', 'none')
				//配置支付参数
				var data = {
					"appId": res.data.appid, //公众号名称，由商户传入     
					"timeStamp": res.data.timeStamp, //时间戳，自1970年以来的秒数     
					"nonceStr": res.data.nonceStr, //随机串     
					"package": res.data.package,
					"signType": res.data.signType, //微信签名方式：     
					"paySign": res.data.paySign //微信签名 
				}
				//订单创建成功之后发起支付
				pay(data)
			}else if (res.code==3){
				update(createOrder(money))
			} else {
				alert('充值失败')
				return false;
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
function pay(data) {
	if(typeof WeixinJSBridge == "undefined") {
		if(document.addEventListener) {
			document.addEventListener('WeixinJSBridgeReady', onBridgeReady(data), false);
		} else if(document.attachEvent) {
			document.attachEvent('WeixinJSBridgeReady', onBridgeReady(data));
			document.attachEvent('onWeixinJSBridgeReady', onBridgeReady(data));
		}
	} else {
		onBridgeReady(data);
	}
}

function onBridgeReady(params) {
	WeixinJSBridge.invoke(
		'getBrandWCPayRequest', params,
		function(res) {
			if(res.err_msg == "get_brand_wcpay_request:ok") {
				console.log('pay success!')
			} // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
		}
	);
}

//function pay(paytime, paystr, paypack, paysigntype, paysign, orderId) { //支付
//	//配置jssdk
//	wx.config({
//		debug: true, // 开启调试模式
//		appId: "appid", // 必填，公众号的唯一标识
//		timestamp: localStorage.getItem("timestamp"), // 必填，生成签名的时间戳
//		nonceStr: localStorage.getItem("nonceStr"), // 必填，生成签名的随机串
//		signature: localStorage.getItem("signature"), // 必填，签名，见附录1
//		jsApiList: ['chooseWXPay'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
//	});
//
//	//调用支付接口
//	wx.ready(function() {
//		wx.chooseWXPay({
//			"timestamp": paytime, // 支付签名时间戳
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
//					var timers = setInterval(function() {
//						checkPayed(orderId, timers)
//					}, 300)
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
//
//function checkPayed(orderId, timers) {
//	//创建充值订单
//	$.ajax({
//		type: "get",
//		url: "http://www.yusong.com.cn/charger/api/v1/customer/pay",
//		data: {
//			openId: localStorage.getItem('openid'),
//			orderCode: orderId
//		},
//		dataType: 'JSON',
//		async: true,
//		success: function(res) {
//			if(res.code == 0) {
//				clearInterval(timers)
//				location.href="charge_success.html"+params
//			} else {
//				location.href = "charge_fail.html"
//			}
//		},
//		error: function() {
//			alert('请求失败')
//		}
//	});
//}