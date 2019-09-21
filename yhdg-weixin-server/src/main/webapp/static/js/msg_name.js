

var baseUrl = getBaseUrl(),
	token = localStorage.getItem('uToken');

$(function() {
	//动态设置字体大小
	useful()
	
	
	var name = '';
	//输入框失去焦点是验证是否为空
	$('.weui-input').on('blur', function() {
		if(!$(this).val()) {
			$('#iosDialog2').css('display', 'block')
		} else {
			$('.weui-btn').removeClass('weui-btn_disabled');
			name = $(this).val()
		}
	})
	//隐藏提示框
	$('.weui-dialog__btn').on('click', function() {
		$('#iosDialog2').css('display', 'none')
	})

	//点击保存
	$('.weui-btn_blue').tap(function() {
		if(!$(this).hasClass('weui-btn_disabled')) {
			console.log(name)
			//提交修改
			changeName(name)
		}
	})
})

function changeName(val) {
	$.ajax({
		type: "post",
		url: baseUrl + "/api/v1/customer/update_info.htm",
		data: JSON.stringify({
			"nickname": val,
		}),
		contentType: "application/json",
		dataType: "JSON",
		async: true,
		headers: {
			"Authorization": "Bearer " + token
		},
		success: function(res) {
			res=JSON.parse(res)
			if(res.code == 0) {
				$('#toast').css('display','block')
				setTimeout(function(){
					$('#toast').css('display','none')
					setTimeout(function(){
//						location.href="person_msg.html"
						window.history.back(-1)
					},1000)
				},1500)

			} else if (res.code==3){
				update(changeName(val))
			} else {
				return false;
			}
		},
		error: function() {
			alert('网路连接错误')
			return false;
		}
	})
}