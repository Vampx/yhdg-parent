<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">模板名称：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="templateName"
                               style="width: 172px; height: 28px;"/></td>
                </tr>
                <#--<tr>-->
                    <#--<td width="70" align="right">公众号：</td>-->
                    <#--<td><input type="text" class="text easyui-validatebox" name="mpCode"-->
                               <#--style="width: 172px; height: 28px;"/></td>-->
                <#--</tr>-->
                <tr>
                    <td width="70" align="right">生活号：</td>
                    <td><input type="text" class="text easyui-validatebox" name="fwCode"
                               style="width: 172px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">变量：</td>
                    <td><textarea style="width:330px;height: 100px;" name="variable" maxlength="512"></textarea></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td><textarea style="width:330px;" name="memo" maxlength="512"></textarea></td>
                </tr>
                <tr>
                    <td align="right">启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_1" checked value="1"/><label
                                for="is_active_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_0" value="0"/><label
                                for="is_active_0">禁用</label>
                        </span>
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
    (function () {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function () {
            form.form('submit', {
                url: '${contextPath}/security/basic/fw_push_message_template/insert.htm',
                success: function (text) {
                    var json = $.evalJSON(text);
                <@app.json_jump/>
                    if (json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        win.window('close');
                        edit(json.data,0)
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
