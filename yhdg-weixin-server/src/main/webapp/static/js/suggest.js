var baseUrl = getBaseUrl(),
	token = localStorage.getItem('uToken');

$(function() {

	//设置字体大小
	useful();

	//输入框失去焦点
	$('.weui-textarea').on('blur', function() {
		if($(this).val) {
			$('.weui-btn').removeClass('weui-btn_disabled')
		}
	})

	//提交意见反馈
	$('.weui-btn').tap(function() {
		if(!$(this).hasClass('weui-btn_disabled')) {
			var val = $('.weui-textarea').val()
			console.log(val)
			if(val) {
				updateToken(submitSuggest(val))
			}
		}
	})

	// toast
	var $toast = $('#toast');
	$('#showToast').on('click', function() {
		if($toast.css('display') != 'none') return;

		$toast.fadeIn(100);
		setTimeout(function() {
			$toast.fadeOut(100);
		}, 2000);
	});

	//点击知道了按钮
	$('#iosDialog2 .weui-dialog__btn').tap(function() {
		$('#iosDialog2').css('display', 'none');
		//		location.href="my.html"
		window.history.back(-1)

	})
})

//提交意见反馈
function submitSuggest(val) {
	$.ajax({
		type: "post",
		url: baseUrl + "/api/v1/feedback/create.htm",
		data: JSON.stringify({
			"content": val
		}),
		contentType: "application/json",
		dataType: "JSON",
		async: true,
		headers: {
			"Authorization": "Bearer " + token
		},
		success: function(res) {
			res = JSON.parse(res);
			console.log(res)
			if(res.code == 0) {
				$('#iosDialog2').css('display', 'block');
			} else if (res.code==3){
				submitSuggest(val)
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