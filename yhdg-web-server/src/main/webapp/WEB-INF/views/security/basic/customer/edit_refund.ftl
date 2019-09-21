<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <input type="hidden" name="mobile" value="${(entity.mobile)!''}">
            <input type="hidden" name="fullname" value="${(entity.fullname)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="100" align="right">扣款金额：</td>
                    <td><input id="balance_${pid}" class="easyui-numberspinner"   style="width:160px;height:28px " required="required" data-options="min:0.00,precision:2" value="${(entity.balance)/100}">&nbsp;&nbsp;元</td>
                </tr>
                <tr>
                    <td align="right">操作人：</td>
                    <td><input type="text" class="text easyui-validatebox" id="handleName_${pid}" name="handleName" maxlength="40" readonly value="${(Session['SESSION_KEY_USER'].username)!''}" /></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td><textarea style="width:330px;" required="required" name="memo" maxlength="200"></textarea></td>
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
                url: '${contextPath}/security/basic/customer/refund.htm',
                onSubmit: function(param) {
                    var balance = $('#balance_${pid}').numberspinner('getValue');
                    param.balance = parseInt(Math.round(balance * 100));
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
