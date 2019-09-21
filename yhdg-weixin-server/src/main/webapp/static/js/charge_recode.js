var baseUrl = getBaseUrl(),
	token = localStorage.getItem('uToken');

$(function() {
	//动态设置字体大小
	useful()

	//上拉加载下拉刷新
	var pages = 0;
	$('body').dropload({
		scrollArea: window,
		domDown: {
			domClass: 'dropload-down',
			domRefresh: '<div class="dropload-refresh">↑上拉加载更多</div>',
			domLoad: '<div class="dropload-load">○加载中...</div>',
			domNoData: '<div class="dropload-noData">暂无数据</div>'
		},
		domUp: {
			domClass: 'dropload-up',
			domRefresh: '<div class="dropload-refresh">↓下拉刷新</div>',
			domUpdate: '<div class="dropload-update">↑释放更新</div>',
			domLoad: '<div class="dropload-load"><span class="loading"></span>加载中</div>'
		},
		loadDownFn: function(me) { //上拉加载
			console.log(pages)
			//加载数据
			updateToken(getOrderList(me, pages))
			pages++
		},
		loadUpFn: function(me) { //下拉刷新
			console.log('up')
			pages = 0;
			//加载数据
			updateToken(getOrderList(me, pages))
		}
	})
})

//获取账单信息
function getOrderList(me, pages) {
	$.ajax({
		type: "post",
		url: baseUrl + "/api/v1/customer_in_out_money/list.htm",
		data: JSON.stringify({
			"offset": pages,
			"limit": 10
		}),
		contentType: "application/json",
		headers: {
			"Authorization": "Bearer " + token
		},
		dataType: "JSON",
		async: true,
		success: function(res) {
			res = JSON.parse(res)
			console.log(res)

			if(res.code == 0) {
				if(res.data.length == 0) {
					
					me.lock();//锁定
					
					me.noData();// 显示无数据
					me.resetload();
					return false
				}
				var str = "";
				res.data.map(function(arr, index) {
					if(arr) {
						str += '<div class="weui-cell"><div class="weui-cell__bd"><p>' + arr.bizTypeName + '</p><p>' + arr.createTime + '</p></div><div class="weui-cell__ft"><p>' + arr.money + '</p></div></div>';
					}
					if(index < 10) {
						//锁定
						me.lock();
						// 显示无数据
						me.noData();
					}
				})
				if(pages == 0) { //如果是下拉刷新
					$('.weui-cells').html(str)
				} else {
					$('.weui-cells').append(str)
				}

				me.resetload();
			}
			else if (res.code==3){
				update(getOrderList(me, pages))
			}else {
				me.resetload();
				console.log(res.message)
				return false;
			}
		},
		error: function() {
			alert('网路连接错误')
			me.resetload();
			return false;
		}
	});
}