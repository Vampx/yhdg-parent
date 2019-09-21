

var reg = /(^0{0,1}1[3|4|5|6|7|8|9][0-9]{9}$)/; //手脚号码验证正则
$(function() {
	
	useful()
	//点击获取验证码
	$('.weui-vcode-btn').on('click', function() {
		console.log('getCode')
		//获取手机号
		var tel = $('.tel').val()
		if(tel) {
			if(reg.test(tel)) {
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
				$.ajax({
					type: "get",
					url: "https://192.9.198.244:8082/charger/api/v1/auth_code/get",
					data: {
						mobile: tel
					},
					dataType: 'JSON',
					async: true,
					success: function(res) {
						if(res.code == 0) {
							console.log(res.message)

						} else {
							alert(res.message)
						}
					},
					error: function() {
						alert('请求失败')
					}
				});
			} else { //弹出提示框
				$('.weui-dialog__bd').html('输入的手机号格式有误，请重新输入！')
				$('.js_dialog').css('display', 'block')
			}
		} else { //弹出提示框
			$('.weui-dialog__bd').html('手机号不能为空,请输入您的手机号！')
			$('.js_dialog').css('display', 'block')
		}
	})
	//关闭弹出框
	$('.weui-dialog__btn').on('click', function() {
		$('.js_dialog').css('display', 'none')
	})

	//点击完成提交手机号
	$('.weui-btn_blue').on('click', function() {
		if(!$(this).hasClass('weui-btn_disabled')) {
			var tel = ""
			var code = $('.code').val()

		}
	})

	//验证码失去焦点
	$('.code').on('blur', function() {
		var tel = $('.tel').val()
		var code = $('.code').val()
		if(tel) {
			if(code) {
				$('.weui-btn_blue').removeClass('weui-btn_disabled').attr('disabled', null)
			} else {
				$('.weui-dialog__bd').html('验证码不能为空,请输入验证码！')
				$('.js_dialog').css('display', 'block')
			}
		} else {
			$('.weui-dialog__bd').html('手机号不能为空,请输入您的手机号！')
			$('.js_dialog').css('display', 'block')
		}
	})
})

	//解绑手机号
function cancelTel() {

	$.ajax({
		type: "get",
		url: "http://www.yusong.com.cn/charger/api/v1/customer/update_phone_or_pw",
		data: {
			mobile: tel,
			authCode: code,
			openId: localStorage.getItem('openid')
		},
		dataType: 'JSON',
		async: true,
		success: function(res) {
			if(res.code == 0) {
				console.log(res.message)
				location.href = "person_msg.html"
			} else {
				alert(res.message)
			}
		},
		error: function() {
			alert('请求失败')
		}
	});
}