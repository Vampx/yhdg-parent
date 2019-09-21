<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="cabinetId" value="${cabinetId}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">箱号：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" maxlength="10" name="boxNum">格式：01</td>
                </tr>
                <tr>
                    <td width="70" align="right">类型：</td>
                    <td>
                        <select name="type" class="easyui-combobox" style="width: 184px; height: 28px;" editable="false">
                            <option value="0">不限</option>
                            <#if TypeEnum??>
                                <#list TypeEnum as line>
                                    <option value="${(line.itemValue)!''}">${(line.itemName)!''}</option>
                                </#list>
                            </#if>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">激活：</td>
                    <td><input type="checkbox" required="true" id="isActive" checked="checked"></td>
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
                url: '${contextPath}/security/hdg/cabinet_box/create.htm',
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
