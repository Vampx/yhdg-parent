$(function() {
	//动态设置字体大小
	useful()
	

	//当卡号输入框失去焦点时，使完成按钮可点击
	$('.code').on('blur', function() {
		if($(this).val()) {
			$('.weui-btn').removeClass('weui-btn_disabled')
		}
	})

	//点击效果
	$('.weui-btn').on('click', function() {
		var that = this;
		if(!$(this).hasClass('weui-btn_disabled')) {
			//点击效果
			$(that).css('opacity', '0.7')
			setTimeout(function() {
				$(that).css('opacity', '1')
			}, 500)

			var code = $('.code').val()
			//发起绑定卡号请求
			$.ajax({
				type: "get",
				url: "http://www.yusong.com.cn/charger/api/v1/customer/icCardBind",
				data: {
					openId: localStorage.getItem('openid'),
					IcCard: code
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

	})
})