//手机号码验证正则
var reg = /(^0{0,1}1[3|4|5|6|7|8|9][0-9]{9}$)/,
	baseUrl = getBaseUrl(),
	token = localStorage.getItem('uToken'),
	msgCode = "",
	tel = getParam('tel', location.href);//获取电话号码
	
console.log(tel)

$(function() {
	//动态设置字体大小
	useful()
	
	var showTel = tel.slice(0, 3) + '****' + tel.slice(7, 11)
	//更换手机号界面显示电话号码
	$('.tel span').html(showTel)

	//点击获取验证码
	$('.weui-vcode-btn').on('click', function() {
		if($('.tel').val() || reg.test(tel)) {
			if(reg.test($('.tel').val()) || reg.test(tel)) {
				console.log('getCode')
				$('.weui-vcode-btn').attr('disabled', 'disabled') //设置为不可点击
				var time = 59
				var timers = setInterval(function() {
					$('.weui-vcode-btn').html(time + 's')
					time--
					if(time < -1) {
						$('.weui-vcode-btn').html('获取验证码')
						$('.weui-vcode-btn').attr('disabled', null) //恢复点击
						clearInterval(timers)
					}
				}, 1000)

				//发起验证码请求
				updateToken(getCode())
				

			} else {
				$('#iosDialog2 .weui-dialog__bd').html('手机号输入格式有误，请重新输入！')
				$('#iosDialog2').css('display', 'block')
			}
		} else {
			$('#iosDialog2 .weui-dialog__bd').html('手机号不能为空！')
			$('#iosDialog2').css('display', 'block')
		}

	})

	//更换手机号码失去焦点
	$('.newTel').on('blur', function() {
		var newTel = $(this).val()
		var code = $('.code').val()
		if(code) {
			if(newTel) {
				if(reg.test(newTel)) {
					$('.weui-btn_blue').removeClass('weui-btn_disabled').attr('disabled', null)
				} else {
					$('#iosDialog2 .weui-dialog__bd').html('输入的手机号格式有误，请重新输入！')
					$(' #iosDialog2.js_dialog').css('display', 'block')
					return false;
				}
			} else {
				$('#iosDialog2 .weui-dialog__bd').html('手机号不能为空,请输入手机号！')
				$('#iosDialog2.js_dialog').css('display', 'block')
				return false;
			}
		} else {
			$('#iosDialog2 .weui-dialog__bd').html('验证码不能为空,请输入您的验证码！')
			$('#iosDialog2.js_dialog').css('display', 'block')
			return false;
		}
	})

	//更换手机号点击完成
	$('.weui-btn-change').on('click', function() {
		if(!$(this).hasClass('weui-btn_disabled')) {
			var newTel = $('.newTel').val()
			var code = $('.code').val()
			if(code == msgCode || code) {
				//提示确认框
				$('#iosDialog1 .weui-dialog__bd p+p').html(newTel);
				$('#iosDialog1').css('display', 'block');
				//点击确认提交修改手机号
				$('#iosDialog1 .weui-dialog__btn_primary').tap(function() {
					console.log(newTel)
					$('#iosDialog1').css('display', 'none');
					//发起更换手机号
					changeTel(newTel, code)
				})
			} else {
				$('#iosDialog2 .weui-dialog__bd').html('验证码错误，请重新输入！')
				$('#iosDialog2').css('display', 'block')
			}
		}
	})

	//绑定手机号时，验证码输入框失去焦点
	$('.code').on('blur', function() {
		var code = $('.code').val()
		if(code) {
			$('.weui-btn_blue').removeClass('weui-btn_disabled');
		} else {
			$('.weui-dialog__bd').html('验证码不能为空,请输入验证码！')
			$('.js_dialog').css('display', 'block')
		}
	})

	//绑定手机点击完成提交手机号
	$('.weui-btn-bind').tap(function() {
		console.log('bind')
//			window.open('http://localhost:8020/weui/user/pages/person_msg.html')

		
		if(!$(this).hasClass('weui-btn_disabled')) {
			var tel = $('.tel').val()
			var code = $('.code').val()

			if(code == msgCode) {
				//发起绑定
				bindTel(tel, code)
			} else {
				$('#iosDialog2 .weui-dialog__bd').html('验证码错误，请重新输入！')
				$('#iosDialog2').css('display', 'block')
			}
		}
	})

	//关闭提示弹出框1
	$('#iosDialog2 .weui-dialog__btn').tap(function() {
		$('#iosDialog2').css('display', 'none')
	})

	//关闭提示弹出框1
	$('#iosDialog1 .weui-dialog__btn_default').tap(function() {
		$('#iosDialog1').css('display', 'none')
	})

})
//获取url参数的方法
function getParam(key, str) {
	var lot = str ? str : location.search;
	var reg = new RegExp(".*" + key + "\\s*=([^=&#]*)(?=&|#|).*", "g");
	return decodeURIComponent(lot.replace(reg, "$1"));
}

//发起验证码请求
function getCode() {
	if(!reg.test(tel)) {
		tel = $('.tel').val();
	}
	$.ajax({
		type: "post",
		url: baseUrl + "/api/v1/auth_code/get.htm",
		data: JSON.stringify({
			"mobile": tel
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
				msgCode = res.data.authCode
			}else if (res.code==3){
				update(getCode())
			} else {
				console.log(res.message)
				return false;
			}
		},
		error: function() {
			alert('网路连接错误')
			return false;
		}
	})
}

//发送更换手机号请求
function changeTel(tel, code) {
	$.ajax({
		type: "post",
		url: baseUrl + "/api/v1/customer/update_mobile.htm",
		data: JSON.stringify({
			"mobile": tel,
			"authCode": code
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
				//修改成功后
				$('#toast').css('display', 'block')
				setTimeout(function() {
					$('#toast').css('display', 'none')
					setTimeout(function() {
						window.history.back(-1)
					}, 500)
				}, 1500)
			} else {
				console.log(res.message)
				return false;
			}
		},
		error: function() {
			alert('网路连接错误')
			return false;
		}
	})
}

//发送绑定手机号请求
function bindTel(tel, code) {
	console.log(tel)
	console.log(code)
	$.ajax({
		type: "post",
		url: baseUrl + "/api/v1/customer/bind_mobile.htm",
		data: JSON.stringify({
			"mobile": tel,
			"authCode": code
		}),
		contentType: "application/json",
		dataType: "JSON",
		async: true,
		headers: {
			"Authorization": "Bearer " + token
		},
		success: function(res) {
			res = JSON.parse(res)
			if(res.code == 0) {
				//修改成功后
				$('#toast').css('display', 'block')
				setTimeout(function() {
					$('#toast').css('display', 'none')
					setTimeout(function() {
						window.history.back(-1)
					}, 500)
				}, 1500)
			} else {
				console.log(res.message)
				return false;
			}
		},
		error: function() {
			alert('网路连接错误')
			return false;
		}
	})
}