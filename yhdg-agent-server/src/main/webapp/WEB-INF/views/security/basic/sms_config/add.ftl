<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" id="is_active_${pid}" name="isActive" value="1" />
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="80" align="right">短信接口：</td>
                    <td>
                        <select name="smsType" class="easyui-combobox" style="width: 182px; height: 30px;">
                            <#list typeList as e>
                                <option value="${e.getValue()}">${(e.getName())!''}</option>
                            </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">账号：</td>
                    <td><input type="text"maxlength="20" class="text easyui-validatebox" name="account" /></td>
                </tr>
                <tr>
                    <td width="80" align="right">密码：</td>
                    <td><input type="text" maxlength="40" class="text easyui-validatebox" name="password" /></td>
                </tr>
                <tr>
                    <td width="80" align="right">签名：</td>
                    <td><input type="text" class="text easyui-validatebox" name="sign"  maxlength="10"/></td>
                </tr>
                <tr>
                    <td width="80" align="right">签名位置：</td>
                    <td>
                        <select class="easyui-combobox" name="signPlace" style="height:28px; width: 182px;">
                            <#list signPlaceEnum as e>
                                <option value="${e.getValue()}">${e.getName()}</option>
                            </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">是否启用：</td>
                    <td><input type="checkbox" class="checkbox" name="active" checked value="1" onclick="$('#is_active_${pid}').val(this.checked ? 1 : 0)"></td>
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
                url: '${contextPath}/security/basic/sms_config/create.htm',
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
