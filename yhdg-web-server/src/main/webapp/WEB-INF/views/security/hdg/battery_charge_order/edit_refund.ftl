<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="100" align="right">退款金额：</td>
                    <td><input id="refund_money_${pid}" class="easyui-numberspinner"   style="width:160px;height:28px " readonly="true" required="required" value="${(entity.money / 100.0)?string("#.##")}" data-options="min:0.00,precision:2">&nbsp;&nbsp;元</td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td><textarea style="width:330px;" required="required" name="refundReason" maxlength="40"></textarea></td>
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
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/hdg/battery_charge_order/refund.htm',
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
                    }
                }
            });
        });
        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>
