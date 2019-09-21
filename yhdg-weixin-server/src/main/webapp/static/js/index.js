//var openid = getParam('openId', location.href), //获取openId

var secret = getSecret(),
	baseUrl = getBaseUrl(),
	openid = "ohxZKwSgUui2OCAwD_rU7ct56UzM"; //测试
//	openid=getParam('openId',location.href);//正式

var markers = [{
	id: "123456",
	chargerName: "某某路充电桩",
	distance: 100,
	freePoint: 3,
	lng: 120.535345345,
	lat: 30.4534534,
	street: "七贤路1号",
	status: 1
}];
localStorage.setItem('openid', 'openid');

$(function() {
	//动态设置字体大小
	useful()

	//					configJSSDK()

	//初始化地图,正式流程
	//		init()

	updateToken(getMarkers(30.348158, 120.019613)) //测试

	//点击扫一扫事件
	$('.scan').on('click', function() {
		//		scan()
	})

	//点击导航事件
	$('.navBtn').on('click', function() {
		var fromcoord = $(this).attr('data-from').replace(/\s/ig, '');
		var tocoord = $(this).attr('data-to').replace(/\s/ig, '');
		location.href = "http://apis.map.qq.com/uri/v1/routeplan?type=walk&from=当前位置&fromcoord=" + fromcoord + "&to=充电桩&tocoord=" + tocoord + "&policy=1&referer=myapp"
	})

})

//调用扫一扫
function scan() {
	if(sessionStorage.getItem('jssdkConfig')) {
		//配置jssdk
		wx.config(sessionStorage.getItem('jssdkConfig'));
		//调用扫一扫接口
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
}

//初始化地图
function init() {
	//正式流程，先登录
	login()
}

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
				//获取jssdk
				configJSSDK(res.data.token)
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

//获取jssdk参数
function configJSSDK() {
	//根据token获取jssdk配置参数
	$.ajax({
		type: "post",
		url: baseUrl + "/api/v1/weixin/refresh_js_api_ticket.htm",
		data: JSON.stringify({
			"webPath": location.href.split('#')[0] //当前页面的完整路径
		}),
		contentType: 'application/json',
		headers: {
			"Authorization": "Bearer " + localStorage.getItem('uToken')
		},
		dataType: 'JSON',
		success: function(res) {
			res = JSON.parse(res)
			console.log(res)

			if(res.code == 0) {

				var configObj = {
					debug: true, // 开启调试模式
					appId: "wx55cee24808d1d259", // 必填，公众号的唯一标识
					timestamp: res.data.timestamp, // 必填，生成签名的时间戳
					nonceStr: res.data.noncestr, // 必填，生成签名的随机串
					signature: res.data.signature, // 必填，签名，见附录1
					jsApiList: ['scanQRCode', 'getLocation'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
				}
				console.log(configObj)
				//当前页面缓存配置参数
				sessionStorage.setItem('jssdkConfig', configObj)
				//调用获取当前位置接口
				getLocation(configObj)
			} else {
				console.log(res.message)
				return false;
			}
		},
		error: function() {
			console.log('网络错误')
		}
	});
}

//调用获取位置接口
function getLocation(obj) {
	//配置jssdk
	wx.config(obj);

	wx.ready(function() {
		wx.getLocation({
			type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
			success: function(res) {
				var lat = res.latitude; // 纬度，浮点数，范围为90 ~ -90
				var lng = res.longitude; // 经度，浮点数，范围为180 ~ -180。
				//将坐标转成百度坐标
				var convertor = new BMap.Convertor();
				var ggPoint = new BMap.Point(lng, lat);
				var pointArr = [];
				pointArr.push(ggPoint);
				convertor.translate(pointArr, 1, 5, function(data) { //1代表wgs84坐标，5代表百度坐标
					console.log(data)
					if(data.status == 0) {
						//获取附近充电桩
						getMarkers(data.points[0].lat, data.points[0].lng)
					}
				})

			}
		});
	});
}

//查询附近充电桩
function getMarkers(lat, lng) {
	console.log(lat)
	console.log(lng)
	console.log(localStorage.getItem('uToken'))
	$.ajax({
		type: "post",
		url: baseUrl + "/api/v1/charger/nearest.htm",
		data: JSON.stringify({
			"lng": lng,
			"lat": lat
		}),
		dataType: 'json',
		async: true,
		contentType: 'application/json',
		headers: {
			"Authorization": "Bearer " + localStorage.getItem('uToken')
		},
		success: function(res) {
			console.log(res)
			if(res.code == 0) {
				var data = res.data;
				//将获取到的百度坐标转成腾讯坐标
				for(var i = 0, arr = []; i < data.length; i++) {
					arr.push(new qq.maps.LatLng(data[i].lat, data[i].lng))
				}
				console.log(arr)
				qq.maps.convertor.translate(arr, 1, function(res) {
					console.log(res)
					for(var i = 0; i < res.length; i++) {
						data[i].lat = res[i].lat
						data[i].lng = res[i].lng
					}
					//初始化地图
					initMap(lat, lng, data)
				})
			} else if(res.code == 3) {
				update(getMarkers(lat, lng))
			} else {
				console.log(res.message)
				return false;
			}
		},
		error: function() {
			alert('网络链接失败！');
			return false;
		}
	});
}

function initMap(lat, lon, markers) { //加载地图
	//将获取到的百度坐标转成腾讯地图坐标
	qq.maps.convertor.translate(new qq.maps.LatLng(lat, lon), 3, function(res) {
		var latlng = res[0];
		var map = new qq.maps.Map(document.getElementById("container"), {
			center: latlng,
			mapTypeId: qq.maps.MapTypeId.ROADMAP,
			zoom: 13,
			zoomControl: false
		})

		//当前所在位置
		new qq.maps.Marker({
			position: latlng,
			map: map,
			icon: new qq.maps.MarkerImage('images/position@2x.png'),
		})

		//查询到的最近点的信息
		$('.charge_name').html(markers[0].chargerName)
		$('.charge_num').html(markers[0].freePoint + '个')
		$('.charge_distance').html(markers[0].distance + 'm')
		$('.charge_adr').html(markers[0].street)

		//将当前点的坐标信息和最近点的坐标信息放入导航按钮
		//		console.log(markers[0].lat.toFixed(6))
		//		console.log(markers[0].lng)
		$('.navBtn').attr('data-from', latlng).attr('data-to', markers[0].lat.toFixed(6) + ',' + markers[0].lng.toFixed(6));

		//设置地图的高度
		var mapHeight = $('.weui-tab__panel').height() - $('.foot').height()
		//		console.log(mapHeight)
		$('#container').css('height', mapHeight + 'px')
		//		console.log(markers)
		//快递柜分布位置
		markers.map(function(marker, index) {
			//			console.log(marker)
			var point = new qq.maps.LatLng(marker.lat, marker.lng);
			var icon = "";
			switch(marker.status) {
				case 1:
					icon = new qq.maps.MarkerImage('images/green_s_position@2x.png');
					break;
				case 2:
					icon = new qq.maps.MarkerImage('images/red_s_position@2x.png');
					break;
				case 3:
					icon = new qq.maps.MarkerImage('images/gray_s_position@2x.png');
					break;
			}
			//			console.log(point)
			//			console.log(icon)
			var center = new qq.maps.Marker({
				position: point,
				map: map,
				icon: icon
			})
			//图标点击事件
			qq.maps.event.addListener(center, 'click', (function() {
				return function() {
					$('.charge_name').html(marker.chargerName)
					$('.charge_num').html(marker.freePoint + '个')
					$('.charge_distance').html(marker.distance + 'm')
					$('.charge_adr').html(marker.street)
					//导航路线
					search(latlng, point)
				}
			})());
		})

		//获取驾车路线的方案
		var drivingService = new qq.maps.DrivingService({
			map: map,
			//展现结果
			panel: document.getElementById('infoDiv')
		});

		//设置路线
		function search(start, end) {
			//设置驾车方案
			drivingService.setPolicy(qq.maps.DrivingPolicy['LEAST_TIME']);
			//设置驾车的区域范围
			drivingService.setLocation("杭州");
			//设置回调函数
			drivingService.setComplete(function(result) {
				console.log(result)
				if(result.type == qq.maps.ServiceResultType.MULTI_DESTINATION) {
					alert("起终点不唯一");
				}
			});
			//设置检索失败回调函数
			drivingService.setError(function(data) {
				alert(data);
			});
			//设置驾驶路线的起点和终点
			drivingService.search(start, end);
		}

		//显示框和回到地图中心点按钮
		$('#container').append('<div class="goback"><img class="back" src="images/back.png"/></div><div class="prompt"><img src="images/prompt.png"/></div>')
		$('.goback').on('click', function() {
			//			console.log('click')
			map.panTo(latlng)
		})
	});

}