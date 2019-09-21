var baseUrl = getBaseUrl(),
	token = localStorage.getItem('uToken'),
	//	openid=getParam('openId',location.href);//正式
	openid="ohxZKwSgUui2OCAwD_rU7ct56UzM";//测试

$(function() {
	//动态设置字体大小
	useful()
	
	//获取个人信息
	login()
	
	//弹出或者隐藏解绑提示框
	//  $('.cancel').on('click',function(e){
	//      console.log(this)
	//      // $('#dialog').fadeIn(200)
	//      $('#dialog').css('display','block')
	//  })
	//  $('.weui-dialog__btn_default').on('click',function(){
	//      console.log('hide')
	//      // $(this).fadeOut(200);
	//      $('#dialog').css('display','none')
	//  })

	//关于我们
	$('.about_us').on('click', function() {
		location.href = "http://www.yusong.com.cn"
		//发起请求
		//		$.ajax({
		//			type:"get",
		//			url:"http://www.yusong.com.cn/charger/api/v1/about_us",
		//			async:true,
		//			dataType:'JSON',
		//			success:function(res){
		//				if(res.code==0){
		//					var url=res.data[0].aboutUrl;
		//					location.href=url;
		//				}
		//			},
		//			error:function(){
		//				console.log('err')
		//			}
		//		});
	})
	
	//点击扫一扫事件
//	$('.scan').on('click', function() {
//		console.log('scan')
////		scan()
//	})

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
			if(res.code == 0) {
				var expireIn = (new Date).getTime() + (res.data.expireIn - 200) * 1000
				//存储获取到的token,expireIn
				localstorage.setItem('uToken', res.data.token)
				localStorage.setItem('uExpireIn', expireIn)
				
				updateToken(getMsg())
			} else {
				console.log(res.message)
				return false
			}
		},
		error: function() {
			console.log('网络错误！')
		}
	});
}
//获取个人信息
function getMsg() {
	//	var token = localStorage.getItem('oToken');
	console.log(token)
	$.ajax({
		type: "post",
		url: baseUrl + "/api/v1/customer/info.htm",
		dataType: "JSON",
		contentType: "application/json",
		async: true,
		headers: {
			"Authorization": "Bearer " + token
		},
		success: function(res) {

			res = JSON.parse(res)
			console.log(res)
			
			if(res.code == 0) {
				$('.img img').attr('src', res.data.photoPath);
				$('.user_name').html(res.data.nickname)
				$('.user_tel').html(res.data.mobile)
			} else if (res.code==3){
				update(getMsg())
			}else{
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

function scan() { //调用扫一扫
	wx.config({
		debug: true, // 开启调试模式
		appId: "appid", // 必填，公众号的唯一标识
		timestamp: localStorage.getItem("timestamp"), // 必填，生成签名的时间戳
		nonceStr: localStorage.getItem("nonceStr"), // 必填，生成签名的随机串
		signature: localStorage.getItem("signature"), // 必填，签名，见附录1
		jsApiList: ['scanQRCode', 'getLocation'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
	});
	//调用扫一扫
	wx.ready(function() {
		wx.scanQRCode({
			needResult: 0, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
			scanType: ["qrCode", "barCode"], // 可以指定扫二维码还是一维码，默认二者都有
			success: function(res) {
				var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
			}
		});
	});
}