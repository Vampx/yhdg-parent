var baseUrl = getBaseUrl(),
	token = localStorage.getItem('uToken');

$(function(){
	//设置字体大小
	useful();
	
	//获取余额
	updateToken(getBalance())
})

function getBalance(){
	console.log('in')
	$.ajax({
		type: "post",
		url: baseUrl + "/api/v1/customer/balance.htm",
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
				var account=parseInt(res.data.balance)+parseInt(res.data.cardBalance)
				$('.all').html(account.toFixed(2))
				$('.telbalance').html(parseInt(res.data.balance).toFixed(2)+'元')
				$('.cardbalance').html(res.data.cardBalance.toFixed(2)+'元')
			} else if (res.code==3){
				update(getBalance())
			}else {
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
