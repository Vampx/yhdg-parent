<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <input type="hidden" name="refundPhoto" id="photo_path_${pid}">
            <input type="hidden" name="payType" id="pay_type_${pid}" value="${(entity.payType)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="100" align="right">退款金额：</td>
                    <td><input id="refund_money_${pid}" class="easyui-numberspinner"   style="width:160px;height:28px " required="required" value="${((entity.money-entity.refundMoney) / 100.0)?string("#.##")}" data-options="min:0.00,precision:2">&nbsp;&nbsp;元</td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">退款备注：</td>
                    <td><textarea style="width:330px;" name="memo" maxlength="40"></textarea></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">上传退款凭证：</td>
                    <td>
                        <div class="portrait">
                            <a href="javascript:void(0)"><img id="image_${pid}" src="${app.imagePath}/user.jpg"/><span>退款凭证</span></a>
                        </div>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button id="refund_btn" class="btn btn_red ok">确定</button>
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
            var payType = $('#pay_type_${pid}').val();
            var path = $('#photo_path_${pid}').val();
            <#--if (payType == ${APPLY_ALIPAY} || payType == ${APPLY_WEIXIN}) {-->
                <#--if (path == null || path == "") {-->
                    <#--$.messager.alert('提示信息', '请上传退款凭证', 'info');-->
                    <#--return false;-->
                <#--}-->
            <#--}-->
            $("#refund_btn").attr("disabled",true);
            var url ='${contextPath}/security/zc/vehicle_customer_foregift_order/refund.htm';
            if(${refundFlag}==2){
                 url ='${contextPath}/security/zc/vehicle_customer_foregift_order/again_refund.htm';
            }
            form.form('submit', {
                url: url,
                onSubmit: function(param) {
                    var refundMoney = $('#refund_money_${pid}').numberspinner('getValue');
                    param.refundMoney = parseInt(Math.round(refundMoney * 100));
                    return true;
                },
                success: function(text) {
                    var json = $.evalJSON(text);
                <@app.json_jump/>
                    if(json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                        win.window('close');
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
                title: '上传头像',
                href: "${contextPath}/security/zc/vehicle_customer_foregift_order/photo_path.htm",
                event: {
                    onClose: function() {
                    }
                }
            });
        });

    })()
</script>
