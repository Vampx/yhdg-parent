<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${order.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">OpenId：</td>
                    <td><input type="text" maxlength="40" style="width: 300px;" class="text easyui-validatebox" required="true" name="openId" value="${(order.openId)!''}"/></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">真实姓名：</td>
                    <td><input type="text" maxlength="60" class="text easyui-validatebox" required="true" name="fullName" value="${(order.fullName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">操作人：</td>
                    <td><input type="text" maxlength="60" class="text easyui-validatebox" readonly="readonly" value="${operatorName}"/></td>
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
        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        win.find('button.ok').click(function() {
            $.messager.progress();
            form.form('submit', {
                url: '${contextPath}/security/basic/balance_transfer_order/reset.htm',
                success: function(text) {
                    $.messager.progress('close');
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
    })();
</script>