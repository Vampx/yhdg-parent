var baseUrl = getBaseUrl(),
	token = localStorage.getItem('uToken'),
	uploadUrl = getUploadUrl();

$(function() {
	//设置字体大小
	useful();

	//获取个人信息
	updateToken(getMsg())

	//上传头像
	$('.js_file').on('change', function(e) {
		var files = e.target.files;
		var reader = new FileReader();
		reader.onloadend = function() {
			$('.header_img')[0].src = reader.result;
			//上传头像
			upload(files)
		}
		if(files[0]) {
			reader.readAsDataURL(files[0]);
		} else {
			$('.header_img')[0].src = "";
		}
	})

	//修改手机号
	$('.changeTel').tap(function() {
		//		console.log($('.changeTel .weui-cell__ft p').html())
		var tel = $('.changeTel .weui-cell__ft p').html() || ""
		if(tel) {
			location.href = "msg_tel_n.html?tel=" + tel
		} else {
			$('.js_dialog .weui-dialog__bd').html('请先绑定绑手机号！');
			$('.js_dialog').css('display', 'block');
		}
	})

	//解绑手机号
	$('.cancelTel').tap(function() {
		var tel = $('.cancelTel .weui-cell__ft p').html() || ""
		console.log(tel)
		if(tel) {
			$('#iosDialog1 .weui-dialog__bd span').html(tel);
			$('#iosDialog1').css('display', 'block');
		} else {
			$('.js_dialog .weui-dialog__bd').html('请先绑定绑手机号！');
			$('.js_dialog').css('display', 'block');
		}
	})

	//点击确认解绑
	$('.confirm').tap(function() {
		var tel = $('.cancelTel .weui-cell__ft p').html()
		console.log(tel)
		updateToken(cancelTel(tel))

	})

	//绑定手机号
	$('.bindTel').tap(function() {
		var tel = $('.bindTel .weui-cell__ft p').html() || ""
		console.log(tel)
		if(!tel) {
			location.href = "msg_tel.html"
		} else {
			$('.js_dialog .weui-dialog__bd').html('请先解绑手机号！');
			$('.js_dialog').css('display', 'block');
		}
	})

	//修改昵称
	$('.user_name').tap(function() {
		location.href = "msg_name.html"
	})

	//点击知道了
	$('.weui-dialog__btn').tap(function() {
		$('.js_dialog').css('display', 'none');
	})
})

//获取个人信息
function getMsg() {
	$.ajax({
		type: "post",
		url: baseUrl + "/api/v1/customer/info.htm",
		data: {},
		contentType: "application/json",
		dataType: "JSON",
		async: true,
		headers: {
			"Authorization": "Bearer " + localStorage.getItem('uToken')
		},
		success: function(res) {
			res = JSON.parse(res)

			if(res.code == 0) {
				var data = res.data;

				$('.header_img').attr('src', uploadUrl + data.photoPath.slice(4));
				$('.user_name .weui-cell__ft p').html(data.nickname)
				$('.changeTel .weui-cell__ft p').html(data.mobile)
				$('.cancelTel .weui-cell__ft p').html(data.mobile)
				$('.bindTel .weui-cell__ft p').html(data.mobile)
				$('.card').html(data.icCard)
			} else if (res.code==3){
				update(getMsg())
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

//上传头像
function upload(files) {
	var formData = new FormData();
	if(files[0]) {
		formData.append('file', files[0]);
		formData.append('type', 1);
	}

	$.ajax({
		type: "post",
		url: 'https://192.9.198.244:8442/security/upload/attachment.htm',
		data: formData,
		cache: false,
		processData: false,
		contentType: false,
		success: function(res) {
			console.log(res)
			if(res.code == 0) {
				var path = res.data[0].filePath;
				console.log(path)
				//修改头像
				updateToken(changeHeader(path))
			}
		},
		error: function() {
			alert('网路连接错误')
			return false;
		}
	})
}

//修改头像
function changeHeader(imgPath) {
	$.ajax({
		type: "post",
		url: baseUrl + "/api/v1/customer/update_info.htm",
		data: JSON.stringify({
			"photoPath": imgPath
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
				//				location.href = "person_msg.html"
				console.log('success')
			} else if (res.code==3){
				update(changeHeader(imgPath))
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

//解绑手机号
function cancelTel(tel) {
	console.log(typeof tel)
	$.ajax({
		type: "post",
		url: baseUrl + "/api/v1/customer/unbind_mobile.htm",
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
				location.href = "person_msg.html"
			} else if (res.code==3){
				update(cancelTel(tel))
			}  else {
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