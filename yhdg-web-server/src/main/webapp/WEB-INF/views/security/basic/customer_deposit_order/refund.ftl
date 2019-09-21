<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="photoPath" id="photo_path_${pid}">
            <input type="hidden" name="id" id="id_${pid}" value="${(id)!''}">

            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">退款金额：</td>
                    <td><input id="money_${pid}" name="refundMoney" class="easyui-numberspinner"  style="width: 197px; height: 28px;" required="required" data-options="min:0.01,precision:2" value="${(refundMoney / 100.0)?string("#.##")}">&nbsp;&nbsp;元</td>
                </tr>
                <tr>
                    <td width="70" align="right">退款凭证：</td>
                    <td >
                        <div class="portrait">
                            <a href="javascript:void(0)"><img id="image_${pid}" src="${app.imagePath}/user.jpg" /><span>上传退款凭证</span></a>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td width="70"  align="right">退款原因：</td>
                    <td >
                        <textarea  maxlength="40" style="width: 190px; height: 60px;" name="refundReason"></textarea>
                    </td>
                </tr>


            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    function set_portrait (param) {
        $('#image_${pid}').attr('src', '${(controller.appConfig.staticUrl)!''}' + param.filePath);
        $('#photo_path_${pid}').val(param.filePath);
        $('#' + param.pid).window('close');
    }
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/basic/customer_deposit_order/confirm_refund.htm',
                success: function(text) {
                    var json = $.evalJSON(text);
                <@app.json_jump/>
                    if(json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        });
        win.find('button.close').click(function() {
            win.window('close');
        });
        win.find('.portrait').click(function() {
            App.dialog.show({
                css: 'width:558px;height:150px;overflow:visible;',
                title: '上传退款凭证',
                href: "${contextPath}/security/basic/customer_deposit_order/photo_path.htm",
                event: {
                    onClose: function() {
                    }
                }
            });
        });

    })()
</script>