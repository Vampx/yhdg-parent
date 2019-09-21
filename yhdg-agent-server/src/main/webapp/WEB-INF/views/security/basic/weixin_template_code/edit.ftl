<div class="popup_body">
    <div class="ui_table">
        <form id="form_${pid}">
            <input type="hidden" name="id" value="${entity.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">标题：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="templateName" value="${(entity.templateName)!''}" maxlength="40"/></td>
                    <td width="70" align="right"></td>
                    <td>
                    </td>
                </tr>
                <tr>
                    <td align="right">模板ID：</td>
                    <td colspan="3">
                        <textarea class="text easyui-validatebox" required="true" name="templateCode" style="width:435px; height: 32px;">${(entity.templateCode)!''}</textarea>
                    </td>
                </tr>
                <tr>
                    <td align="right">变量：</td>
                    <td colspan="3">
                        <textarea class="text easyui-validatebox" required="true" name="variable" style="width:435px; height: 32px;">${(entity.variable)!''}</textarea>
                    </td>
                </tr>
                <tr>
                    <td align="right">颜色：</td>
                    <td colspan="3">
                        <textarea class="text easyui-validatebox" required="true" name="color" style="width:435px; height: 32px;">${(entity.color)!''}</textarea>
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

<script type="text/javascript">
    (function() {
        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/basic/weixin_template_code/update.htm',
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