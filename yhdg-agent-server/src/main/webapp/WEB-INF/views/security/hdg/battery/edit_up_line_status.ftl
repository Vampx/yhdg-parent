<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="agentId" value="${Session['SESSION_KEY_USER'].agentId}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">电池编号：</td>
                    <td><input type="text" class="text easyui-validatebox" name="id" maxlength="40" value=""/></td>
                </tr>
                <tr>
                    <td align="right">IMEI：</td>
                    <td><input type="text" class="text easyui-validatebox" name="code" maxlength="40"
                               value=""/></td>
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
    (function () {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function () {
            if (!form.form('validate')) {
                return false;
            }
            var id = win.find('input[name=id]').val();
            var code = win.find('input[name=code]').val();
            if((id == null || id == '')&&(code == null || code == '') ) {
                $.messager.alert('提示信息', '电池编号和IMEI请至少输入一项', 'info');
                return;
            }
            form.form('submit', {
                url: '${contextPath}/security/hdg/battery/update_up_line_status.htm',
                success: function (text) {
                    var json = $.evalJSON(text);
                <@app.json_jump/>
                    if (json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        });
        win.find('button.close').click(function () {
            win.window('close');
        });
    })()
</script>