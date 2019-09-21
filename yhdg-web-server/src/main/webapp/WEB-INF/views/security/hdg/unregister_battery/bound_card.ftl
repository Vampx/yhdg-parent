<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">电池编号：</td>
                    <td><input type="text" class="text easyui-validatebox" name="id" maxlength="8"
                               required="true" validType="unique[]" style="width: 173px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">IMEI：</td>
                    <td><input maxlength="40" class="text easyui-validatebox" name="code" required="true" readonly value="${(entity.id)!''}"
                               validType="uniqueCode[]" style="width: 173px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">二维码：</td>
                    <td><input type="text" class="text easyui-validatebox" name="qrcode" required="true" style="width: 173px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">外壳编号：</td>
                    <td><input type="text" class="text easyui-validatebox" name="shellCode" required="true" style="width: 173px; height: 28px;"/></td>
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
            form.form('submit', {
                url: '${contextPath}/security/hdg/battery/bound_card.htm',
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