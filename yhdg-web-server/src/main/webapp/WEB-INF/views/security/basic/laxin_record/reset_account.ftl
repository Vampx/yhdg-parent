<div class="popup_body">
    <div class="ui_table">
        <form id="form_${pid}" method="post">
            <input type="hidden" name="id" value="${entity.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">OpenId：</td>
                    <td><input type="text" maxlength="17" class="text easyui-validatebox" name="mpOpenId" value="${(entity.mpOpenId)!''}" maxlength="40"/></td>

                </tr>
                <tr>
                    <td width="70" align="right">姓名：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" name="accountName" value="${(entity.accountName)!''}"/></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>

<script type="text/javascript">
    (function() {
        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/basic/laxin_record/reset_account.htm',
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

    })();


</script>