<div class="popup_body">
    <div class="ui_table">
        <div id="qrcode_${pid}" style="margin-left: 130px; margin-top: 20px;"></div>
        <div style="text-align: center;font-size: 20px;margin-top: 10px;margin-bottom: 20px">请使用微信扫码付款</div>
    </div>
</div>
<div class="popup_btn" style="margin-right: 0px;">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    var pid = '${pid}',
        win = $('#' + pid);

    (function() {

        console.log('${url}');
        new QRCode(document.getElementById("qrcode_${pid}"), '${url}');  // 设置要生成二维码的链接

        win.find('button.close').click(function() {
            win.window('close');
        });

        setTimeout('queryStatus()',2000);
    })();

    function queryStatus(){
        $.ajax({
            cache: false,
            async: false,
            type: 'POST',
            url: "${contextPath}/security/basic/agent_deposit_order/query_status.htm",
            data: {
                id: '${id}'
            },
            dataType: 'json',
            success: function (json) {
                <@app.json_jump/>
                console.log(json);
                if(json.status==1){
                    setTimeout('queryStatus()',2000);
                }else if(json.status==2){
                    $.messager.alert('提示信息', "支付成功", 'info');
                    setTimeout(function () {
                        win.window('close');
                    },2000);
                }
            }
        });
    }
</script>
