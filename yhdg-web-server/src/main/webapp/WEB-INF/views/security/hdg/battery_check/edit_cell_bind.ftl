<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">原电池条码：</td>
                    <td><input type="text" maxlength="40" class="text"  name="shellCode"
                               value="${(entity.shellCode)!''}" readonly
                               style="width: 173px; height: 28px;"/></td>
                </tr>
                </tr>
                    <td align="right">新电池条码：</td>
                    <td><input type="text" maxlength="40" class="text" name="newShellCode"
                               style="width: 173px; height: 28px;"/></td>
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

            var newShellCode = win.find('input[name=newShellCode]').val();
            if(newShellCode == null || newShellCode == '') {
                $.messager.alert('提示信息', '新电池条码不能为空', 'info');
                return;
            }

            form.form('submit', {
                url: '${contextPath}/security/hdg/battery_check/update_cell_bind.htm',
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